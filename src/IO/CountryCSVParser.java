package IO;

import gui.GUIRegion;
import model.CountryData;
import model.Region;
import model.World;

import javax.swing.*;
import java.awt.*;
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
  private int rowNumber = 0;
  private int colNumber = 0;
  private String[] columnNames = new String[33];

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
        if( counter == 0 ) columnNames = varArray;
        if (counter > 1) assignAttributes(varArray);
        counter++;
        rowNumber ++;
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
    CountryData data = null;
    // To prevent array index out of bounds error, ensure all data is filled out
    if( atributes.length >= 33 )
    {
      String country = atributes[0];
      if( country.length() <= 0 ) {
        errors("Country Name null ",atributes, 0, counter);
      }
      currentRegion = findCurrentRegion(country);
      if( currentRegion != null )
      {
        data = new CountryData();
        data.setPopulation( Double.parseDouble(atributes[1]) );
        if( Double.parseDouble(atributes[1]) <= 0 ) {
          errors("Population is less than or equal to 0.",atributes, 1, counter);
        }
        data.setMedianAge(Double.parseDouble(atributes[2]));
        data.setBirthRate(Double.parseDouble(atributes[3]));
        data.setMortality(Double.parseDouble(atributes[4]));
        data.setMigration(Double.parseDouble(atributes[5]));
        data.setUndernourish(Double.parseDouble(atributes[6]));

        data.setCornProduction(Double.parseDouble(atributes[7]));
        data.setCornExports(Double.parseDouble(atributes[8]));
        data.setCornImports(Double.parseDouble(atributes[9]));
        data.setCornLand(Double.parseDouble(atributes[10]));

        data.setWheatProduction(Double.parseDouble(atributes[11]));
        data.setWheatExports(Double.parseDouble(atributes[12]));
        data.setWheatImports(Double.parseDouble(atributes[13]));
        data.setWheatLand(Double.parseDouble(atributes[14]));

        data.setRiceProduction(Double.parseDouble(atributes[15]));
        data.setRiceExports(Double.parseDouble(atributes[16]));
        data.setRiceImports(Double.parseDouble(atributes[17]));
        data.setRiceLand(Double.parseDouble(atributes[18]));

        data.setSoyProduction(Double.parseDouble(atributes[19]));
        data.setSoyExports(Double.parseDouble(atributes[20]));
        data.setSoyImports(Double.parseDouble(atributes[21]));
        data.setSoyLand(Double.parseDouble(atributes[22]));

        data.setOtherProduction(Double.parseDouble(atributes[23]));
        data.setOtherExports(Double.parseDouble(atributes[24]));
        data.setOtherImports(Double.parseDouble(atributes[25]));
        data.setOtherLand(Double.parseDouble(atributes[26]));

        data.setLandArea( Double.parseDouble(atributes[27]) );
        if( Double.parseDouble(atributes[27]) <= 0 ) {
          errors("Land area is less than or equal to 0.",atributes, 27, counter);
        }
        data.setArableOpen( Double.parseDouble(atributes[28]) );
        data.setOrganic( Double.parseDouble(atributes[29]) );
        data.setConventional( Double.parseDouble(atributes[30]) );
        data.setGmo( Double.parseDouble(atributes[31]) );

        data.setShippingLat(Double.parseDouble(atributes[32]));
        data.setShippingLon(Double.parseDouble(atributes[33]));

        currentRegion.setOfficialCountry();
        currentRegion.setCountryData(data);
      }
    }
    else{
      JOptionPane.showMessageDialog(null, "Not enough data in csv in row: "+ counter, "Error in CSV", JOptionPane.ERROR_MESSAGE);
    }

  }

  public void errors(String msg, String[] atributes, int col, int row){
    JFrame frame = new JFrame();
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    JTextArea errorMsg = new JTextArea("Error in csv column: "+col+" row: "+row);
//    String rowData[][] = new String[1][atributes.length];
//    rowData[0] = atributes;
//    System.out.println(columnNames);
//    System.out.println(rowData);
//    JTable table = new JTable(rowData, columnNames);

    //JScrollPane scrollPane = new JScrollPane(table);
    JButton save = new JButton("Save");
    save.addActionListener(new java.awt.event.ActionListener() {
      public void actionPerformed(java.awt.event.ActionEvent evt) {
        JOptionPane.showMessageDialog(null, "Save successful!", "INFO: ", JOptionPane.INFORMATION_MESSAGE);
      }
    });
    frame.add(errorMsg, BorderLayout.PAGE_START);
   // frame.add(scrollPane, BorderLayout.CENTER);
    frame.add(save,BorderLayout.PAGE_END);
    frame.setSize(300, 150);
    frame.setVisible(true);
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
