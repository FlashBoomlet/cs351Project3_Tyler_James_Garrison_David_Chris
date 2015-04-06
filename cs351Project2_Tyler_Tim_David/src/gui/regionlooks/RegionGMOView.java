package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the % GMO use of the region.
 */
class RegionGMOView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.GMO;
  private static double LIMIT = colors.length;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double land = 0.0;
    if( gRegion.getOfficialCountry() ) {
      land = gRegion.getCountryData().getGmo(true);
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
