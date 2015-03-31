package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the % corn of the regions.
 */
class RegionWheatView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.WHEAT;
  private static double LIMIT = colors.length / 1.0;

  /**
   * Method takes % corn from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double limit = colors.length / gRegion.getRegion().getAttributes().getCropP("Arable land");

    if (gRegion == null || gRegion.getRegion().getAttributes() == null) {
      System.err.println("(!) GUI REGION or attribute set is null!");
      return;
    }

    double corn = gRegion.getRegion().getAttributes().getCropP("Corn");
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (corn * limit);
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
