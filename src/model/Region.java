package model;

import gui.displayconverters.MapConverter;

import java.util.HashSet;
import java.util.List;

/**
 * Created by winston on 1/23/15.
 * Phase_01
 * CS 351 spring 2015
 *
 *
 * Restructured by:
 * @author Tyler Lynch <lyncht@unm.edu>
 */
public interface Region
{
  String getName();

  void setName(String name);

  String getFlag();

  public HashSet<? extends WorldCell> getAllCells();

  public HashSet<? extends WorldCell> getArableCells();

  void setFlag(String flagLocation);

  List<MiniArea> getPerimeter();

  void setPerimeter(List<MiniArea> perimeter);

  public void setLandCells(WorldArray worldArray, MapConverter converter);

  public void setCountryData(CountryData data);

  public CountryData getCountryData();

  public void setOfficialCountry();

  public boolean getOfficialCountry();

  public void setFirstCrops();

  public void setCrops();


  boolean containsMapPoint(MapPoint mapPoint);


  void addLandTile(LandTile tile);
}
