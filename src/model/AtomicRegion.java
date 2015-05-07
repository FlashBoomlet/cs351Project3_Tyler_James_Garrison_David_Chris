package model;


import java.util.*;

import model.common.CropIdeals;
import model.common.EnumCropType;

import static model.LandTile.Field.*;
import static model.common.EnumCropType.*;


/**
 Represent a homogeneous area. Defined by a perimeter and various planting
 attributes. The class acts as a kind of container for the parsed XML data.

 Heavily modified from original representation.  Should consider refactor. - dmr

 @author winston riley

 Restructured by:
 @author Tyler Lynch <lyncht@unm.edu>

 Further restructuring, code porting
 @author david
 */
public class AtomicRegion implements Region, CropIdeals
{

  private List<MiniArea> perimeter;
  private String name;
  private String flagLocation;
  private HashMap<Region, Integer> relationshipMap = new HashMap<>();
  private CountryData data = null;
  private boolean officialCountry = false;
  private int OTHER_AVG_HIGH;
  private int OTHER_AVG_LOW;
  private int OTHER_MAX_HIGH;
  private int OTHER_MAX_LOW;
  private int OTHER_RAIN_HIGH;
  private int OTHER_RAIN_LOW;
  private Collection<LandTile> landTiles = new HashSet<>();
  private Collection<LandTile> relevantTiles = new HashSet<>();


  /**
   @return name
   */
  @Override
  public String getName()
  {
    return name;
  }


  /**
   Set the name of the region.

   @param name
   */
  @Override
  public void setName(String name)
  {
    this.name = name;
  }


  /**
   @return The flag location or null if none
   */
  @Override
  public String getFlag()
  {
    if (!flagLocation.contains("\n"))
    {
      return flagLocation;
    }
    else
    {
      return null;
    }
  }


  /**
   @param flagLocation
   flag's location
   */
  @Override
  public void setFlag(String flagLocation)
  {
    this.flagLocation = flagLocation;
  }


  /**
   @return All of the mini areas of the region.
   */
  @Override
  public List<MiniArea> getPerimeter()
  {
    return perimeter;
  }


  /**
   Set the mini areas of the region.

   @param perimeter
   */
  @Override
  public void setPerimeter(List<MiniArea> perimeter)
  {
    this.perimeter = perimeter;
  }


  @Override
  public void addLandTiles(Collection<LandTile> tiles)
  {
    landTiles.addAll(tiles);
  }


  /**
   @return Prints atomic region and the name.
   */
  public String toString()
  {
    return "AtomicRegion{" +
           "name='" + name + '\'' +
           '}';
  }


  public void setRelationship(HashMap<Region, Integer> rel)
  {
    relationshipMap.putAll(rel);
  }


  private class CropNum
  {

    protected int num = 0;
    protected EnumCropType crop;
    protected int priority = 0;


    public CropNum(int num, EnumCropType crop)
    {
      this.num = num;
      this.crop = crop;
      if (crop.equals(WHEAT))
      {
        priority = 0;
      }
      else if (crop.equals(SOY))
      {
        priority = 1;
      }
      else if (crop.equals(CORN))
      {
        priority = 2;
      }
      else if (crop.equals(RICE))
      {
        priority = 3;
      }
      else if (crop.equals(OTHER_CROPS))
      {
        priority = 4;
      }
    }
  }


