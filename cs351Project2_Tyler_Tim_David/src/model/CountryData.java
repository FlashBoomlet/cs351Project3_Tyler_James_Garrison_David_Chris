package model;

/**
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.2.2015
 */
public class CountryData
{
  private double population;
  private double averageAge;
  private double birthRate;
  private double mortality;
  private double migration;
  private double undernourish;

  private double cornProduction;
  private double cornExports;
  private double cornImports;
  private double cornLand;
  private double wheatProduction;
  private double wheatExports;
  private double wheatImports;
  private double wheatLand;
  private double riceProduction;
  private double riceExports;
  private double riceImports;
  private double riceLand;
  private double soyProduction;
  private double soyExports;
  private double soyImports;
  private double soyLand;
  private double otherProduction;
  private double otherExports;
  private double otherImports;
  private double otherLand;
  private double landArea;
  private double arableOpen;
  private double organic;
  private double conventional;
  private double gmo;

  //Zero Order Approximation
  private double countryConsumption;
  private double perCapitaConsumption;
  private double baseYield;

  /**
   * Empty constructor
   */
  public CountryData(){}


  /*
   *************************************************************
   *
   * GETTERS
   *
   *************************************************************
   */


  /**
   * get the population
   * @return population population
   */
  public double getPopulation()
  {
    return population;
  }
  /**
   * get the averageAge
   * @return averageAge averageAge
   */
  public double getAverageAge()
  {
    return averageAge;
  }
  /**
   * get the birthRate
   * @return birthRate birthRate
   */
  public double getBirthRate()
  {
    return birthRate;
  }
  /**
   * get the mortality
   * @return mortality mortality
   */
  public double getMortality()
  {
    return mortality;
  }
  /**
   * get the migration
   * @return migration migration
   */
  public double getMigration()
  {
    return migration;
  }
  /**
   * get the undernourish
   * @return undernourish undernourish
   */
  public double getUndernourish()
  {
    return undernourish;
  }
  /**
   * get the cornProduction
   * @return cornProduction cornProduction
   */
  public double getCornProduction()
  {
    return cornProduction;
  }
  /**
   * get the cornExports
   * @return cornExports cornExports
   */
  public double getCornExports()
  {
    return cornExports;
  }
  /**
   * get the cornImports
   * @return cornImports cornImports
   */
  public double getCornImports()
  {
    return cornImports;
  }
  /**
   * get the cornLand
   * @return cornLand cornLand
   */
  public double getCornLand()
  {
    return cornLand;
  }
  /**
   * get the wheatProduction
   * @return wheatProduction wheatProduction
   */
  public double getWheatProduction()
  {
    return wheatProduction;
  }
  /**
   * get the wheatExports
   * @return wheatExports wheatExports
   */
  public double getWheatExports()
  {
    return wheatExports;
  }
  /**
   * get the wheatImports
   * @return wheatImports wheatImports
   */
  public double getWheatImports()
  {
    return wheatImports;
  }
  /**
   * get the wheatLand
   * @return wheatLand wheatLand
   */
  public double getWheatLand()
  {
    return wheatLand;
  }
  /**
   * get the riceProduction
   * @return riceProduction riceProduction
   */
  public double getRiceProduction()
  {
    return riceProduction;
  }
  /**
   * get the riceExports
   * @return riceExports riceExports
   */
  public double getRiceExports()
  {
    return riceExports;
  }
  /**
   * get the riceImports
   * @return riceImports riceImports
   */
  public double getRiceImports()
  {
    return riceImports;
  }
  /**
   * get the riceLand
   * @return riceLand riceLand
   */
  public double getRiceLand()
  {
    return riceLand;
  }
  /**
   * get the soyProduction
   * @return soyProduction soyProduction
   */
  public double getSoyProduction()
  {
    return soyProduction;
  }
  /**
   * get the soyExports
   * @return soyExports soyExports
   */
  public double getSoyExports()
  {
    return soyExports;
  }
  /**
   * get the soyImports
   * @return soyImports soyImports
   */
  public double getSoyImports()
  {
    return soyImports;
  }
  /**
   * get the soyLand
   * @return soyLand soyLand
   */
  public double getSoyLand()
  {
    return soyLand;
  }
  /**
   * get the otherProduction
   * @return otherProduction otherProduction
   */
  public double getOtherProduction()
  {
    return otherProduction;
  }
  /**
   * get the otherExports
   * @return otherExports otherExports
   */
  public double getOtherExports()
  {
    return otherExports;
  }
  /**
   * get the otherImports
   * @return otherImports otherImports
   */
  public double getOtherImports()
  {
    return otherImports;
  }
  /**
   * get the otherLand
   * @return otherLand otherLand
   */
  public double getOtherLand()
  {
    return otherLand;
  }
  /**
   * get the landArea
   * @return landArea landArea
   */
  public double getLandArea()
  {
    return landArea;
  }
  /**
   * get the arableOpen
   * @return arableOpen arableOpen
   */
  public double getArableOpen()
  {
    return arableOpen;
  }
  /**
   * get the organic
   * @return organic organic
   */
  public double getOrganic()
  {
    return organic;
  }
  /**
   * get the conventional
   * @return conventional conventional
   */
  public double getConventional()
  {
    return conventional;
  }
  /**
   * get the gmo
   * @return gmo gmo
   */
  public double getGmo()
  {
    return gmo;
  }


  /*
   *************************************************************
   *
   * ALL OF THE SETTERS
   *
   *************************************************************
   */


