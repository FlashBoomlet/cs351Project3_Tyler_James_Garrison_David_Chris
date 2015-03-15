package IO;
import model.World;
import model.WorldCell;

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.io.FileReader;
/**
 * Created by Tim on 3/14/15.
 */
public class WorldDataParser
{
  private World world = null;
  private WorldCell [][] worldCells = null;
  private int LONGITUDE_CELL_NUM = 40075/10 + 1; //40075 km = Circumference of the earth around the equator
  private int LATITUDE_CELL_NUM = 40008/10 + 1;  //40008 km = Circumference from pole to pole

  public WorldDataParser (World input)
  {
    world = input;
    worldCells = world.getWorldCells();
  }

  public void parsePrecip (String fileURL)
  {
    try
    {
      Scanner rainScan = new Scanner(new FileReader(fileURL));
      String [] months;
      double [] monthsDouble = null;
      String current = null;
      int cellX = 0;
      int cellY = 0;
      double currentLon = 0;
      double currentLat = 0;
      while (rainScan.hasNext())
      {
        if (rainScan.hasNextDouble())
        {
          currentLon = rainScan.nextDouble();
          currentLat = rainScan.nextDouble();
          while (!worldCells[cellX][cellY].pointInCell(currentLon, currentLat))
          {
            if (cellY == LATITUDE_CELL_NUM - 1)
            {
              if (cellX == LONGITUDE_CELL_NUM - 1)
              {
                System.out.println("Point is in no cell.");
              }
              cellY = 0;
              cellX++;
            }
            else
            {
              cellY++;
            }
          }
          current = rainScan.nextLine();
          months = current.split("\\s+");
          for (int i = 0; i < months.length; i++)
          {
            monthsDouble[1] = Double.parseDouble(months[i]);
          }
          worldCells[cellX][cellY].setAllPrecip(monthsDouble);
        }
        else
        {
          System.out.println("OOPS");
        }
      }
    }
    catch (FileNotFoundException e)
    {
      e.printStackTrace();
    }
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
