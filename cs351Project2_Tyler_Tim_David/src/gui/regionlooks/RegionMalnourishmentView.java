package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.RegionAttributes;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.UNDERNOURISHMENT_RATE;

/**
 * Created  by David M on 3/19/2015
 */
class RegionMalnourishmentView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.NOURISHMENT;
  private static double LIMIT = colors.length / RegionAttributes.LIMITS.get(UNDERNOURISHMENT_RATE);


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double nourish = gRegion.getRegion().getAttributes().getAttribute(UNDERNOURISHMENT_RATE);
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (nourish * LIMIT);
      if(select < colors.length)
        color = colors[select];
      else
        color = colors[colors.length-1];
    }

    g.setColor(color);
    g.fillPolygon(gRegion.getPoly());

    g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
    g.drawPolygon(gRegion.getPoly());
  }
}
