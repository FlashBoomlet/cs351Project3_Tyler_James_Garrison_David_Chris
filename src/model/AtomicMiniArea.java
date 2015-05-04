package model;


import gui.displayconverters.MapConverter;

import java.awt.geom.Path2D;
import java.awt.geom.Rectangle2D;
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
  private Path2D perim = new Path2D.Double();
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
    perim.moveTo(perimeter.get(0).getLon(), perimeter.get(0).getLat());
    for (int i = 1; i < perimeter.size(); i++)
    {
      MapPoint mp = perimeter.get(i);
      perim.lineTo(mp.getLon(), mp.getLat());
    }
    perim.closePath();
    this.perimeter = perimeter;
  }


  public String toString()
  {
    return "AtomicMiniArea{" +
        "name='" + name + '\'' +
        '}';
  }

  /**
   * More efficient cell retrieval, uses bounding box of area to get
   * relevant cells.
   * @param worldArray that you would like to update
   * @param cells that you wish to update
   */
  public void setLandCells (WorldArray worldArray, HashSet<WorldCell> cells, MapConverter mapConverter)
  {
    Path2D.Float tempPerimeter = new Path2D.Float();
    MapPoint current = perimeter.get(0);
    tempPerimeter.moveTo(current.getLon(), current.getLat());
    for (int i = 1; i < perimeter.size(); i++)
    {
      current = perimeter.get(i);
      tempPerimeter.lineTo(current.getLon(), current.getLat());
      tempPerimeter.moveTo(current.getLon(), current.getLat());
    }
    tempPerimeter.lineTo(perimeter.get(0).getLon(), perimeter.get(0).getLat());
    tempPerimeter.closePath();
    Rectangle2D bound = tempPerimeter.getBounds2D();
    int [] start = worldArray.getNumber(bound.getX(), bound.getY());
    int xStart = start[0];
    int yStart = start[1];
    int [] end = worldArray.getNumber(bound.getX() + bound.getWidth(), bound.getY() + bound.getHeight());
    int xEnd = end[0];
    int yEnd = end[1];
    WorldCell currentCell;
    for (int i = xStart; i < xEnd; i++)
    {
      for (int j = yStart; j < yEnd; j++)
      {
        currentCell = worldArray.get(i, j);
        if (!currentCell.hasCountry && tempPerimeter.contains(currentCell.getLon(), currentCell.getLat()));
        {
          if( cells != null )
          {
            cells.add(currentCell);
            currentCell.setToArea();
          }
        }
      }
    }
  }


  @Override
  public boolean containsMapPoint(MapPoint mapPoint)
  {
    return perim.contains(mapPoint.getLon(), mapPoint.getLat());
  }
}
