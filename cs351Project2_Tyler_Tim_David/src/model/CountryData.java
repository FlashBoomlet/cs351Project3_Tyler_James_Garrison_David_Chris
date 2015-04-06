package model;

import main.SettingsScreen;

/**
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.2.2015
 */
public class CountryData
{
  // General
  private double population;
  private double medianAge;
  private double birthRate;
  private double mortality;
  private double migration;
  private double undernourish;
  // Corn
  private double cornProduction;
  private double cornExports;
  private double cornImports;
  private double cornLand;
  // Wheat
  private double wheatProduction;
  private double wheatExports;
  private double wheatImports;
  private double wheatLand;
  // Rice
  private double riceProduction;
  private double riceExports;
  private double riceImports;
  private double riceLand;
  // Soy
  private double soyProduction;
  private double soyExports;
  private double soyImports;
  private double soyLand;
  // Other
  private double otherProduction;
  private double otherExports;
  private double otherImports;
  private double otherLand;
  // Land information
  private double landArea;
  private double arableOpen;
  private double organic;
  private double conventional;
  private double gmo;

  //Zero Order Approximation
  private double countryConsumption;
  private double perCapitaConsumption;
  private double baseYield;
  private double randomization = 0;

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
   * @param metricUnits   boolean for data type
   * @return population population
   */
  public double getPopulation(boolean metricUnits)
  {
    return population;
  }
  /**
   * get the medianAge
   * @param metricUnits   boolean for data type
   * @return medianAge medianAge
   */
  public double getMedianAge(boolean metricUnits)
  {
    return medianAge;
  }
  /**
   * get the birthRate
   * @param metricUnits   boolean for data type
   * @return birthRate birthRate
   */
  public double getBirthRate(boolean metricUnits)
  {
    return birthRate;
  }
  /**
   * get the mortality
   * @param metricUnits   boolean for data type
   * @return mortality mortality
   */
  public double getMortality(boolean metricUnits)
  {
    return mortality;
  }
  /**
   * get the migration
   * @param metricUnits   boolean for data type
   * @return migration migration
   */
  public double getMigration(boolean metricUnits)
  {
    return migration;
  }
  /**
   * get the undernourish
   * @param metricUnits   boolean for data type
   * @return undernourish undernourish
   */
  public double getUndernourish(boolean metricUnits)
  {
    return undernourish;
  }
  /**
   * get the cornTotal
   * @param metricUnits   boolean for data type
   * @return cornTotal cornProduction
   */
  public double getCornTotal(boolean metricUnits)
  {
    return (cornProduction + cornImports - cornExports);
  }
  /**
   * get the cornLand
   * @param metricUnits   boolean for data type
   * @return cornLand cornLand
   */
  public double getCornLand(boolean metricUnits)
  {
    return cornLand;
  }
  /**
   * get the WheatTotal
   * @param metricUnits   boolean for data type
   * @return WheatTotal WheatTotal
   */
  public double getWheatTotal(boolean metricUnits)
  {
    return (wheatProduction + wheatImports - wheatExports);
  }
  /**
   * get the wheatLand
   * @param metricUnits   boolean for data type
   * @return wheatLand wheatLand
   */
  public double getWheatLand(boolean metricUnits)
  {
    return wheatLand;
  }
  /**
   * get the riceTotal
   * @param metricUnits   boolean for data type
   * @return riceTotal riceProduction
   */
  public double getRiceTotal(boolean metricUnits)
  {
    return (riceProduction + riceImports - riceExports);
  }
  /**
   * get the riceLand
   * @param metricUnits   boolean for data type
   * @return riceLand riceLand
   */
  public double getRiceLand(boolean metricUnits)
  {
    return riceLand;
  }
  /**
   * get the soyTotal
   * @param metricUnits   boolean for data type
   * @return soyTotal soyProduction
   */
  public double getSoyTotal(boolean metricUnits)
  {
    return (soyProduction + soyImports - soyExports);
  }
  /**
   * get the soyLand
   * @param metricUnits   boolean for data type
   * @return soyLand soyLand
   */
  public double getSoyLand(boolean metricUnits)
  {
    return soyLand;
  }
  /**
   * get the otherTotal
   * @param metricUnits   boolean for data type
   * @return otherTotal otherProduction
   */
  public double getOtherTotal(boolean metricUnits)
  {
    return (otherProduction + otherImports - otherExports);
  }
  /**
   * get the CropTotal
   * @param metricUnits   boolean for data type
   * @return CropTotal CropTotal
   */
  public double getCropTotal(boolean metricUnits)
  {
    double rtnVal = (
      getOtherTotal(metricUnits) +
        getSoyTotal(metricUnits) +
        getRiceTotal(metricUnits) +
        getWheatTotal(metricUnits) +
        getCornTotal(metricUnits)
    );
    return rtnVal;
  }
  /**
   * get the otherLand
   * @param metricUnits   boolean for data type
   * @return otherLand otherLand
   */
  public double getOtherLand(boolean metricUnits)
  {
    return otherLand;
  }
  /**
   * get the landArea
   * @param metricUnits   boolean for data type
   * @return landArea landArea
   */
  public double getLandArea(boolean metricUnits)
  {
    return landArea;
  }
  /**
   * get the arableOpen
   * @param metricUnits   boolean for data type
   * @return arableOpen arableOpen
   */
  public double getArableOpen(boolean metricUnits)
  {
    return arableOpen;
  }
  /**
   * get the organic
   * @param metricUnits   boolean for data type
   * @return percent of organic
   */
  public double getOrganic(boolean metricUnits)
  {
    return organic;
  }
  /**
   * get the conventional
   * @param metricUnits   boolean for data type
   * @return percent of conventional
   */
  public double getConventional(boolean metricUnits)
  {
    return conventional;
  }
  /**
   * get the gmo
   * @param metricUnits   boolean for data type
   * @return percent of gmo
   */
  public double getGmo(boolean metricUnits)
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
   * set the medianAge
   * @param medianAge medianAge
   */
  public void setMedianAge(double medianAge)
  {
    this.medianAge = medianAge;
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

  public void randomizePercent()
  {
    randomization = 100 * SettingsScreen.getRandomizeBounds();
  }

  /**
   * Get the Countries Consumption rate
   * @param metricUnits   boolean for data type
   * @return countryConsumption rate
   */
  public double getCountryConsumption(boolean metricUnits)
  {
    return countryConsumption;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumption(boolean metricUnits)
  {
    return perCapitaConsumption;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYield(boolean metricUnits)
  {
    return baseYield;
  }


   /*
   *************************************************************
   *
   * iterateYear
   *
   *************************************************************
   */


  /**
   * Calculates the the next years data based on data brought in, or rates calculated from it
   */
  public void iterateYear(WorldArray worldArray, Region region)
  {
    //calculateCountryConsumption();
    //calculatePerCapitaConsumption();
    //calculateBaseYield();
    //worldArray.addNoise();
    //region.setCrops();
  }

  /**
   *
   * @param name of the crop you would like
   * @param metricUnits   boolean for data type
   * @return the amount of the crop you wish to get
   */
  public double getCropP(String name, boolean metricUnits)
  {
    return 0;
  }

  /**
   *
   * @param name of the Crop you would like to adjust
   * @return if the cropAdjustment was successful
   */
  public boolean adjustmentByName(String name, boolean increase )
  {
    boolean success = false;
    switch( name )
    {
      case "Corn":
        // has to be greater than 100 square km's
        if( !increase && cornProduction >= 100 )
        {
          setCornProduction(cornProduction - 100);
        }
        else if( increase )
        {
          setCornProduction(cornProduction + 100);
        }
        success = true;
        break;
      case "Wheat":
        // has to be greater than 100 square km's
        if( !increase && wheatProduction >= 100 )
        {
          setWheatProduction(wheatProduction - 100);
        }
        else if( increase )
        {
          setWheatProduction(wheatProduction + 100);
        }
        success = true;
        break;
      case "Soy":
        // has to be greater than 100 square km's
        if( !increase && soyProduction >= 100 )
        {
          setSoyProduction(soyProduction - 100);
        }
        else if( increase )
        {
          setSoyProduction(soyProduction + 100);
        }
        success = true;
        break;
      case "Rice":
        // has to be greater than 100 square km's
        if( !increase && riceProduction >= 100 )
        {
          setRiceProduction(riceProduction - 100);
        }
        else if( increase )
        {
          setRiceProduction(riceProduction + 100);
        }
        success = true;
        break;
      case "Other":
        // has to be greater than 100 square km's
        if( !increase && otherProduction >= 100 )
        {
          setOtherProduction(otherProduction-100);
        }
        else if( increase )
        {
          setOtherProduction(otherProduction+100);
        }
        success = true;
        break;
      case "Organic":
        // has to be greater than 5%
        if( !increase && organic >= 0.05 )
        {
          if( getLandUseTotal() < 0.95 ) findMaxAndIncrease();
          setOrganic(organic - 0.05);
        }
        else if( increase )
        {
          if( getLandUseTotal() >= 0.95 ) findMaxAndDecrease();
          setOrganic(organic + 0.05);
        }
        success = true;
        break;
      case "Conventional":
        // has to be greater than 5%
        if( !increase && conventional >= 0.05 )
        {
          if( getLandUseTotal() < 0.95 ) findMaxAndIncrease();
          setConventional(conventional - 0.05);
        }
        else if( increase )
        {
          if( getLandUseTotal() >= 0.95 ) findMaxAndDecrease();
          setConventional(conventional + 0.05);
        }
        success = true;
        break;
      case "GMO":
        // has to be greater than 5%
        if( !increase && gmo >= 0.05 )
        {
          if( getLandUseTotal() < 0.95 ) findMaxAndIncrease();
          setGmo(gmo - 0.05);
        }
        else if( increase )
        {
          if( getLandUseTotal() >= 0.95 ) findMaxAndDecrease();
          setGmo(gmo+0.05);
        }
        success = true;
        break;
      default:
        // Do nothing
    }
    if( success ){
      //Call to re-arrange data on grid for this region
    }
    return success;
  }

  private void findMaxAndDecrease()
  {
    double max = conventional;
    if( organic >= max && gmo <= max )
    {
      organic -= 0.05;
    }
    else if ( gmo >= max && organic <= max )
    {
      gmo -= 0.05;
    }
    else
    {
      conventional -= 0.05;
    }
  }

  private void findMaxAndIncrease()
  {
    double max = conventional;
    if( organic >= max && gmo <= max )
    {
      organic += 0.05;
    }
    else if ( gmo >= max && organic <= max )
    {
      gmo += 0.05;
    }
    else
    {
      conventional += 0.05;
    }
  }

  private double getLandUseTotal()
  {
    return (conventional + organic + gmo);
  }
}
