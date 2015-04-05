package model;

import model.World;
import java.util.ArrayList;
/**
 * Created by Tim on 3/14/15.
 */
public class WorldCell
{
  private boolean originalPrecip = false;
  private boolean originalTAVG = false;
  private ArrayList<Float> avg = new ArrayList<>();
  private ArrayList<Float> max = new ArrayList<>();
  private ArrayList<Float> min = new ArrayList<>();
  private boolean originalTMAX = false;
  private boolean originalTMIN = false;
  public boolean hasCountry = false;
  private double lon = 0;
  private double lat = 0;
  private int x = 0;
  private int y = 0;
  private float annualHigh = 0;
  private float annualLow = 0;
  //private double elevation = 0;
  private float annualPrecip = -1;
  private float monthlyDayAvg;
  private float monthlyNightAvg;
  private float tempAvg = 0;
  private String currentCrop = "None";
  //private CropType previousCrop;
  private float currentCropState;
  //private CropState previousCropState;
  private float currentCropPenalty;

  public WorldCell (double lonIn, double latIn)
  {
    lat = latIn;
    lon = lonIn;
  }

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
  }

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

  public float getMonthlyDayAvg ()
  {
    return monthlyDayAvg;
  }

  public float getMonthlyNightAvg ()
  {
    return monthlyNightAvg;
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
  }*/

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
  }

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

  public void setMonthlyDayAvg (float day)
  {
    monthlyDayAvg = day;
  }

  public void setMonthlyNightAvg (float night)
  {
    monthlyNightAvg = night;
  }

  public void setToArea ()
  {
    hasCountry = true;
  }

  public void setTempAvg (float avg)
  {
    tempAvg = avg;
  }

  public void setOriginalTAVG (float avg)
  {
    originalTAVG = true;
    if (this.avg.size() == 0)
    {
      tempAvg = avg;
      this.avg.add(avg);
    }
    else
    {
      this.avg.add(avg);
      float temp = 0;
      for (Float current: this.avg)
      {
        temp = temp + current;
      }
      tempAvg = temp / this.avg.size();
    }
  }

  public void setOriginalTMAX (float high)
  {
    originalTMAX = true;
    if (max.size() == 0)
    {
      annualHigh = high;
      max.add(high);
    }
    else
    {
      max.add(high);
      float temp = 0;
      for (Float current: max)
      {
        temp = temp + current;
      }
      annualHigh = temp / max.size();
    }
  }

  public void setOriginalTMIN (float low)
  {
    originalTMIN = true;
    if (min.size() == 0)
    {
      annualLow = low;
      min.add(low);
    }
    else
    {
      min.add(low);
      float temp = 0;
      for (Float current: min)
      {
        temp = temp + current;
      }
      annualLow = temp / min.size();
    }
  }
  /*
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
