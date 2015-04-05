package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the % soy of the regions.
 */
class RegionSoyView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.SOY;

  /**
   * Method takes % soy from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double limit = 0; //colors.length / gRegion.getRegion().getAttributes().getCropP("Arable land");

    if (gRegion == null ){ //|| gRegion.getRegion().getAttributes() == null) {
      System.err.println("(!) GUI REGION or attribute set is null!");
      return;
    }

    double soy = 0; //gRegion.getRegion().getAttributes().getCropP("Soy");
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (soy * limit);
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
