package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import java.awt.*;


/**
 * Created  by David M on 3/22/2015
 * <p/>
 * Over lay view. Expresses the median age in the below divergent color
 * spectrum.
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
