package model;

import model.World;
import java.util.ArrayList;
/**
 * Created by Tim on 3/14/15.
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

  public WorldCell (double lonIn, double latIn)
  {
    lat = latIn;
    lon = lonIn;
  }
/*
  public boolean isOriginal ()
  {
    return originalPrecip;
  }

  public boolean isOriginalTAVG ()
  {
    return originalTAVG;
  }

  public boolean isOriginalTMAX ()
  {
    return originalTMAX;
  }

  public boolean isOriginalTMIN ()
  {
    return originalTMIN;
  }*/

  public boolean hasCountry ()
  {
    return hasCountry;
  }

  public double getLat ()
  {
    return lat;
  }

  public double getLon ()
  {
    return lon;
  }

  /*
  public double getElevation ()
  {
    return elevation;
  }*/
  public float getPrecip ()
  {
    return annualPrecip;
  }

  public float getAnnualHigh ()
  {
    return annualHigh;
  }

  public float getAnnualLow ()
  {
    return annualLow;
  }

  public float getTempAvg ()
  {
    return tempAvg;
  }

  public String getCrop ()
  {
    return currentCrop;
  }

  public float getCropState ()
  {
    return currentCropState;
  }

  public float getCurrentCropPenalty ()
  {
    return currentCropPenalty;
  }

  /*
  public void setElevation (double elev)
  {
    if (elev >= 0 && elev <= 8850)
    {
      elevation = elev;
    }
    else
    {
      System.out.println("Invalid elevation.");
    }
  }

  public void setAllPrecip (float[] months)
  {
    originalPrecip = true;
    float temp = 0;
    if (months.length == 12)
    {
      if (annualPrecip == -1)
      {
        for (int i = 0; i < 12; i++)
        {
          temp = temp + months[i];
        }
        annualPrecip = temp;
      }
      else
      {
        for (int i = 0; i < 12; i++)
        {
          temp = temp + months[i];
        }
        annualPrecip = (annualPrecip + temp) / 2;
      }
    }
    else
    {
      System.out.println("Need 12 months to set Precip");
    }
  }*/

  public void setPrecip (float precip)
  {
    annualPrecip = precip;
  }

  public void setAnnualHigh (float high)
  {
    annualHigh = high;
  }

  public void setAnnualLow (float low)
  {
    annualLow = low;
  }

  public void setToArea ()
  {
    hasCountry = true;
  }

  public void setTempAvg (float avg)
  {
    tempAvg = avg;
  }

/*
  public void setOriginalTAVG (float avg)
  {
    originalTAVG = true;
    tempAvg = avg;
  }

  public void setOriginalTMAX (float high)
  {
    originalTMAX = true;
    annualHigh = high;
  }

  public void setOriginalTMIN (float low)
  {
    originalTMIN = true;
    annualLow = low;
  }

  public enum CropType
  {
    WHEAT,
    CORN,
    SOY,
    RICE,
    OTHER,
    NONE
  }*/

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

  public void updateFirstYear (String crop, Float state)
  {
    currentCrop = crop;
    currentCropState = state;
  }
}
