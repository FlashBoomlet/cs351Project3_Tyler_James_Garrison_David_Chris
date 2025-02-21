package model;

import gui.displayconverters.MapConverter;

import java.util.Collection;
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

  void setFlag(String flagLocation);

  List<MiniArea> getPerimeter();

  void setPerimeter(List<MiniArea> perimeter);

  void addLandTiles(Collection<LandTile> tiles);

  void setCountryData(CountryData data);

  CountryData getCountryData();

  void setOfficialCountry();

  boolean getOfficialCountry();

  void setFirstCrops();

  void setCrops();

  boolean containsMapPoint(MapPoint mapPoint);


  void addLandTile(LandTile tile);


  Collection<LandTile> getArableCells();


  Collection<LandTile> getLandTiles();
}
