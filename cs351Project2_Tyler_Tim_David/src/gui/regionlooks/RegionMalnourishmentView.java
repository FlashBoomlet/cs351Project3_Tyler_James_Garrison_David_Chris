package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import java.awt.*;

/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the % malnourished of the region.
 */
class RegionMalnourishmentView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.NOURISHMENT;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double nourish = 100;
    if( gRegion.getOfficialCountry() )
    {
      nourish = gRegion.getCountryData().getUndernourish(true);
    }

    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      int select = 1000;
      if( nourish < 3 ) select = 0;
      else if( nourish < 5 ) select = 1;
      else if( nourish < 10 ) select = 2;
      else if( nourish < 15 ) select = 3;
      else if( nourish < 25 ) select = 4;
      else if( nourish < 40 ) select = 5;
      else if( nourish < 50 ) select = 6;
      else if( nourish < 100 ) select = 7;
      else if( nourish >= 200 ) select = 8;


      if(select < colors.length)
        color = colors[select];
      else
        color = colors[colors.length-1];

      // If the region is not a recognized region, treat it as a dead region
      if( !gRegion.getOfficialCountry() ) {
        color = ColorsAndFonts.DEAD_REGION;
      }
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
