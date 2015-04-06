package gui.regionlooks;


import gui.Camera;
import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import model.WorldCell;
import gui.displayconverters.EquirectangularConverter;
import java.awt.*;
import java.util.HashSet;


/**
 * Created by David M. on 3/31/2015
 * CS 351 spring 2015
 * <p/>
 * Represents the average temperatures of the tiles.
 */
class RegionAverageTemperatureView implements RegionView
{
  private static EquirectangularConverter converter = new EquirectangularConverter();
  private static Color[] colors = ColorsAndFonts.TEMPERATURE;
  private static double LIMIT = colors.length / 100.0;

  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Color color = Color.white;

    for( Polygon p: gRegion.getPoly() )
    {
      g.setColor(color);
      g.fillPolygon(p);
      g.setColor(ColorsAndFonts.PASSIVE_REGION_OUTLINE);
      g.drawPolygon(p);
    }

    HashSet<WorldCell> relevantCells = gRegion.getRegion().getArableCells();
    if (!gRegion.isActive() && WorldPresenter.calcDistance(main.Game.getCamera()) == Camera.CAM_DISTANCE.CLOSE_UP )
    {
      for(WorldCell cell: relevantCells){

        int select = (int) (cell.getTempAvg() * LIMIT);
        if(select<0) select = 0;
        if(select < colors.length)
          color = colors[select];
        else
          color = colors[colors.length-1];

        color = Color.red;

        g.setColor(color);
        g.fillOval((int) converter.lonToX(cell.getLon()), (int) converter.latToY(cell.getLat()),990,680);
      }
    }

  }
}
