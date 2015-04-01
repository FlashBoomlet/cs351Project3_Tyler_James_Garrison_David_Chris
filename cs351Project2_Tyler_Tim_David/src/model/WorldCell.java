package model;

import model.World;
/**
 * Created by Tim on 3/14/15.
 */
public class WorldCell
{
  private boolean original = false;
  private double lon = 0;
  private double lat = 0;
  private int x = 0;
  private int y = 0;
  private double annualHigh = 0;
  private double annualLow = 0;
  private double elevation = 0;
  private double annualPrecip = -1;
  private double monthlyDayAvg;
  private double monthlyNightAvg;

  public WorldCell (double lonIn, double latIn)
  {
    lat = latIn;
    lon = lonIn;
  }

  public boolean isOriginal ()
  {
    return original;
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
    original = true;
    double temp = 0;
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

  public void setPrecip (double precip)
  {
    annualPrecip = precip;
  }

  public double getPrecip ()
  {
    return annualPrecip;
  }
}
