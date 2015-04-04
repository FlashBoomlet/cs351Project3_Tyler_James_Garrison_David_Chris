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
    return "$";
  }

  /**
   * Returns the land used symbol. used in display contexts to show the
   * appropriate currency annotation.
   *
   * @return string representing the land used of the converter.
   */
  public String getLandUsedSymbol(boolean metricUnits){ return "km^2"; }

  /**
   * Converts the given measurement in inches into the Converters equivalent.
   *
   * @param inches value in inches
   * @return the converters equivalent inch, returned as a double.
   */
  public double convertInches(double inches)
  {
    return inches;
  }

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of inch
   *
   * @return string representing the converters unite for inches (eg. mm or in)
   */
  public String getInchSymbol(boolean metricUnits)
  {
    return "In.";
  }

  /**
   * Converts the given measurement in feet into the Converters equivalent.
   *
   * @param feet value measured in feet
   * @return the converters equivalent of feet, returned as a double.
   */
  public double convertFeet(double feet)
  {
    return feet;
  }

  /**
   * Returns the appropriate symbol corresponding the the converters 'version'
   * of feet
   *
   * @return string representing the converters unite for feet (eg. m or ft)
   */
  public String getFeetSymbol(boolean metricUnits)
  {
    return "ft.";
  }

  /**
   * Converts the given measurement in Fahrenheit into the Converters equivalent.
   *
   * @param temp value measured in Fahrenheit
   * @return the converters equivalent of Fahrenheit, returned as a double.
   */
  public double convertFahrenheit(double temp)
  {
    return temp;
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
    return "F°";
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

}
