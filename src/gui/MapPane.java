package gui;

import gui.hud.NavMap;
import gui.regionlooks.RegionNameDraw;
import gui.regionlooks.RegionViewFactory;
import model.MiniArea;

import javax.swing.*;
import javax.swing.event.MouseInputListener;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.Collection;
import java.util.LinkedList;

/**
 * @author david
 *         created: 2015-02-03
 *         <p/>
 *         description:
 *         MapPane is a Swing JPanel encapsulating the view through a Camera
 *         object and its user control interface.  The MapPane communicates
 *         input events corresponding to its domain to the WorldPresenter
 *         to allow regions to be selected and displayed properly
 *
 *         Edited by David M on 3/19/2015
 *         -updating overlays and documenting overlay process. SEARCH "OVERLAY-DOC"
 */


public class MapPane extends JPanel
  implements MouseWheelListener, MouseInputListener, KeyListener
{

  private final static double MAP_VISIBILITY_SCALE = 100;
  private final static int CAMERA_STEP = 10;
  private final static double ZOOM_STEP = .05;
  private final static double SCROLL_FACTOR = .05;
  private static int heightAdjust;

  private final static int
    UP = 38,
    LEFT = 37,
    DOWN = 40,
    RIGHT = 39,
    SHIFT = 16;

  private boolean
    isUPdepressed,
    isDOWNdepressed,
    isLEFTdepressed,
    isRIGHTdepressed,
    isSHIFTdepressed;


  private boolean drawMultiSelect;
  private Point2D multiSelectFrom;
  private Rectangle2D dragRect;

  public static WorldPresenter presenter;
  private Camera cam;
  private Point2D dragFrom;

  private boolean doMultiSelect;
  private boolean dynamicNameDrawing;

  /* Actions associated with the KeyBinding mapping framework */
  //"OVERLAY-DOC" make sure to update and read RegionViewFactory.java as well as OverlaySelect
  //Create an action for each overlay you would like
  //and update key bindings below SEARCH "KEY-BINDING-DOC"

  private Action happyOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.HAPPINESS);
    }
  };
  private Action nourishmentOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.NOURISHMENT);
    }
  };
  private Action precipitationOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.PRECIPITATION);
    }
  };
  private Action defaultOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.NONE);
    }
  };
  private Action plantingZoneOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.PLANTING_ZONE);
    }
  };
  private Action soilTypeOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.SOIL_TYPE);
    }
  };
  private Action populationOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.POPULATION);
    }
  };
  private Action ageOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.AGE);
    }
  };
  private Action cornOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.CORN);
    }
  };
  private Action wheatOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.WHEAT);
    }
  };
  private Action riceOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.RICE);
    }
  };
  private Action soyOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.SOY);
    }
  };
  private Action otherOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.OTHER);
    }
  };
  private Action organicOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.ORGANIC);
    }
  };
  private Action birthRateOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.BIRTHS);
    }
  };
  private Action avgTemperatureOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.AVG_TEMPERATURE);
    }
  };
  private Action highTemperatureOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.HIGH_TEMPERATURE);
    }
  };
  private Action lowTemperatureOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.LOW_TEMPERATURE);
    }
  };
  private Action migrationOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.MIGRATION);
    }
  };
  private Action mortalityOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.MORTALITY);
    }
  };
  private Action conventionalOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.CONVENTIONAL);
    }
  };
  private Action gmoOverlay = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setCurrentOverlay(RegionViewFactory.Overlay.GMO);
    }
  };
  // test key binding
  public static Action stepWorld = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      presenter.setWorldForward(365);
    }
  };

  /**
   * Instantiate this MapPane with a Camera to provide transforms and a
   * WorldPresenter to provide an interface into the model space
   * @param cam   Camera controlling the map transformations
   * @param presenter   WorldPresenter to
   */
  public MapPane(Camera cam, WorldPresenter presenter,Dimension dim,int feedPanelHeight)
  {
    this.cam = cam;
    this.presenter = presenter;
    this.heightAdjust = feedPanelHeight;

    /* 'this' handles most of its controls, for convenience */
    addMouseListener(this);
    addMouseWheelListener(this);
    addMouseMotionListener(this);
    addKeyListener(this);

    setBackground(ColorsAndFonts.OCEANS);
    dynamicNameDrawing = true; /* more like sexyNameDrawing */
    setPreferredSize(dim);
    setLocation(0,0);
    setDoubleBuffered(true);

    // set up keybindings. "KEY-BINDING-DOC"
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('1'), "default");
    getActionMap().put("default", defaultOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('2'), "cornview");
    getActionMap().put("cornview", cornOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('3'), "wheatview");
    getActionMap().put("wheatview", wheatOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('4'), "riceview");
    getActionMap().put("riceview", riceOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('5'), "soyview");
    getActionMap().put("soyview", soyOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('6'), "otherview");
    getActionMap().put("otherview", otherOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('7'), "organicview");
    getActionMap().put("organicview", organicOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('8'), "conventionalview");
    getActionMap().put("conventionalview", conventionalOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('9'), "gmoview");
    getActionMap().put("gmoview", gmoOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('q'), "avgtemperatureview");
    getActionMap().put("avgtemperatureview", avgTemperatureOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('w'), "hightemperatureview");
    getActionMap().put("hightemperatureview", highTemperatureOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('e'), "lowtemperatureview");
    getActionMap().put("lowtemperatureview", lowTemperatureOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('r'), "precipitationview");
    getActionMap().put("precipitationview", precipitationOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('t'), "happy");
    getActionMap().put("happy", happyOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('y'), "nourishmentview");
    getActionMap().put("nourishmentview", nourishmentOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('u'), "populationview");
    getActionMap().put("populationview", populationOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('i'), "ageview");
    getActionMap().put("ageview", ageOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('o'), "birthview");
    getActionMap().put("birthview", birthRateOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('p'), "migrationview");
    getActionMap().put("migrationview", migrationOverlay);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('a'), "mortalityview");
    getActionMap().put("mortalityview", mortalityOverlay);

    /* OSX quirk, maybe: "4" does not fire repeatedly on hold, regardless of
       modifiers (e.g. "pressed"). This holds for all single keys tested.
       When modified by a "shift", holding will fire events repeatedly, and allow
       for world stepping (and demise) at an accelerated rate */
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("shift pressed s"), "step");
    //getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke('s'), "step");
    getActionMap().put("step", stepWorld);

  }

  /**
   @return if MapPane is drawing names dynamically (based on heuristic estimate
   of screen space occupied by a region
   */
  public boolean isDynamicNameDrawing()
  {
    return dynamicNameDrawing;
  }

  /**
   Set the name drawing mode for regions.  True -> names are drawn when regions
   occupy a reasonable proportion of the screen, False -> constant name drawing
   based simply on Camera height
   @param dynamicNameDrawing  True if names are to be drawn dynamically
   */
  public void setDynamicNameDrawing(boolean dynamicNameDrawing)
  {
    this.dynamicNameDrawing = dynamicNameDrawing;
  }


  /**
   Overridden paintComponent handles all map drawing.  Model-dependent drawing
   is handled by the GUIRegions and their associated RegionViews.  Interface
   dependent drawing logic is handled in this class
   @param g Graphics context to draw to
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    if (cam.getDistance() == Camera.CAM_DISTANCE.CLOSE_UP || cam.getDistance() == Camera.CAM_DISTANCE.MEDIUM) {
      //for antialiasing
     // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
    }
    g2.setTransform(cam.getTransform());

    Collection<GUIRegion> regionsToDraw = presenter.getRegionsInView(cam);
    NavMap.updateLocation(presenter.getViewBox(cam));

    for (GUIRegion region : regionsToDraw)
    {
      region.draw(g2);
    }

    if (dynamicNameDrawing)
    {
      double screenArea = cam.getViewBounds().getWidth() * cam.getViewBounds().getWidth();
      for (GUIRegion region : regionsToDraw)
      {
        for( Polygon p: region.getPoly() )
        {
          if (screenArea / region.getSurfaceArea(p) < MAP_VISIBILITY_SCALE)
          {
            RegionNameDraw.draw(g2, region, p);
          }
        }
      }
    }
    else
    {
      if (cam.getDistance() != Camera.CAM_DISTANCE.LONG)
      {
        for (GUIRegion region : regionsToDraw) {
          for( Polygon p: region.getPoly())
          {
            RegionNameDraw.draw(g2, region, p);
          }
        }
      }
    }

    if (drawMultiSelect)
    {
      g2.setTransform(new AffineTransform()); /* reset transform! */
      drawDragRect(g2);
    }
  }

  /*
    draws the rectangle representing the multi-select rectangle using pleasant
    transparencies
   */
  private void drawDragRect(Graphics2D g2)
  {
    g2.setColor(ColorsAndFonts.SELECT_RECT_OUTLINE);
    g2.draw(dragRect);
    g2.setColor(ColorsAndFonts.SELECT_RECT_FILL);
    g2.fill(dragRect);
  }


  /**
   Respond to any input events that may have triggered by input events
   */
  public void update()
  {
    if (isDOWNdepressed && isSHIFTdepressed)
    {
      cam.zoomOut(ZOOM_STEP);
    }
    else if (isUPdepressed && isSHIFTdepressed)
    {
      cam.zoomIn(ZOOM_STEP);
    }
    else if (isDOWNdepressed)
    {
      cam.translateRelativeToView(0, CAMERA_STEP);
    }
    else if (isUPdepressed)
    {
      cam.translateRelativeToView(0, -CAMERA_STEP);
    }
    else if (isLEFTdepressed)
    {
      cam.translateRelativeToView(-CAMERA_STEP, 0);
    }
    else if (isRIGHTdepressed)
    {
      cam.translateRelativeToView(CAMERA_STEP, 0);
    }
    else
    {
      // do no-thing...
    }
  }


  /**
   Overridden KeyPressed sets flags that can be interpreted at the control-polling
   rate.  Flags are unset in keyReleased
   @param e KeyEvent fired by a key press
   */
  @Override
  public void keyPressed(KeyEvent e)
  {

    switch (e.getKeyCode())
    {
      case UP:
        isUPdepressed = true;
        break;
      case LEFT:
        isLEFTdepressed = true;
        break;
      case DOWN:
        isDOWNdepressed = true;
        break;
      case RIGHT:
        isRIGHTdepressed = true;
        break;
      case SHIFT:
        isSHIFTdepressed = true;
        break;
      default:
        /* do nothing.  Other keys are interpreted by keybindings
         in the inputmap for better resolution and control */
    }
  }


  @Override
  public void keyTyped(KeyEvent e)
  { /*do nothing*/ }


  /**
   Overridden keyReleased method unsets flags that indicate which key presses
   must be responded to when controls are polled
   @param e KeyEvent fired by a key release
   */
  @Override
  public void keyReleased(KeyEvent e)
  {
    switch (e.getKeyCode())
    {
      case UP:
        isUPdepressed = false;
        break;
      case LEFT:
        isLEFTdepressed = false;
        break;
      case DOWN:
        isDOWNdepressed = false;
        break;
      case RIGHT:
        isRIGHTdepressed = false;
        break;
      case SHIFT:
        isSHIFTdepressed = false;
        break;
      default:
    }
  }


  /**
   Overridden mouseClicked controls region selection, (multi/single) and can
   attempt to center the map to a click location
   @param e MouseEvent fired by a mouse click
   */
  @Override
  public void mouseClicked(MouseEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    if (e.isControlDown())
    {
      cam.centerAbsolute(mapClick.getX(), mapClick.getY());
    }
    else
    {
      if (e.isMetaDown())
      {
        presenter.appendClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
      else
      {
        presenter.singleClickAt(mapClick.getX(), mapClick.getY(), cam);
      }
    }
  }


  /**
   Overridden mousePressed method initializes variables that define the behavior
   directly after the MouseEvent fired, (e.g. whether the user is multi-selecting
   or not)
   @param e MouseEvent fired by a mouse press
   */
  @Override
  public void mousePressed(MouseEvent e)
  {
    doMultiSelect = isSHIFTdepressed;
    if (doMultiSelect) presenter.setActivelyDragging(true);
    multiSelectFrom = e.getPoint();
    dragFrom = e.getPoint();
  }


  /**
   Overridden mouseReleased method resets flags that control multi-select behavior
   @param e MouseEvent fired by a mouse release
   */
  @Override
  public void mouseReleased(MouseEvent e)
  {
    doMultiSelect = false;
    drawMultiSelect = false;
    presenter.setActivelyDragging(false);
  }


  @Override
  public void mouseEntered(MouseEvent e)
  { /* do nothing */ }


  /* create a rectangle defined by two corner points */
  private Rectangle2D rectFromCornerPoints(Point2D p1, Point2D p2)
  {
    double x = Math.min(p1.getX(), p2.getX());
    double y = Math.min(p1.getY(), p2.getY());
    double w = Math.abs(p1.getX() - p2.getX());
    double h = Math.abs(p1.getY() - p2.getY());
    return new Rectangle2D.Double(x, y, w, h);
  }


  @Override
  public void mouseExited(MouseEvent e)
  { /* do nothing */}


  /**
   Overridden mouseWheelMoved controls zooming on the map
   @param e MouseWheelEvent fired by mouse wheel motion
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    Point2D mapClick = convertToMapSpace(e.getPoint());

    /*todo: generalize conversion from wheel rotation to zoom */

    cam.zoomAbsolute(e.getPreciseWheelRotation() * SCROLL_FACTOR, mapClick.getX(), mapClick.getY());
    main.Game.repaintMapScale();
  }


  /**
   Overridden mouseDragged controls either camera panning (when no modifier key
   is held) or multi-select (when shift is held)
   @param e MouseEvent fired by a mouse drag
   */
  @Override
  public void mouseDragged(MouseEvent e)
  {

    if (doMultiSelect)
    {
      drawMultiSelect = true;
      Point2D p1 = convertToMapSpace(multiSelectFrom);
      Point2D p2 = convertToMapSpace(e.getPoint());
      dragRect = rectFromCornerPoints(multiSelectFrom, e.getPoint());
      presenter.selectAll(rectFromCornerPoints(p1, p2), cam);
    }
    else
    {
      double dx = -(e.getPoint().getX() - dragFrom.getX());
      double dy = -(e.getPoint().getY() - dragFrom.getY());
      cam.translateRelativeToView(dx, dy);
      dragFrom = e.getPoint();
    }

  }


  @Override
  public void mouseMoved(MouseEvent e)
  {/* do nothing */}


  /* helper function converts a point in screen-space to a point in map-space
     this encapsulates what is almost certainly unnecessary error handling,
     given usage of the AffineTransforms (checks NoninvertibleTransformExceptions)
   */
  private Point2D convertToMapSpace(Point2D screenClick)
  {
    AffineTransform a = cam.getTransform();
    try
    {
      a.invert();
    }
    catch (NoninvertibleTransformException e)
    {
      e.printStackTrace();
      System.exit(1);
    }
    Point2D mapClick = new Point2D.Double();
    a.transform(screenClick, mapClick);
    return mapClick;
  }
}
