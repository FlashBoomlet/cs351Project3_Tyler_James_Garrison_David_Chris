package model;

import main.SettingsScreen;

import java.util.HashSet;

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
  private double prevUndernourish;
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
  private double countryConsumptionWheat;
  private double perCapitaConsumptionWheat;
  private double baseYieldWheat;
  private double countryConsumptionCorn;
  private double perCapitaConsumptionCorn;
  private double baseYieldCorn;
  private double countryConsumptionSoy;
  private double perCapitaConsumptionSoy;
  private double baseYieldSoy;
  private double countryConsumptionRice;
  private double perCapitaConsumptionRice;
  private double baseYieldRice;
  private double countryConsumptionOther;
  private double perCapitaConsumptionOther;
  private double baseYieldOther;
  private double randomization = 0;

  //Base Yield calculations
  private double idealLand = 1;
  private double acceptableLand = 0;
  private double acceptableRate = 0.6; //Pretty sure this and poorRate are un-needed, since they will always be .6 and .25.
  private double poorLand = 0;
  private double poorRate = 0.25;

  //Country Center
  private double centerLat;
  private double centerLon;

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
  /**
   * get the centerLat
   * @return centerLat
   */
  public double getShippingLat()
  {
    return centerLat;
  }
  /**
   * get the centerLon
   * @return centerLon
   */
  public double getShippingLon()
  {
    return centerLon;
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
  /**
   * set the centerLat
   * @param centerLat centerLat
   */
  public void setShippingLat(double centerLat)
  {
    this.centerLat = centerLat;
  }
  /**
   * set the centerLon
   * @param centerLon centerLon
   */
  public void setShippingLon(double centerLon)
  {
    this.centerLon = centerLon;
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
  public void calculateZeroOrder(int [] landNums)
  {
    calculateCountryConsumption();
    calculatePerCapitaConsumption();
    calculateBaseYield(landNums);
  }

  private void calculateCountryConsumption()
  {
    //countryConsumption = getCropTotal(true);
    countryConsumptionCorn = cornProduction - cornExports + cornImports;
    countryConsumptionWheat = wheatProduction - wheatExports + wheatImports;
    countryConsumptionSoy = soyProduction - soyExports +soyImports;
    countryConsumptionRice = riceProduction - riceExports + riceImports;
    countryConsumptionOther = otherProduction - otherExports + otherImports;
  }

  private void calculatePerCapitaConsumption()
  {
    // Based on the Country consumption
    perCapitaConsumptionCorn = countryConsumptionCorn/(population-((0.5)*undernourish));
    perCapitaConsumptionWheat = countryConsumptionWheat/(population-((0.5)*undernourish));
    perCapitaConsumptionSoy = countryConsumptionSoy/(population-((0.5)*undernourish));
    perCapitaConsumptionRice = countryConsumptionRice/(population-((0.5)*undernourish));
    perCapitaConsumptionOther = countryConsumptionOther/(population-((0.5)*undernourish));
  }

  /**
   * Crop yield base case
   */
  private void calculateBaseYield(int[] landNums)
  {
    //baseYield = getProdution()/(idealLand+(acceptableLand*acceptableRate)+(poorLand*poorRate));
    baseYieldCorn = cornProduction/(landNums[0]+(landNums[1]*acceptableRate)+(landNums[2]*poorRate));
    baseYieldWheat = wheatProduction/(landNums[3]+(landNums[4]*acceptableRate)+(landNums[5]*poorRate));
    baseYieldSoy = soyProduction/(landNums[6]+(landNums[7]*acceptableRate)+(landNums[8]*poorRate));
    baseYieldRice = riceProduction/(landNums[9]+(landNums[10]*acceptableRate)+(landNums[11]*poorRate));
    baseYieldOther = otherProduction/(landNums[12]+(landNums[13]*acceptableRate)+(landNums[14]*poorRate));
  }

  private double getProdution()
  {
    return (cornProduction + wheatProduction + soyProduction + riceProduction + otherProduction);
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
  public double getCountryConsumptionCorn(boolean metricUnits)
  {
    return countryConsumptionCorn;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumptionCorn(boolean metricUnits)
  {
    return perCapitaConsumptionCorn;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYieldCorn(boolean metricUnits)
  {
    return baseYieldCorn;
  }

  /**
   * Get the Countries Consumption rate
   * @param metricUnits   boolean for data type
   * @return countryConsumption rate
   */
  public double getCountryConsumptionWheat(boolean metricUnits)
  {
    return countryConsumptionWheat;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumptionWheat(boolean metricUnits)
  {
    return perCapitaConsumptionWheat;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYieldWheat(boolean metricUnits)
  {
    return baseYieldWheat;
  }

  /**
   * Get the Countries Consumption rate
   * @param metricUnits   boolean for data type
   * @return countryConsumption rate
   */
  public double getCountryConsumptionSoy(boolean metricUnits)
  {
    return countryConsumptionSoy;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumptionSoy(boolean metricUnits)
  {
    return perCapitaConsumptionSoy;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYieldSoy(boolean metricUnits)
  {
    return baseYieldSoy;
  }

  /**
   * Get the Countries Consumption rate
   * @param metricUnits   boolean for data type
   * @return countryConsumption rate
   */
  public double getCountryConsumptionRice(boolean metricUnits)
  {
    return countryConsumptionRice;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumptionRice(boolean metricUnits)
  {
    return perCapitaConsumptionRice;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYieldRice(boolean metricUnits)
  {
    return baseYieldRice;
  }

  /**
   * Get the Countries Consumption rate
   * @param metricUnits   boolean for data type
   * @return countryConsumption rate
   */
  public double getCountryConsumptionOther(boolean metricUnits)
  {
    return countryConsumptionOther;
  }

  /**
   * Get the Per Capita Consumption rate
   * @param metricUnits   boolean for data type
   * @return Per Capita Consumption rate
   */
  public double getPerCapitaConsumptionOther(boolean metricUnits)
  {
    return perCapitaConsumptionOther;
  }

  /**
   * Get the Base Yield rate
   * @param metricUnits   boolean for data type
   * @return baseYield rate
   */
  public double getBaseYieldOther(boolean metricUnits)
  {
    return baseYieldOther;
  }

   /*
   *************************************************************
   *
   * iterateYear
   *
   *************************************************************
   */


  private void calculateProduction(Region region)
  {
    HashSet<WorldCell> relevantCells = region.getArableCells();
    double corn = 0.0;
    double wheat = 0.0;
    double rice = 0.0;
    double soy = 0.0;
    double other = 0.0;
    for (WorldCell cell : relevantCells) {
      switch(cell.getCrop()) {
        case "Corn":
          corn += cell.getCurrentCropPenalty();
          break;
        case "Wheat":
          wheat += cell.getCurrentCropPenalty();
          break;
        case "Rice":
          rice += cell.getCurrentCropPenalty();
          break;
        case "Soy":
          soy += cell.getCurrentCropPenalty();
          break;
        case "Other":
          other += cell.getCurrentCropPenalty();
          break;
        default:
          break;
      }
    }
    cornProduction = corn * baseYieldCorn;
    wheatProduction = wheat * baseYieldWheat;
    riceProduction = rice * baseYieldRice;
    soyProduction = soy * baseYieldSoy;
    otherProduction = other * baseYieldOther;
  }

  /**
   * Calculates the the next years data based on data brought in, or rates calculated from it
   */
  public void iterateYear(Region region)
  {
    prevUndernourish = undernourish;
    calculateProduction(region);
    if( undernourish > prevUndernourish )
    {
      mortality = (mortality+((0.2)*(prevUndernourish-undernourish)))/(population/1000);
    }
    population = population + (birthRate+migration-mortality)*(population/1000);
    calculatePerCapitaConsumption();
    calculateProduction(region);
    // Should be one of the first things as it places the crops based on what the user specifies in the GUI
    region.setCrops();
    calculateProduction(region);
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
        if( !increase && cornLand >= 100 )
        {
          setCornLand(cornLand - 100);
        }
        else if( increase )
        {
          setCornLand(cornLand + 100);
        }
        success = true;
        break;
      case "Wheat":
        // has to be greater than 100 square km's
        if( !increase && wheatLand >= 100 )
        {
          setWheatLand(wheatLand - 100);
        }
        else if( increase )
        {
          setWheatLand(wheatLand + 100);
        }
        success = true;
        break;
      case "Soy":
        // has to be greater than 100 square km's
        if( !increase && soyLand >= 100 )
        {
          setSoyLand(soyLand - 100);
        }
        else if( increase )
        {
          setSoyLand(soyLand + 100);
        }
        success = true;
        break;
      case "Rice":
        // has to be greater than 100 square km's
        if( !increase && riceLand >= 100 )
        {
          setRiceLand(riceLand - 100);
        }
        else if( increase )
        {
          setRiceLand(riceLand + 100);
        }
        success = true;
        break;
      case "Other":
        // has to be greater than 100 square km's
        if( !increase && otherLand >= 100 )
        {
          setOtherLand(otherLand-100);
        }
        else if( increase )
        {
          setOtherLand(otherLand+100);
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
