package model;
import java.util.Random;
import java.util.HashSet;

/**
 * Created by Tim on 3/15/15.
 */
public class WorldArray
{
  private int X_CELLS;
  private int Y_CELLS;
  private WorldCell [][] worldCells;
  float randPercent;

  public WorldArray (int lonLength, int latHeight, float randPercent)
  {
    worldCells = new WorldCell [lonLength][latHeight];
    X_CELLS = lonLength;
    Y_CELLS = latHeight;
    initCells();
    this.randPercent = randPercent;
  }

  private void initCells ()
  {
    double tempLon = 0;
    double tempLat = 0;
    for (int i = 0; i < X_CELLS; i++)
    {
      for (int j = 0; j < Y_CELLS; j++)
      {
        tempLon = (i - (X_CELLS/2)) * 180 / (X_CELLS/2);
        tempLat = (j - (Y_CELLS/2)) / ((float) (Y_CELLS/2));
        tempLat = Math.asin(tempLat);
        tempLat = tempLat * (180 / Math.PI);
        worldCells [i][j] = new WorldCell(tempLon, tempLat);
      }
    }
  }

  public int getXSize ()
  {
    return X_CELLS;
  }

  public int getYSize ()
  {
    return Y_CELLS;
  }

  public int [] getNumber (double lon, double lat)
  {
    int [] out = new int [2];
    double tempX = (((lon/180) * X_CELLS/2) + X_CELLS/2);
    double tempY = ((Y_CELLS/2 * Math.sin(Math.PI * lat/180)/(Math.PI/2)) + Y_CELLS/2);
    out [0] = (int) Math.round(tempX);
    out [1] = (int) Math.round(tempY);
    return out;
  }

  /**
   *
   * @param lon
   * @param lat
   * @return
   */
  public WorldCell get (double lon, double lat, boolean accurate)
  {
    double tempX = (((lon/180) * X_CELLS/2) + X_CELLS/2);
    double tempY = ((Y_CELLS/2 * Math.sin(Math.PI * lat/180)/(Math.PI/2)) + Y_CELLS/2);
    if (accurate)
    {
      return worldCells[(int) Math.round(tempX)][(int) Math.round(tempY)];
    }
    else
    {
      return worldCells[(int) tempX][(int) tempY];
    }
  }

  /**
   * Can only deal with going off the edge one direction at a time. get (-1, -1)
   * will result in an incorrect result.
   * @param x
   * @param y
   * @return
   */
  public WorldCell get (int x, int y)
  {
    /*
    if (x < (X_CELLS / 2) && x > (- X_CELLS /2) && y < (Y_CELLS /2) && y > (- Y_CELLS /2))
    {
      return worldCells[x + X_CELLS/2][y + Y_CELLS/2];
    }
    */
    int modifiedX = x;
    int modifiedY = y;
    if (x < X_CELLS && x > -1 && y < Y_CELLS && y > -1)
    {
      return worldCells[x][y];
    }
    else
    {
      if (x >= X_CELLS)
      {
        modifiedX = x - X_CELLS;
      }
      else if (x < 0)
      {
        modifiedX = x + X_CELLS;
      }
      else if (y >= Y_CELLS)
      {
        if (x < (X_CELLS/2))
        {
          modifiedX = x + X_CELLS/2;
        }
        else
        {
          modifiedX = x - X_CELLS/2;
        }
        modifiedY = Y_CELLS - (y - Y_CELLS) - 1;
      }
      else if (y < 0)
      {
        if (x < (X_CELLS/2))
        {
          modifiedX = x + X_CELLS/2;
        }
        else
        {
          modifiedX = x - X_CELLS/2;
        }
        modifiedY = (- y) - 1;
      }
      return get (modifiedX, modifiedY);
    }
  }

