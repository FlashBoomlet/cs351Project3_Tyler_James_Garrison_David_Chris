package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;

import java.awt.*;

import static model.RegionAttributes.PLANTING_ATTRIBUTES.HAPPINESS;

/**
 * Created by winston on 1/27/15.
 * Phase_01
 * CS 351 spring 2015
 * <p/>
 * Represents the happiness index of the regions.
 */
class RegionHappyView implements RegionView
{
  /**
   * Method extracts happiness index from region and displays it.
   *
   * @param g
   * @param gRegion
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    if (gRegion == null || gRegion.getRegion().getAttributes() == null)
    {
      System.err.println("(!) GUI REGION or attribute set is null!");
      return;
    }

    Color color;
    if (gRegion.isActive())
    {
      color = Color.CYAN;
    }
    else
    {
      double happinessLevel = gRegion.getRegion().getAttributes().getAttribute(HAPPINESS);

      if  (happinessLevel< 0.25)
      {
        color = new Color((float) (happinessLevel*3.3), (float) 0.0, (float) 0.0);
        //color = Color.RED;
      }
      else if (happinessLevel<0.5)
      {
        color = new Color((float) (happinessLevel*2), (float) (happinessLevel*2), (float) 0.0);
        //color = Color.YELLOW;
      }
      else
      {
        color = new Color((float) 0, (float) happinessLevel, (float) 0.0);
        //color = Color.GREEN;
      }
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