  /**
   * set the population
   * @param population population
   */
  public void setPopulation(double population)
  {
    this.population = population;
  }
  /**
   * set the averageAge
   * @param averageAge averageAge
   */
  public void setAverageAge(double averageAge)
  {
    this.averageAge = averageAge;
  }
  /**
   * set the birthRate
   * @param birthRate birthRate
   */
  public void setBirthRate(double birthRate)
  {
    this.birthRate = birthRate;
  }
  /**
   * set the mortality
   * @param mortality mortality
   */
  public void setMortality(double mortality)
  {
    this.mortality = mortality;
  }
  /**
   * set the migration
   * @param migration migration
   */
  public void setMigration(double migration)
  {
    this.migration = migration;
  }
  /**
   * set the undernourish
   * @param undernourish undernourish
   */
  public void setUndernourish(double undernourish)
  {
    this.undernourish = undernourish;
  }
  /**
   * set the cornProduction
   * @param cornProduction cornProduction
   */
  public void setCornProduction(double cornProduction)
  {
    this.cornProduction = cornProduction;
  }
  /**
   * set the cornExports
   * @param cornExports cornExports
   */
  public void setCornExports(double cornExports)
  {
    this.cornExports = cornExports;
  }
  /**
   * set the cornImports
   * @param cornImports cornImports
   */
  public void setCornImports(double cornImports)
  {
    this.cornImports = cornImports;
  }
  /**
   * set the cornLand
   * @param cornLand cornLand
   */
  public void setCornLand(double cornLand)
  {
    this.cornLand = cornLand;
  }
  /**
   * set the wheatProduction
   * @param wheatProduction wheatProduction
   */
  public void setWheatProduction(double wheatProduction)
  {
    this.wheatProduction = wheatProduction;
  }
  /**
   * set the wheatExports
   * @param wheatExports wheatExports
   */
  public void setWheatExports(double wheatExports)
  {
    this.wheatExports = wheatExports;
  }
  /**
   * set the wheatImports
   * @param wheatImports wheatImports
   */
  public void setWheatImports(double wheatImports)
  {
    this.wheatImports = wheatImports;
  }
  /**
   * set the wheatLand
   * @param wheatLand wheatLand
   */
  public void setWheatLand(double wheatLand)
  {
    this.wheatLand = wheatLand;
  }
  /**
   * set the riceProduction
   * @param riceProduction riceProduction
   */
  public void setRiceProduction(double riceProduction)
  {
    this.riceProduction = riceProduction;
  }
  /**
   * set the riceExports
   * @param riceExports riceExports
   */
  public void setRiceExports(double riceExports)
  {
    this.riceExports = riceExports;
  }
  /**
   * set the riceImports
   * @param riceImports riceImports
   */
  public void setRiceImports(double riceImports)
  {
    this.riceImports = riceImports;
  }
  /**
   * set the riceLand
   * @param riceLand riceLand
   */
  public void setRiceLand(double riceLand)
  {
    this.riceLand = riceLand;
  }
  /**
   * set the soyProduction
   * @param soyProduction soyProduction
   */
  public void setSoyProduction(double soyProduction)
  {
    this.soyProduction = soyProduction;
  }
  /**
   * set the soyExports
   * @param soyExports soyExports
   */
  public void setSoyExports(double soyExports)
  {
    this.soyExports = soyExports;
  }
  /**
   * set the soyImports
   * @param soyImports soyImports
   */
  public void setSoyImports(double soyImports)
  {
    this.soyImports = soyImports;
  }
  /**
   * set the soyLand
   * @param soyLand soyLand
   */
  public void setSoyLand(double soyLand)
  {
    this.soyLand = soyLand;
  }
  /**
   * set the otherProduction
   * @param otherProduction otherProduction
   */
  public void setOtherProduction(double otherProduction)
  {
    this.otherProduction = otherProduction;
  }
  /**
   * set the otherExports
   * @param otherExports otherExports
   */
  public void setOtherExports(double otherExports)
  {
    this.otherExports = otherExports;
  }
  /**
   * set the otherImports
   * @param otherImports otherImports
   */
  public void setOtherImports(double otherImports)
  {
    this.otherImports = otherImports;
  }
  /**
   * set the otherLand
   * @param otherLand otherLand
   */
  public void setOtherLand(double otherLand)
  {
    this.otherLand = otherLand;
  }
  /**
   * set the landArea
   * @param landArea landArea
   */
  public void setLandArea(double landArea)
  {
    this.landArea = landArea;
  }
  /**
   * set the arableOpen
   * @param arableOpen arableOpen
   */
  public void setArableOpen(double arableOpen)
  {
    this.arableOpen = arableOpen;
  }
  /**
   * set the organic
   * @param organic organic
   */
  public void setOrganic(double organic)
  {
    this.organic = organic;
  }
  /**
   * set the conventional
   * @param conventional conventional
   */
  public void setConventional(double conventional)
  {
    this.conventional = conventional;
  }
  /**
   * set the gmo
   * @param gmo gmo
   */
  public void setGmo(double gmo)
  {
    this.gmo = gmo;
  }



  /*
   *************************************************************
   *
   * calculateZeroOrder
   *
   *************************************************************
   */


  /**
   * Calculates the Zero-Order Approximations
   */
  public void calculateZeroOrder()
  {
    calculateCountryConsumption();
    calculatePerCapitaConsumption();
    calculateBaseYield();
  }

  private void calculateCountryConsumption()
  {
    // 2014 = produced + import -export
  }

  private void calculatePerCapitaConsumption()
  {
    // Based on the Country consumption
  }

  private void calculateBaseYield()
  {
    // yeah....
  }

  /**
   * Get the Countries Consumption rate
   * @return countryConsumption rate
   */
  public double getCountryConsumption()
  {
    return countryConsumption;
  }

  /**
   * Get the Per Capita Consumption rate
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumption()
  {
    return perCapitaConsumption;
  }

  /**
   * Get the Base Yield rate
   * @return baseYield rate
   */
  public double getBaseYield()
  {
    return baseYield;
  }
}
