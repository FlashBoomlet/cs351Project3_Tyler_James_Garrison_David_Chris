package model;

/**
 * Created by Tim on 3/14/15.
 * A cell representing 10 km^2 of
 * area on the globe.
 */
public class WorldCell
{
  public boolean hasCountry = false;
  private double lon = 0;
  private double lat = 0;
  private float annualHigh = -1;
  private float annualLow = -1;
  private float annualPrecip = -1;
  private float tempAvg = -1;
  private String currentCrop = "None";
  private float currentCropState;
  private float currentCropPenalty;

  /**
   * Sets the cell's longitude and latitude coordinates.
   * @param lonIn
   * @param latIn
   */
  public WorldCell (double lonIn, double latIn)
  {
    lat = latIn;
    lon = lonIn;
  }

  /**
   * @return  If the cell is assigned to a region.
   */
  public boolean hasCountry ()
  {
    return hasCountry;
  }

  /**
   * @return  Latitude
   */
  public double getLat ()
  {
    return lat;
  }

  /**
   * @return  Longitude
   */
  public double getLon ()
  {
    return lon;
  }

  /**
   * @return The cell's precipitation.
   */
  public float getPrecip ()
  {
    return annualPrecip;
  }

  /**
   * @return The cell's average high temperature.
   */
  public float getAnnualHigh ()
  {
    return annualHigh;
  }

  /**
   * @return The cell's average low temperature.
   */
  public float getAnnualLow ()
  {
    return annualLow;
  }

  /**
   * @return The cell's average temperature.
   */
  public float getTempAvg ()
  {
    return tempAvg;
  }

  /**
   * @return The cell's crop.
   */
  public String getCrop ()
  {
    return currentCrop;
  }

  /**
   * @return The state of the cell's crop (Ideal/Acceptable/Poor).
   */
  public float getCropState ()
  {
    return currentCropState;
  }

  /**
   * @return The penalty to production due to movement/replanting.
   */
  public float getCurrentCropPenalty ()
  {
    return currentCropPenalty;
  }

  /**
   * The cell's precipitation.
   * @param precip
   */
  public void setPrecip (float precip)
  {
    annualPrecip = precip;
  }

  /**
   * The cell's average high temperature.
   * @param high
   */
  public void setAnnualHigh (float high)
  {
    annualHigh = high;
  }

  /**
   * The cell's average low temperature.
   * @param low
   */
  public void setAnnualLow (float low)
  {
    annualLow = low;
  }

  /**
   * Sets whether the cell is assigned to a country to true;
   */
  public void setToArea ()
  {
    hasCountry = true;
  }

  /**
   * The cell's average temperature.
   * @param avg
   */
  public void setTempAvg (float avg)
  {
    tempAvg = avg;
  }


  /**
   * Updates the cell's crop for the next year.
   * @param newCrop
   * @param newState
   */
  public void update (String newCrop, Float newState)
  {
    if (currentCrop.equals("None"))
    {
      currentCropPenalty = (float) 0.1;
    }
    else if (currentCrop.equals(newCrop))
    {
      currentCropPenalty = (float) 0.5;
    }
    else
    {
      currentCropPenalty = 1;
    }
    currentCrop = newCrop;
    currentCropState = newState;
  }

  /**
   * Sets the cell's crop for the first year.
   * @param crop
   * @param state
   */
  public void updateFirstYear (String crop, Float state)
  {
    currentCrop = crop;
    currentCropState = state;
  }
}
