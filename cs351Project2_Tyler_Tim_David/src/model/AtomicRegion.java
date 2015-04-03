package model;

import java.util.List;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class AtomicRegion implements Region
{
  private List<MiniArea> perimeter;
  private String name;
  private RegionAttributes attributes;
  private String flagLocation;
  private WorldCell[] landCells;

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

  }
}
