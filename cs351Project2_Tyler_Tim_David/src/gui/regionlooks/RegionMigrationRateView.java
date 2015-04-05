package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created  by David M on 3/22/2015
 */
class RegionMigrationRateView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.MIGRATION;
  private static double LIMIT = colors.length / 120.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double rate = 0.0;
    if( gRegion.getOfficialCountry() ) {
      rate = gRegion.getCountryData().getMigration(true)+60.0;
      if(rate <0.0) rate = 0.0;
    }
    Color color = Color.cyan;
    if (!gRegion.isActive())
    {
      int select = (int) (rate * LIMIT);
      if(select<0) select = 0;
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
