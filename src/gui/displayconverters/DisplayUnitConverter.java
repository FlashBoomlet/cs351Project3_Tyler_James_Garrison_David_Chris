package gui.displayconverters;


/**
 * Created by winston on 2/6/15.
 * <p/>
 * Class is responsible for converting and properly displaying unites of
 * measurements in different systems of measurements. All conventions happen in
 * terms of the model unit system, which uses the customary United States units.
 * <p/>
 * for more info see: http://en.wikipedia.org/wiki/United_States_customary_units
 */
public class DisplayUnitConverter
{

  /**
   * Returns the currency symbol. used in display contexts to show the
   * appropriate currency annotation.
   *
   * @return string representing the currency of the converter.
   */
  public String getCurrencySymbol(boolean metricUnits)
  {
    if( metricUnits ) return "$";
    else return "$";
  }

  /**
   * Returns the land used symbol. used in display contexts to show the
   * appropriate currency annotation.
   *
   * @return string representing the land used of the converter.
   */
  public String getLandUsedSymbol(boolean metricUnits)
  {
    if( metricUnits ) return "sqKm";
    else return "sqM";
  }

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of inch
   *
   * @return string representing the converters unite for inches (eg. mm or in)
   */
  public String getInchSymbol(boolean metricUnits)
  {
    if( metricUnits ) return "mm.";
    else return "in.";
  }

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of feet
   *
   * @return string representing the converters unite for feet (eg. m or ft)
   */
  public String getFeetSymbol(boolean metricUnits)
  {
    if( metricUnits ) return "m.";
    else return "ft.";
  }

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of Fahrenheit
   *
   * @return string representing the converters unite for Fahrenheit
   * (eg. C° or F°)
   */
  public String getTmpSymbol(boolean metricUnits)
  {
    if( metricUnits ) return "C°";
    else return "F°";
  }

  /**
   * Returns the appropriate symbol for '%'
   *
   * @return string representing '%'
   */
  public String getPercentSymbol(boolean metricUnits)
  {
    return "%";
  }

  /**
   * Returns the unit of measure for rate of populations
   *
   * @return string ":1000 persons"
   */
  public String getRateSymbol(boolean metricUnits)
  {

    return ":1000 persons";
  }

  /**
   * Returns the unit of measuring age
   *
   * @return string " years of age"
   */
  public String getYearsSymbol(boolean metricUnits)
  {
    return " years of age";
  }

  /**
   * Returns the unit for the scale
   *
   * @return string " years of age"
   */
  public String getScaleSymbol(boolean metricUnits)
  {
    if( metricUnits ) return "Km";
    else return "Miles";
  }
}
