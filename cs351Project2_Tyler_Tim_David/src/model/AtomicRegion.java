package model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;
import java.awt.geom.Path2D;
import model.common.CropIdeals;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 * Restructured by:
 * @author Tyler Lynch <lyncht@unm.edu>
 */
public class AtomicRegion implements Region, CropIdeals
{
  private List<MiniArea> perimeter;
  private String name;
  private String flagLocation;
  private HashSet<WorldCell> landCells = new HashSet<>();
  private HashSet<WorldCell> relevantCells = new HashSet<>();
  private CountryData data = null;
  private boolean officialCountry = false;
  private int OTHER_AVG_HIGH;
  private int OTHER_AVG_LOW;
  private int OTHER_MAX_HIGH;
  private int OTHER_MAX_LOW;
  private int OTHER_RAIN_HIGH;
  private int OTHER_RAIN_LOW;


  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setFlag(String flagLocation)
  {
    this.flagLocation = flagLocation;
  }

  @Override
  public String getFlag()
  {
    if( !flagLocation.contains("\n") ) return flagLocation;
    else return null;
  }
  @Override
  public void setName(String name)
  {
    this.name = name;
  }


  @Override
  public List<MiniArea> getPerimeter()
  {
    return perimeter;
  }


  @Override
  public void setPerimeter(List<MiniArea> perimeter)
  {
    this.perimeter = perimeter;
  }


  public String toString()
  {
    return "AtomicRegion{" +
        "name='" + name + '\'' +
        '}';
  }

  public void setLandCells (WorldArray worldArray)
  {
    for (MiniArea area: perimeter)
    {
      area.setLandCells(worldArray, landCells);
    }
  }
@Override
  public HashSet<WorldCell> getAllCells ()
  {
    return landCells;
  }
@Override
  public HashSet<WorldCell> getArableCells ()
  {
    return relevantCells;
  }

