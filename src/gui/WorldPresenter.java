package gui;

import gui.displayconverters.MapConverter;
import gui.hud.OverlaySelect;
import gui.regionlooks.RegionView;
import gui.regionlooks.RegionViewFactory;
import model.Region;
import model.World;
import model.WorldArray;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.util.*;
import java.util.List;

import static gui.Camera.CAM_DISTANCE;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Manages how the regions are displayed and rendered.
 */
public class WorldPresenter extends Observable
{
  private boolean DEBUG = false;
  private CAM_DISTANCE lastDistance;
  public MapConverter mpConverter;
  private Collection<GUIRegion> modelRegions;
  private static Collection<GUIRegion> backgroundRegions;
  private ActiveRegionList activeRegions;
  private static World world;
  private boolean activelyDraging;
  private static RegionViewFactory regionViewFactory;


  /**
   * Class constructor. Expects a reference to both a map converter, and a
   * world.
   *
   * @param mpConverter converter that defies the rules of the map projection.
   * @param world       set of game regions (in opposition to gui only regions).
   */
  public WorldPresenter(MapConverter mpConverter, World world)
  {
    this.modelRegions = new ArrayList<>();
    this.backgroundRegions = new ArrayList<>();
    this.mpConverter = mpConverter;
    this.regionViewFactory = new RegionViewFactory();
    this.activeRegions = new ActiveRegionList();
    this.lastDistance = CAM_DISTANCE.LONG;
    this.world = world;
    this.activelyDraging = false;
  }

  /**
   * Sets the specified collection of regions to be background graphical regions.
   * This regions set does not have a baring on the logic or model of the game,
   * it is  used for aesthetic presentation.
   *
   * @param regions back ground set of regions. (regions show in LONG shots)
   */
  public void setBackgroundRegions(Collection<Region> regions)
  {
    RegionView background = regionViewFactory.getBackgroundMapView();
    backgroundRegions = wrapRegions(regions, background);
  }

  public boolean isActivelyDragging()
  {
    return activelyDraging;
  }

  public void setActivelyDragging(boolean activelyDragging)
  {
    this.activelyDraging = activelyDragging;
    setChanged();
    notifyObservers();
  }

  /**
   * Set the given collection of regions as the model of the game. These
   * regions can be selected and inspected (in distinction to the background
   * regions).
   *
   * @param regions set of regions that constitute the model and logical
   *                entities of the game.
   */
  public void setModelRegions(Collection<Region> regions)
  {
    RegionView backG = regionViewFactory.getViewFromDistance(CAM_DISTANCE.LONG);
    modelRegions = wrapRegions(regions, backG);
  }

  /*
   * Initialization method only called during the constructor.
   * transforms model regions into gui regions.
   */
  private Collection<GUIRegion> wrapRegions(Collection<Region> regions, RegionView regionView)
  {
    Collection<GUIRegion> guiRs = new ArrayList<>();
    for (Region region : regions)
    {
      GUIRegion guir = new GUIRegion(region, mpConverter, regionView);
      guiRs.add(guir);
    }
    return guiRs;
  }


  /**
   * Marks every region that intersects the specified rectangle as active.
   * used with the camera object to enable click and drag selection of map
   * regions.
   *
   * @param rect bounding rectangle for selection
   */
  public void selectAll(Rectangle2D rect, Camera camera)
  {
    Collection<GUIRegion> regionsInView = getIntersectingRegions(rect, getRegionsInView(camera));
    activeRegions.clear();
    for (GUIRegion r : regionsInView)
    {
      activeRegions.add(r);
    }
  }

