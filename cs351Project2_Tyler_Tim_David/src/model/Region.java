package model;

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

  public RegionAttributes getAttributes();

  public void setAttributes(RegionAttributes attributes);

  public void setCountryData(CountryData data);

  public CountryData getCountryData();
}
