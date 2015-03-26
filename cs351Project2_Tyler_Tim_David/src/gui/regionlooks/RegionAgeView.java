package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.RegionAttributes;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.MEDIAN_AGE;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.POPULATION;

/**
 * Created  by David M on 3/22/2015
 */
class RegionAgeView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.AGE;
  private static double LIMIT = colors.length / RegionAttributes.LIMITS.get(MEDIAN_AGE);

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double ages = 100 * gRegion.getRegion().getAttributes().getAttribute(MEDIAN_AGE);
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (ages * LIMIT);
      if(select < colors.length)
        color = colors[select];
      else
        color = colors[colors.length-1];
    }

    g.setColor(color);
    for( Polygon p: gRegion.getPoly() )
    {
      g.fillPolygon(p);
      g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
      g.drawPolygon(p);
    }
  }
}
