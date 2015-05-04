package model;


import model.common.EnumCropType;
import model.common.OtherCropsData;

import static model.LandTile.FIELD.*;
import static model.common.CropZoneData.*;

import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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

  private final Map<FIELD, Float> floatData = new HashMap<>();
  private final Map<FIELD, Integer> intData = new HashMap<>();

  private int countryID = 0;
  private float elevation = 0;     /* in meters above sea level */
  private float maxAnnualTemp = 0; /* in degrees Celsius. */
  private float minAnnualTemp = 0; /* in degrees Celsius. */
  private float avgDayTemp = 0;    /* in degrees Celsius. */
  private float avgNightTemp = 0;  /* in degrees Celsius. */
  private float rainfall = 0;      /* in cm */
  private float proj_maxAnnualTemp = 0; /* in degrees Celsius. */
  private float proj_minAnnualTemp = 0; /* in degrees Celsius. */
  private float proj_avgDayTemp = 0;    /* in degrees Celsius. */
  private float proj_avgNightTemp = 0;  /* in degrees Celsius. */
  private float proj_rainfall = 0;      /* in cm */
  private MapPoint center;
  private EnumCropType currCrop;


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
   Constructor using custom binary file

   @param buf
   custom binary file
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

    for (FIELD field : FIELD.values())
    {
      if(isFloatField(field)) floatData.put(field, buf.getFloat(field.byteIndex()));
      else intData.put(field, buf.getInt(field.byteIndex()));
    }
    center = new MapPoint(floatData.get(LONGITUDE), floatData.get(LATITUDE));
  }


  /**
   @return tool tip text when cursor over tile
   */
  public String toolTipText()
  {
    return String.format("<html>(lon:%.2f, lat:%.2f)<br>" +
                         "rainfall:%.6fcm<br>" +
                         "daily temp range: (%.2f C, %.2f C)<br>" +
                         "yearly temp range: (%.2f C, %.2f C)<br>" +
                         "crop: %s</html>",
                         center.getLon(), center.getLat(), rainfall,
                         avgNightTemp, avgDayTemp, minAnnualTemp, maxAnnualTemp, currCrop);
  }


  public ByteBuffer toByteBuffer()
  {
    ByteBuffer buf = ByteBuffer.allocate(SIZE_IN_BYTES);
    for (FIELD field : FIELD.FLOAT_FIELDS) buf.putFloat(field.byteIndex(), getFloatData(field));
    for(FIELD field : FIELD.INT_FIELDS) buf.putInt(field.byteIndex(), getIntData(field));
    return buf;
  }


  public int getIntData(FIELD field)
  {
    Integer val = intData.get(field);
    return val == null? 0 : val;
  }


  private float getFloatData(FIELD field)
  {
    Float val = floatData.get(field);
    return val == null? 0 : val;
  }


  public double getLon()
  {
    return center.getLon();
  }


  public double getLat()
  {
    return center.getLat();
  }


  public MapPoint getCenter()
  {
    return center;
  }


  /**
   Mutates tile's values when year changes

   @param yearsRemaining
   years remaining in game
   */
  @Deprecated
  public void stepTile(int yearsRemaining)
  {
    /* todo: fix this in terms of fields that need modification based on projection */
    maxAnnualTemp = interpolate(maxAnnualTemp, proj_maxAnnualTemp, yearsRemaining, 1);
    minAnnualTemp = interpolate(minAnnualTemp, proj_minAnnualTemp, yearsRemaining, 1);
    avgDayTemp = interpolate(avgDayTemp, proj_avgDayTemp, yearsRemaining, 1);
    avgNightTemp = interpolate(avgNightTemp, proj_avgNightTemp, yearsRemaining, 1);
    rainfall = interpolate(rainfall, proj_rainfall, yearsRemaining, 1);
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


  public int getCountryID()
  {
    return countryID;
  }


  public void setCountryID(int id)
  {
    countryID = id;
  }


  public float getElevation()
  {
    return getData(ELEVATION);
  }


  public float getData(FIELD field)
  {
    if(isFloatField(field)) return getFloatData(field);
    else return getIntData(field);

  }


  public float getMaxAnnualTemp()
  {
    return getData(CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH);
  }


  public void setMaxAnnualTemp(float val)
  {
    putData(CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH, val);
  }


  public void putData(FIELD field, float val)
  {
    if(isFloatField(field)) floatData.put(field, val);
    else  intData.put(field, (int) val);
  }

  public void putData(FIELD field, int val)
  {
    if(isIntField(field)) intData.put(field, val);
    else floatData.put(field, Float.valueOf(val));
  }

  public float getMinAnnualTemp()
  {
    return getData(CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH);
  }


  public void setMinAnnualTemp(float val)
  {
    putData(CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH, val);
  }


  public float getAvgDayTemp()
  {
    return avgDayTemp;
  }


  public void setAvgDayTemp(float avgDayTemp)
  {
    this.avgDayTemp = avgDayTemp;
  }


  public float getAvgNightTemp()
  {
    return avgNightTemp;
  }


  public void setAvgNightTemp(float avgNightTemp)
  {
    this.avgNightTemp = avgNightTemp;
  }


  public float getRainfall()
  {
    return rainfall;
  }


  public void setRainfall(float rainfall)
  {
    this.rainfall = Math.max(0, rainfall);
  }


  public void setProj_maxAnnualTemp(float proj_maxAnnualTemp)
  {
    this.proj_maxAnnualTemp = proj_maxAnnualTemp;
  }


  public void setProj_minAnnualTemp(float proj_minAnnualTemp)
  {
    this.proj_minAnnualTemp = proj_minAnnualTemp;
  }


  public void setProj_avgDayTemp(float proj_avgDayTemp)
  {
    this.proj_avgDayTemp = proj_avgDayTemp;
  }


  public void setProj_avgNightTemp(float proj_avgNightTemp)
  {
    this.proj_avgNightTemp = proj_avgNightTemp;
  }


  public void setProj_rainfall(float proj_rainfall)
  {
    this.proj_rainfall = proj_rainfall;
  }


  public void setCurrCrop(EnumCropType crop)
  {
    currCrop = crop;
  }


  /**
   Rates tile's suitability for a particular crop.

   @param crop
   crop for which we want rating (wheat, corn, rice, or soy)

   @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)

   @throws NullPointerException
   if called with argument EnumCropType.OTHER_CROPS, will throw an
   exception because OTHER_CROPS required climate varies by country;
   rating cannot be calculated using crop alone.
   */
  @Deprecated
  public EnumCropZone rateTileForCrop(EnumCropType crop) throws NullPointerException
  {
    throw new UnsupportedOperationException("in progress");
    /*
    int cropDayT = crop.dayTemp;
    int cropNightT = crop.nightTemp;
    int cropMaxT = crop.maxTemp;
    int cropMinT = crop.minTemp;
    int cropMaxR = crop.maxRain;
    int cropMinR = crop.minRain;
    double tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    double rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum rainfall range
    if (isBetween(avgDayTemp, cropNightT, cropDayT) &&
      isBetween(avgNightTemp, cropNightT, cropDayT) &&
      isBetween(rainfall, cropMinR, cropMaxR) &&
      maxAnnualTemp <= cropMaxT && minAnnualTemp >= cropMinT)
    {
      return EnumCropZone.IDEAL;
    }
    else if (isBetween(avgDayTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(avgNightTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(rainfall, cropMinR - 0.3 * rRange, cropMaxR + 0.3 * rRange) &&
      maxAnnualTemp <= cropMaxT && minAnnualTemp >= cropMinT)
    {
      return EnumCropZone.ACCEPTABLE;
    }
    else
    {
      return EnumCropZone.POOR;                                               // otherwise tile is POOR for crop
    }
    */
  }


  /**
   Rate tile's suitability for a particular country's  other crops.

   @param otherCropsData
   a country's otherCropsData object

   @return EnumCropZone (IDEAL, ACCEPTABLE, or POOR)
   */
  @Deprecated
  public EnumCropZone rateTileForOtherCrops(OtherCropsData otherCropsData)
  {
    throw new UnsupportedOperationException("in progress");
    /*
    float cropDayT = otherCropsData.dayTemp;
    float cropNightT = otherCropsData.nightTemp;
    float cropMaxT = otherCropsData.maxTemp;
    float cropMinT = otherCropsData.minTemp;
    float cropMaxR = otherCropsData.maxRain;
    float cropMinR = otherCropsData.minRain;
    float tRange = cropDayT - cropNightT;                               // tempRange is crop's optimum day-night temp range
    float rRange = cropMaxR - cropMinR;                                 // rainRange is crop's optimum rainfall range
    if (isBetween(avgDayTemp, cropNightT, cropDayT) &&
      isBetween(avgNightTemp, cropNightT, cropDayT) &&
      isBetween(rainfall, cropMinR, cropMaxR) &&
      minAnnualTemp >= cropMinT && maxAnnualTemp <= cropMaxT)
    {
      return EnumCropZone.IDEAL;
    }
    else if (isBetween(avgDayTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(avgNightTemp, cropNightT - 0.3 * tRange, cropDayT + 0.3 * tRange) &&
      isBetween(rainfall, cropMinR - 0.3 * rRange, cropMaxR + 0.3 * rRange) &&
      minAnnualTemp >= cropMinT && maxAnnualTemp <= cropMaxT)
    {
      return EnumCropZone.ACCEPTABLE;
    }
    else
    {
      return EnumCropZone.POOR;                                               // otherwise tile is POOR for crop
    }
    */
  }


  /**
   Get percent of country's yield for crop tile will yield, base on its zone rating and
   current use

   @param crop
   crop in question
   @param zone
   tile's zone for that crop

   @return percent of country's yield tile can yield
   */
  public double getTileYieldPercent(EnumCropType crop, EnumCropZone zone)
  {
    double zonePercent = 0;
    double usePercent;
    switch (zone)
    {
      case IDEAL:
        zonePercent = 1;
        break;
      case ACCEPTABLE:
        zonePercent = 0.6;
        break;
      case POOR:
        zonePercent = 0.25;
        break;
    }
    if (currCrop == crop)
    {
      usePercent = 1;
    }
    else if (currCrop == null)
    {
      usePercent = 0.1;
    }
    else
    {
      usePercent = 0.5;
    }
    return zonePercent * usePercent;
  }


  private boolean isBetween(Number numToTest, Number lowVal, Number highVal)
  {
    if (numToTest.doubleValue() >= lowVal.doubleValue() &&
        numToTest.doubleValue() <= highVal.doubleValue())
    {
      return true;
    }
    else
    {
      return false;
    }
  }


  /**
   Retuns the crop currently planted on this tile.

   @return planted crop
   */
  public EnumCropType getCurrentCrop()
  {
    return currCrop;
  }


  /**
   FIELD enum generalizes the binary format the LandTiles can be stored in
   */
  public enum FIELD
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
    COUNTRYID,;

    public static final FIELD[] FLOAT_FIELDS =
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

    public static final FIELD[] INT_FIELDS =
        {
            COUNTRYID,
        };

    public static final List<FIELD> INT_FIELD_LIST = Arrays.asList(INT_FIELDS);
    public static final List<FIELD> FLOAT_FIELD_LIST = Arrays.asList(FLOAT_FIELDS);

    public static boolean isFloatField(FIELD f)
    {
      return FLOAT_FIELD_LIST.contains(f);
    }

    public static boolean isIntField(FIELD f)
    {
      return INT_FIELD_LIST.contains(f);
    }

    /* FIELD enum must only reference data whose primitive size (in bytes) is 4 */
    public static final int FIELD_SIZE_IN_BYTES = 4;
    public static final int SIZE = values().length;
    public static final int SIZE_IN_BYTES = SIZE * FIELD_SIZE_IN_BYTES;

    @Override
    public String toString()
    {
      return name().replaceAll("\\s", " ").toLowerCase();
    }


    /**
     @return  this FIELD's byte buffer index
     */
    int byteIndex()
    {
      return ordinal() * FIELD_SIZE_IN_BYTES;
    }

  }

}
