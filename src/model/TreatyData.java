package model;

/**
 * Data structure for Treaties
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 5.5.2015
 */
public class TreatyData
{
  int id;
  String treaty;
  String tag;
  String description;
  double likelyhood;
  String pro;
  String con;
  double gmo;
  double organic;
  double conventional;
  double corn;
  double soy;
  double rice;
  double wheat;
  double other;
  double level;
  private String sponsor = "SPONSOR";

  /**
   *
   */
  public TreatyData() { /* Do Nothing */ }

  public int getId()
  {
    return id;
  }

  public void setId(int id)
  {
    this.id = id;
  }

  public String getTreaty()
  {
    return treaty;
  }

  public void setTreaty(String treaty)
  {
    this.treaty = treaty;
  }

  public String getTag()
  {
    return tag;
  }

  public void setTag(String tag)
  {
    this.tag = tag;
  }

  public String getDescription()
  {
    return description;
  }

  public void setDescription(String description)
  {
    this.description = description;
  }

  public double getLikelyhood()
  {
    return likelyhood;
  }

  public void setLikelyhood(double likelyhood)
  {
    this.likelyhood = likelyhood;
  }

  public String getPro()
  {
    return pro;
  }

  public void setPro(String pro)
  {
    this.pro = pro;
  }

  public String getCon()
  {
    return con;
  }

  public void setCon(String con)
  {
    this.con = con;
  }

  public double getGmo()
  {
    return gmo;
  }

  public void setGmo(double gmo)
  {
    this.gmo = gmo;
  }

  public double getOrganic()
  {
    return organic;
  }

  public void setOrganic(double organic)
  {
    this.organic = organic;
  }

  public double getConventional()
  {
    return conventional;
  }

  public void setConventional(double conventional)
  {
    this.conventional = conventional;
  }

  public double getCorn()
  {
    return corn;
  }

  public void setCorn(double corn)
  {
    this.corn = corn;
  }

  public double getSoy()
  {
    return soy;
  }

  public void setSoy(double soy)
  {
    this.soy = soy;
  }

  public double getRice()
  {
    return rice;
  }

  public void setRice(double rice)
  {
    this.rice = rice;
  }

  public double getWheat()
  {
    return wheat;
  }

  public void setWheat(double wheat)
  {
    this.wheat = wheat;
  }

  public double getOther()
  {
    return other;
  }

  public void setOther(double other)
  {
    this.other = other;
  }

  public double getLevel()
  {
    return level;
  }

  public void setLevel(double level)
  {
    this.level = level;
  }

  public String getSponsor() { return sponsor; }

  public void setSponsor(String sponsor) { this.sponsor = sponsor; }

}
