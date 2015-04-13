package model.common;

/**
 * Created by Tim on 4/5/15.
 * Contains the ideal conditions for the
 * four crops.
 */
public interface CropIdeals
{
  public final int WHEAT_AVG_HIGH = 24;
  public final int WHEAT_AVG_LOW = 21;
  public final int WHEAT_MAX_HIGH = 35;
  public final int WHEAT_MAX_LOW = 4;
  public final int WHEAT_RAIN_HIGH = 38;
  public final int WHEAT_RAIN_LOW = 31;

  public final int RICE_AVG_HIGH = 27;
  public final int RICE_AVG_LOW = 21;
  public final int RICE_MAX_HIGH = 27;
  public final int RICE_MAX_LOW = 15;
  public final int RICE_RAIN_HIGH = 300;
  public final int RICE_RAIN_LOW = 115;

  public final int CORN_AVG_HIGH = 35;
  public final int CORN_AVG_LOW = 5;
  public final int CORN_MAX_HIGH = 45;
  public final int CORN_MAX_LOW = 0;
  public final int CORN_RAIN_HIGH = 50;
  public final int CORN_RAIN_LOW = 45;

  public final int SOY_AVG_HIGH = 35;
  public final int SOY_AVG_LOW = 18;
  public final int SOY_MAX_HIGH = 40;
  public final int SOY_MAX_LOW = 15;
  public final int SOY_RAIN_HIGH = 70;
  public final int SOY_RAIN_LOW = 45;
}
