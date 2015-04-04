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
  // 122 is the oldest man to ever live to date 4/4/2015
  private static double LIMIT = colors.length / 122.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double ages = 122;
    if( gRegion.getOfficialCountry() ) {
      ages = gRegion.getCountryData().getMedianAge(true);
    }
    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = (int) ((ages) * LIMIT);
      if(select < colors.length)
        color = colors[select];
      else
        color = colors[colors.length-1];
    }

    // If the region is not a recognized region, treat it as a dead region
    if( !gRegion.getOfficialCountry() ) {
      color = ColorsAndFonts.DEAD_REGION;
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
