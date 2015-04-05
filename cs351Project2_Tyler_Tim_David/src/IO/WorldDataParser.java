package IO;
import model.World;
import model.WorldCell;
import model.WorldArray;
import java.util.HashMap;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.FileOutputStream;
import java.io.File;
import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.IOException;
import java.util.ArrayList;
/**
 * Created by Tim on 3/14/15.
 */
public class WorldDataParser
{
  private World world = null;
  private WorldArray worldArray = null;
  private int X_CELLS;
  private int Y_CELLS;
  //private HashMap<Integer, Coord> sites = new HashMap<>();
  //private int LONGITUDE_CELL_NUM = 40075/10 + 1; //40075 km = Circumference of the earth around the equator
  //private int LATITUDE_CELL_NUM = 40008/10 + 1;  //40008 km = Circumference from pole to pole

  public WorldDataParser (World input)
  {
    world = input;
    worldArray = world.getWorldArray();
    X_CELLS = worldArray.getXSize();
    Y_CELLS = worldArray.getYSize();
  }

  public void parseMaxTemp (String fileURL)
  {
    try
    {
      Scanner scan = new Scanner(new FileReader(fileURL));
      int counterX = 0;
      int counterY = 0;
      while (scan.hasNext())
      {
        worldArray.get(counterX, counterY).setAnnualHigh(Float.parseFloat(scan.nextLine()));
        if (counterX == X_CELLS)
        {
          break;
        }
        else if (counterY == Y_CELLS)
        {
          counterY = 0;
          counterX++;
        }
        else
        {
          counterY++;
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public void parseMinTemp (String fileURL)
  {
    try
    {
      Scanner scan = new Scanner(new FileReader(fileURL));
      int counterX = 0;
      int counterY = 0;
      while (scan.hasNext())
      {
        worldArray.get(counterX, counterY).setAnnualLow(Float.parseFloat(scan.nextLine()));
        if (counterX == X_CELLS)
        {
          break;
        }
        else if (counterY == Y_CELLS)
        {
          counterY = 0;
          counterX++;
        }
        else
        {
          counterY++;
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public void parsePrecip (String fileURL)
  {
    try
    {
      Scanner scan = new Scanner(new FileReader(fileURL));
      int counterX = 0;
      int counterY = 0;
      while (scan.hasNext())
      {
        worldArray.get(counterX, counterY).setPrecip(Float.parseFloat(scan.nextLine()));
        if (counterX == X_CELLS)
        {
          break;
        }
        else if (counterY == Y_CELLS)
        {
          counterY = 0;
          counterX++;
        }
        else
        {
          counterY++;
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public void parseAvgTemp (String fileURL)
  {
    try
    {
      Scanner scan = new Scanner(new FileReader(fileURL));
      int counterX = 0;
      int counterY = 0;
      float temp = 0;
      while (scan.hasNext())
      {
        temp = Float.parseFloat(scan.nextLine());
        worldArray.get(counterX, counterY).setTempAvg(temp);
        if (counterX == X_CELLS)
        {
          break;
        }
        else if (counterY == Y_CELLS)
        {
          counterY = 0;
          counterX++;
        }
        else
        {
          counterY++;
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  /*
  public void parseSites (String fileURL)
  {
    try
    {
      Scanner siteScan = new Scanner(new FileReader(fileURL));
      String current = null;
      int end = 0;
      int counter = 0;
      int numCount = 0;
      int station = 0;
      double currentLon = 0;
      double currentLat = 0;
      while (siteScan.hasNext())
      {
        counter = 0;
        numCount = 0;
        current = siteScan.nextLine();
        while (numCount < 4)
        {
          if (current.charAt(counter) != '\t' && current.charAt(counter) != ' ')
          {
            if (numCount == 0)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              station = Integer.parseInt(current.substring(counter, end));
              numCount++;
              counter = end;
            }
            else if (numCount == 1)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              currentLat = Double.parseDouble(current.substring(counter, end));
              numCount++;
              counter = end;
            }
            else if (numCount == 2)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              currentLon = Double.parseDouble(current.substring(counter, end));
              break;
            }
            else if (numCount == 3)
            {
              break;
            }
          }
          counter++;
        }
        sites.put((Integer) station, new Coord(currentLon, currentLat));
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  public void parsePrecip (String fileURL)
  {
    try
    {
      Scanner rainScan = new Scanner(new FileReader(fileURL));
      String [] strings;
      float [] months = new float [12];
      String current = null;
      int cellX = 0;
      int cellY = 0;
      double currentLon = 0;
      double currentLat = 0;
      System.out.println("about to parse");
      while (rainScan.hasNext())
      {
        current = rainScan.nextLine();
        current = current.trim();
        strings = current.split("\\s+");
        currentLon = Double.parseDouble(strings[0]);
        currentLat = Double.parseDouble(strings[1]);
        for (int i = 0; i < 12; i++)
        {
          months[i] = Float.parseFloat(strings[i + 2]);
        }
        worldArray.get(currentLon, currentLat, false).setAllPrecip(months);
      }
      System.out.println("About to finalize");
      finalizePrecip();
      System.out.println("About to print");
      print("precip.txt");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  private void finalizePrecip ()
  {
    int oops = 0;
    int numFound = 0;
    int step = 2;
    int counter = 1;
    float temp = 0;
    float tempX = 0;
    float tempY = 0;
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i,j).isOriginal())
        {
          numFound = 0;
          while (step < X_CELLS)
          {
            if (worldArray.get(i+step, j).isOriginal())
            {
              temp = worldArray.get(i+step,j).getPrecip() - worldArray.get(i, j).getPrecip();
              while (counter < step)
              {
                worldArray.get(i+counter, j).setPrecip((temp/step) * counter + worldArray.get(i, j).getPrecip());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < X_CELLS)
          {
            if (worldArray.get(i-step, j).isOriginal())
            {
              temp = worldArray.get(i-step,j).getPrecip() - worldArray.get(i, j).getPrecip();
              while (counter < step)
              {
                worldArray.get(i-counter, j).setPrecip((temp/step) * counter + worldArray.get(i, j).getPrecip());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j+step).isOriginal())
            {
              temp = worldArray.get(i,j+step).getPrecip() - worldArray.get(i, j).getPrecip();
              while (counter < step)
              {
                worldArray.get(i, j+counter).setPrecip((temp/step) * counter + worldArray.get(i, j).getPrecip());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j-step).isOriginal())
            {
              temp = worldArray.get(i, j-step).getPrecip() - worldArray.get(i, j).getPrecip();
              while (counter < step)
              {
                worldArray.get(i, j-counter).setPrecip((temp/step) * counter + worldArray.get(i, j).getPrecip());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          if (numFound != 4)
          {
            oops++;
            //System.out.println("Initial precip data not a grid in worldArrays: " + numFound);
          }
        }
      }
    }
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i, j).getPrecip() == -1)
        {
          step = 1;
          counter = 1;
          while (step < X_CELLS)
          {
            if (worldArray.get(i + step, j).getPrecip() != -1)
            {
              tempX = worldArray.get(i + step, j).getPrecip();
              break;
            }
            step++;
          }
          while (counter < X_CELLS)
          {
            if (worldArray.get(i - counter, j).getPrecip() != -1)
            {
              tempX = tempX - worldArray.get(i - counter, j).getPrecip();
              break;
            }
            counter++;
          }
          tempX = (tempX / (step + counter) * counter) + worldArray.get(i - counter, j).getPrecip();
          step = 1;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j + step).getPrecip() != -1)
            {
              tempY = worldArray.get(i, j + step).getPrecip();
              break;
            }
            step++;
          }
          while (counter < Y_CELLS)
          {
            if (worldArray.get(i, j - counter).getPrecip() != -1)
            {
              tempY = tempY - worldArray.get(i, j - counter).getPrecip();
              break;
            }
            counter++;
          }
          tempY = (tempY / (step + counter) * counter) + worldArray.get(i, j - counter).getPrecip();
          worldArray.get(i, j).setPrecip((tempX + tempY) / 2);
        }
      }
    }
  }

  private double [] avgMonths (double [] arrayOne, double [] arrayTwo)
  {
    double [] temp = new double [12];
    for (int i = 0; i < 12; i++)
    {
      temp[i] = (arrayOne[i] + arrayTwo[i]) / 2;
    }
    return temp;
  }

  public void parseAvgTemp (String fileURL)
  {
    try
    {
      Scanner avgScan = new Scanner(new FileReader(fileURL));
      String current = null;
      int end = 0;
      int counter = 0;
      int numCount = 0;
      int station = -1;
      int year = 0;
      float avgTemp = 0;
      double currentLon = 0;
      double currentLat = 0;
      ArrayList<Float> averages = new ArrayList<>();
      int previousStation = -1;
      float AvgOfAvg = 0;
      while (avgScan.hasNext())
      {
        previousStation = station;
        counter = 0;
        numCount = 0;
        current = avgScan.nextLine();
        //current = current.trim();
        //strings = current.split("\\s+");
        while (numCount < 4)
        {
          if (!Character.isWhitespace(current.charAt(counter)))
          {
            if (numCount == 0)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              station = Integer.parseInt(current.substring(counter, end));
              numCount++;
              counter = end;
            }
            else if (numCount == 1)
            {
              //ignore
              numCount++;
            }
            else if (numCount == 2)
            {
              year = Integer.parseInt(current.substring(counter, counter + 4));
              numCount++;
              counter = counter + 7;
            }
            else if (numCount == 3)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              avgTemp = Float.parseFloat(current.substring(counter, end));
              break;
            }
          }
          counter++;
        }
        if (year == 2013 && sites.get(station) != null && sites.get(station).lat > -180)
        {
          currentLon = sites.get((Integer) station).lon;
          currentLat = sites.get((Integer) station).lat;
          if (station == previousStation)
          {
            averages.add(avgTemp);
            for (Float currentAvg: averages)
            {
              AvgOfAvg = AvgOfAvg + currentAvg;
            }
            AvgOfAvg = AvgOfAvg / averages.size();
            //System.out.println("lon: " + currentLon + " lat: " + currentLat + " avg: " + AvgOfAvg);
            worldArray.get(currentLon, currentLat, false).setOriginalTMAX(AvgOfAvg);
          }
          else
          {
            averages.clear();
            averages.add(avgTemp);
            //System.out.println("lon: " + currentLon + " lat: " + currentLat + " avg: " + avgTemp);
            worldArray.get(currentLon, currentLat, false).setOriginalTMAX(avgTemp);
          }
        }
        else
        {
          station = previousStation;
        }
      }
      finalizeAVG();
      print("tmax.txt");
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }

  private void finalizeAVG ()
  {
    int numFound = 0;
    int step = 2;
    int counter = 1;
    float temp = 0;
    float tempX = 0;
    float tempY = 0;
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i,j).isOriginalTMAX())
        {
          numFound = 0;
          while (step < X_CELLS)
          {
            if (worldArray.get(i+step, j).isOriginalTMAX())
            {
              temp = worldArray.get(i+step,j).getAnnualHigh() - worldArray.get(i, j).getAnnualHigh();
              while (counter < step)
              {
                worldArray.get(i+counter, j).setAnnualHigh((temp / step) * counter + worldArray.get(i, j).getAnnualHigh());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < X_CELLS)
          {
            if (worldArray.get(i-step, j).isOriginalTMAX())
            {
              temp = worldArray.get(i-step,j).getAnnualHigh() - worldArray.get(i, j).getAnnualHigh();
              while (counter < step)
              {
                worldArray.get(i-counter, j).setAnnualHigh((temp / step) * counter + worldArray.get(i, j).getAnnualHigh());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j+step).isOriginalTMAX())
            {
              temp = worldArray.get(i,j+step).getAnnualHigh() - worldArray.get(i, j).getAnnualHigh();
              while (counter < step)
              {
                worldArray.get(i, j+counter).setAnnualHigh((temp / step) * counter + worldArray.get(i, j).getAnnualHigh());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          step = 2;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j-step).isOriginalTMAX())
            {
              temp = worldArray.get(i, j-step).getAnnualHigh() - worldArray.get(i, j).getAnnualHigh();
              while (counter < step)
              {
                worldArray.get(i, j-counter).setAnnualHigh((temp / step) * counter + worldArray.get(i, j).getAnnualHigh());
                counter++;
              }
              numFound++;
              break;
            }
            step++;
          }
          if (numFound != 4)
          {
            //oops++;
            //System.out.println(worldArray.get(i,j).getAnnualLow());
          }
        }
      }
    }
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i, j).getAnnualHigh() == -1)
        {
          step = 1;
          counter = 1;
          while (step < X_CELLS)
          {
            if (worldArray.get(i + step, j).getAnnualHigh() != -1)
            {
              tempX = worldArray.get(i + step, j).getAnnualHigh();
              break;
            }
            step++;
          }
          while (counter < X_CELLS)
          {
            if (worldArray.get(i - counter, j).getAnnualHigh() != -1)
            {
              tempX = tempX - worldArray.get(i - counter, j).getAnnualHigh();
              break;
            }
            counter++;
          }
          tempX = (tempX / (step + counter) * counter) + worldArray.get(i - counter, j).getAnnualHigh();
          step = 1;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j + step).getAnnualHigh() != -1)
            {
              tempY = worldArray.get(i, j + step).getAnnualHigh();
              break;
            }
            step++;
          }
          while (counter < Y_CELLS)
          {
            if (worldArray.get(i, j - counter).getAnnualHigh() != -1)
            {
              tempY = tempY - worldArray.get(i, j - counter).getAnnualHigh();
              break;
            }
            counter++;
          }
          tempY = (tempY / (step + counter) * counter) + worldArray.get(i, j - counter).getAnnualHigh();
          worldArray.get(i, j).setAnnualHigh((tempX + tempY) / 2);
        }
      }
    }
  }

  public void print (String filename)
  {
    try
    {
      File out = new File (filename);
      FileOutputStream fos = new FileOutputStream(out);
      BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(fos));
      for (int i = 0; i < X_CELLS; i++)
      {
        for (int j = 0; j < Y_CELLS; j++)
        {
          bw.write(Float.toString(worldArray.get(i, j).getAnnualHigh()));
          bw.newLine();
        }
      }
      bw.close();
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }

  private class Coord
  {
    double lat = 0;
    double lon = 0;

    public Coord (double lon, double lat)
    {
      this.lon = lon;
      this.lat = lat;
    }
  }*/
}

