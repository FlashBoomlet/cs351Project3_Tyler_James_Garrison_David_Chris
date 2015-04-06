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

  public boolean isActive()
  {
    return isActive;
  }

  public void setActive(boolean isActive)
  {
    this.isActive = isActive;
  }

  public RegionView getLook()
  {
    return look;
  }

  public void setLook(RegionView look)
  {
    this.look = look;
  }

  public String getName()
  {
    return region.getName();
  }

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

  public boolean flag(){
    if( region.getFlag() != null ) return true;
    return false;
  }

  public BufferedImage getFlag()
  {
    loadFlag();
    return image;
  }

  public LinkedList<Polygon> getPoly()
  {
    polyList.clear();
    for( MiniArea MA: region.getPerimeter())
    {
      polyList.add(converter.regionToPolygon(MA));
    }
    return polyList;
  }

  public double getSurfaceArea(Polygon p)
  {
    double localSA = 0;
    localSA += p.getBounds().getWidth() * p.getBounds().getHeight();
    return localSA;
  }

  public LinkedList<Area> getArea()
  {
    for( Polygon p: getPoly() )
    {
      area.add(new Area(p));
    }
    return area;
  }

  public void draw(Graphics g)
  {
    look.draw(g, this);
  }

  public Region getRegion()
  {
    return region;
  }


  public void setCountryData(CountryData data)
  {
    region.setCountryData(data);
    this.data = data;
  }

  public CountryData getCountryData()
  {
    return data;
  }

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

  public void setOfficialCountry()
  {
    officialCountry = true;
  }

  public boolean getOfficialCountry()
  {
    return officialCountry;
  }
}
