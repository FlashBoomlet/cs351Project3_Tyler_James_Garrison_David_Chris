package model;


import model.common.EnumCropType;

import static model.LandTile.Field.*;
import static model.common.EnumCropType.*;

import java.nio.ByteBuffer;
import java.util.*;


/**
 @author david
 created: 2015-03-21
 <p/>
 description:
 LandTile class describes a single section of the equal area projection of the
 world map.  It holds elevation, climate and climate projection data found
 at www.worldclim.org.  The class also describes a ByteBuffer format for a
 custom binary file.  This allows the data from the raw sets to be parsed, projected
 and averaged across equal-area sections of the globe once.  This data can then
 simply be loaded in at the program start. (see CropZoneDataIO class) */

public class LandTile
{

  public static final LandTile NO_DATA = new LandTile(-180, 0); /* in Pacific, no man's land */
  private static final Map<Field, Float> maxVals = new HashMap<>();
  private static final Map<Field, Float> minVals = new HashMap<>();
  private static final Random RNG = new Random();
  private static final float RANDOM_VARIANCE = 0.5f;
  private static final double RANDOMNESS_FACTOR = 1d;

  private final Map<Field, Float> floatData = new HashMap<>();
  private final Map<Field, Integer> intData = new HashMap<>();

  private MapPoint center;
  private EnumCropType currentCrop = NONE;
  private float cropState;
  private float currentCropPenalty;


  /**
   Constructor used for initial creation of data set

   @param lon
   longitude of this LandTile
   @param lat
   latitude of this LandTile
   */
  public LandTile(double lon, double lat)
  {
    floatData.put(LONGITUDE, (float) lon);
    floatData.put(LATITUDE, (float) lat);
    center = new MapPoint(lon, lat);
  }


  /**
   Constructor to use when reading in ByteBuffers from LandTileIO class.

   @param buf
   ByteBuffer holding the data from which this LandTile should be created
   */
  public LandTile(ByteBuffer buf)
  {
    int len = buf.array().length;
    if (len != SIZE_IN_BYTES)
    {
      throw new IllegalArgumentException(
          String.format("ByteBuffer of incorrect size%n" +
                        "Expected: %d%n" +
                        "Received: %d%n", SIZE_IN_BYTES, len));
    }

    for (Field field : Field.byteBufferValues())
    {
      if (isFloatField(field))
      {
        float val = buf.getFloat(field.byteIndex());
        floatData.put(field, val);

        Float max = maxVals.get(field);
        if (max == null || val > max) maxVals.put(field, val);

        Float min = minVals.get(field);
        if (min == null || val < min) minVals.put(field, val);
      }
      else
      {
        intData.put(field, buf.getInt(field.byteIndex()));
      }
    }
    center = new MapPoint(floatData.get(LONGITUDE), floatData.get(LATITUDE));
  }


  public static float getMax(Field field)
  {
    Float val = maxVals.get(field);
    return val == null ? 0 : val;
  }


  public static float getMin(Field field)
  {
    Float val = minVals.get(field);
    return val == null ? 0 : val;
  }


  /**
   Find an interpolated value given extremes of the range across which to
   interpolate, the number of steps to divide that range into and the step of
   the range to find.

   @param start
   beginning (min) of the range
   @param end
   end (max) of the range
   @param slices
   slices to divide range into
   @param n
   slice desired from interpolation

   @return interpolated value
   */
  public static float interpolate(float start, float end, float slices, float n)
  {
    if (slices < 0) return end;
    float stepSize = (end - start) / slices;
    return n * stepSize + start;
  }


  /**
   @return tool tip text when cursor over tile
   */
  public String toolTipText()
  {
    return String.format("<html>(lon:%.2f, lat:%.2f)<br>" +
                         "rainfall:%.6fcm<br>" +
                         "diurnal temperature range: (%.2f C)<br>" +
                         "mean temperature: (%.2f C)<br>" +
                         "yearly temp range: (%.2f C, %.2f C)<br>" +
                         "crop: %s</html>",
                         center.getLon(), center.getLat(),
                         getData(CURRENT_ANNUAL_PRECIPITATION),
                         getData(CURRENT_DIURNAL_RANGE),
                         getData(CURRENT_ANNUAL_MEAN_TEMPERATURE),
                         getData(CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH),
                         getData(CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH),
                         currentCrop);
  }