  /**
   * Registers a single selection click at the given point.
   * If the point given correspond to a region, that region will become selected,
   * and any other regions that were selected will be de-selected.
   * <p/>
   * If there is no corresponding region nothing will happen.
   *
   * @param x x coord of click
   * @param y y coord of click
   */
  public void singleClickAt(double x, double y, Camera camera)
  {
    /**
     * The regions are reversed so that any region that is drawn on top of other
     * regions will be drawn first and not the other way around.
     */
    List<GUIRegion> regionsInView = (List<GUIRegion>) getRegionsInView(camera);
    Collections.reverse(regionsInView);

    for (GUIRegion guir : regionsInView)
    {
      for( Polygon p: guir.getPoly() )
      {
        if (p.contains(x, y))
        {
          if (activeRegions.contains(guir))
          {
            activeRegions.remove(guir);
          }
          else
          {
            activeRegions.clear();
            activeRegions.add(guir);
          }
          return; //for early loop termination.
        }
      }

    }
    activeRegions.clear(); // deselects all regions when mouse click on ocean.
  }

  /**
   * Registers a region append click at the specified point.
   * if there is a region at said point, its state will be toggled.
   * This method is called to incrementally build up a group of selected
   * regions, that need not be contiguous.
   *
   * @param x x coord.
   * @param y y coord.
   */
  public void appendClickAt(double x, double y, Camera camera)
  {
    List<GUIRegion> regionsInView = (List<GUIRegion>) getRegionsInView(camera);
    Collections.reverse(regionsInView);

    for (GUIRegion guir : regionsInView)
    {
      for( Polygon p: guir.getPoly())
      {
        if (p.contains(x, y))
        {
          if (activeRegions.contains(guir))
          {
            activeRegions.remove(guir);
          }
          else
          {
            activeRegions.add(guir);
          }
          return; //for early loop termination.
        }
      }
    }
  }

  /**
   * Clears the active region list and thus hides the side panel
   */
  public void clearActiveList()
  {
    activeRegions.clear();
  }

  /**
   * Given a Camera, this method returns all the GUI regions 'in view',
   * and adjusts the look to the appropriate level of detail.
   *
   * @param camera camera object used to extract 'height' and viewing angle on
   *               map.
   * @return all the regions in view, all set to the appropriate rendering rules.
   */
  public Collection<GUIRegion> getRegionsInView(Camera camera)
  {
    Rectangle2D inViewBox = camera.getViewBounds();
    Collection<GUIRegion> regionsInView = null;

    if (DEBUG && lastDistance != calcDistance(camera))
    {
      lastDistance = calcDistance(camera);
      System.out.println("current Camera pos: " + lastDistance);
    }

    switch (calcDistance(camera))
    {
      /*
       * This code is being saved for future implementation. This allows you to zoom into smaller areas than just countries
      case CLOSE_UP:
        regionsInView = getIntersectingRegions(inViewBox, modelRegions);
        break;

      case MEDIUM:
        regionsInView = getIntersectingRegions(inViewBox, modelRegions);
        break;
      */

      case CLOSE_UP: case MEDIUM: case LONG:
        regionsInView = getIntersectingRegions(inViewBox, backgroundRegions);
        break;

      default:
        System.err.println(calcDistance(camera) + " (!)Not handled by getRegionsInView");
        System.exit(1);
    }

    RegionView regionView = regionViewFactory.getViewFromDistance(calcDistance(camera));
    setRegionLook(regionView, regionsInView);
    return regionsInView;
  }

  public static Collection<GUIRegion> getAllRegions()
  {
    return backgroundRegions;
  }

  /**
   *
   * method will return null if a GUIRegion doesn't
   * exist with the specified name
   *
   * @param name of theGUIRegion to find
   * @return the GUIRegion with the given name
   */
  public GUIRegion getSingleRegion(String name)
  {
    for (GUIRegion gr: backgroundRegions)
    {

      if (gr.getName().trim().equals(name.trim())) return gr;
    }

    return null;
  }

  public Rectangle2D getViewBox(Camera camera)
  {
    return camera.getViewBounds();
  }

  /**
   * Set the look of any Region View over lay.
   * Updates comboBox in the user controls
   *
   * @param currentOverlay over lay to be displayed.
   */
  public static void setCurrentOverlay(RegionViewFactory.Overlay currentOverlay)
  {
    regionViewFactory.setCurrentOverlay(currentOverlay);
    OverlaySelect.updateOverlaySelect(currentOverlay);
  }

