package IO;

import gui.GUIRegion;
import model.CountryData;

import javax.swing.*;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Collection;

/**
 * Parses all CSV data and places inside of the data structures for the
 * regions/countries that will be controlled by the game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.13.2015
 */
public class PolicyCSVParser {

  private String csvLocation = "resources/data/policyData.csv";

  private BufferedReader br = null;
  private String line = "";
  private int counter = 0;
  private Collection<GUIRegion> regions;
  private int rowNumber = 0;
  private int colNumber = 0;
  private String[] columnNames = new String[14];

  /**
   * CountryCSVParser is the constructor to parse data for all countries being utalized in this game
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 4.13.2015
   *
   * @param regions in the world
   */
  public PolicyCSVParser(Collection<GUIRegion> regions)
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
   * @since 4.13.2015
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
   * @since 4.13.2015
   *
   * @param atributes for a certain country
   */
  private void assignAttributes(String[] atributes)
  {
    GUIRegion currentRegion = null;
    CountryData data = null;
    // To prevent array index out of bounds error, ensure all data is filled out
    if( atributes.length >= 14 )
    {
      Double.parseDouble(atributes[1]);

    }
    else{
      JOptionPane.showMessageDialog(null, "Not enough data in csv in row: "+ counter, "Error in CSV", JOptionPane.ERROR_MESSAGE);
    }

  }

  private void errors(String msg, String[] atributes, int col, int row){
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

}
