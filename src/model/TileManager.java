package model;


import model.common.AbstractClimateData;
import static model.common.AbstractScenario.*;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.*;


/**
 * @author: david
 * cs351
 * WorldFoodGame project
 * <p/>
 * description:
 * <p/>
 * TileManager wraps the collection of LandTiles and encapsulates the
 * equal area projection used to define the tile space.
 * It provides an interface for mutating the climate data in the tiles by year and
 * obtaining various useful subsets of tiles and/or individual tiles and data by
 * coordinates.
 */

public class TileManager
{
  public static final LandTile NO_DATA = new LandTile(-180, 0); /* in pacific */

  public static final int EARTH_CIRCUMFERENCE = 40000; /* approximate, in km */
  public static final int EARTH_RADIUS = 6350; /* km */
  public static final int EARTH_AREA = (int) (4 * EARTH_RADIUS * EARTH_RADIUS * Math.PI);
  public static final int TILE_AREA = 2500; /* square km */
  public static final int NUM_TILES = EARTH_AREA / TILE_AREA;
  public static final int PROJECTION_ASPECT = 3; /* approximate (π) */
  public static final int ROWS = (int) Math.sqrt(NUM_TILES / PROJECTION_ASPECT);
  public static final int COLS = ROWS * PROJECTION_ASPECT;

  public static final double MIN_LAT = -90;
  public static final double MAX_LAT = 90;
  public static final double MIN_LON = -180;
  public static final double MAX_LON = 180;
  public static final double LAT_RANGE = MAX_LAT - MIN_LAT;
  public static final double LON_RANGE = MAX_LON - MIN_LON;

  public static final double DLON = LON_RANGE / COLS;
  public static final double DLAT = LAT_RANGE / ROWS;

  /* these are fairly rough estimates for the distance between two tiles on the
   X and Y axes.  Should be acceptable for our purposes */
  public static final double DX_KM = EARTH_CIRCUMFERENCE / COLS;
  public static final double DY_KM = EARTH_CIRCUMFERENCE / ROWS * 0.5;

  /* max radius from selected tiles to add noise to each year */
  public static final double NOISE_RADIUS = 100; /* in km */

  private static final Random RNG = new Random();
  private World world;

  private LandTile[][] tiles = new LandTile[ROWS][COLS];


  private List<LandTile> countryTiles = new ArrayList<>();
  private List<LandTile> allTiles;
  private List<LandTile> dataTiles;
  private double randomPercent = 1f;

  public TileManager()
  {
    for(LandTile[] row : tiles) Arrays.fill(row, NO_DATA);
  }

  public static TileManager createEmptyTileManager()
  {
    TileManager mgr = new TileManager();
    initTiles(mgr.tiles);
    return mgr;
  }

  /* initialize a new tile set with proper latitude and longitude center points.
    This is really only used for making tiles for a new data set*/
  private static void initTiles(LandTile[][] tileset)
  {
    System.out.println(COLS);
    System.out.println(ROWS);
    for (int row = 0; row < ROWS; row++)
    {
      for (int col = 0; col < COLS; col++)
      {
        double lon = colToLon(col);
        double lat = rowToLat(row);
        tileset[row][col] = new LandTile(lon, lat);
      }
    }
  }


  /**
   * @return all the tiles in this manager in a List
   */
  public List<LandTile> allTiles()
  {
    if (allTiles == null)
    {
      allTiles = new ArrayList<>();
      for (LandTile[] arr : tiles) allTiles.addAll(Arrays.asList(arr));
    }
    return allTiles;
  }


  /* return the theoretical center longitude line of tiles in a given column */
  public static double colToLon(int col)
  {
    return LON_RANGE * ((double) col) / COLS - MAX_LON + 0.5 * DLON;
  }


  /* return the theoretical center latitude line of tiles in a given row */
  public static double rowToLat(int row)
  {
    /* bring the row index into floating point range [-1,1] */
    double measure = (row * 2d / ROWS) - 1;

    /* arcsin brings measure into spherical space.  Convert to degrees, and shift
      by half of DLAT (~latitude lines covered per tile)  */
    return Math.toDegrees(Math.asin(measure)) + 0.5 * DLAT;
  }

