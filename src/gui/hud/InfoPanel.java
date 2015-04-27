
package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.displayconverters.DisplayUnitConverter;
import main.Game;
import main.SettingsScreen;
import model.CountryData;
import model.Region;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.*;
import java.util.List;
import static javax.swing.ScrollPaneConstants.*;

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
public class InfoPanel extends JPanel implements Observer, ActionListener
{
  /* width = 1 allows panel to be resized intelligently upon container
     instantiation.  Height is important, however, to ensure the child components
     are able to display their info correctly */
  private final static Dimension size = new Dimension(220, 1);
  /*
   * Components
   */
  private MiniViewBox miniViewBox;
  private StatPane stats;
  private StatPane cropStats;
  private StatPane landStats;
  private InfoPanelUserControls infoPanelUserControls;
  private WorldPresenter presenter;
  private DisplayUnitConverter converter = new DisplayUnitConverter();
  private Region region = null;
  private static JButton hide;
  private static JButton units;
  private static JButton policy;
  private static JButton trade;
  private static CardSelector policySelector;
  private static CardSelector tradeSelector;
  /*
   * Variables
   */
  private static boolean metricUnits = true;
  private boolean singeCountry = true;
  private static List<CountryData> countryDataList = new LinkedList<>();
  private static List<GUIRegion> officialRegions = new LinkedList<>();
  private final int MIN_WIDTH = 0;
  private final int MAX_WIDTH;
  private final int STANDARD_HEIGHT;
  // if true all info will be displayed
  //if false show a small collapsed version
  private static boolean isOpen = false;
  private OpenerThread openerThread;

  /**
   Instantiate the InfoPanel
   @param frameWidth width of this component
   @param frameHeight height of this component
   */
  public InfoPanel(int frameWidth, int frameHeight,int y)
  {
    super();
    // init
    miniViewBox = new MiniViewBox(" ",frameWidth, frameHeight);
    stats = new StatPane("REGION(S) DATA:",frameWidth,frameHeight);
    cropStats = new StatPane("CROP(S) DATA:",frameWidth,frameHeight);
    landStats = new StatPane("LAND DATA:",frameWidth,frameHeight);
    infoPanelUserControls = new InfoPanelUserControls();

    metricUnits = SettingsScreen.getUnits();

    // Variable for the smooth movement, oh yeah, its silky smooth
    STANDARD_HEIGHT = frameHeight;
    MAX_WIDTH = frameWidth;

    //config
    this.setLayout(new FlowLayout(FlowLayout.LEFT,0,0));
    // GridBagConstraints c = new GridBagConstraints();
    this.setMinimumSize(size);
    this.setLocation(0, y);
    this.setPreferredSize(new Dimension(frameWidth, frameHeight));
    this.setBackground(GUI_BACKGROUND);
    this.setBorder(BorderFactory.createLineBorder(ColorsAndFonts.GUI_TEXT_COLOR.darker()));

    //to ensure that the entire bargraph is shown
    int statsWidth = (int) (frameWidth*.99)-5;

    //wire
    miniViewBox.setPreferredSize(new Dimension(statsWidth, (frameHeight / 4)));
    this.add(miniViewBox);

    int generalHeight = ( (frameHeight*3) / 16);
    int landHeight = ( (frameHeight*3) / 16);
    int cropHeight = frameHeight - ( ((frameHeight*5) / 16) + generalHeight + landHeight);

    policy = new JButton("POLICY");
    policy.addActionListener(this);

    trade = new JButton("TRADE");
    trade.addActionListener(this);

    JPanel scrollCon = new JPanel();
    scrollCon.setOpaque(true);
    scrollCon.setLayout(new FlowLayout());
    scrollCon.setPreferredSize(new Dimension(statsWidth, frameHeight));
    scrollCon.setBackground(GUI_BACKGROUND);

    //Policy Button and Trade Button
    scrollCon.add(policy);
    scrollCon.add(trade);
    //General
    stats.setPreferredSize(new Dimension( statsWidth, generalHeight));
    scrollCon.add(stats);
    //Land
    landStats.setPreferredSize(new Dimension( statsWidth, landHeight));
    scrollCon.add(landStats);
    //Crops
    cropStats.setPreferredSize(new Dimension( statsWidth, cropHeight));
    scrollCon.add(cropStats);

    JScrollPane scrollFrame = new JScrollPane(scrollCon);
    scrollFrame.setOpaque(false);
    scrollFrame.setBackground(GUI_BACKGROUND);
    scrollFrame.setPreferredSize(new Dimension( frameWidth, generalHeight+landHeight+cropHeight ));
    scrollFrame.getVerticalScrollBar().setPreferredSize(new Dimension(5, 100));
    scrollFrame.getHorizontalScrollBar().setPreferredSize(new Dimension(0, 0));
    scrollFrame.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);

    //User Controls
    add(scrollFrame);
    infoPanelUserControls.setPreferredSize(new Dimension( statsWidth, ((frameHeight*1)/40) ));
    this.add(infoPanelUserControls);




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
   * Set the Card Selector
   * @param selector component
   * @param name of the cardSelector
   */
  public void setCardSelector(CardSelector selector, String name)
  {
    switch (name)
    {
      case "POLICY":
        policySelector = selector;
        policySelector.setVisible(false);
        break;
      case "TRADE":
        tradeSelector = selector;
        tradeSelector.setVisible(false);
        break;
      default:
    }
  }

