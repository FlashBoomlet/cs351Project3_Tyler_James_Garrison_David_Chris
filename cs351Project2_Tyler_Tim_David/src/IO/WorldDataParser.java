package IO;
import model.World;
import model.WorldCell;
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
  //private int LONGITUDE_CELL_NUM = 40075/10 + 1; //40075 km = Circumference of the earth around the equator
  //private int LATITUDE_CELL_NUM = 40008/10 + 1;  //40008 km = Circumference from pole to pole

  public WorldDataParser (World input)
  {
    world = input;
    worldArray = world.getWorldArray();
    X_CELLS = worldArray.getXSize();
    Y_CELLS = worldArray.getYSize();
  }

  public void parsePrecip (String fileURL)
  {
    try
    {
      Scanner rainScan = new Scanner(new FileReader(fileURL));
      String [] strings;
      double [] months = new double [12];
      String current = null;
      int cellX = 0;
      int cellY = 0;
      double currentLon = 0;
      double currentLat = 0;
      while (rainScan.hasNext())
      {
        current = rainScan.nextLine();
        strings = current.split("\\s+");
        currentLon = Double.parseDouble(strings[0]);
        currentLat = Double.parseDouble(strings[1]);
        for (int i = 0; i < 12; i++)
        {
          months[i] = Double.parseDouble(strings[i + 2]);
        }
        worldArray.get(currentLon, currentLat, false).setAllPrecip(months);
      }
      //finalizePrecip();
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
  }
/**
  private void finalizePrecip ()
  {
    double [] tempAvg = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
    int numFound = 0;
    int step = 0;
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        if (worldArray.get(i,j).getPrecip() != -1)
        {
          while (numFound < 4)
          {
           // if (i + step )
          }
        }
      }
    }
  }
*/
  private double [] avgMonths (double [] arrayOne, double [] arrayTwo)
  {
    double [] temp = new double [12];
    for (int i = 0; i < 12; i++)
    {
      temp[i] = (arrayOne[i] + arrayTwo[i]) / 2;
    }
    return temp;
  }

  public void parseElevation (String fileURL)
  {

  }

  public void parseMaxTemp (String fileURL)
  {

  }

  public void parseMinTemp (String fileURL)
  {

  }

  public void parseDayTemp (String fileURL)
  {

  }

  public void parseNightTemp (String fileURL)
  {

  }

}
