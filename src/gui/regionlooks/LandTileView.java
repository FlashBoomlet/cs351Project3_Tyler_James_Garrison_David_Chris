package gui.regionlooks;


import gui.GUIRegion;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import model.LandTile;
import model.TileManager;
import model.common.EnumCropType;

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
  private final LandTile.Field field;
  private float max;
  private float min;
  private boolean init = true;


  /**
   Construct a new LandTileView for a given Field
   @param field Field to be rendered by this view
   */
  public LandTileView(LandTile.Field field)
  {
    this.field = field;
  }


  /**
   @return identifying String
   */
  @Override
  public String toString()
  {
    return "LandTileView: " + field;
  }


  /**
   Specify drawing behavior for the LandTileView
   @param g       Graphics context to draw on
   @param gRegion GuiRegion to render
   */
  @Override
  public void draw(Graphics g, GUIRegion gRegion)
  {
    Graphics2D g2 = (Graphics2D) g;

    /* get range of values if not already determined.
      Range is read from disk when LandTiles are loaded, so order of operations matters, and with
      all RegionViews being made in a static context (see RegionViewFactory) the *correct*
      ordering is probably not guaranteed.  Hence the initialization check here.
      Range is not read on demand, since this method is called NumberOfCountries *
      NumberOfRepaintsPerSecond times per second, and involves quick, but not insignificant
      hashmap lookups.
     */
    if (init)
    {
      init = false;
      max = LandTile.getMax(field);
      min = LandTile.getMin(field);
    }


    boolean active = gRegion.isActive();

    for (LandTile tile : gRegion.getLandTiles())
    {
      Point2D pt = converter.mapPointToPoint(tile.getCenter());

      /* data is coarse.  Try to fill the gaps appropriately */
      double width = converter.lonToX(TileManager.DLON) * 1.3;
      double height = -converter.latToY(TileManager.DLAT) * 1.3;

      /* scale the height to counter Equal Area projection distortion used in TileManager */
      height = height * (1 + Math.abs(Math.sin(Math.toRadians(tile.getLat()))));

      /* make the pretty colors...make it lighter if region is active */
      Color color;
      if(field == LandTile.Field.CROP_TYPE) color = getScaledColor(tile.getCrop(), tile);
      else color = getScaledColor(tile.getData(field), field, min, max);
      if(active) color = color.brighter();

      /* file the shape */
      g2.setColor(color);
      g2.fill(new Ellipse2D.Double(pt.getX() - width / 2, pt.getY() - height / 2, width, height));
    }
  }


  /* make a color for a value somewhere in between min and max values, given the Field from which
  that value came
   */
  private static Color getScaledColor(float data, LandTile.Field field, float min, float max)
  {
    float scale = (data - min) / (max - min);
    switch (field)
    {

      /* four defined cases: values are not awful. Mostly calibrated by eyeballing the hue spectrum
      and trial and error */
      case CURRENT_ANNUAL_PRECIPITATION:
        return Color.getHSBColor((float) (Math.sqrt(Math.log(scale+1))), 0.4f, 0.8f);
      case CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH:
        return Color.getHSBColor(scale*.3f+.5f, 0.4f, 0.8f);


      /* Mean and Max temps use inverted scale.  Makes colors more intuitive, says I */
      case CURRENT_ANNUAL_MEAN_TEMPERATURE:
      return Color.getHSBColor((-scale + 1)*.5f+.1f, 0.5f, 0.8f);
      case CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH:
        return Color.getHSBColor((-scale + 1)*.25f, 0.4f, 0.8f);


      default:
        /* default behavior makes a greyscale color */
        return new Color(scale, scale, scale);
    }
  }

  private static Color getScaledColor(EnumCropType c, LandTile tile)
  {
    Color color;
    switch(c)
    {
      case CORN:
        color = new Color(155, 151, 53);
        break;
      case SOY:
        color = new Color(105, 234, 100);
        break;
      case WHEAT:
        color = new Color(207, 160, 122);
        break;
      case RICE:
        color = new Color(62, 163, 193);
        break;
      case OTHER_CROPS:
        color = new Color(167, 142, 209);
        break;
      default:
        color = Color.white;
        break;
    }
    return color;
  }
}
