package gui;

import gui.displayconverters.MapConverter;
import gui.hud.NavMap;
import gui.regionlooks.RegionView;
import model.Region;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Area;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 */
public class GUIRegion
{
  private Region region;
  private MapConverter converter; //set at class def.
  private Area area;
  private boolean isActive;
  private RegionView look;
  private Polygon poly;
  private BufferedImage image;

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

  public Polygon getPoly()
  {
    if (poly == null)
    {
      poly = converter.regionToPolygon(region);
    }
    return poly;
  }

  public double getSurfaceArea()
  {
    return getPoly().getBounds().getWidth() * getPoly().getBounds().getHeight();
  }

  public Area getArea()
  {
    if (area == null)
    {
      area = new Area(getPoly());
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
}
