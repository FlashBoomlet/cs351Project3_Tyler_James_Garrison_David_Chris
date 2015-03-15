package model;

import model.World;
/**
 * Created by Tim on 3/14/15.
 */
public class WorldCell
{
  public static double EDGE_X_LON = (180*2)/(40075/10);
  public static double EDGE_Y_LAT = (90*2)/(40008/10);
  private double lon = 0;
  private double lat = 0;
  private double annualHigh = 0;
  private double annualLow = 0;
  private double elevation = 0;
  private double [] monthlyPrecip = {-1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1};
  private double [] monthlyDayAvg = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
  private double [] monthlyNightAvg = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};

  public WorldCell (double lonIn, double latIn)
  {
    lat = latIn;
    lon = lonIn;
  }

  public double getLat ()
  {
    return lat;
  }

  public double getLon ()
  {
    return lon;
  }

  public double getElevation ()
  {
    return elevation;
  }

  public double getAnnualHigh ()
  {
    return annualHigh;
  }

  public double getAnnualLow ()
  {
    return annualLow;
  }

  public boolean pointInCell (double lonPoint, double latPoint)
  {
    if (latPoint >= lat && latPoint < (lat + EDGE_Y_LAT) && lonPoint >= lon && lonPoint < (lon + EDGE_X_LON))
    {
      return true;
    }
    return false;
  }

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

  public void setAllPrecip (double[] months)
  {
    if (months.length == 12)
    {
      if (monthlyPrecip [0] == -1)
      {
        for (int i = 0; i < 12; i++)
        {
          monthlyPrecip[i] = months[i];
        }
      }
      else
      {
        for (int i = 0; i < 12; i++)
        {
          monthlyPrecip[i] = (months[i] + monthlyPrecip[i]) / 2;
        }
      }
    }
    else
    {
      System.out.println("Need 12 months to set Precip");
    }
  }

  public double getMonthPrecip (int index)
  {
    if (index < 13 && index > 0)
    {
      return monthlyPrecip[index - 1];
    }
    System.out.println(index + " is not a valid month for Precip");
    return -1;
  }
}