  /**
   * Get a tile by longitude and latitude
   *
   * @param lon
   *     degrees longitude
   * @param lat
   *     degrees latitude
   *
   * @return the tile into which the specified longitude and latitude coordinates
   * fall.  If no tile exists at that point, NO_DATA is returned
   */
  public LandTile getTile(double lon, double lat)
  {
    if (!coordsInBounds(lon, lat))
    {
      throw new IllegalArgumentException(
          String.format("coordinates out of bounds. lon: %.3f, lat: %.3f", lon, lat)
      );
    }

    /* equal area projection is encapsulated here */
    int col = lonToCol(lon);
    int row = latToRow(lat);
    LandTile tile = tiles[row][col];
    return tile == null ? NO_DATA : tile;
  }


  /* check given longitude and latitude coordinates for validity */
  private boolean coordsInBounds(double lon, double lat)
  {
    return lon >= MIN_LON && lon <= MAX_LON && lat >= MIN_LAT && lat <= MAX_LAT;
  }


  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private static int lonToCol(double lon)
  {
    return (int) Math.min((COLS * (lon + MAX_LON) / LON_RANGE), COLS - 1);
  }


  /* given a longitude line, return the column index corresponding to tiles
    containing that line */
  private static int latToRow(double lat)
  {
    /* sine of latitude, shifted into [0,2] */
    double sinShift = Math.sin(Math.toRadians(lat)) + 1;
    double row = ROWS * sinShift / 2;

    /* take minimum of row and max row value, for the outlier lat = 90 */
    return (int) Math.min(row, ROWS - 1);
  }


  /**
   * Mutates all the tile data based on projections maintained within each tile
   * and noise added randomly.
   */
  public void stepTileData()
  {
    List<LandTile> tiles = dataTiles();
    for (LandTile tile : tiles)
    {
      int currentYear = world.getCurrentYear();
      tile.stepTile(END_YEAR - currentYear);
    }

    /* shuffle tiles before adding noise */
    Collections.shuffle(tiles);

    /* take ten percent of tiles, add noise */
    for (LandTile tile : tiles.subList(0, tiles.size() / 10))
    {
      addNoiseByTile(tile);
    }
  }

  public void retainAll(Collection<LandTile> toRetain)
  {
    for(LandTile t : toRetain) if(!allTiles().contains(t)) removeTile(t);
  }


  /**
   * Returns a Collection of the tiles held by this TileManager that actually
   * contain data.  This, in effect, excludes tiles that would be over ocean and
   * those at the extremes of latitude.  For all tiles, use allTiles();
   *
   * @return a Collection holding only those tiles for which there exists raster
   * data.
   */
  public List<LandTile> dataTiles()
  {
    if (null == dataTiles)
    {
      dataTiles = new ArrayList<>();
      for (LandTile t : allTiles())
      {
        if (NO_DATA != t) dataTiles.add(t);
      }
    }
    return dataTiles;
  }


  /* adds noise to the parameters of all the tiles within the NOISE_RADIUS of
    a given tile */
  private void addNoiseByTile(LandTile tile)
  {
    int centerRow = latToRow(tile.getLat());
    int centerCol = lonToCol(tile.getLon());

    /* calculate min and max row and column based on radius of noise addition and
      the current tile's location in the data */
    int minRow = centerRow - (int) (NOISE_RADIUS / DY_KM);
    int maxRow = centerRow + (int) (NOISE_RADIUS / DY_KM);
    int minCol = centerCol - (int) (NOISE_RADIUS / DX_KM);
    int maxCol = centerCol + (int) (NOISE_RADIUS / DX_KM);

    /* get the source tile's data.
      All noise is added as a function of these values */
    float minTemp = tile.getMinAnnualTemp();
    float maxTemp = tile.getMaxAnnualTemp();
    float pmTemp = tile.getAvgNightTemp();
    float amTemp = tile.getAvgDayTemp();
    float rain = tile.getRainfall();

    /* noise is also a function of two random numbers in range [0,1]
    (generated once per source?) */
    double r1 = RNG.nextDouble();
    double r2 = RNG.nextDouble();

    float dMaxMinTemp = calcTileDelta(minTemp, maxTemp, r1, r2);
    float dAMPMTemp = calcTileDelta(pmTemp, amTemp, r1, r2);
    float dRainfall = calcTileDelta(0, rain, r1, r2);

    LandTile neighbor;

    for (int r = minRow; r < maxRow; r++)
    {
      for (int c = minCol; c < maxCol; c++)
      {
        /* allow overlap in data to account for the sphere */
        int colIndex = c < 0 ? COLS + c : c % COLS;
        int rowIndex = r < 0 ? ROWS + r : r % ROWS;
        if (colIndex < 0 || rowIndex < 0)
        {
          System.out.printf("c: %d, colIndex: %d, r: %d, rowIndex: %d", c, colIndex, r, rowIndex);
        }
        if (tiles[rowIndex][colIndex] == NO_DATA) continue;

        neighbor = tiles[rowIndex][colIndex];

        double xDist = DX_KM * (centerCol - c);
        double yDist = DY_KM * (centerRow - r);

        double dist = Math.sqrt(xDist * xDist + yDist * yDist);

        if (dist < NOISE_RADIUS)
        {

          double r3 = RNG.nextDouble() * 2;

          float toAddMaxMin = scaleDeltaByDistance(dMaxMinTemp, dist, r3);
          float toAddAMPM = scaleDeltaByDistance(dAMPMTemp, dist, r3);
          float toAddRain = scaleDeltaByDistance(dRainfall, dist, r3);

        }
      }
    }
  }


