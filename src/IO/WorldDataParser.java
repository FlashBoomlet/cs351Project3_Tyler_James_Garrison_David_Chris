package IO;
import model.World;

import model.WorldArray;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;

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

  /**
   * Read in max temperature data line by line.
   * @param fileURL
   */
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

  /**
   * Read in min temperature data line by line.
   * @param fileURL
   */
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

  /**
   * Read in precipitation data line by line.
   * @param fileURL
   */
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

  /**
   * Read in average temperature data line by line.
   * @param fileURL
   */
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
}

