package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;


/**
 * Created by David M. on 3/22/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the soil types of the regions.
 */
class RegionSoilTypeView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.SOIL;

  /**
   * Method takes soil types from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double soil = 0.0;
    double limit = 0.0;

    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (soil * limit);
      if(select < colors.length)
        color = colors[select];
      else
        color = colors[colors.length-1];
    }

    for( Polygon p: gRegion.getPoly() )
    {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
      g.drawPolygon(p);
    }
  }
}
