package gui;

import gui.displayconverters.MapConverter;
import gui.hud.NavMap;
import gui.regionlooks.RegionView;
import model.CountryData;
import model.MiniArea;
import model.Region;
import model.WorldArray;
import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
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
    ClassLoader cl = this.getClass().getClassLoader();
    InputStream in = cl.getResourceAsStream( region.getFlag() );
    try
    {
      image = ImageIO.read(in);
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find flag image!");
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

  /**
   * Advances to the next year.
   * @param worldArray  All of the World Cells to update.
   */
  public void iterateYear(WorldArray worldArray)
  {
    /*
     * It shouldn't need a null check but for some reason I am getting a null pointer
     * exception. This fixes it and updated properly when called by the update function.
     */
    if( data != null )
    {
      data.iterateYear(worldArray, region);
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
}