  /**
   Places crops in something resembling an optimal configuration.
   This code was ported to LandTile representation from WorldCell.  Should still work. - dmr
   */
  public void setCrops()
  {
    if (data == null)
    {
      return;
    }
    HashSet<LandTile> leftovers = new HashSet<>();
    HashSet<LandTile> leftovers2 = new HashSet<>();
    HashSet<LandTile> leftovers3 = new HashSet<>();
    LinkedList<CropNum> cropPriority = new LinkedList<>();
    double arableTotal = data.getArableOpen(true);
    arableTotal = arableTotal + data.getCornLand(true);
    arableTotal = arableTotal + data.getRiceLand(true);
    arableTotal = arableTotal + data.getOtherLand(true);
    arableTotal = arableTotal + data.getWheatLand(true);
    arableTotal = arableTotal + data.getSoyLand(true);

    int tilesNeeded = (int) arableTotal / TileManager.TILE_AREA;
    setPriority(arableTotal, tilesNeeded, cropPriority);

    //Ideal placement
    for (LandTile cell : relevantTiles)
    {
      if (!cropPriority.isEmpty())
      {
        for (CropNum current : cropPriority)
        {
          if (checkIdeal(cell, current.crop) == 0)
          {
            cell.update(current.crop, (float) 1);
            if (current.num - 1 == 0)
            {
              cropPriority.remove(current);
            }
            else
            {
              resortCrop(current, cropPriority);
            }
            break;
          }
          else
          {
            leftovers.add(cell);
          }
        }
      }
      else
      {
        leftovers.add(cell);
      }
    }
    if (cropPriority.isEmpty())
    {
      clearCells(leftovers);
      return;
    }

    //Acceptable placement
    for (LandTile cell : leftovers)
    {
      if (!cropPriority.isEmpty())
      {
        for (CropNum current : cropPriority)
        {
          if (checkIdeal(cell, current.crop) == 1)
          {
            cell.update(current.crop, (float) 0.6);
            if (current.num - 1 == 0)
            {
              cropPriority.remove(current);
            }
            else
            {
              resortCrop(current, cropPriority);
            }
            break;
          }
          else
          {
            leftovers2.add(cell);
          }
        }
      }
      else
      {
        leftovers2.add(cell);
      }
    }
    if (cropPriority.isEmpty())
    {
      clearCells(leftovers2);
      return;
    }

    //Poor placement
    CropNum current;
    for (LandTile cell : leftovers2)
    {
      if (!cropPriority.isEmpty())
      {
        current = cropPriority.peek();
        cell.update(current.crop, (float) 0.25);
        if (current.num - 1 == 0)
        {
          cropPriority.remove(current);
        }
        else
        {
          resortCrop(current, cropPriority);
        }
      }
      else
      {
        leftovers3.add(cell);
      }
    }
    clearCells(leftovers3);
  }


