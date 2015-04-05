package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.WorldCell;

import java.awt.*;
import java.util.HashSet;


/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the temperatures of the regions.
 */
class RegionTemperatureView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.TEMPERATURE;
  private static double LIMIT = 0; //colors.length / RegionAttributes.LIMITS.get(AVE_MONTH_TEMP_HI);

  /**
   * Method takes temperatures from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    if (gRegion == null ) //|| gRegion.getRegion().getAttributes() == null)
    {
      System.err.println("(!) GUI REGION or attribute set is null!");
      return;
    }
    HashSet<WorldCell> relevantCells = gRegion.getRegion().getArableCells();
    for(WorldCell cell: relevantCells){
      System.out.println("lat :"+cell.getLat()+" long :"+cell.getLon());
    }
    double temp = 0; //gRegion.getRegion().getAttributes().getAttribute(AVE_MONTH_TEMP_HI);
    double low = 0; //gRegion.getRegion().getAttributes().getAttribute(AVE_MONTH_TEMP_LO); // is 0.0 right now...
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (temp * LIMIT);
      if(select<0) select = 0;
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
