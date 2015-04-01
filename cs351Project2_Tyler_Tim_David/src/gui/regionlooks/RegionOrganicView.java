package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the % organic of the regions.
 */
class RegionOrganicView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.ORGANIC;

  /**
   * Method takes % organic from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double limit = colors.length /0.4;

    if (gRegion == null || gRegion.getRegion().getAttributes() == null) {
      System.err.println("(!) GUI REGION or attribute set is null!");
      return;
    }

    double organic = gRegion.getRegion().getAttributes().getCropP("Organic Land");
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (organic * limit);
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