  public float getData(Field field)
  {
    if (isFloatField(field))
    {
      return getFloatData(field);
    }
    else if (isIntField(field))
    {
      return getIntData(field);
    }
    else if(field == CROP_TYPE)
    {
      return currentCrop.ordinal();
    }
    else return 0f;
  }


  /* checks floatData map, return data if present, 0 otherwise */
  private float getFloatData(Field field)
  {
    Float val = floatData.get(field);
    return val == null ? 0 : val;
  }


  /* checks intData map, returns data if present, 0 otherwise */
  public int getIntData(Field field)
  {
    Integer val = intData.get(field);
    return val == null ? 0 : val;
  }


  /**
   Updates the cell's crop for the next year.

   @param newCrop   new EnumCropType to put in this LandTile
   @param newState  new crop state to put in this LandTile
   */
  public void update(EnumCropType newCrop, Float newState)
  {
    if (currentCrop.equals(NONE))
    {
      currentCropPenalty = (float) 0.1;
    }
    else if (!currentCrop.equals(newCrop))
    {
      currentCropPenalty = (float) 0.5;
    }
    else
    {
      currentCropPenalty = 1;
    }
    currentCrop = newCrop;
    cropState = newState;
  }


  /**
   @return  a ByteBuffer representing this LandTile according to the indexing scheme defined in the
   Field enum in this class
   */
  public ByteBuffer toByteBuffer()
  {
    ByteBuffer buf = ByteBuffer.allocate(SIZE_IN_BYTES);
    for (Field field : Field.FLOAT_FIELDS) buf.putFloat(field.byteIndex(), getFloatData(field));
    for (Field field : Field.INT_FIELDS) buf.putInt(field.byteIndex(), getIntData(field));
    return buf;
  }


  /**
   @return the center longitude of this LandTile
   */
  public double getLon()
  {
    return center.getLon();
  }


  /**
   @return the center latitiude of this LandTile
   */
  public double getLat()
  {
    return center.getLat();
  }


  /**
   @return the center MapPoint of this tile. (model space)
   */
  public MapPoint getCenter()
  {
    return center;
  }


  /**
   @return this tile's crop state (used by planting algorithm defined by Tyler, Tim and David's
   Milestone II planting algorithm
   */
  public float getCropState()
  {
    return cropState;
  }


  /**
   Set this LandTile's data for a given field

   @param field
   Field to set
   @param val
   value to set
   @return true if data was successfully placed, false otherwise
   */
  public boolean putData(Field field, int val)
  {

    if (isIntField(field))
    {
      intData.put(field, val);
      return true;
    }
    else if (isFloatField(field))
    {
      floatData.put(field, Float.valueOf(val));
      return true;
    }
    return false;
  }


  /**
   Overridden method that handles putting EnumCropType data in this LandTile (only if the field
   parameter is Field.CROP_TYPE).  This does not incur planting penalties! Use this only if that
   is the desired behavior.

   @param field
   Field enum to put data at.  If not CROP_TYPE, nothing is change, false is returned
   @param crop
   EnumCropType to put in this tile

   @return true if data was successfully mutated, false otherwise
   */
  public boolean putData(Field field, EnumCropType crop)
  {
    if (field == CROP_TYPE)
    {
      currentCrop = crop;
      return true;
    }
    return false;
  }


  /**
   @return the current EnumCropType planted in this LandTile
   */
  public EnumCropType getCrop()
  {
    return currentCrop;
  }


  /**
   Update this tile's crop state and crop for the first year
   @param crop  EnumCropType to plant in this LandTile
   @param state cropState to set
   */
  public void updateFirstYear(EnumCropType crop, float state)
  {
    cropState = state;
    currentCrop = crop;
  }


  /**
   Step this tile based on the current date given in a Calendar object.  Data are interpolated
   based on projections and mutated randomly with a normal distribution and constant variance and
   randomness parameters (class local)
   @param calendar  Calender object holding the current date
   */
  public void step(Calendar calendar)
  {
    int currentYear = calendar.get(Calendar.YEAR);


    for (Field field : CURRENT_FIELDS)
    {
      float curr = getData(field);
      float proj = getData(currentToProjected(field));
      curr = interpolate(curr, proj, (float) currentYear / World.END_YEAR);
      curr += RNG.nextGaussian() * RANDOM_VARIANCE * RANDOMNESS_FACTOR;
      putData(field, curr);
    }
  }


