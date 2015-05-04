package IO;


import model.LandTile;
import model.TileManager;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

import static model.LandTile.FIELD.*;
import static model.LandTile.FIELD.ELEVATION;


/**
 @author david
 created: 2015-03-22
 <p/>
 description: */
public class LandDataIO
{

  private static final double DATA_ARC_STEP = 0.041666666;
  private static final int DATA_ROWS = 3600;
  private static final int DATA_COLS = 8640;
  private static String DEFAULT_FILE = "resources/data/landtile-rawdata/land-data.bil";

  public static void main(String[] args)
  {
    RawDataConverter.DATA_FIELD[] fields =
        {
            RawDataConverter.DATA_FIELD.BIOC1,
            RawDataConverter.DATA_FIELD.BIOC2,
            RawDataConverter.DATA_FIELD.BIOC3,
            RawDataConverter.DATA_FIELD.BIOC4,
        };
    long start = System.currentTimeMillis();
    validateFile(DEFAULT_FILE);
    System.out.printf("took %fs to read file%n", (System.currentTimeMillis()-start)/1000f);
  }


  private static void test()
  {
    TileManager mgr = TileManager.createEmptyTileManager();
    String root = "resources/data/landtile-rawdata/";
    Collection<RawDataConverter.DATA_FIELD> fields = new ArrayList<>();
    fields.add(RawDataConverter.DATA_FIELD.BIOC1);
    writeToFile(mgr, "resources/data/landtile-rawdata/land-data.bil");
  }



  private static void validateFile(String file)
  {
    TileManager mgr = parseFile(file);
    Map<RawDataConverter.DATA_FIELD, Float> avgs = new HashMap<>();
    Map<RawDataConverter.DATA_FIELD, Integer> counts = new HashMap<>();

    int nulls = 0;
    int no_data = 0;
    for(LandTile t : mgr.allTiles())
    {
      if(t == null) nulls++;
      else if(t == LandTile.NO_DATA) no_data++;
      else
      {
        for(RawDataConverter.DATA_FIELD field : RawDataConverter.DATA_FIELD.values())
        {
          if(!avgs.containsKey(field))
          {
            avgs.put(field, t.getData(field.landTileField));
            counts.put(field, 1);
          }
          else
          {
            float avg = avgs.get(field);
            int count = counts.get(field);
            avg = (avg * count + t.getData(field.landTileField)) / (++count);
            avgs.put(field, avg);
            counts.put(field, count);
          }
        }
      }
    }
    System.out.println("nulls : " + nulls);
    System.out.println("no_data : " + no_data);
    for(RawDataConverter.DATA_FIELD f: RawDataConverter.DATA_FIELD.values())
    {
      System.out.printf("%s average : %f%n", f.toString(), avgs.get(f));
    }
  }


