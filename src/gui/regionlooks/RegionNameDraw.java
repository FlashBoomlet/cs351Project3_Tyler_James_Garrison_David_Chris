package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * 
 * RegionView that draws the names of Regions somewhere near the center of the
 * region via a static method, which is called in an order or with an appropriate
 * graphics context to allow the names to be seen above other display elements 
 */
public class RegionNameDraw
{

  /**
   Draw the name of a GUIRegion on a given graphics context
   @param g   graphics context to draw to
   @param gRegion   GUIRegion whose name is to be drawn
   */
  public static void draw(Graphics g, GUIRegion gRegion, Polygon p)
  {
    Graphics2D g2d = (Graphics2D) g;

    g2d.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON
    );

    /* save and temporarily reset the graphics transform */
    AffineTransform at = g2d.getTransform();
    g2d.setTransform(new AffineTransform());
    
    /* font in terms of screen-space */
    g2d.setFont(ColorsAndFonts.NAME_VIEW);
    g2d.setColor(ColorsAndFonts.REGION_NAME_FONT_C);

    /* find ~center of region in map-space */
    int x = 0;
    int y = 0;
    int total = 0;

    //Loop through and find polygon with the biggest area and Center on that flag?
    //Another option is average the value based on if there is an area
    /*
    for( Polygon p: gRegion.getPoly())
    {
      total ++;
      x += (int) p.getBounds().getCenterX();
      y += (int) p.getBounds().getCenterY();
    }
    //Find the average x and y coordinates
    x /= total;
    y /= total;
    */

    x += (int) p.getBounds().getCenterX();
    y += (int) p.getBounds().getCenterY();

    Point src = new Point(x,y);
    Point dst = new Point();
    
    try{
      /* convert center point to screen space for drawing */
      AffineTransform atINV = new AffineTransform(at);
      atINV.invert();
      at.transform(src, dst);
    } 
    catch (NoninvertibleTransformException e)
    { /* should never happen: all map transforms should be invertible */
      e.printStackTrace();
    }

    if( gRegion.getName() != null ) g2d.drawString(gRegion.getName(), dst.x, dst.y);
    else g2d.drawString("NAME NOT FOUND IN gRegion", dst.x, dst.y);

    if( gRegion.flag() )
    {
      g2d.drawImage(gRegion.getFlag(), dst.x+5,dst.y+5,30,20,null);
    }
    /* reset the transform for any RegionViews depending on it for proper rendering */
    g2d.setTransform(at);
  }
}