  /**
   Interpolate across a range given a percentage in that range
   @param start start of the range
   @param end   end of the range
   @param percentage  percentage to interpolate
   @return the interpolated value
   */
  public static float interpolate(float start, float end, float percentage)
  {
    return start + (end - start) * percentage;
  }


  /**
   Set this LandTile's data for a given field
   * @param field
   Field to set
   @param val
 value to set
    @return true if data was put successfully, false otherwise
   */
  public boolean putData(Field field, float val)
  {
    if (isFloatField(field))
    {
      floatData.put(field, val);
      return true;
    }
    else if (isIntField(field))
    {
      intData.put(field, (int) val);
      return true;
    }
    return false;
  }


  /**
   Field enum generalizes the binary format the LandTiles can be stored in and all the possible
   data fields they may hold
   */
  public enum Field
  {
    CURRENT_ANNUAL_MEAN_TEMPERATURE,
    CURRENT_DIURNAL_RANGE,
    CURRENT_ISOTHERMALITY,
    CURRENT_TEMPERATURE_SEASONALITY,
    CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH,
    CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH,
    CURRENT_TEMPERATURE_ANNUAL_RANGE,
    CURRENT_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
    CURRENT_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
    CURRENT_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
    CURRENT_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
    CURRENT_ANNUAL_PRECIPITATION,
    CURRENT_PRECIPITATION_OF_WETTEST_MONTH,
    CURRENT_PRECIPITATION_OF_DRIEST_MONTH,
    CURRENT_PRECIPITATION_SEASONALITY,
    CURRENT_PRECIPITATION_OF_WETTEST_QUARTER,
    CURRENT_PRECIPITATION_OF_DRIEST_QUARTER,
    CURRENT_PRECIPITATION_OF_WARMEST_QUARTER,
    CURRENT_PRECIPITATION_OF_COLDEST_QUARTER,
    PROJECTED_ANNUAL_MEAN_TEMPERATURE,
    PROJECTED_DIURNAL_RANGE,
    PROJECTED_ISOTHERMALITY,
    PROJECTED_TEMPERATURE_SEASONALITY,
    PROJECTED_MAX_TEMPERATURE_OF_WARMEST_MONTH,
    PROJECTED_MIN_TEMPERATURE_OF_COLDEST_MONTH,
    PROJECTED_TEMPERATURE_ANNUAL_RANGE,
    PROJECTED_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
    PROJECTED_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
    PROJECTED_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
    PROJECTED_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
    PROJECTED_ANNUAL_PRECIPITATION,
    PROJECTED_PRECIPITATION_OF_WETTEST_MONTH,
    PROJECTED_PRECIPITATION_OF_DRIEST_MONTH,
    PROJECTED_PRECIPITATION_SEASONALITY,
    PROJECTED_PRECIPITATION_OF_WETTEST_QUARTER,
    PROJECTED_PRECIPITATION_OF_DRIEST_QUARTER,
    PROJECTED_PRECIPITATION_OF_WARMEST_QUARTER,
    PROJECTED_PRECIPITATION_OF_COLDEST_QUARTER,
    ELEVATION,
    LONGITUDE,
    LATITUDE,
    COUNTRYID,
    CROP_TYPE,;

    public static final Field[] CURRENT_FIELDS =
        {
            CURRENT_ANNUAL_MEAN_TEMPERATURE,
            CURRENT_DIURNAL_RANGE,
            CURRENT_ISOTHERMALITY,
            CURRENT_TEMPERATURE_SEASONALITY,
            CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH,
            CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH,
            CURRENT_TEMPERATURE_ANNUAL_RANGE,
            CURRENT_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
            CURRENT_ANNUAL_PRECIPITATION,
            CURRENT_PRECIPITATION_OF_WETTEST_MONTH,
            CURRENT_PRECIPITATION_OF_DRIEST_MONTH,
            CURRENT_PRECIPITATION_SEASONALITY,
            CURRENT_PRECIPITATION_OF_WETTEST_QUARTER,
            CURRENT_PRECIPITATION_OF_DRIEST_QUARTER,
            CURRENT_PRECIPITATION_OF_WARMEST_QUARTER,
            CURRENT_PRECIPITATION_OF_COLDEST_QUARTER,
        };

