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
/**
 * Created by Tim on 3/14/15.
 */
public class WorldDataParser
{
  private World world = null;
  private WorldArray worldArray = null;
  private int X_CELLS;
  private int Y_CELLS;
  private HashMap<Integer, Coord> sites = new HashMap<>();
  //private int LONGITUDE_CELL_NUM = 40075/10 + 1; //40075 km = Circumference of the earth around the equator
  //private int LATITUDE_CELL_NUM = 40008/10 + 1;  //40008 km = Circumference from pole to pole

  public WorldDataParser (World input)
  {
    world = input;
    worldArray = world.getWorldArray();
    X_CELLS = worldArray.getXSize();
    Y_CELLS = worldArray.getYSize();
  }

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
              station = Integer.parseInt(current.substring(counter, end - 1));
              numCount++;
            }
            else if (numCount == 1)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              currentLat = Double.parseDouble(current.substring(counter, end - 1));
            }
            else if (numCount == 2)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              currentLon = Double.parseDouble(current.substring(counter, end - 1));
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

  public void parseMaxTemp (String fileURL)
  {

  }

  public void parseMinTemp (String fileURL)
  {

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
      int station = 0;
      int year = 0;
      float avgTemp = 0;
      double currentLon = 0;
      double currentLat = 0;
      while (avgScan.hasNext())
      {
        counter = 0;
        numCount = 0;
        current = avgScan.nextLine();
        //current = current.trim();
        //strings = current.split("\\s+");
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
              station = Integer.parseInt(current.substring(counter, end - 1));
              numCount++;
            }
            else if (numCount == 1)
            {
              //ignore
              numCount++;
            }
            else if (numCount == 2)
            {
              year = Integer.parseInt(current.substring(counter, counter + 3));
              numCount++;
            }
            else if (numCount == 3)
            {
              end = counter + 1;
              while(current.charAt(end) != '\t' && current.charAt(end) != ' ')
              {
                end++;
              }
              avgTemp = Float.parseFloat(current.substring(counter, end - 1));
              break;
            }
          }
          counter++;
        }
        if (year == 2013)
        {
          currentLon = sites.get((Integer) station).lon;
          currentLat = sites.get((Integer) station).lat;
          worldArray.get(currentLon, currentLat, false).setOriginalTAVG(avgTemp);
        }
      }
      finalizeAVG();
      print("tavg.txt");
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
        if (worldArray.get(i,j).isOriginalTAVG())
        {
          numFound = 0;
          while (step < X_CELLS)
          {
            if (worldArray.get(i+step, j).isOriginalTAVG())
            {
              temp = worldArray.get(i+step,j).getTempAvg() - worldArray.get(i, j).getTempAvg();
              while (counter < step)
              {
                worldArray.get(i+counter, j).setTempAvg((temp / step) * counter + worldArray.get(i, j).getTempAvg());
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
            if (worldArray.get(i-step, j).isOriginalTAVG())
            {
              temp = worldArray.get(i-step,j).getTempAvg() - worldArray.get(i, j).getTempAvg();
              while (counter < step)
              {
                worldArray.get(i-counter, j).setTempAvg((temp / step) * counter + worldArray.get(i, j).getTempAvg());
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
            if (worldArray.get(i, j+step).isOriginalTAVG())
            {
              temp = worldArray.get(i,j+step).getTempAvg() - worldArray.get(i, j).getTempAvg();
              while (counter < step)
              {
                worldArray.get(i, j+counter).setTempAvg((temp / step) * counter + worldArray.get(i, j).getTempAvg());
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
            if (worldArray.get(i, j-step).isOriginalTAVG())
            {
              temp = worldArray.get(i, j-step).getTempAvg() - worldArray.get(i, j).getTempAvg();
              while (counter < step)
              {
                worldArray.get(i, j-counter).setTempAvg((temp / step) * counter + worldArray.get(i, j).getTempAvg());
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
            //System.out.println("Initial precip data not a grid in worldArrays: " + numFound);
          }
        }
      }
    }
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i, j).getTempAvg() == -1)
        {
          step = 1;
          counter = 1;
          while (step < X_CELLS)
          {
            if (worldArray.get(i + step, j).getTempAvg() != -1)
            {
              tempX = worldArray.get(i + step, j).getTempAvg();
              break;
            }
            step++;
          }
          while (counter < X_CELLS)
          {
            if (worldArray.get(i - counter, j).getTempAvg() != -1)
            {
              tempX = tempX - worldArray.get(i - counter, j).getTempAvg();
              break;
            }
            counter++;
          }
          tempX = (tempX / (step + counter) * counter) + worldArray.get(i - counter, j).getTempAvg();
          step = 1;
          counter = 1;
          while (step < Y_CELLS)
          {
            if (worldArray.get(i, j + step).getTempAvg() != -1)
            {
              tempY = worldArray.get(i, j + step).getTempAvg();
              break;
            }
            step++;
          }
          while (counter < Y_CELLS)
          {
            if (worldArray.get(i, j - counter).getTempAvg() != -1)
            {
              tempY = tempY - worldArray.get(i, j - counter).getTempAvg();
              break;
            }
            counter++;
          }
          tempY = (tempY / (step + counter) * counter) + worldArray.get(i, j - counter).getTempAvg();
          worldArray.get(i, j).setTempAvg((tempX + tempY) / 2);
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
          bw.write(Float.toString(worldArray.get(i, j).getPrecip()));
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
  }
}