  @Override
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    for (MiniArea area : perimeter)
    {
      if (area.containsMapPoint(mapPoint)) return true;
    }
    return false;
  }


  @Override
  public void addLandTile(LandTile tile)
  {
    landTiles.add(tile);
  }


  @Override
  public Collection<LandTile> getArableCells()
  {
    return relevantTiles;
  }


  @Override
  public Collection<LandTile> getLandTiles()
  {
    return landTiles;
  }


  /**
   Sets the remaining cells to have no crops.

   @param leftovers
   Cells not used for crop placement.
   */
  private void clearCells(Collection<LandTile> leftovers)
  {
    for (LandTile cell : leftovers)
    {
      cell.update(NONE, (float) 1);
    }
  }


  /**
   Sets crops in the first year.
   */
  public void setFirstCrops()
  {
    if (data == null)
    {
      return;
    }
    setOtherIdeals();
    HashSet<LandTile> leftovers = new HashSet<>();
    HashSet<LandTile> leftovers2 = new HashSet<>();
    HashSet<LandTile> leftovers3 = new HashSet<>();
    LinkedList<CropNum> cropPriority = new LinkedList<>();
    double arableTotal;
    arableTotal = data.getArableOpen(true);
    arableTotal = arableTotal + data.getCornLand(true);
    arableTotal = arableTotal + data.getRiceLand(true);
    arableTotal = arableTotal + data.getOtherLand(true);
    arableTotal = arableTotal + data.getWheatLand(true);
    arableTotal = arableTotal + data.getSoyLand(true);
    int tilesNeeded = (int) arableTotal / TileManager.TILE_AREA;
    setPriority(arableTotal, tilesNeeded, cropPriority);

    //Ideal placement
    for (LandTile cell : landTiles)
    {
      if (!cropPriority.isEmpty())
      {
        for (CropNum current : cropPriority)
        {
          if (checkIdeal(cell, current.crop) == 0)
          {
            cell.updateFirstYear(current.crop, (float) 1);
            if (current.num - 1 == 0)
            {
              cropPriority.remove(current);
            }
            else
            {
              resortCrop(current, cropPriority);
            }
            relevantTiles.add(cell);
            break;
          }
          else
          {
            leftovers.add(cell);
          }
        }
      }
      else
      {
        leftovers.add(cell);
      }
    }
    if (cropPriority.isEmpty())
    {
      finalizeCells(leftovers, tilesNeeded);
      return;
    }
    //Acceptable placement
    for (LandTile cell : leftovers)
    {
      if (!cropPriority.isEmpty())
      {
        for (CropNum current : cropPriority)
        {
          if (checkIdeal(cell, current.crop) == 1)
          {
            cell.updateFirstYear(current.crop, (float) 0.6);
            if (current.num - 1 == 0)
            {
              cropPriority.remove(current);
            }
            else
            {
              resortCrop(current, cropPriority);
            }
            relevantTiles.add(cell);
            break;
          }
          else
          {
            leftovers2.add(cell);
          }
        }
      }
      else
      {
        leftovers2.add(cell);
      }
    }
    if (cropPriority.isEmpty())
    {
      finalizeCells(leftovers2, tilesNeeded);
      return;
    }
    //Poor placement
    CropNum current;
    for (LandTile tile : leftovers2)
    {
      if (!cropPriority.isEmpty())
      {
        current = cropPriority.peek();
        tile.updateFirstYear(current.crop, (float) 0.25);
        if (current.num - 1 == 0)
        {
          cropPriority.remove(current);
        }
        else
        {
          resortCrop(current, cropPriority);
        }
        relevantTiles.add(tile);
      }
      else
      {
        leftovers3.add(tile);
      }
    }
    finalizeCells(leftovers3, tilesNeeded);
    calculateYield();
  }


  private void calculateYield()
  {
    int[] landNums = new int[15];
    for (LandTile cell : relevantTiles)
    {
      if (cell.getCrop().equals(NONE))
      {
        continue;
      }
      else if (cell.getCrop().equals(CORN))
      {
        if (cell.getCropState() == 1)
        {
          landNums[0] = landNums[0] + 1;
        }
        else if (cell.getCropState() == (float) 0.6)
        {
          landNums[1] = landNums[1] + 1;
        }
        else if (cell.getCropState() == (float) 0.25)
        {
          landNums[2] = landNums[2] + 1;
        }
      }
      else if (cell.getCrop().equals(WHEAT))
      {
        if (cell.getCropState() == 1)
        {
          landNums[3] = landNums[3] + 1;
        }
        else if (cell.getCropState() == (float) 0.6)
        {
          landNums[4] = landNums[4] + 1;
        }
        else if (cell.getCropState() == (float) 0.25)
        {
          landNums[5] = landNums[5] + 1;
        }
      }
      else if (cell.getCrop().equals(SOY))
      {
        if (cell.getCropState() == 1)
        {
          landNums[6] = landNums[6] + 1;
        }
        else if (cell.getCropState() == (float) 0.6)
        {
          landNums[7] = landNums[7] + 1;
        }
        else if (cell.getCropState() == (float) 0.25)
        {
          landNums[8] = landNums[8] + 1;
        }
      }
      else if (cell.getCrop().equals(RICE))
      {
        if (cell.getCropState() == 1)
        {
          landNums[9] = landNums[9] + 1;
        }
        else if (cell.getCropState() == (float) 0.6)
        {
          landNums[10] = landNums[10] + 1;
        }
        else if (cell.getCropState() == (float) 0.25)
        {
          landNums[11] = landNums[11] + 1;
        }
      }
      else if (cell.getCrop().equals("Other"))
      {
        if (cell.getCropState() == 1)
        {
          landNums[12] = landNums[12] + 1;
        }
        else if (cell.getCropState() == (float) 0.6)
        {
          landNums[13] = landNums[13] + 1;
        }
        else if (cell.getCropState() == (float) 0.25)
        {
          landNums[14] = landNums[14] + 1;
        }
      }
    }
    data.calculateZeroOrder(landNums);
  }


  /**
   Grabs the number of arable cells still needed from
   the total after crop placement.

   @param tiles
   Cells not used yet for crops.
   @param cellsNeeded
   Number of arable land cells.
   */
  private void finalizeCells(Collection<LandTile> tiles, int cellsNeeded)
  {
    int length = cellsNeeded - relevantTiles.size();
    int counter = 0;
    for (LandTile tile : tiles)
    {
      relevantTiles.add(tile);
      counter++;
      if (counter == length)
      {
        break;
      }
    }
  }


  /**
   Checks if a cell is ideal for a certain crop.
   0 is ideal.
   1 is acceptable.
   -1 is poor.

   @param tile
   The cell.
   @param crop
   The crop to check.

   @return Ideal/Acceptable/Poor
   */
  private int checkIdeal(LandTile tile, EnumCropType crop)
  {
    float precip = tile.getData(CURRENT_ANNUAL_PRECIPITATION);
    float tAvg = tile.getData(CURRENT_ANNUAL_MEAN_TEMPERATURE);
    float tMax = tile.getData(CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH);
    float tMin = tile.getData(CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH);
    int ideal = 0;
    int accept = 0;
    switch (crop)
    {
      case WHEAT:
        if (precip > WHEAT_RAIN_LOW && precip < WHEAT_RAIN_HIGH)
        {
          ideal++;
        }
        else if (precip > WHEAT_RAIN_LOW - ((WHEAT_RAIN_HIGH - WHEAT_RAIN_LOW) * 0.3) &&
                 precip < WHEAT_RAIN_HIGH + ((WHEAT_RAIN_HIGH - WHEAT_RAIN_LOW) * 0.3))
        {
          accept++;
        }
        if (tAvg > WHEAT_AVG_LOW && tAvg < WHEAT_AVG_HIGH)
        {
          ideal++;
        }
        else if (precip > WHEAT_AVG_LOW - ((WHEAT_AVG_HIGH - WHEAT_AVG_LOW) * 0.3) &&
                 precip < WHEAT_AVG_HIGH + ((WHEAT_AVG_HIGH - WHEAT_AVG_LOW) * 0.3))
        {
          accept++;
        }
        if (tMax < WHEAT_MAX_HIGH)
        {
          ideal++;
        }
        else if (precip < WHEAT_MAX_HIGH + ((WHEAT_MAX_HIGH - WHEAT_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (tMin > WHEAT_MAX_LOW)
        {
          ideal++;
        }
        else if (precip > WHEAT_MAX_LOW - ((WHEAT_MAX_HIGH - WHEAT_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (ideal == 4)
        {
          return 0;
        }
        else if (ideal == 3 && accept == 1)
        {
          return 1;
        }
        else
        {
          return -1;
        }

      case SOY:
        if (precip > SOY_RAIN_LOW && precip < SOY_RAIN_HIGH)
        {
          ideal++;
        }
        else if (precip > SOY_RAIN_LOW - ((SOY_RAIN_HIGH - SOY_RAIN_LOW) * 0.3) &&
                 precip < SOY_RAIN_HIGH + ((SOY_RAIN_HIGH - SOY_RAIN_LOW) * 0.3))
        {
          accept++;
        }
        if (tAvg > SOY_AVG_LOW && tAvg < SOY_AVG_HIGH)
        {
          ideal++;
        }
        else if (precip > SOY_AVG_LOW - ((SOY_AVG_HIGH - SOY_AVG_LOW) * 0.3) &&
                 precip < SOY_AVG_HIGH + ((SOY_AVG_HIGH - SOY_AVG_LOW) * 0.3))
        {
          accept++;
        }
        if (tMax < SOY_MAX_HIGH)
        {
          ideal++;
        }
        else if (precip < SOY_MAX_HIGH + ((SOY_MAX_HIGH - SOY_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (tMin > SOY_MAX_LOW)
        {
          ideal++;
        }
        else if (precip > SOY_MAX_LOW - ((SOY_MAX_HIGH - SOY_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (ideal == 4)
        {
          return 0;
        }
        else if (ideal == 3 && accept == 1)
        {
          return 1;
        }
        else
        {
          return -1;
        }

      case CORN:

        if (precip > CORN_RAIN_LOW && precip < CORN_RAIN_HIGH)
        {
          ideal++;
        }
        else if (precip > CORN_RAIN_LOW - ((CORN_RAIN_HIGH - CORN_RAIN_LOW) * 0.3) &&
                 precip < CORN_RAIN_HIGH + ((CORN_RAIN_HIGH - CORN_RAIN_LOW) * 0.3))
        {
          accept++;
        }
        if (tAvg > CORN_AVG_LOW && tAvg < CORN_AVG_HIGH)
        {
          ideal++;
        }
        else if (precip > CORN_AVG_LOW - ((CORN_AVG_HIGH - CORN_AVG_LOW) * 0.3) &&
                 precip < CORN_AVG_HIGH + ((CORN_AVG_HIGH - CORN_AVG_LOW) * 0.3))
        {
          accept++;
        }
        if (tMax < CORN_MAX_HIGH)
        {
          ideal++;
        }
        else if (precip < CORN_MAX_HIGH + ((CORN_MAX_HIGH - CORN_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (tMin > CORN_MAX_LOW)
        {
          ideal++;
        }
        else if (precip > CORN_MAX_LOW - ((CORN_MAX_HIGH - CORN_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (ideal == 4)
        {
          return 0;
        }
        else if (ideal == 3 && accept == 1)
        {
          return 1;
        }
        else
        {
          return -1;
        }

      case RICE:

        if (precip > RICE_RAIN_LOW && precip < RICE_RAIN_HIGH)
        {
          ideal++;
        }
        else if (precip > RICE_RAIN_LOW - ((RICE_RAIN_HIGH - RICE_RAIN_LOW) * 0.3) &&
                 precip < RICE_RAIN_HIGH + ((RICE_RAIN_HIGH - RICE_RAIN_LOW) * 0.3))
        {
          accept++;
        }
        if (tAvg > RICE_AVG_LOW && tAvg < RICE_AVG_HIGH)
        {
          ideal++;
        }
        else if (precip > RICE_AVG_LOW - ((RICE_AVG_HIGH - RICE_AVG_LOW) * 0.3) &&
                 precip < RICE_AVG_HIGH + ((RICE_AVG_HIGH - RICE_AVG_LOW) * 0.3))
        {
          accept++;
        }
        if (tMax < RICE_MAX_HIGH)
        {
          ideal++;
        }
        else if (precip < RICE_MAX_HIGH + ((RICE_MAX_HIGH - RICE_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (tMin > RICE_MAX_LOW)
        {
          ideal++;
        }
        else if (precip > RICE_MAX_LOW - ((RICE_MAX_HIGH - RICE_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (ideal == 4)
        {
          return 0;
        }
        else if (ideal == 3 && accept == 1)
        {
          return 1;
        }
        else
        {
          return -1;
        }

      default:
        if (precip > OTHER_RAIN_LOW && precip < OTHER_RAIN_HIGH)
        {
          ideal++;
        }
        else if (precip > OTHER_RAIN_LOW - ((OTHER_RAIN_HIGH - OTHER_RAIN_LOW) * 0.3) &&
                 precip < OTHER_RAIN_HIGH + ((OTHER_RAIN_HIGH - OTHER_RAIN_LOW) * 0.3))
        {
          accept++;
        }
        if (tAvg > OTHER_AVG_LOW && tAvg < OTHER_AVG_HIGH)
        {
          ideal++;
        }
        else if (precip > OTHER_AVG_LOW - ((OTHER_AVG_HIGH - OTHER_AVG_LOW) * 0.3) &&
                 precip < OTHER_AVG_HIGH + ((OTHER_AVG_HIGH - OTHER_AVG_LOW) * 0.3))
        {
          accept++;
        }
        if (tMax < OTHER_MAX_HIGH)
        {
          ideal++;
        }
        else if (precip < OTHER_MAX_HIGH + ((OTHER_MAX_HIGH - OTHER_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (tMin > OTHER_MAX_LOW)
        {
          ideal++;
        }
        else if (precip > OTHER_MAX_LOW - ((OTHER_MAX_HIGH - OTHER_MAX_LOW) * 0.3))
        {
          accept++;
        }
        if (ideal == 4)
        {
          return 0;
        }
        else if (ideal == 3 && accept == 1)
        {
          return 1;
        }
        else
        {
          return -1;
        }
    }


  }


  /**
   Sets up the crop linked list

   @param arableTotal
   Total cells for arable land.
   @param cellsNeeded
   Land needed to be filled with crops.
   @param cropPriority
   The linked list of crops and the number of cells to plant each in.
   */
  private void setPriority(double arableTotal, int cellsNeeded, LinkedList<CropNum> cropPriority)
  {
    int temp = (int) ((data.getCornLand(true) / arableTotal) * cellsNeeded);
    cropPriority.add(new CropNum(temp, CORN));
    data.setCornLand(temp * 100);
    temp = (int) ((data.getWheatLand(true) / arableTotal) * cellsNeeded);
    addCrop(temp, WHEAT, cropPriority);
    data.setWheatLand(temp * 100);
    temp = (int) ((data.getRiceLand(true) / arableTotal) * cellsNeeded);
    addCrop(temp, RICE, cropPriority);
    data.setRiceLand(temp * 100);
    temp = (int) ((data.getSoyLand(true) / arableTotal) * cellsNeeded);
    addCrop(temp, SOY, cropPriority);
    data.setSoyLand(temp * 100);
    temp = (int) ((data.getOtherLand(true) / arableTotal) * cellsNeeded);
    addCrop(temp, OTHER_CROPS, cropPriority);
    data.setOtherLand(temp * 100);
  }


  /**
   Adds a crop to the linked list based on how many to be planted.

   @param number
   How many to be planted.
   @param crop
   The crop.
   @param cropPriority
   The linked list of crops.
   */
  private void addCrop(int number, EnumCropType crop, LinkedList<CropNum> cropPriority)
  {
    boolean inserted = false;
    if (number > 0)
    {
      CropNum newCropNum = new CropNum(number, crop);
      for (int i = 0; i < cropPriority.size(); i++)
      {
        if (cropPriority.get(i).priority > newCropNum.priority)
        {
          cropPriority.add(i, newCropNum);
          inserted = true;
          break;
        }
      }
      if (!inserted)
      {
        cropPriority.add(newCropNum);
      }
    }
  }


  /**
   Sorts a crop back into the linked list,
   prioritizing least amount remaining.

   @param current
   The current crop just checked/planted.
   @param cropPriority
   The linked list of crops.
   */
  private void resortCrop(CropNum current, LinkedList<CropNum> cropPriority)
  {
    boolean inserted = false;
    cropPriority.remove(current);
    current.num = current.num - 1;
    for (int i = 0; i < cropPriority.size(); i++)
    {
      if (cropPriority.get(i).priority > current.priority)
      {
        cropPriority.add(i, current);
        inserted = true;
        break;
      }
    }
    if (!inserted)
    {
      cropPriority.add(current);
    }
  }


  /**
   Sets the ideals for the Other crop
   based on this region's averages.
   */
  private void setOtherIdeals()
  {
    float temp = 0;
    for (LandTile tile : landTiles)
    {
      temp = temp + tile.getData(CURRENT_ANNUAL_MEAN_TEMPERATURE);
    }
    temp = temp / landTiles.size();
    OTHER_AVG_HIGH = (int) (temp + 5);
    OTHER_AVG_LOW = (int) (temp - 5);
    for (LandTile tile : landTiles)
    {
      temp = temp + tile.getData(CURRENT_ANNUAL_PRECIPITATION);
    }
    temp = temp / landTiles.size();
    OTHER_RAIN_HIGH = (int) (temp + 20);
    OTHER_RAIN_LOW = (int) (temp - 20);
    for (LandTile tile : landTiles)
    {
      temp = temp + tile.getData(CURRENT_MAX_TEMPERATURE_OF_WARMEST_MONTH);
    }
    temp = temp / landTiles.size();
    OTHER_MAX_HIGH = (int) temp;
    for (LandTile cell : landTiles)
    {
      temp = temp + cell.getData(CURRENT_MIN_TEMPERATURE_OF_COLDEST_MONTH);
    }
    temp = temp / landTiles.size();
    OTHER_MAX_LOW = (int) temp;
  }


  /**
   Set the region's data.

   @param data
   Country data.
   */
  public void setCountryData(CountryData data)
  {
    this.data = data;
  }


  /**
   Get the region's data.

   @return Country data.
   */
  public CountryData getCountryData()
  {
    return data;
  }


  public void setOfficialCountry()
  {
    officialCountry = true;
  }





  public boolean getOfficialCountry()
  {
    return officialCountry;
  }
}