    public static final Field[] PROJECTED_FIELDS =
        {
            PROJECTED_ANNUAL_MEAN_TEMPERATURE,
            PROJECTED_DIURNAL_RANGE,
            PROJECTED_ISOTHERMALITY,
            PROJECTED_TEMPERATURE_SEASONALITY,
            PROJECTED_MAX_TEMPERATURE_OF_WARMEST_MONTH,
            PROJECTED_MIN_TEMPERATURE_OF_COLDEST_MONTH,
            PROJECTED_TEMPERATURE_ANNUAL_RANGE,
            PROJECTED_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
            PROJECTED_ANNUAL_PRECIPITATION,
            PROJECTED_PRECIPITATION_OF_WETTEST_MONTH,
            PROJECTED_PRECIPITATION_OF_DRIEST_MONTH,
            PROJECTED_PRECIPITATION_SEASONALITY,
            PROJECTED_PRECIPITATION_OF_WETTEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_DRIEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_WARMEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_COLDEST_QUARTER,
        };

    public static final Field[] FLOAT_FIELDS =
        {
            CURRENT_ANNUAL_MEAN_TEMPERATURE,
            CURRENT_DIURNAL_RANGE,
            CURRENT_ISOTHERMALITY,
            CURRENT_TEMPERATURE_SEASONALITY,
            CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH,
            CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH,
            CURRENT_TEMPERATURE_ANNUAL_RANGE,
            CURRENT_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
            CURRENT_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
            CURRENT_ANNUAL_PRECIPITATION,
            CURRENT_PRECIPITATION_OF_WETTEST_MONTH,
            CURRENT_PRECIPITATION_OF_DRIEST_MONTH,
            CURRENT_PRECIPITATION_SEASONALITY,
            CURRENT_PRECIPITATION_OF_WETTEST_QUARTER,
            CURRENT_PRECIPITATION_OF_DRIEST_QUARTER,
            CURRENT_PRECIPITATION_OF_WARMEST_QUARTER,
            CURRENT_PRECIPITATION_OF_COLDEST_QUARTER,
            PROJECTED_ANNUAL_MEAN_TEMPERATURE,
            PROJECTED_DIURNAL_RANGE,
            PROJECTED_ISOTHERMALITY,
            PROJECTED_TEMPERATURE_SEASONALITY,
            PROJECTED_MAX_TEMPERATURE_OF_WARMEST_MONTH,
            PROJECTED_MIN_TEMPERATURE_OF_COLDEST_MONTH,
            PROJECTED_TEMPERATURE_ANNUAL_RANGE,
            PROJECTED_MEAN_TEMPERATURE_OF_WETTEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_DRIEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_WARMEST_QUARTER,
            PROJECTED_MEAN_TEMPERATURE_OF_COLDEST_QUARTER,
            PROJECTED_ANNUAL_PRECIPITATION,
            PROJECTED_PRECIPITATION_OF_WETTEST_MONTH,
            PROJECTED_PRECIPITATION_OF_DRIEST_MONTH,
            PROJECTED_PRECIPITATION_SEASONALITY,
            PROJECTED_PRECIPITATION_OF_WETTEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_DRIEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_WARMEST_QUARTER,
            PROJECTED_PRECIPITATION_OF_COLDEST_QUARTER,
            ELEVATION,
            LONGITUDE,
            LATITUDE,
        };

    public static final Field[] INT_FIELDS =
        {
            COUNTRYID,
        };

    public static final List<Field> INT_FIELD_LIST = Arrays.asList(INT_FIELDS);
    public static final List<Field> FLOAT_FIELD_LIST = Arrays.asList(FLOAT_FIELDS);

    /* Field enum must only reference data whose primitive size (in bytes) is 4, could be
     changed, later */
    public static final int FIELD_SIZE_IN_BYTES = 4;
    public static final int SIZE_IN_BYTES = byteBufferValues().size() * FIELD_SIZE_IN_BYTES;


    public static boolean isFloatField(Field f)
    {
      return FLOAT_FIELD_LIST.contains(f);
    }


    public static boolean isIntField(Field f)
    {
      return INT_FIELD_LIST.contains(f);
    }


    public static Field currentToProjected(Field current)
    {
      return Field.values()[current.ordinal() + CURRENT_FIELDS.length];
    }


    public static Collection<Field> byteBufferValues()
    {
      Collection<Field> list = new ArrayList<>(FLOAT_FIELD_LIST);
      list.addAll(INT_FIELD_LIST);
      return list;
    }


    @Override
    public String toString()
    {
      return name().replaceAll("\\s", " ").toLowerCase();
    }


    /**
     @return this Field's byte buffer index
     */
    int byteIndex()
    {
      return ordinal() * FIELD_SIZE_IN_BYTES;
    }

  }

}