  /**
   *Somewhat inefficient, could try to visit ocean cells
   */
  public void addNoise ()
  {
    int cellsToVisit = (int) (X_CELLS * Y_CELLS * .1);
    int counter = 0;
    Random rand = new Random();
    HashSet <WorldCell> visited = new HashSet();
    int tempX = 0;
    int tempY = 0;
    float r1 = rand.nextFloat();
    float r2 = rand.nextFloat();
    float deltaOne = 0;
    float deltaTwo = 0;
    float deltaThree = 0;
    while (counter < cellsToVisit)
    {
      tempX = rand.nextInt(X_CELLS);
      tempY = rand.nextInt(Y_CELLS);
      if (!visited.contains(worldCells[tempX][tempY]) && worldCells[tempX][tempY].hasCountry())
      {
        deltaOne = (float) ((worldCells[tempX][tempY].getAnnualHigh() - worldCells[tempX][tempY].getAnnualLow()) * .1 * randPercent * (r1 - r2));
        worldCells[tempX][tempY].setAnnualHigh(worldCells[tempX][tempY].getAnnualHigh() + deltaOne);
        worldCells[tempX][tempY].setAnnualLow(worldCells[tempX][tempY].getAnnualLow() + deltaOne);
        //deltaTwo = (float) ((worldCells[tempX][tempY].getMonthlyDayAvg() - worldCells[tempX][tempY].getMonthlyNightAvg()) * .1 * randPercent * (r1 - r2));
        //worldCells[tempX][tempY].setMonthlyDayAvg(worldCells[tempX][tempY].getMonthlyDayAvg() + deltaTwo);
        //worldCells[tempX][tempY].setMonthlyNightAvg(worldCells[tempX][tempY].getMonthlyNightAvg() + deltaTwo);
        deltaThree = (float) (worldCells[tempX][tempY].getPrecip() * .1 * randPercent * (r1 - r2));
        worldCells[tempX][tempY].setPrecip(worldCells[tempX][tempY].getPrecip() + deltaThree);
        for (int i = -1; i < 2; i++)
        {
          //if land
          noiseHelp(tempX + i, tempY + 1, deltaOne, deltaTwo, deltaThree, rand.nextFloat());
        }
        for (int j = -1; j < 2; j++)
        {
          //if land
          noiseHelp(tempX + j, tempY - 1, deltaOne, deltaTwo, deltaThree, rand.nextFloat());
        }
        //if land
        noiseHelp(tempX + 1, tempY, deltaOne, deltaTwo, deltaThree, rand.nextFloat());
        //if land
        noiseHelp(tempX - 1, tempY, deltaOne, deltaTwo, deltaThree, rand.nextFloat());
        visited.add(worldCells[tempX][tempY]);
      }
      else
      {
        continue;
      }
      counter++;
    }
  }

  private void noiseHelp (int x, int y, float deltaOne, float deltaTwo, float deltaThree, float r3)
  {
    worldCells[x][y].setAnnualHigh(worldCells[x][y].getAnnualHigh() + (float) (deltaOne/Math.log(Math.E + 100 * r3)));
    worldCells[x][y].setAnnualLow(worldCells[x][y].getAnnualLow() + (float) (deltaOne/Math.log(Math.E + 100 * r3)));
    //worldCells[x][y].setMonthlyDayAvg(worldCells[x][y].getMonthlyDayAvg() + (float) (deltaTwo/Math.log(Math.E + 100 * r3)));
    //worldCells[x][y].setMonthlyNightAvg(worldCells[x][y].getMonthlyNightAvg() + (float) (deltaTwo/Math.log(Math.E + 100 * r3)));
    worldCells[x][y].setPrecip(worldCells[x][y].getPrecip() + (float) (deltaThree/Math.log(Math.E + 100 * r3)));
  }

  /**
   public boolean pointInCell (double lonPoint, double latPoint)
   {
   if (latPoint >= lat && latPoint < (lat + EDGE_Y_LAT) && lonPoint >= lon && lonPoint < (lon + EDGE_X_LON))
   {
   return true;
   }
   return false;
   }
   */
}
