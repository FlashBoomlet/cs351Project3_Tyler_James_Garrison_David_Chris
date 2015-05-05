package gui.regionlooks;


import gui.Camera;
import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import model.LandTile;
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

    for(LandTile tile : gRegion.getLandTiles())
    {

    }
  }

}
