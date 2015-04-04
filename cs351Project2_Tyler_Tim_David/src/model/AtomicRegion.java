package model;

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
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
  private HashSet<WorldCell> landCells;
  private HashSet<WorldCell> relevantCells;
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

  /**
   * How to get amount of land for each crop?
   */
  public void setFirstCrops ()
  {
    int counter = 0;
    HashSet <WorldCell> leftovers = new HashSet<>();
    TreeMap<Integer, String> cropPriority = new TreeMap<>();
    double arableTotal = data.getArableOpen() + data.getCornLand() + data.getRiceLand() + data.getOtherLand() + data.getWheatLand() + data.getSoyLand();
    int cellsNeeded = (int) arableTotal/100;
    int temp = (int) ((data.getCornLand()/arableTotal)* cellsNeeded);
    cropPriority.put((Integer) temp, "Corn");
    temp = (int) ((data.getWheatLand()/arableTotal)* cellsNeeded);
    cropPriority.put((Integer) temp, "Wheat");
    temp = (int) ((data.getRiceLand()/arableTotal)* cellsNeeded);
    cropPriority.put((Integer) temp, "Rice");
    temp = (int) ((data.getSoyLand()/arableTotal)* cellsNeeded);
    cropPriority.put((Integer) temp, "Soy");
    temp = (int) ((data.getOtherLand()/arableTotal)* cellsNeeded);
    cropPriority.put((Integer) temp, "Other");
    /*
    for (String s: attributes.getAllCrops())
    {
      temp = (int)
      cropPriority.put((Integer) attributes.getCropP(s), s);
    }*/
    for (WorldCell cell: landCells)
    {
      for (Map.Entry<Integer, String> entry: cropPriority.entrySet())
      {
        if (checkIdeal(cell, entry.getValue()) == 0)
        {
          cell.updateFirstYear(entry.getValue(), (float) 1);
          if (entry.getKey() - 1 == 0)
          {
            cropPriority.remove(entry); //Pretty sure this is wrong
          }
          else
          {
            cropPriority.put(entry.getKey() - 1, entry.getValue());
          }
          break;
        }
        else
        {
          leftovers.add(cell);
        }
      }
    }
    for (WorldCell cell: leftovers)
    {

    }
  }

  private int checkIdeal (WorldCell cell, String crop)
  {
    return 0;
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
}