  /*
     * sets the regions to the respective views as a
     * function of their active state
     */
  private void setRegionLook(RegionView look, Collection<GUIRegion> guiRegions)
  {
    for (GUIRegion region : guiRegions)
    {
      region.setLook(look);
    }
  }

  /*
   * Creates discrete view steps to handel region presentation rules.
   */
  public static CAM_DISTANCE calcDistance(Camera camera)
  {
    return camera.getDistance();
  }


  /*
   * Returns all the regions in the given collection that
   * intersect the given rectangle
   */
  private List<GUIRegion> getIntersectingRegions(Rectangle2D r,
                                                 Collection<GUIRegion> regions)
  {
    List<GUIRegion> regionsInR = new ArrayList<>();

    for (GUIRegion g : regions)
    {
      for( Polygon p: g.getPoly() )
      {
        if (p.intersects(r))
        {
          if( !regionsInR.contains(g) ) regionsInR.add(g);
        }
      }
    }

    return regionsInR;
  }

  public int countIntersectingRegions(Rectangle2D r)
  {
    return getIntersectingRegions(r, modelRegions).size()
      + getIntersectingRegions(r, backgroundRegions).size();


  }

  public int countIntersectingPoints(Rectangle2D r)
  {
    int sum = 0;
    for (GUIRegion g : modelRegions)
    {
      for( Polygon p: g.getPoly() )
      {
        int n = p.npoints;
        for (int i = 0; i < n; i++)
        {
          if (r.contains(p.xpoints[i], p.ypoints[i])) sum++;
        }
      }
    }
    return sum;
  }

  public List<GUIRegion> getActiveRegions()
  {
    if (activeRegions.isEmpty())
    {
      return null;
    }
    else
    {
      return activeRegions.getList();
    }
  }


  /**
   * advances the game world forward one year.
   */
  public void setWorldForward(int numOfdays)
  {
    if (world.setByDays(numOfdays))
    {
      setChanged();
      notifyObservers();
    }
  }

  public static Date getWorldDate()
  {
    return world.getCurrentDate().getTime();
  }

  public static Date resetWorldDate()
  {
    world.setCurrentDate(Calendar.getInstance());
    return getWorldDate();
  }

  /**
   * Private class  that manages and the active/passive state of the region.
   * also deals the marking changes
   */
  private class ActiveRegionList
  {
    private List<GUIRegion> activeRegions;

    public ActiveRegionList()
    {
      activeRegions = new ArrayList<>();
    }

    /**
     * Returns the element at the specified position in this list.
     *
     * @param index index of the element to return
     * @return the element at the specified position in this list
     * @throws IndexOutOfBoundsException if the index is out of range
     */
    public GUIRegion get(int index)
    {
      return activeRegions.get(index);
    }

    /**
     * Returns the number of elements in this list.  If this list contains
     * more than <tt>Integer.MAX_VALUE</tt> elements, returns
     * <tt>Integer.MAX_VALUE</tt>.
     *
     * @return the number of elements in this list
     */
    public int size()
    {
      return activeRegions.size();
    }

    /**
     * Returns <tt>true</tt> if this list contains no elements.
     *
     * @return <tt>true</tt> if this list contains no elements
     */
    public boolean isEmpty()
    {
      return activeRegions.isEmpty();
    }

    public void add(GUIRegion region)
    {
      if (contains(region)) return;

      region.setActive(true);
      activeRegions.add(region);

      setChanged();
      notifyObservers();
    }

    /*
     * makes and returns list of all active regions.
     */
    public List<GUIRegion> getList()
    {
      return new ArrayList<>(activeRegions);
    }


    public GUIRegion remove(GUIRegion region)
    {
      int index = activeRegions.indexOf(region);
      if (index == -1) return null;

      GUIRegion guir = activeRegions.remove(index);
      guir.setActive(false);

      setChanged();
      notifyObservers();

      return guir;
    }

    public boolean contains(GUIRegion region)
    {
      return activeRegions.contains(region);
    }

    public void clear()
    {
      for (GUIRegion r : activeRegions) r.setActive(false);
      activeRegions.clear();

      setChanged();
      notifyObservers();
    }
  }
}