  /**
   * Overrides action performed.
   * Detects which button is clicked and either pauses the game or shows the settings
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    JButton tempBtn = (JButton) e.getSource();
    String name = tempBtn.getText();

    if( name == "POLICY" )
    {
      policySelector.setVisible(true);
    }
    else if( name == "TRADE" )
    {
      tradeSelector.setVisible( true );
    }
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
    BarPanel bp14 = getBarPanel( cornTotal, "Corn", totalCrops, false );
    statPane.addBar(bp14);
    BarPanel bp13 = getBarPanel( wheatTotal, "Wheat", totalCrops, false);
    statPane.addBar(bp13);
    BarPanel bp12 = getBarPanel( riceTotal, "Rice", totalCrops, false );
    statPane.addBar(bp12);
    BarPanel bp11 = getBarPanel( soyTotal, "Soy", totalCrops, false );
    statPane.addBar(bp11);
    BarPanel bp10 = getBarPanel( otherTotal, "Other", totalCrops, false );
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
   * @param crop that you wish to adjust
   * @return updated crop amount
   */
  public static void adjustCrop(boolean increase, String crop)
  {
    boolean error = false;
    // This will only be called if the countryDataList is greater than 1
    for( CountryData cd: countryDataList)
    {
      cd.adjustmentByName(crop, increase);
    }
    //Make a Call to update WorldArray

    //Update Panel Stats
    main.Game.infoPanel.update(null, null);
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
    policySelector.setVisible(false);
    tradeSelector.setVisible(false);
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

    if (officialRegions.size() == 0 || activeRegions == null || activeRegions.size() == 0 )
    {
      this.setVisible(false);
      policySelector.resetLocation();
      tradeSelector.resetLocation();
      clearDisplay();
    }
    //only show the panel if isn't already visible, no need to keep reanimating it
    else if(!this.isVisible())
    {
      // SHOW PANEL
      this.setVisible(true);
      fancyShow();
      displayAllGUIRegions(officialRegions);
    }
  }

  /**
   * Called to update the units
   * @param unitsB true for metric or false for english
   */
  public static void setMetricUnits(boolean unitsB)
  {
    metricUnits = unitsB;
    main.Game.infoPanel.update(null, null);
    if( unitsB )
    {
      units.setText("English");
    }
    else
    {
      units.setText("Metric");
    }

  }

  /**
   * hidePanel and clear active regions when settings are altered and or
   * the game is paused
   */
  public void hidePanel()
  {
    getPresenter().clearActiveList();
    main.Game.infoPanel.update(null, null);
    policySelector.setVisible(false);
    tradeSelector.setVisible(false);
    this.setVisible(false);
    policySelector.resetLocation();
    tradeSelector.resetLocation();
  }

  class InfoPanelUserControls extends JPanel implements ActionListener
  {
    InfoPanelUserControls()
    {
      super();
      this.setBackground(GUI_BACKGROUND);
      setLayout(new BorderLayout());

      hide = new JButton("Hide/Clear");
      hide.addActionListener(this);

      units = new JButton("English");
      units.addActionListener(this);

      add(units,BorderLayout.WEST);
      add(hide,BorderLayout.EAST);
    }
    /**
     * Overrides action performed.
     * Detects which button is clicked and either pauses the game or shows the settings
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e) {
      JButton tempBtn = (JButton) e.getSource();
      String text = tempBtn.getText();

      if( text == "Hide/Clear" )
      {
        getPresenter().clearActiveList();
      }
      else if( text == "Metric" )
      {
        setMetricUnits(true);
        SettingsScreen.updateUnits(true);
      }
      else if( text == "English" )
      {
        setMetricUnits(false);
        SettingsScreen.updateUnits(false);
      }
      main.Game.infoPanel.update(null, null);
    }
  }



  private void fancyShow()
  {
    /*
     * create thread for silky smooth open and close
     */
    if (openerThread == null || !openerThread.isAlive())
    {
      openerThread = new OpenerThread();
      openerThread.start();
    }
  }

  /*
  * runs to set the size of the panel through an
  * animation, a thread is used so that players
  * can continue to manipulate the map while the resizing is happening
  */
  private class OpenerThread extends Thread
  {

    //I just played with the values until I got something that felt right
    @Override
    public void run()
    {
      if (!isVisible())
      {
        for (int i = MAX_WIDTH; i>= MIN_WIDTH;i-=8)
        {
          setSize(i,STANDARD_HEIGHT);

          //call to fix the jittering
          getParent().repaint();

          try
          {
            this.sleep(5);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
        }

        setSize(MIN_WIDTH,STANDARD_HEIGHT);
      }
      else
      {
        for (int i = MIN_WIDTH; i<=MAX_WIDTH; i+=8)
        {
          setSize(i,STANDARD_HEIGHT);

          //call to fix the jittering
          getParent().repaint();
          try
          {
            this.sleep(5);
          }
          catch (InterruptedException e)
          {
            e.printStackTrace();
          }
        }
        setSize(MAX_WIDTH,STANDARD_HEIGHT);
      }
      this.interrupt();
    }
  }

}
