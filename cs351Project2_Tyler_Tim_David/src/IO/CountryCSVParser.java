package IO;

import model.Region;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

/**
 * Parses all CSV data and places inside of the data structures for the
 * regions/countries that will be controlled by the game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.2.2015
 */
public class CountryCSVParser {

  private String csvLocation = "resources/data/countryData.csv";

  private BufferedReader br = null;
  private String line = "";
  private int counter = 0;
  private List<Region> regions;

  /**
   * CountryCSVParser is the constructor to parse data for all countries being utalized in this game
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 4.2.2015
   *
   * @param regions in the world
   */
  public CountryCSVParser(List<Region> regions )
  {
    this.regions = regions;
    parseCSV();
  }

  /**
   * parseCSV reads one line of a csv file in at a time.
   * It then splits that line at the ',' and assigns the values to a String[]
   * It then passes the String[] into a helper method that finds the correct country and
   *  assigns the country the values
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 4.2.2015
   *
   */
  private void parseCSV()
  {
    try
    {
      br = new BufferedReader(new FileReader(csvLocation));

      while( (line = br.readLine()) != null )
      {
        String[] varArray = line.split(",");
        // Skip over col titles and types
        if (counter > 1) assignAttributes(varArray);
        counter++;
      }
    }
    catch (IOException e) {}
    finally
    {
      if (br != null)
      {
        try
        {
          br.close();
        }
        catch (IOException e) {}
      }
    }
  }

  /**
   * Values are currently assigned to temporary variables
   * Goal is to have all of the variables directly imputed to a region /country
   * Need to add implementation of country search by name
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 4.2.2015
   *
   * @param atributes for a certain country
   */
  private void assignAttributes(String[] atributes)
  {
    // To prevent array index out of bounds error, ensure all data is filled out
    if( atributes.length >= 31 )
    {
      String country = atributes[0];

      double population = Double.parseDouble(atributes[1]);
      double averageAge = Double.parseDouble(atributes[2]);
      double birthRate = Double.parseDouble(atributes[3]);
      double mortality = Double.parseDouble(atributes[4]);
      double migration = Double.parseDouble(atributes[5]);
      double undernourish = Double.parseDouble(atributes[6]);

      double cornProduction = Double.parseDouble(atributes[7]);
      double cornExports = Double.parseDouble(atributes[8]);
      double cornImports = Double.parseDouble(atributes[9]);
      double cornLand = Double.parseDouble(atributes[10]);
      double wheatProduction= Double.parseDouble(atributes[11]);
      double wheatExports = Double.parseDouble(atributes[12]);
      double wheatImports = Double.parseDouble(atributes[13]);
      double wheatLand = Double.parseDouble(atributes[14]);
      double riceProduction = Double.parseDouble(atributes[15]);
      double riceExports = Double.parseDouble(atributes[16]);
      double riceImports = Double.parseDouble(atributes[17]);
      double riceLand = Double.parseDouble(atributes[18]);
      double soyProduction = Double.parseDouble(atributes[19]);
      double soyExports = Double.parseDouble(atributes[20]);
      double soyImports = Double.parseDouble(atributes[21]);
      double soyLand = Double.parseDouble(atributes[22]);
      double otherProduction = Double.parseDouble(atributes[23]);
      double otherExports = Double.parseDouble(atributes[24]);
      double otherImports = Double.parseDouble(atributes[25]);
      double otherLand = Double.parseDouble(atributes[26]);
      double landArea = Double.parseDouble(atributes[27]);
      double arableOpen = Double.parseDouble(atributes[28]);
      double organic = Double.parseDouble(atributes[29]);
      double conventional = Double.parseDouble(atributes[30]);
      double gmo = Double.parseDouble(atributes[31]);
    }

  }
}
