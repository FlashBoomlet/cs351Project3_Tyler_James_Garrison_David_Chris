package gui.regionlooks;


import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.WorldCell;

import java.awt.*;
import java.util.HashSet;


/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the low temperatures of the region tiles.
 */
class RegionLowTemperatureView implements RegionView
{
  private static Color[] colors = ColorsAndFonts.TEMPERATURE;
  private static double LIMIT = colors.length / 100.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion) {
    Color color = Color.CYAN;

    HashSet<WorldCell> relevantCells = gRegion.getRegion().getArableCells();
    if (!gRegion.isActive()) {
      for (WorldCell cell : relevantCells) {
        //System.out.println("lat :"+cell.getLat()+" long :"+cell.getLon());

        int select = (int) (cell.getTempAvg() * LIMIT);
        if (select < 0) select = 0;
        if (select < colors.length)
          color = colors[select];
        else
          color = colors[colors.length - 1];

        g.setColor(color);
        g.drawRect((int) cell.getLat(), (int) cell.getLon(), 2, 2);
      }
    }
  }
}
