package model;

import java.util.HashSet;
import java.util.List;
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
  private CountryData data = null;

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

  public void setCountryData(CountryData data)
  {
    this.data = data;
  }

  public CountryData getCountryData()
  {
    return data;
  }
}
