package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.RegionAttributes;
import static model.RegionAttributes.PLANTING_ATTRIBUTES.*;

import java.awt.*;

/**
 * Created by winston on 2/8/15.
 *
 * Edited by David M on 3/19/2015
 */
class RegionRainView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.RAIN_FALL;
  private static double LIMIT = colors.length / RegionAttributes.LIMITS.get(ANNUAL_RAINFALL);


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double rains = gRegion.getRegion().getAttributes().getAttribute(ANNUAL_RAINFALL);
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) (rains * LIMIT);
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
