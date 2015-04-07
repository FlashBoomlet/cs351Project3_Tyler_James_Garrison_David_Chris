package gui;

import gui.displayconverters.MapConverter;
import gui.hud.NavMap;
import gui.regionlooks.RegionView;
import model.*;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 * Restructured by:
 * @author Tyler Lynch <lyncht@unm.edu>
 *
 * Structure for regions to be displayed by the GUI, dependent on
 * AtomicRegions.
 */
public class GUIRegion
{
  private Region region;
  private MapConverter converter; //set at class def.
  private LinkedList<Area> area = new LinkedList<>();
  private boolean isActive;
  private RegionView look;
  private LinkedList<Polygon> polyList = new LinkedList<>();
  private BufferedImage image;
  private boolean officialCountry = false;
  private CountryData data = null;
  private double[] centerOfShipping = new double[2];
  // Could be more advance in the future
  public static ArrayList<GUIRegion> exportedTo = new ArrayList<>();
  public static ArrayList<GUIRegion> importedFrom = new ArrayList<>();
  public static Collection<GUIRegion> preferedTrade = main.Game.getWorldPresenter().getAllRegions();

  /**
   * Loads the region's flag.
   * @param region      The appropriate Region.
   * @param converter   The appropriate MapConverter.
   * @param look        The appropriate RegionView
   */
  public GUIRegion(Region region, MapConverter converter, RegionView look)
  {
    this.region = region;
    this.converter = converter;
    this.look = look;

    if( region.getFlag() != null )
    {
      //For error catching data
      //System.out.println("Loading " + region.getName() + ": " + region.getFlag());
      loadFlag();
    }
  }

  public void setLandCells(WorldArray worldArray)
  {
    region.setLandCells(worldArray, converter);
  }

  /**
   * Get the center of shipping location
   * @return array where index 0 is the latitude location and index 1 is the longitude location
   */
  public double[] getCenterOfShipping()
  {
    centerOfShipping[0] = data.getShippingLat();
    centerOfShipping[1] = data.getShippingLon();
    return centerOfShipping;
  }

  /**
   * @return  Is region active.
   */
  public boolean isActive()
  {
    return isActive;
  }

  /**
   * Sets the region to active or not.
   * @param isActive  Whether the region should be active or not.
   */
  public void setActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  /**
   * @return  The region's view.
   */
  public RegionView getLook()
  {
    return look;
  }

  /**
   * Set the Region's view.
   * @param look  The RegionView
   */
  public void setLook(RegionView look)
  {
    this.look = look;
  }

  public String getName()
  {
    return region.getName();
  }

  /**
   * Loads the region's flag.
   */
  public void loadFlag() {
    try
    {
      image = ImageIO.read(new File(region.getFlag()));
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find flag image for" + getName() );
    }
  }

  /**
   * @return Returns if the region has a flag.
   */
  public boolean flag(){
    if( region.getFlag() != null ) return true;
    return false;
  }

  /**
   * @return Returns the region's flag.
   */
  public BufferedImage getFlag()
  {
    loadFlag();
    return image;
  }

  /**
   * @return  The Polygon representing the region.
   */
  public LinkedList<Polygon> getPoly()
  {
    polyList.clear();
    for( MiniArea MA: region.getPerimeter())
    {
      polyList.add(converter.regionToPolygon(MA));
    }
    return polyList;
  }

  /**
   * Returns the surface area of the polygon passed in.
   * @param p The surface area.
   * @return
   */
  public double getSurfaceArea(Polygon p)
  {
    double localSA = 0;
    localSA += p.getBounds().getWidth() * p.getBounds().getHeight();
    return localSA;
  }

  /**
   * @return  The region's list of AtomicMiniAreas.
   */
  public LinkedList<Area> getArea()
  {
    for( Polygon p: getPoly() )
    {
      area.add(new Area(p));
    }
    return area;
  }

  /**
   * Draws the region.
   * @param g Graphics
   */
  public void draw(Graphics g)
  {
    look.draw(g, this);
  }

  /**
   * Returns the AtomicRegion of this region.
   * @return  AtomicRegion
   */
  public Region getRegion()
  {
    return region;
  }

  /**
   * Sets the data of the country of this
   * and its region.
   * @param data
   */
  public void setCountryData(CountryData data)
  {
    region.setCountryData(data);
    this.data = data;
  }

  /**
   * @return  The Country Data of this region.
   */
  public CountryData getCountryData()
  {
    return data;
  }

  private NextYear ny;
  /**
   * Advances to the next year.
   */
  public void iterateYear()
  {
    ny = new NextYear();
  }

  public void startIterate()
  {
    ny.start();
  }

  private class NextYear extends Thread
  {
    private Thread t;
    /* Empty Constructor */
    private NextYear() {}

    public void run()
    {
      try
      {
        /*
         * It shouldn't need a null check but for some reason I am getting a null pointer
         * exception. This fixes it and updated properly when called by the update function.
         */
        if( data != null ) {
          data.iterateYear(region);
          synchronized (World.class)
          {
            World.doneUpdate();
          }
        }
      }
      catch (Exception e) {}
    }
    /**
     * start overrides Thread's start
     * Creates new Thread and then starts said Thread
     */
    public void start()
    {
      if (t == null)
      {
        t = new Thread(this, (String) getName());
        t.start ();
      }
    }
  }


  /**
   * Sets to an official country.
   */
  public void setOfficialCountry()
  {
    officialCountry = true;
  }

  /**
   * @return  Whether an official country or not.
   */
  public boolean getOfficialCountry()
  {
    return officialCountry;
  }

  public boolean canExport()
  {
    if( data.getTotalExport() > data.getTotalImport() ) return true;
    return false;
  }
}
