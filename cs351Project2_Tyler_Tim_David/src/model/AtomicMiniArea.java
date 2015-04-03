package model;


import java.awt.geom.Path2D;
import java.util.List;
import java.util.HashSet;


/**
 * Represent a homogeneous area. Defined by a perimeter and various planting
 * attributes. The class acts as a kind of container for the parsed XML data.
 *
 * @author winston riley
 */
public class AtomicMiniArea implements MiniArea
{
  private List<MapPoint> perimeter;
  private String name;

  @Override
  public String getName()
  {
    return name;
  }

  @Override
  public void setName(String name)
  {
    this.name = name;
  }


  @Override
  public List<MapPoint> getPerimeter()
  {
    return perimeter;
  }


  @Override
  public void setPerimeter(List<MapPoint> perimeter)
  {
    this.perimeter = perimeter;
  }


  public String toString()
  {
    return "AtomicMiniArea{" +
        "name='" + name + '\'' +
        '}';
  }

  /**
   * A VERY inefficient way of finding all the cells within
   * in a mini area to be stored by the main area.
   * @param worldArray
   */
  public void setLandCells (WorldArray worldArray, HashSet <WorldCell> cells)
  {
    Path2D.Float tempPerimeter = new Path2D.Float();
    MapPoint current = perimeter.get(0);
    tempPerimeter.moveTo(current.getLon(), current.getLat());
    for (int i = 1; i < perimeter.size(); i++)
    {
      current = perimeter.get(i);
      tempPerimeter.lineTo(current.getLon(), current.getLat());
    }
    tempPerimeter.closePath();
    WorldCell currentCell;
    for (int i = 0; i < worldArray.getXSize(); i++)
    {
      for (int j = 0; j < worldArray.getYSize(); j++)
      {
        currentCell = worldArray.get(i, j);
        if (tempPerimeter.contains(currentCell.getLon(), currentCell.getLat())) ;
        {
          cells.add(currentCell);
        }
      }
    }
  }
}
