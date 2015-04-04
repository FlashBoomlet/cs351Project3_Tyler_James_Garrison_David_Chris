package gui.regionlooks;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.RegionAttributes;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.POPULATION;

/**
 * Created  by David M on 3/22/2015
 */
class RegionPopulationView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.POPULATION;
  private static double LIMIT = colors.length / 100_000_000.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    double pops = 100_000_000.0;
    if( gRegion.getOfficialCountry() ) {
      pops = gRegion.getCountryData().getPopulation(true);
    }
    Color color = Color.YELLOW;
    if (gRegion.isActive())
    {
      color = new Color(0x00EE00);
    }
    else
    {
      int select = (int) (pops * LIMIT);
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
