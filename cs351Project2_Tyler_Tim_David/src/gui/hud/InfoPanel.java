
package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.displayconverters.DisplayUnitConverter;
import model.CountryData;
import model.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;

import static gui.ColorsAndFonts.BAR_GRAPH_NEG;
import static gui.ColorsAndFonts.GUI_BACKGROUND;

/**
 * Created by winston on 2/3/15.
 *
 * Scrapped by Tyler on 4.2.15
 * Re-created by
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.3.15
 *
 */
public class InfoPanel extends JPanel implements Observer
{
  /* width = 1 allows panel to be resized intelligently upon container
     instantiation.  Height is important, however, to ensure the child components
     are able to display their info correctly */
  private final static Dimension size = new Dimension(220, 1);
  private MiniViewBox miniViewBox;
  private StatPane stats;
  private StatPane cropStats;
  private StatPane landStats;

  private DisplayUnitConverter converter = new DisplayUnitConverter();
  private static boolean metricUnits = true;
  private WorldPresenter presenter;

  private boolean singeCountry = true;
  private Region region = null;
  private static List<CountryData> countryDataList = new LinkedList<>();
  private static List<GUIRegion> officialRegions = new LinkedList<>();

  private int fullHeight;
  private int fullWidth;

  /**
   Instantiate the InfoPanel
   @param frameWidth width of this component
   @param frameHeight height of this component
   */
  public InfoPanel(int frameWidth, int frameHeight,int y)
  {
    // init
    miniViewBox = new MiniViewBox(" ",frameWidth, frameHeight);
    stats = new StatPane("REGION(S) DATA:",frameWidth,frameHeight);
    cropStats = new StatPane("CROP(S) DATA:",frameWidth,frameHeight);
    landStats = new StatPane("LAND DATA:",frameWidth,frameHeight);

    //config
    this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    // GridBagConstraints c = new GridBagConstraints();
    this.setMinimumSize(size);
    this.setLocation(0, y);
    this.setPreferredSize(new Dimension(frameWidth, frameHeight));
    fullHeight = frameHeight;
    fullWidth = frameWidth;
    this.setBackground(GUI_BACKGROUND);
    this.setBorder(BorderFactory.createLineBorder(ColorsAndFonts.GUI_TEXT_COLOR.darker()));

    //wire
    miniViewBox.setPreferredSize(new Dimension(frameWidth, (frameHeight / 4)));
    this.add(miniViewBox);

    int generalHeight = frameHeight - ( (frameHeight*13) / 16);
    int cropHeight = frameHeight -( (frameHeight*13) / 16);
    int landHeight = frameHeight - ( ((frameHeight*5) / 16) + generalHeight + cropHeight);
    //to ensure that the entire bargraph is shown
    int statsWidth = (int) (frameWidth*.98);
    //General
    stats.setPreferredSize(new Dimension( statsWidth, generalHeight));
    this.add(stats);
    //Land
    landStats.setPreferredSize(new Dimension( statsWidth, cropHeight));
    this.add(landStats);
    //Crops
    cropStats.setPreferredSize(new Dimension( statsWidth, landHeight));
    this.add(cropStats);




    // key binding to switch between different Unite Display objects.
    getInputMap(WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke("M"), "switchToMetric");
    getActionMap().put("switchToMetric", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        metricUnits = true;
        update(null, null);
      }
    });


    getInputMap(WHEN_IN_FOCUSED_WINDOW)
      .put(KeyStroke.getKeyStroke("A"), "switchToAmerican");
    getActionMap().put("switchToAmerican", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        metricUnits = false;
        update(null, null);
      }
    });

  }

  /**
   * Retuns the word presenter object, provides some safety if the object has
   * not been set.
   *
   * @return word presenter object.
   */
  private WorldPresenter getPresenter()
  {
    if (presenter == null)
    {
      throw new NullPointerException("World Presenter is null in InfoPanel!");
    }
    return presenter;
  }

  /**
   * Set the corresponding word presenter. This object is used to know what
   * the info panel should be presenting and how.
   * @param presenter to get regions selection information from.
   */
  public void setPresenter(WorldPresenter presenter)
  {
    this.presenter = presenter;
    presenter.addObserver(this);
  }

  /**
   * Returns the current DisplayConverter object.
   */
  public DisplayUnitConverter getConverter()
  {
    return converter;
  }

  /**
   * set the Converter object to use with displaying region information.
   */
  public void setConverter(DisplayUnitConverter converter)
  {
    this.converter = converter;
  }

  /**
   * sets the Info panel title lable, generally used to display the name of the
   * selected region.
   * @param title to be displayed in the info panel.
   */
  public void setTitle(String title)
  {
    miniViewBox.setTitle(title);
  }



  /**
   * Display the Specified Attribute object in the info panel. This method
   * delegates and handles clearing the previously displayed information.
   */
  public void showAttributes()
  {
    //Stats Panel
    stats.clearBarPlots();
    displayStats(stats);
    stats.revalidate();

    //Crops Data
    cropStats.clearBarPlots();
    displayCrops(cropStats);
    cropStats.revalidate();

    //Land Data
    landStats.clearBarPlots();
    displayLand(landStats);
    landStats.revalidate();
  }


  /**
   * Controls the presentation logic for displaying the general data
   * in the info panel for the specified region(s).
   *
   * @param statPane GUI element to 'write' to.
   */
  private void displayStats(StatPane statPane)
  {
    double population = 0;
    double medianAge = 0;
    double birthRate = 0;
    double mortality = 0;
    double migration = 0;
    double undernourish = 0;

    if( singeCountry )
    {
      CountryData cd = countryDataList.get(0);
      population = cd.getPopulation(metricUnits);
      medianAge = cd.getMedianAge(metricUnits);
      birthRate = cd.getBirthRate(metricUnits);
      mortality = cd.getMortality(metricUnits);
      migration = cd.getMigration(metricUnits);
      undernourish = cd.getUndernourish(metricUnits);
    }
    else
    {
      // Sum up data
      for( CountryData cd : countryDataList )
      {
        population += cd.getPopulation(metricUnits);
        medianAge += cd.getMedianAge(metricUnits);
        birthRate += cd.getBirthRate(metricUnits);
        mortality += cd.getMortality(metricUnits);
        migration += cd.getMigration(metricUnits);
        undernourish += cd.getUndernourish(metricUnits);
      }
      // Average data -counld be more acurate by finding the median
      medianAge /= countryDataList.size();
      // Average data
      birthRate /= countryDataList.size();
      mortality /= countryDataList.size();
      migration /= countryDataList.size();
      undernourish /= countryDataList.size();
    }

    /*
     * General Country Data
     */
    BarPanel bp1 = getBarPanel( population, "Population" , 1, false );
    statPane.addBar(bp1);
    BarPanel bp2 = getBarPanel( medianAge, "Median Age" , 122, false );
    statPane.addBar(bp2);
    BarPanel bp3 = getBarPanel( birthRate, "Birth Rate" , 100, false );
    statPane.addBar(bp3);
    BarPanel bp4 = getBarPanel( mortality, "Mortality Rate" , 100, false );
    statPane.addBar(bp4);
    BarPanel bp5 = getBarPanel( migration, "Migration Rate" , 100, false );
    statPane.addBar(bp5);
    BarPanel bp6 = getBarPanel( undernourish, "Unnourished" , 100, false );
    statPane.addBar(bp6);
  }

  /**
   * Controls the presentation logic for displaying the crop data
   * in the info panel for the specified region(s).
   *
   * @param statPane GUI element to 'write' to.
   */
  private void displayCrops(StatPane statPane)
  {

    double cornTotal = 0;
    double wheatTotal = 0;
    double riceTotal = 0;
    double soyTotal = 0;
    double otherTotal = 0;

    double totalCrops = 0;

    if( singeCountry )
    {
      CountryData cd = countryDataList.get(0);

      cornTotal = cd.getCornTotal(metricUnits);
      wheatTotal = cd.getWheatTotal(metricUnits);
      riceTotal = cd.getRiceTotal(metricUnits);
      soyTotal = cd.getSoyTotal(metricUnits);
      otherTotal = cd.getOtherTotal(metricUnits);

      totalCrops = cd.getCropTotal(metricUnits);
    }
    else
    {
      // Sum up data
      for( CountryData cd : countryDataList )
      {
        cornTotal += cd.getCornTotal(metricUnits);
        wheatTotal += cd.getWheatTotal(metricUnits);
        riceTotal += cd.getRiceTotal(metricUnits);
        soyTotal += cd.getSoyTotal(metricUnits);
        otherTotal += cd.getOtherTotal(metricUnits);

        totalCrops += cd.getCropTotal(metricUnits);
      }
    }
    /*
     * Crop Information
     */
    BarPanel bp14 = getBarPanel( cornTotal, "Corn", totalCrops, true );
    statPane.addBar(bp14);
    BarPanel bp13 = getBarPanel( wheatTotal, "Wheat", totalCrops, true );
    statPane.addBar(bp13);
    BarPanel bp12 = getBarPanel( soyTotal, "Rice", totalCrops, true );
    statPane.addBar(bp12);
    BarPanel bp11 = getBarPanel( riceTotal, "Soy", totalCrops, true );
    statPane.addBar(bp11);
    BarPanel bp10 = getBarPanel( otherTotal, "Other", totalCrops, true );
    statPane.addBar(bp10);
  }

  /**
   * Controls the presentation logic for displaying the data on the Land
   * in the info panel for the specified region(s).
   *
   * @param statPane GUI element to 'write' to.
   */
  private void displayLand(StatPane statPane)
  {
    double organic = 0;
    double conventional = 0;
    double gmo = 0;


    if( singeCountry )
    {
      CountryData cd = countryDataList.get(0);

      organic = cd.getOrganic(metricUnits);
      conventional = cd.getConventional(metricUnits);
      gmo = cd.getGmo(metricUnits);
    }
    else
    {
      // Sum up data
      for( CountryData cd : countryDataList )
      {
        organic += cd.getOrganic(metricUnits);
        conventional += cd.getConventional(metricUnits);
        gmo += cd.getGmo(metricUnits);
      }
      // Average data
      organic /= countryDataList.size();
      conventional /= countryDataList.size();
      gmo /= countryDataList.size();
    }
    /*
     * Land Information
     */
    BarPanel bp7 = getBarPanel( organic, "Organic" , 1, true);
    statPane.addBar(bp7);
    BarPanel bp8 = getBarPanel( conventional, "Conventional" , 1, true);
    statPane.addBar(bp8);
    BarPanel bp9 = getBarPanel( gmo, "GMO" , 1, true);
    statPane.addBar(bp9);
  }

  /**
   * long (and ugly) method to handel the tedious logic of displaying each
   * region attribute in the appropriate way, with the appropriate color.
   *<p>
   * ie. $ 2.23 for money, 34.23 F° for temperature etc...
   */
  private BarPanel getBarPanel(double value, String name, double totalPercent, boolean adjustable)
  {
    // somewhat sensible defaults.
    //RegionAttributes converted = getConverter().convertAttributes(attributesSet);
    int FULL_BAR = 1; // TO CREATE A LABEL, overloading the concept of bar.
    String PrimaryLabel = name;
    Color barColor = BAR_GRAPH_NEG;
    double ratio = Math.abs(value / totalPercent);

    // I don't want the java to string method, I want the number how I want it
    NumberFormat formatter = new DecimalFormat("#,###");
    NumberFormat percentFormatter = new DecimalFormat("#,##0.0#");

    String secondaryLabel = formatter.format(value);

    switch(name)
    {
      case "Population":
        ratio = 0;
        break;
      case "Median Age":
        barColor = Color.YELLOW;
        secondaryLabel += getConverter().getYearsSymbol(metricUnits);
        break;
      case "Birth Rate":
      case "Mortality Rate":
      case "Migration Rate":
        barColor = Color.GREEN;
        secondaryLabel += getConverter().getRateSymbol(metricUnits);
        break;
      case "Unnourished":
        barColor = Color.RED;
        secondaryLabel += getConverter().getRateSymbol(metricUnits);
        break;
      case "Corn":
        barColor = new Color(0xEBEB33);
        secondaryLabel += " " + getConverter().getLandUsedSymbol(metricUnits);
        break;
      case "Wheat":
        barColor = new Color(0xFF9900);
        secondaryLabel += " " + getConverter().getLandUsedSymbol(metricUnits);
        break;
      case "Rice":
        barColor = new Color(0xE6E6E6);
        secondaryLabel += " " + getConverter().getLandUsedSymbol(metricUnits);
        break;
      case "Soy":
        barColor = new Color(0xC2A385);
        secondaryLabel += " " + getConverter().getLandUsedSymbol(metricUnits);
        break;
      case "Other":
        barColor = new Color(0xFF66FF);
        secondaryLabel += " " + getConverter().getLandUsedSymbol(metricUnits);
        break;
      case "Organic":
        barColor = new Color(0x66FF33);
        secondaryLabel = percentFormatter.format(value*100);
        secondaryLabel += " " + getConverter().getPercentSymbol(metricUnits);
        break;
      case "Conventional":
        barColor = new Color(0x996633);
        secondaryLabel = percentFormatter.format(value*100);
        secondaryLabel += " " + getConverter().getPercentSymbol(metricUnits);
        break;
      case "GMO":
        barColor = new Color(0xFF5050);
        secondaryLabel = percentFormatter.format(value*100);
        secondaryLabel += " " + getConverter().getPercentSymbol(metricUnits);
        break;
      default:
        ratio = 0;
        // no nothing, fall back on the above default values.
    }
    return new BarPanel(barColor, ratio, PrimaryLabel, secondaryLabel, adjustable);
  }

  /**
   * Convert the happiness double into a string for gui presentation.
   */
  private String getHappyLabel(double ratio)
  {
    if (ratio < 0.25)
    {
      return "DESPONDENT";
    }
    else if (ratio < 0.5)
    {
      return "UNHAPPY";
    }
    else if (ratio < 0.75)
    {
      return "HAPPY";
    }
    else
    {
      return "ECSTATIC";
    }
  }

  /**
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param amount you wish to adjust the crop by
   * @param crop that you wish to adjust
   * @return updated crop amount
   */
  public static double adjustCrop(double amount, String crop)
  {
    boolean error = false;
    // This will only be called if the countryDataList is greater than 1
    CountryData countryData = countryDataList.get(0);

    if( countryData.cropAdjustmentByName(crop, (1 + amount)) ) {
    }
    else
    {
      String errMsg;
      if( amount > 0 ) errMsg = "You can't add more land to this area. You have reached 100% use. Take away use.";
      else errMsg = "You can't subtract more land to this area. You have reached 0% use. Add more use.";

      String msg = "Invalid adjustment for " + crop + "\n" + errMsg;

      JOptionPane.showMessageDialog(main.Game.frame,msg,"Invalid crop adjustment",
        JOptionPane.ERROR_MESSAGE);
    }
    return countryData.getCropP(crop,metricUnits);
  }

  /* display color as a function of happiness */
  private Color getHappyColor(double ratio)
  {
    return ratio < 0.5 ? Color.red : Color.cyan;
  }

  /**
   * Resets the info panel, used when no information should be in the Display.
   */
  public void clearDisplay()
  {
    miniViewBox.setTitle(" ");
    miniViewBox.setDrawableRegions(null);
    stats.clearBarPlots();
  }

  /**
   * Renders the Stats for the list of regions given. This is also used
   * when only drawing a single region (i.e. length of collection = 1).
   *
   * @param officialRegions List of regions to display state for.
   */
  public void displayAllGUIRegions(List<GUIRegion> officialRegions)
  {
    // single region display logic
    if (officialRegions.size() == 1)
    {
      singeCountry = true;
      setTitle(officialRegions.get(0).getName());
      showAttributes();
      miniViewBox.setAlph(0.0f);
    }
    else if (officialRegions.size() > 1) // multi region display logic.
    {
      singeCountry = false;
      clearDisplay();
      setTitle("AVERAGE:");
      miniViewBox.setAlph(1f);
      if (!presenter.isActivelyDragging()) // delays summation until drag is over.
      {
        showAttributes();
      }
    }


    miniViewBox.setDrawableRegions(officialRegions);
  }

  /**
   * This method is called whenever the observed object is changed. An
   * application calls an <tt>Observable</tt> object's
   * <code>notifyObservers</code> method to have all the object's
   * observers notified of the change.
   *
   * @param o   the observable object.
   * @param arg an argument passed to the <code>notifyObservers</code>
   */
  @Override
  public void update(Observable o, Object arg)
  {
    List<GUIRegion> activeRegions = getPresenter().getActiveRegions();

    //Clear all of the country data that will be displayed first
    countryDataList.clear();
    officialRegions.clear();

    if (activeRegions != null)
    {
      for (GUIRegion gr : activeRegions) {
        if ( gr.getOfficialCountry() )
        {
          if( gr.getCountryData() != null ) countryDataList.add(gr.getCountryData());
          officialRegions.add(gr);
        }
      }
    }

    if (officialRegions.size() == 0 || activeRegions == null )
    {
      // HIDE PANEL
      this.setVisible(false);
      clearDisplay();
    }
    else
    {
      // SHOW PANEL
      this.setVisible(true);
      displayAllGUIRegions(officialRegions);
    }
  }
}
