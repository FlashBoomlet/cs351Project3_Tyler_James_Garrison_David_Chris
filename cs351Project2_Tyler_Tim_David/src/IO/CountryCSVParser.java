package IO;

import gui.GUIRegion;
import model.CountryData;
import model.Region;
import model.World;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;
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
  private Collection<GUIRegion> regions;

  /**
   * CountryCSVParser is the constructor to parse data for all countries being utalized in this game
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 4.2.2015
   *
   * @param regions in the world
   */
  public CountryCSVParser(Collection<GUIRegion> regions )
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
    GUIRegion currentRegion = null;
    // To prevent array index out of bounds error, ensure all data is filled out
    if( atributes.length >= 31 )
    {
      String country = atributes[0];
      currentRegion = findCurrentRegion(country);
      if( currentRegion != null )
      {
        CountryData data = new CountryData();
        data.setPopulation( Double.parseDouble(atributes[1]) );
        data.setMedianAge( Double.parseDouble(atributes[2]) );
        data.setBirthRate( Double.parseDouble(atributes[3]) );
        data.setMortality( Double.parseDouble(atributes[4]) );
        data.setMigration( Double.parseDouble(atributes[5]) );
        data.setUndernourish( Double.parseDouble(atributes[6]) );

        data.setCornProduction( Double.parseDouble(atributes[7]) );
        data.setCornExports( Double.parseDouble(atributes[8]) );
        data.setCornImports( Double.parseDouble(atributes[9]) );
        data.setCornLand( Double.parseDouble(atributes[10]) );

        data.setWheatProduction( Double.parseDouble(atributes[11]) );
        data.setWheatExports( Double.parseDouble(atributes[12]) );
        data.setWheatImports( Double.parseDouble(atributes[13]) );
        data.setWheatLand( Double.parseDouble(atributes[14]) );

        data.setRiceProduction( Double.parseDouble(atributes[15]) );
        data.setRiceExports( Double.parseDouble(atributes[16]) );
        data.setRiceImports( Double.parseDouble(atributes[17]) );
        data.setRiceLand( Double.parseDouble(atributes[18]) );

        data.setSoyProduction( Double.parseDouble(atributes[19]) );
        data.setSoyExports( Double.parseDouble(atributes[20]) );
        data.setSoyImports( Double.parseDouble(atributes[21]) );
        data.setSoyLand( Double.parseDouble(atributes[22]) );

        data.setOtherProduction( Double.parseDouble(atributes[23]) );
        data.setOtherExports( Double.parseDouble(atributes[24]) );
        data.setOtherImports( Double.parseDouble(atributes[25]) );
        data.setOtherLand( Double.parseDouble(atributes[26]) );

        data.setLandArea( Double.parseDouble(atributes[27]) );
        data.setArableOpen( Double.parseDouble(atributes[28]) );
        data.setOrganic( Double.parseDouble(atributes[29]) );
        data.setConventional( Double.parseDouble(atributes[30]) );
        data.setGmo( Double.parseDouble(atributes[31]) );

        data.calculateZeroOrder();
        currentRegion.setOfficialCountry();
        currentRegion.setCountryData(data);
      }
    }

  }

  /**
   *
   * @param name
   * @return a region that matches
   */
  private GUIRegion findCurrentRegion(String name)
  {
    for( GUIRegion gr: regions)
    {
      if( gr.getName().equals(name) ) {
        return gr;
      }
    }
    return null;
  }
}
