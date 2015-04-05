package model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.LinkedList;
import java.awt.geom.Path2D;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 * Restructured by:
 * @author Tyler Lynch <lyncht@unm.edu>
 */
public class AtomicRegion implements Region
{
  private List<MiniArea> perimeter;
  private String name;
  private RegionAttributes attributes;
  private String flagLocation;
  private HashSet<WorldCell> landCells = new HashSet<>();
  private HashSet<WorldCell> relevantCells = new HashSet<>();
  private CountryData data = null;
  private boolean officialCountry = false;

  @Override
  public RegionAttributes getAttributes()
  {
    return attributes;
  }

  @Override
  public void setAttributes(RegionAttributes attributes)
  {
    this.attributes = attributes;
  }

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
  public HashSet<WorldCell> getRelevantCells(){
    return relevantCells;
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

  public HashSet<WorldCell> getAllCells ()
  {
    return landCells;
  }

  public HashSet<WorldCell> getArableCells ()
  {
    return relevantCells;
  }

  public void setCrops()
  {
    HashSet <WorldCell> leftovers = new HashSet<>();
    HashSet <WorldCell> leftovers2 = new HashSet<>();
    HashSet <WorldCell> leftovers3 = new HashSet<>();
    LinkedList<CropNum> cropPriority = new LinkedList<>();
    double arableTotal = data.getArableOpen(true) + data.getCornLand(true) + data.getRiceLand(true) + data.getOtherLand(true) + data.getWheatLand(true) + data.getSoyLand(true);
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
    double arableTotal = data.getArableOpen(true) + data.getCornLand(true) + data.getRiceLand(true) + data.getOtherLand(true) + data.getWheatLand(true) + data.getSoyLand(true);
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
    return 0;
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

  public void setCountryData(CountryData data)
  {
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