  /**
   * Calculates the delta value used to add noise to tiles, given a maximum and
   * minimum value for the parameter, and two random numbers in range [0,1]
   * To use this for the annual rainfall delta, use 0 as the minimum.
   *
   * @param min
   *     minimum value in the range of the data
   * @param max
   *     maximum value in the range of the data
   * @param r1
   *     first random number between 0 and 1
   * @param r2
   *     second random number between 0 and 1
   *
   * @return the delta value used to randomize climate data
   */
  public float calcTileDelta(double min, double max, double r1, double r2)
  {
    return (float) (0.1 * (max - min) * randomPercent * (r1 - r2));
  }


  /**
   * Calculates the actual amount to add to a given parameter in a LandTile, given
   * the delta value, the distance between the tile from which noise is being added
   * and a random number in range [0,2]
   *
   * @param delta
   *     calculated delta value
   * @param distance
   * @param r3
   */
  public float scaleDeltaByDistance(double delta, double distance, double r3)
  {
    return (float) (delta / (Math.log(Math.E + distance * r3)));
  }


  /**
   * Add a given tile to the data set.
   * This should really only be used when reading in a new set of tiles from
   * a file.
   *
   * @param tile
   *     LandTile to add
   */
  public void putTile(LandTile tile)
  {
    double lon = tile.getLon();
    double lat = tile.getLat();
    tiles[latToRow(lat)][lonToCol(lon)] = tile;
  }


  /**
   * Registers a tile as having been associated with a country.  Due to gaps in the
   * country data, if a set of tiles covering all the land is desired, use dataTiles()
   *
   * @param tile
   *     tile to register
   */
  public void registerCountryTile(LandTile tile)
  {
    countryTiles.add(tile);
  }


  /**
   * Returns a Collection of tiles that have been registered with a country.
   * This is dependent on the usage of registerCountryTile() at initial data
   * creation. (Also maybe should be refactored to another location?)
   *
   * @return Collection of those LandTiles that have been registered with a Country
   */
  public List<LandTile> countryTiles()
  {
    return countryTiles;
  }


  /**
   * remove a given tile from the underlying set of tiles.  This has the effect
   * of placing a NO_DATA tile at the location of the given tile in the full
   * projection (assuming the given tile was found)
   *
   * @param tile
   *     tile to remove
   *
   * @return true if the tile was found and removed
   */
  public boolean removeTile(LandTile tile)
  {
    int col = lonToCol(tile.getLon());
    int row = latToRow(tile.getLat());

    /* check if tile is in the right position */
    boolean ret = tiles[row][col] == tile;

    if (!ret) /* only search if the tile is not in its proper place */
    {
      loop:
      for (int r = 0; r < ROWS; r++)
      {
        for (int c = 0; c < COLS; c++)
        {
          if (ret = (tiles[r][c] == tile))
          {
            /* "remove" first instance of tile from underlying array */
            tiles[r][c] = NO_DATA;
            break loop;
          }
        }
      }
    }
    /* tile is in the right place, remove it */
    else
    {
      tiles[row][col] = NO_DATA;
    }

    /* pull all tiles from method ref, to guarantee the list exists */
    ret = ret || allTiles().remove(tile);
    ret = ret || countryTiles.remove(tile);

    /* ret is true iff the tile was a member of one of the underlying structures */
    return ret;
  }


  /* check given row and column indices for validity */
  private boolean indicesInBounds(int row, int col)
  {
    return col >= 0 && col < COLS && row >= 0 && row < ROWS;
  }


  /**
   * Exception used if accessors from the AbstractClimateData class are given
   * invalid coordinates
   */
  static class NoDataException extends IllegalArgumentException
  {

    public NoDataException(String msg)
    {
      super(msg);
    }
  }
}
