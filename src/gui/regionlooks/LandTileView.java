package gui.regionlooks;


import gui.GUIRegion;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import model.LandTile;
import model.TileManager;

import java.awt.*;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;


/**
 @author David Ringo
 created: 2015.05.05
 class:   cs351
 project: cs351Project3_Tyler_James_Garrison_David_Chris
 description:

 General RegionView for viewing LandTile data fields.  Implemented for the four views originally
 in Tyler, Tim and David's codebase.  Others can be added too.
 Basic strategy is to find minimum and maximum values for a given field and scale colors through
 either a restricted range of hues in the HSL colorspace or through greyscale, where white
 represents the max and black the min.
 */
public class LandTileView implements RegionView
{

  private final MapConverter converter = new EquirectangularConverter();
  private final LandTile.FIELD field;
  private float max;
  private float min;
  private boolean init = true;


  public LandTileView(LandTile.FIELD field)
  {
    this.field = field;
  }


  @Override
  public String toString()
  {
    return "LandTileView: " + field;
  }


  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Graphics2D g2 = (Graphics2D) g;
    if (init)
    {
      init = false;
      max = LandTile.getMax(field);
      min = LandTile.getMin(field);
    }
    for (LandTile tile : gRegion.getLandTiles())
    {
      Point2D pt = converter.mapPointToPoint(tile.getCenter());
      double width = converter.lonToX(TileManager.DLON) * 1.3;
      double height = -converter.latToY(TileManager.DLAT) * 1.3;
      height = height * (1 + Math.abs(Math.sin(Math.toRadians(tile.getLat()))));

      g2.setColor(getScaledColor(tile, field, min, max));
      g2.fill(new Ellipse2D.Double(pt.getX() - width / 2, pt.getY() - height / 2, width, height));
    }
  }


  private static Color getScaledColor(LandTile tile, LandTile.FIELD field, float min, float max)
  {
    float scale = (tile.getData(field) - min) / (max - min);
    switch (field)
    {
      case CURRENT_ANNUAL_PRECIPITATION:
        return Color.getHSBColor((float) (Math.sqrt(Math.log(scale+1))), 0.4f, 0.8f);
      case CURRENT_ANNUAL_MEAN_TEMPERATURE:
        return Color.getHSBColor(scale*.4f+.18f, 0.5f, 0.8f);
      case CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH:
        return Color.getHSBColor(scale*.3f+.5f, 0.4f, 0.8f);
      case CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH:
        return Color.getHSBColor(scale*.25f, 0.4f, 0.8f);
    }
    float val = (tile.getData(field) - min) / (max - min);
    return new Color(val, val, val);

  }

}