  public void setCrops()
  {
    if (data == null)
    {
      return;
    }
    HashSet <WorldCell> leftovers = new HashSet<>();
    HashSet <WorldCell> leftovers2 = new HashSet<>();
    HashSet <WorldCell> leftovers3 = new HashSet<>();
    LinkedList<CropNum> cropPriority = new LinkedList<>();
    //double arableTotal = data.getArableOpen(true) + data.getCornLand(true) + data.getRiceLand(true) + data.getOtherLand(true) + data.getWheatLand(true) + data.getSoyLand(true);
    double arableTotal = data.getArableOpen(true);
    arableTotal = arableTotal + data.getCornLand(true);
    arableTotal = arableTotal + data.getRiceLand(true);
    arableTotal = arableTotal + data.getOtherLand(true);
    arableTotal = arableTotal + data.getWheatLand(true);
    arableTotal = arableTotal + data.getSoyLand(true);
    int cellsNeeded = (int) arableTotal/100;
    setPriority(arableTotal, cellsNeeded, cropPriority);
    //Ideal placement
    for (WorldCell cell: relevantCells)
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
    for (WorldCell cell: leftovers)
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
    for (WorldCell cell: leftovers2)
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

  private void clearCells (HashSet<WorldCell> leftovers)
  {
    for (WorldCell cell: leftovers)
    {
      cell.update("None", (float) 1);
    }
  }

  /**
   * Algorithm would be best if crop priority was
   * based on crop pickiness.
   */
  public void setFirstCrops()
  {
    if (data == null)
    {
      return;
    }
    setOtherIdeals();
    /*
    for (String s: attributes.getAllCrops())
    {
      temp = (int)
      cropPriority.put((Integer) attributes.getCropP(s), s);
    }*/
    HashSet <WorldCell> leftovers = new HashSet<>();
    HashSet <WorldCell> leftovers2 = new HashSet<>();
    HashSet <WorldCell> leftovers3 = new HashSet<>();
    LinkedList<CropNum> cropPriority = new LinkedList<>();
    double arableTotal = 0;
    //double arableTotal = data.getArableOpen(true) + data.getCornLand(true) + data.getRiceLand(true) + data.getOtherLand(true) + data.getWheatLand(true) + data.getSoyLand(true);
    arableTotal = data.getArableOpen(true);
    arableTotal = arableTotal + data.getCornLand(true);
    arableTotal = arableTotal + data.getRiceLand(true);
    arableTotal = arableTotal + data.getOtherLand(true);
    arableTotal = arableTotal + data.getWheatLand(true);
    arableTotal = arableTotal + data.getSoyLand(true);
    int cellsNeeded = (int) arableTotal/100;
    setPriority(arableTotal, cellsNeeded, cropPriority);
    //Ideal placement
    for (WorldCell cell: landCells)
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
            relevantCells.add(cell);
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
      finalizeCells(leftovers, cellsNeeded);
      return;
    }
    //Acceptable placement
    for (WorldCell cell: leftovers)
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
            relevantCells.add(cell);
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
      finalizeCells(leftovers2, cellsNeeded);
      return;
    }
    //Poor placement
    CropNum current;
    for (WorldCell cell: leftovers2)
    {
      if (!cropPriority.isEmpty())
      {
        current = cropPriority.peek();
        cell.updateFirstYear(current.crop, (float) 0.25);
        if (current.num - 1 == 0)
        {
          cropPriority.remove(current);
        }
        else
        {
          resortCrop(current, cropPriority);
        }
        relevantCells.add(cell);
      }
      else
      {
        leftovers3.add(cell);
      }
    }
    finalizeCells(leftovers3, cellsNeeded);
  }

  private void finalizeCells (HashSet<WorldCell> leftovers, int cellsNeeded)
  {
    int length = cellsNeeded - relevantCells.size();
    int counter = 0;
    for (WorldCell cell: leftovers)
    {
      relevantCells.add(cell);
      counter++;
      if (counter == length)
      {
        break;
      }
    }
  }

  private int checkIdeal (WorldCell cell, String crop)
  {
    float precip = cell.getPrecip();
    float tAvg = cell.getTempAvg();
    float tMax = cell.getAnnualHigh();
    float tMin = cell.getAnnualLow();
    int ideal = 0;
    int accept = 0;
    if (crop.equals("Wheat"))
    {
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
    }
    else if (crop.equals("Soy"))
    {
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
    }
    else if (crop.equals("Corn"))
    {
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
    }
    else if (crop.equals("Rice"))
    {
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
    }
    else if (crop.equals("Other"))
    {
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
    return -1;
  }

  private void setPriority (double arableTotal, int cellsNeeded, LinkedList<CropNum> cropPriority)
  {
    int temp = (int) ((data.getCornLand(true)/arableTotal)* cellsNeeded);
    cropPriority.add(new CropNum(temp, "Corn"));
    temp = (int) ((data.getWheatLand(true)/arableTotal)* cellsNeeded);
    addCrop(temp, "Wheat", cropPriority);
    temp = (int) ((data.getRiceLand(true)/arableTotal)* cellsNeeded);
    addCrop(temp, "Rice", cropPriority);
    temp = (int) ((data.getSoyLand(true)/arableTotal)* cellsNeeded);
    addCrop(temp, "Soy", cropPriority);
    temp = (int) ((data.getOtherLand(true)/arableTotal)* cellsNeeded);
    addCrop(temp, "Other", cropPriority);
  }

  private void addCrop (int number, String crop, LinkedList<CropNum> cropPriority)
  {
    boolean inserted = false;
    if (number > 0)
    {
      for (int i = 0; i < cropPriority.size(); i++)
      {
        if (cropPriority.get(i).num > number)
        {
          cropPriority.add(i, new CropNum(number, crop));
          inserted = true;
          break;
        }
      }
      if (!inserted)
      {
        cropPriority.add(new CropNum(number, crop));
      }
    }
  }

  private void resortCrop (CropNum current, LinkedList<CropNum> cropPriority)
  {
    boolean inserted = false;
    cropPriority.remove(current);
    current.num = current.num - 1;
    for (int i = 0; i < cropPriority.size(); i++)
    {
      if (cropPriority.get(i).num > current.num)
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

  private void setOtherIdeals ()
  {
    float temp = 0;
    for (WorldCell cell: landCells)
    {
      temp = temp + cell.getTempAvg();
    }
    temp = temp / landCells.size();
    OTHER_AVG_HIGH = (int) (temp + 5);
    OTHER_AVG_LOW = (int) (temp - 5);
    for (WorldCell cell: landCells)
    {
      temp = temp + cell.getPrecip();
    }
    temp = temp / landCells.size();
    OTHER_RAIN_HIGH = (int) (temp + 20);
    OTHER_RAIN_LOW = (int) (temp - 20);
    for (WorldCell cell: landCells)
    {
      temp = temp + cell.getAnnualHigh();
    }
    temp = temp / landCells.size();
    OTHER_MAX_HIGH = (int) temp;
    for (WorldCell cell: landCells)
    {
      temp = temp + cell.getAnnualLow();
    }
    temp = temp / landCells.size();
    OTHER_MAX_LOW = (int) temp;
  }

  public void setCountryData(CountryData data)
  {
    //System.out.println(name);
    this.data = data;
  }

  public CountryData getCountryData()
  {
    return data;
  }

  public void setOfficialCountry()
  {
    officialCountry = true;
  }


  private class CropNum
  {
    protected int num = 0;
    protected String crop;

    public CropNum(int num, String crop)
    {
      this.num = num;
      this.crop = crop;
    }
  }

  public boolean getOfficialCountry()
  {
    return officialCountry;
  }
}
