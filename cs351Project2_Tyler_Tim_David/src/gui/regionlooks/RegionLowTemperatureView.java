package gui.regionlooks;


import gui.Camera;
import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.displayconverters.EquirectangularConverter;
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
  private static EquirectangularConverter converter = new EquirectangularConverter();
  private static Color[] colors = ColorsAndFonts.TEMPERATURE;
  private static double LIMIT = colors.length / 100.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Rectangle cameraBounds = main.Game.getCamera().getViewBounds().getBounds();
    for (Polygon p : gRegion.getPoly()) {
      if(cameraBounds.intersects(p.getBounds())) {
        Color color = Color.white;
        if (gRegion.isActive()) color = Color.cyan;

        g.setColor(color);
        g.fillPolygon(p);
        g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
        g.drawPolygon(p);

        Camera.CAM_DISTANCE distance = WorldPresenter.calcDistance(main.Game.getCamera());
        if (!gRegion.isActive() && distance == Camera.CAM_DISTANCE.CLOSE_UP || distance == Camera.CAM_DISTANCE.MEDIUM) {
          HashSet<WorldCell> relevantCells = gRegion.getRegion().getArableCells();
          for (WorldCell cell : relevantCells) {
            int cellX = (int) converter.lonToX(cell.getLon());
            int cellY = (int) converter.latToY(cell.getLat());
            if(cameraBounds.contains(cellX, cellY)){
              System.out.println(cell.getAnnualLow());
              int select = (int) ((cell.getAnnualLow() + 50.0) * LIMIT);
              if (select < 0) select = 0;
              if (select < colors.length)
                color = colors[select];
              else
                color = colors[colors.length - 1];

              g.setColor(color);
              g.fillOval(cellX, cellY, 990, 680);
            }
          }
        }
      }
    }
  }
}
