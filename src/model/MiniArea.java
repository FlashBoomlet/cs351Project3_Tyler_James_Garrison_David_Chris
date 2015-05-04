package model;

import gui.displayconverters.MapConverter;

import java.util.List;
import java.util.HashSet;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 */
public interface MiniArea
{
  String getName();

  void setName(String name);

  List<MapPoint> getPerimeter();

  void setPerimeter(List<MapPoint> perimeter);

  void setLandCells(WorldArray worldArray, HashSet<WorldCell> cells, MapConverter mapConverter);


  boolean containsMapPoint(MapPoint mapPoint);
}
