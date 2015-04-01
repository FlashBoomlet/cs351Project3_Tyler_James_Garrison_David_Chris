package model;

/**
 * Created by Gandalf on 3/15/15.
 */
public class WorldArray
{
  private int X_CELLS;
  private int Y_CELLS;
  private WorldCell [][] worldCells;

  public WorldArray (int lonLength, int latHeight)
  {
    worldCells = new WorldCell [lonLength][latHeight];
    X_CELLS = lonLength;
    Y_CELLS = latHeight;
    initCells();
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
        tempLat = (180 / Math.PI) * Math.asin((j - (Y_CELLS/2)) / (Y_CELLS/2));
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