  public static TileManager parseFile(String filename)
  {
    TileManager mgr = new TileManager();
    try (FileInputStream in = new FileInputStream(filename))
    {
      long start = System.currentTimeMillis();
      long count = 0;
      byte bytes[] = new byte[LandTile.FIELD.SIZE_IN_BYTES];
      ByteBuffer buf = ByteBuffer.allocate(LandTile.FIELD.SIZE_IN_BYTES);
      LandTile tile;
      while (in.read(bytes) != -1)
      {
        if (++count % 10000 == 0)
        {
          System.out.printf("read %d tiles in %fs%n",
                            count, (System.currentTimeMillis() - start) / 1000d);
        }
        buf.clear();
        buf.put(bytes);
        tile = new LandTile(buf);
        mgr.putTile(tile);

      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }

    return mgr;
  }


  /**
   Writes ALL tiles in the specified TileManager to file.  If a subset is required, use the
   overloaded version that accepts a Collection of LandTiles

   @param mgr
   TileManger whose tiles are to be written
   @param filename
   Relative path to write to
   */
  public static void writeToFile(TileManager mgr, String filename)
  {
    writeToFile(mgr.allTiles(), filename);
  }


  public static void writeToFile(Collection<LandTile> tiles, String filename)
  {
    try (FileOutputStream out = new FileOutputStream(filename))
    {
      for (LandTile t : tiles)
      {
        out.write(t.toByteBuffer().array());
      }
    }
    catch (IOException e)
    {
      e.printStackTrace();
    }
  }


  private static class RawDataConverter
  {

    private static final String DEFAULT_ROOT = "resources/data/landtile-rawdata/";
    private static final int NO_DATA_DEFAULT_CURR = -9999;
    private static final int NO_DATA_DEFAULT_PROJ = -32768;
    private final Map<LandTile, DataPoint> map = new ConcurrentHashMap<>();
    private final TileManager manager;
    private String rootDirectory;


    private RawDataConverter(TileManager manager, String root)
    {
      this.manager = manager;
      rootDirectory = root.endsWith("/") ? root : root + "/";
    }


    private void appendData(Collection<DATA_FIELD> fields)
    {
      final Map<Runnable, Boolean> done = new ConcurrentHashMap<>();
      for (final DATA_FIELD field : fields)
      {
        Runnable target = new Runnable()
        {
          @Override
          public void run()
          {
            readField(field);
            setField(field);
            done.put(this, true);
          }

          @Override
          public String toString()
          {
            return field.toString();
          }
        };

        done.put(target, false);
        Thread t = new Thread(target);
        t.start();
      }

      int total = done.keySet().size();
      while(true)
      {
        int numDone = 0;
        for(Runnable r : done.keySet()) if(done.get(r)) numDone++;
        if(total - numDone < 1) break;
        if(total - numDone < 5)
        {
          System.out.printf("%d left of %d%n", total-numDone, total);
          for(Runnable r : done.keySet()) if(!done.get(r)) System.out.println(r);
          try
          {
            Thread.sleep(2000);
          }
          catch (InterruptedException e)
          {
          }
        }
      }
    }


    private void readField(DATA_FIELD field)
    {
      long start = System.currentTimeMillis();

      try (FileInputStream stream = new FileInputStream(new File(rootDirectory + field.filename)))
      {
        for (int row = 0; row < DATA_ROWS; row++)
        {
          if (row % 100 == 0) System.out.println(field + " - row: " + row);

          for (int col = 0; col < DATA_COLS; col++)
          {
            double lat = 90 - row * DATA_ARC_STEP;
            double lon = -180 + col * DATA_ARC_STEP;

            LandTile tile = manager.getTile(lon, lat);
            if (!map.containsKey(tile)) map.put(tile, new DataPoint());

            DataPoint point = map.get(tile);

            byte first = (byte) stream.read();
            byte second = (byte) stream.read();

            float data = twoByteToSignedInt(first, second);
            if (!(data == field.noDataVal)) point.putData(field, data);
            else
            {
            }
          }
        }
      }
      catch (IOException e)
      {
        e.printStackTrace();
      }
      System.out.printf("reading and averaging field:%s took: %dms%n",
                        field, System.currentTimeMillis() - start);
    }


    private void setField(DATA_FIELD field)
    {
      for (LandTile tile : map.keySet())
      {
        tile.putData(field.landTileField, map.get(tile).getData(field) * DATA_FIELD.SCALE);
      }
    }


    private static int twoByteToSignedInt(byte first, byte second)
    {
      /* raster data is little-endian, two-byte signed ints */
      return (short) ((second << 8) | (first & 0xff));
    }


    static void createNewDataFile()
    {
      createNewDataFile(DATA_FIELD.values());
    }


    static void createNewDataFile(DATA_FIELD fields[])
    {
      TileManager mgr = TileManager.createEmptyTileManager();
      RawDataConverter rdc = new RawDataConverter(mgr,
                                                  DEFAULT_ROOT);
      rdc.appendData(Arrays.asList(fields));
      mgr.retainAll(rdc.getCompleteTiles());
      System.out.printf("%d data tiles%n", mgr.dataTiles().size());
      writeToFile(mgr.dataTiles(), DEFAULT_FILE);
    }


    private Collection<LandTile> getCompleteTiles()
    {
      Collection<LandTile> tiles = new ArrayList<>();
      for (LandTile t : map.keySet()) if (map.get(t).hasAllData()) tiles.add(t);
      System.out.printf("got %d complete tiles%n", tiles.size());
      return tiles;
    }


    public enum DATA_FIELD
    {
      BIOC1("bioc1.bil", NO_DATA_DEFAULT_CURR, CURRENT_ANNUAL_MEAN_TEMPERATURE),
      BIOC2("bioc2.bil", NO_DATA_DEFAULT_CURR, CURRENT_DIURNAL_RANGE),
      BIOC3("bioc3.bil", NO_DATA_DEFAULT_CURR, CURRENT_ISOTHERMALITY),
      BIOC4("bioc4.bil", NO_DATA_DEFAULT_CURR, CURRENT_TEMPERATURE_SEASONALITY),
      BIOC5("bioc5.bil", NO_DATA_DEFAULT_CURR, CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH),
      BIOC6("bioc6.bil", NO_DATA_DEFAULT_CURR, CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH),
      BIOC7("bioc7.bil", NO_DATA_DEFAULT_CURR, CURRENT_TEMPERATURE_ANNUAL_RANGE),
      BIOC8("bioc8.bil", NO_DATA_DEFAULT_CURR, CURRENT_MEAN_TEMPERATURE_OF_WETTEST_QUARTER),
      BIOC9("bioc9.bil", NO_DATA_DEFAULT_CURR, CURRENT_MEAN_TEMPERATURE_OF_DRIEST_QUARTER),
      BIOC10("bioc10.bil", NO_DATA_DEFAULT_CURR, CURRENT_MEAN_TEMPERATURE_OF_WARMEST_QUARTER),
      BIOC11("bioc11.bil", NO_DATA_DEFAULT_CURR, CURRENT_MEAN_TEMPERATURE_OF_COLDEST_QUARTER),
      BIOC12("bioc12.bil", NO_DATA_DEFAULT_CURR, CURRENT_ANNUAL_PRECIPITATION),
      BIOC13("bioc13.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_WETTEST_MONTH),
      BIOC14("bioc14.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_DRIEST_MONTH),
      BIOC15("bioc15.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_SEASONALITY),
      BIOC16("bioc16.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_WETTEST_QUARTER),
      BIOC17("bioc17.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_DRIEST_QUARTER),
      BIOC18("bioc18.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_WARMEST_QUARTER),
      BIOC19("bioc19.bil", NO_DATA_DEFAULT_CURR, CURRENT_PRECIPITATION_OF_COLDEST_QUARTER),
      BIOP1("biop1.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_ANNUAL_MEAN_TEMPERATURE),
      BIOP2("biop2.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_DIURNAL_RANGE),
      BIOP3("biop3.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_ISOTHERMALITY),
      BIOP4("biop4.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_TEMPERATURE_SEASONALITY),
      BIOP5("biop5.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MAX_TEMPERATURE_OF_WARMEST_MONTH),
      BIOP6("biop6.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MIN_TEMPERATURE_OF_COLDEST_MONTH),
      BIOP7("biop7.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_TEMPERATURE_ANNUAL_RANGE),
      BIOP8("biop8.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MEAN_TEMPERATURE_OF_WETTEST_QUARTER),
      BIOP9("biop9.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MEAN_TEMPERATURE_OF_DRIEST_QUARTER),
      BIOP10("biop10.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MEAN_TEMPERATURE_OF_WARMEST_QUARTER),
      BIOP11("biop11.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_MEAN_TEMPERATURE_OF_COLDEST_QUARTER),
      BIOP12("biop12.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_ANNUAL_PRECIPITATION),
      BIOP13("biop13.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_WETTEST_MONTH),
      BIOP14("biop14.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_DRIEST_MONTH),
      BIOP15("biop15.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_SEASONALITY),
      BIOP16("biop16.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_WETTEST_QUARTER),
      BIOP17("biop17.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_DRIEST_QUARTER),
      BIOP18("biop18.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_WARMEST_QUARTER),
      BIOP19("biop19.bil", NO_DATA_DEFAULT_PROJ, PROJECTED_PRECIPITATION_OF_COLDEST_QUARTER),
      ELEV("altitude.bil", NO_DATA_DEFAULT_CURR, ELEVATION),
      ;

      static final int SIZE = values().length;
      static final float SCALE = 0.1f;
      String filename;
      int noDataVal;
      LandTile.FIELD landTileField;


      DATA_FIELD(String filename, int noData, LandTile.FIELD landTileField)
      {
        this.landTileField = landTileField;
        this.filename = filename;
        noDataVal = noData;
      }


      int idx()
      {
        return ordinal();
      }
    }


    private static class DataPoint
    {

      float data[] = new float[DATA_FIELD.SIZE];
      int points[] = new int[DATA_FIELD.SIZE];
      boolean hasData[] = new boolean[DATA_FIELD.SIZE];


      public DataPoint()
      {
        Arrays.fill(data, 0);
        Arrays.fill(points, 0);
        Arrays.fill(hasData, false);
      }


      public void putData(DATA_FIELD field, float val)
      {
        hasData[field.idx()] = true;
        data[field.idx()] =
            (data[field.idx()] * points[field.idx()] + val) / (points[field.idx()] + 1);
        points[field.idx()]++;
      }


      public boolean hasData(DATA_FIELD field)
      {
        return hasData[field.idx()];
      }


      public boolean hasAllData()
      {
        for (boolean b : hasData) if (!b) return false;
        return true;
      }


      public float getData(DATA_FIELD field)
      {
        return data[field.idx()];
      }
    }
  }
}