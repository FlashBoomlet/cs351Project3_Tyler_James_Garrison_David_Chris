package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created  by David M on 3/22/2015
 */
class RegionConventionalView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.CONVENTIONAL;
  private static double LIMIT = colors.length;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double land = 0.0;
    if( gRegion.getOfficialCountry() ) {
      land = gRegion.getCountryData().getConventional(true);
    }
    Color color = Color.cyan;
    if (!gRegion.isActive())
    {
      int select = (int) (land * LIMIT);
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
