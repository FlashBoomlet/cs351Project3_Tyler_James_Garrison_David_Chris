package main.StartScreen;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.displayconverters.DisplayUnitConverter;
import gui.hud.BarPanel;
import gui.hud.MiniViewBox;
import gui.hud.PieChart.ChartKey;
import gui.hud.PieChart.PieChart;
import gui.hud.PieChart.Slice;
import gui.hud.StatPane;
import main.Trigger;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

import static gui.ColorsAndFonts.BAR_GRAPH_NEG;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * Container for the country selection and preview
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 04/30/15
 * */
class BeginScreen extends JPanel implements ActionListener
{
  private JButton beginGame;
  private int frameWidth;
  private int frameHeight;
  Font FONT = new Font("Tahoma", Font.PLAIN, 14);
  private GUIRegion selectedCountry = null;
  private JLabel selectedCountryLabel;
  Collection<GUIRegion> guiRegions;
  ArrayList<GUIRegion> guiRegionsOfficial = new ArrayList<>();
  Color backGround = ColorsAndFonts.GUI_BACKGROUND;
  Color foreGroundColor = ColorsAndFonts.GUI_TEXT_COLOR;
  private LowerLeft left;
  private RightLeft right;
  private MiniMapDisplay miniMap;
  private DisplayUnitConverter converter = new DisplayUnitConverter();
  boolean metricUnits = true;


  private ArrayList<Slice> landArray = new ArrayList<>();
  Slice[] landSlices = {
    new Slice(0, new Color(102,255,51,155), "Organic"),
    new Slice(0, new Color(153,102,51,155),  "Conventional" ),
    new Slice(0, new Color(255,80,80,155), "GMO" ) };
  private ArrayList<Slice> cropArray = new ArrayList<>();
  Slice[] cropSlices = {
    new Slice(0, new Color(235,235,51,155), "Corn"),
    new Slice(0, new Color(255,153,0,155),  "Wheat" ),
    new Slice(0, new Color(230,230,230,155), "Rice" ),
    new Slice(0, new Color(194,163,133,155), "Soy"),
    new Slice(0, new Color(255,102,255,155), "Other")
  };



  /*
   * Components for the begin screen
   */
  private CountrySelect countrySelect;
  private CountryPreview countryPreview;
  private Trigger trigger;

  private Action startAction = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      trigger.setPlayer(selectedCountry);
      StartScreen.buttonAction("BEGIN");
    }
  };

  /**
   * Constructor
   */
  BeginScreen(Trigger trigger)
  {
    super();
    this.trigger = trigger;
    this.frameWidth = main.Game.frameWidth;
    this.frameHeight = main.Game.frameHeight;

    setOpaque(true);
    setBackground(backGround);
    setLocation(0, 0);

    setSize(frameWidth, frameHeight);
    setLayout(new BorderLayout());

    beginGame = new JButton("BEGIN");
    beginGame.addActionListener(this);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "startaction");
    getActionMap().put("startaction", startAction);

    JPanel contentHolder = new JPanel();
    contentHolder.setOpaque(false);
    contentHolder.setPreferredSize(new Dimension((int) (getWidth()), (int) (getHeight() * (.90))));
    contentHolder.setLayout(new GridLayout(1,2));
    /*
     * Add components to the content holder
     */
    countrySelect = new CountrySelect();
    countryPreview = new CountryPreview();
    contentHolder.add(countrySelect);
    contentHolder.add(countryPreview);
    add(contentHolder, BorderLayout.NORTH);

    JPanel buttonCon = new JPanel();
    buttonCon.setPreferredSize(new Dimension((int) (frameWidth), (int) (frameHeight * (.10))));
    buttonCon.setOpaque(false);
    buttonCon.add(StartScreen.back);
    buttonCon.add(beginGame);
    selectedCountryLabel = new JLabel("United States of America");
    buttonCon.add(selectedCountryLabel);
    selectedCountryLabel.setVisible(false);
    add(buttonCon, BorderLayout.SOUTH);
  }

  /**
   * Overrides action performed.
   * Detects which button is clicked, gets the text and passes it along to buttonClicked
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    JButton tempBtn = (JButton) e.getSource();
    String name = tempBtn.getText();

    trigger.setPlayer(selectedCountry);
    StartScreen.buttonAction(name);
  }

  /**
   *
   * @return a GUIRegion country that the player selects
   */
  private GUIRegion fetchCountry(String name)
  {
    for (GUIRegion r : guiRegions)
    {
      if( r.getName() == name ) return r;
    }
    return null;
  }

  /**
   *  Panel to select what country you would like
   */
  private class CountrySelect extends JPanel implements MouseListener
  {
    public final EmptyBorder PADDING_BORDER = new EmptyBorder(2, 2, 2, 2);
    // Null color basically
    private final Color BORDER_COL = new Color(0.0f,0.0f,0.0f,0.0f);


    private CountrySelect()
    {
      super();
      setOpaque(false);
      setBackground(Color.RED);

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) ((frameWidth/2)*(.5));
      int height = (int) (frameHeight*(.75));
      int x = ((frameWidth/4)-(width/2));
      int y = (frameHeight-height)/4;
      int scrollW = 10;

      /*
       * Taking hacking of the positioning to a whole new level.
       */
      setBorder(new CompoundBorder(BorderFactory.createMatteBorder(y, x, frameHeight-(height+y),(frameWidth/2)-(width+x), BORDER_COL), PADDING_BORDER));

      guiRegions = WorldPresenter.getAllRegions();

      for (GUIRegion r : guiRegions) {
        if (r.getOfficialCountry()) {
          guiRegionsOfficial.add(r);
        }
      }
      
      int regionCount = guiRegionsOfficial.size();
      int labelH = (int) (FONT.getSize()*(1.2));
      int countryHeight = (int) (regionCount * (labelH) );
      JPanel countries = new JPanel();
      countries.setBackground(backGround);
      countries.setPreferredSize(new Dimension(width-scrollW, countryHeight));
      countries.setLayout(new BoxLayout(countries,BoxLayout.PAGE_AXIS));
      countries.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

      setFont(FONT);
      for (GUIRegion r : guiRegionsOfficial)
      {
        JLabel tempLabel = new JLabel(r.getName());
        tempLabel.setForeground(foreGroundColor);
        if (r.getName().equalsIgnoreCase("United States of America")) {
          selectedCountry = r;
        }
        tempLabel.addMouseListener(this);
        countries.add(tempLabel);
      }

      JScrollPane scrollPane = new JScrollPane(countries);
      scrollPane.setOpaque(false);
      scrollPane.setLocation(0, 0);
      //scrollPane.setBorder(BorderFactory.createEmptyBorder());
      scrollPane.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(5,5,5,5, Color.LIGHT_GRAY), PADDING_BORDER));
      scrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
      scrollPane.setPreferredSize(new Dimension(width, height));
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(scrollW, 10));
      scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(scrollW, 10));
      scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
      add(scrollPane );

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      /* Do nothing */
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      /* Do nothing */
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();
      String name = temp.getText();

      selectedCountry = fetchCountry(name);
      selectedCountryLabel.setText( selectedCountry.getName() );
      refreshDisplay();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();

      temp.setOpaque(true);
      temp.setForeground(Color.RED);
      temp.setBackground(Color.LIGHT_GRAY);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();

      temp.setOpaque(false);
      temp.setForeground(foreGroundColor);
      temp.setBackground(backGround );
    }

  }

  private void refreshDisplay()
  {
    left.statsRefresh();
    right.rightRefresh();
    miniMap.updateMiniDisplay();
  }

  /**
   * Information about the selected country that you have selected
   */
  private class CountryPreview extends JPanel
  {

    private CountryPreview()
    {
      super();
      setOpaque(false);
      setLayout(new GridLayout(2,1));

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) ((getWidth()/2)*(.5));
      int height = (int) (this.getHeight()*(.5));
      int x = ((getWidth()/2)-(width/2));
      int y = ((getWidth()/2)-(height/2));

      /*
       * Create a mini map view of the country for this area
       *
       * Add a constructor for something to this
       */
      miniMap = new MiniMapDisplay();
      add(miniMap);

      /*
       * Lower container Only for formatting
       */
      JPanel lowerCon = new JPanel();
      lowerCon.setOpaque(false);
      lowerCon.setLayout(new GridLayout(1,2));

      /*
       * Lower Left Container Panel to hold statistics on  your country/character that you choose
       *
       * Add a constructor for something to this
       */
      left = new LowerLeft();
      lowerCon.add( left );

      /*
       * Lower right Container Panel to hold pie chars and data on your country/character that you choose
       *
       * Add a constructor for something to this
       */
      right = new RightLeft();
      lowerCon.add( right );

      add(lowerCon);
      refreshDisplay();
    }
  }

  private class LowerLeft extends JPanel
  {
    private StatPane stats;

    /**
     * Constructor
     */
    private LowerLeft()
    {
      super();
      setOpaque(false);

      setLayout(new GridLayout(2, 1));
      // The dimensions are irrelevant layout overrides
      stats = new StatPane(selectedCountry.getName() + "'s Data:",frameWidth,frameHeight);
      stats.setOpaque(false);
      add(stats);

      statsRefresh();
    }

    private void statsRefresh()
    {
      //Stats Panel
      stats.clearBarPlots();
      displayStats(stats);
      stats.revalidate();
    }

    /**
     * Override paint
     * @param g graphics you wish to have
     */
    @Override
    public void paint(Graphics g)
    {
      super.paint(g);
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

      statPane.setTitle(selectedCountry.getName() + "'s Data:" );

      if(selectedCountry != null )
      {
        CountryData cd = selectedCountry.getCountryData();
        population = cd.getPopulation(metricUnits);
        medianAge = cd.getMedianAge(metricUnits);
        birthRate = cd.getBirthRate(metricUnits);
        mortality = cd.getMortality(metricUnits);
        migration = cd.getMigration(metricUnits);
        undernourish = cd.getUndernourish(metricUnits);
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
     * Returns the current DisplayConverter object.
     */
    public DisplayUnitConverter getConverter()
    {
      return converter;
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
  }

  private class RightLeft extends JPanel
  {
    private JPanel pieHolderTop;
    private JPanel pieHolderBottom;


    /**
     * Constructor
     */
    private RightLeft()
    {
      super();
      setOpaque(false);

      setLayout(new GridLayout(2,1));

      pieHolderTop = new JPanel();
      pieHolderTop.setOpaque(false);
      pieHolderTop.setLayout(new GridLayout(1,2));

      pieHolderBottom = new JPanel();
      pieHolderBottom.setOpaque(false);
      pieHolderBottom.setLayout(new GridLayout(1,2));

      add(pieHolderBottom);
      add(pieHolderTop);

      rightRefresh();
    }


    /**
     * Override paint
     * @param g graphics you wish to have
     */
    @Override
    public void paint(Graphics g)
    {
      super.paint(g);
    }


    private void rightRefresh()
    {
      //Crops Data
      pieHolderTop.removeAll();
      pieHolderBottom.removeAll();

      displayCrops();
      displayLand();

      repaint();
    }

    /**
     * Controls the presentation logic for displaying the crop data
     * in the info panel for the specified region(s).
     *
     */
    private void displayCrops()
    {

      double cornTotal = 0;
      double wheatTotal = 0;
      double riceTotal = 0;
      double soyTotal = 0;
      double otherTotal = 0;


      if( selectedCountry != null  )
      {
        CountryData cd = selectedCountry.getCountryData();

        cornTotal = cd.getCornTotal(metricUnits);
        wheatTotal = cd.getWheatTotal(metricUnits);
        riceTotal = cd.getRiceTotal(metricUnits);
        soyTotal = cd.getSoyTotal(metricUnits);
        otherTotal = cd.getOtherTotal(metricUnits);

      }

      // Update the slice information
      cropSlices[0].updateSlice(cornTotal, new Color(235,235,51,100), "Corn");
      cropSlices[1].updateSlice(wheatTotal, new Color(255,153,0,100),  "Wheat" );
      cropSlices[2].updateSlice(riceTotal, new Color(230,230,230,100), "Rice" );
      cropSlices[3].updateSlice(soyTotal, new Color(194,163,133,100), "Soy");
      cropSlices[4].updateSlice(otherTotal, new Color(255,102,255,100), "Other");

      cropArray.clear();
      for( int i = 0; i < cropSlices.length ; i++)
      {
        cropArray.add(cropSlices[i]);
      }

      int chartWidth = frameWidth/8 ;
      Rectangle landRect = new Rectangle(0,0,chartWidth,chartWidth);
      Rectangle keyRect = new Rectangle(0,0,chartWidth,chartWidth);

      pieHolderTop.add(new PieChart(landRect, cropArray));
      pieHolderTop.add(new ChartKey(keyRect, cropArray));
    }

    /**
     * Controls the presentation logic for displaying the data on the Land
     * in the info panel for the specified region(s).
     *
     */
    private void displayLand()
    {
      double organic = 0;
      double conventional = 0;
      double gmo = 0;


      if( selectedCountry != null) {
        CountryData cd = selectedCountry.getCountryData();

        organic = cd.getOrganic(metricUnits);
        conventional = cd.getConventional(metricUnits);
        gmo = cd.getGmo(metricUnits);
      }

      // Update the slice information
      landSlices[0].updateSlice(organic*100, new Color(102,255,51,100), "Organic");
      landSlices[1].updateSlice(conventional*100, new Color(153,102,51,100),  "Conventional" );
      landSlices[2].updateSlice(gmo*100, new Color(255,80,80,100), "GMO" );

      landArray.clear();
      for( int i = 0; i < landSlices.length ; i++)
      {
        landArray.add(landSlices[i]);
      }

      int chartWidth = frameWidth/8 ;
      Rectangle landRect = new Rectangle(0,0,chartWidth,chartWidth);
      Rectangle keyRect = new Rectangle(0,0,chartWidth,chartWidth);

      pieHolderBottom.add(new PieChart(landRect, landArray));
      pieHolderBottom.add(new ChartKey(keyRect, landArray));
    }
  }

  private class MiniMapDisplay extends JPanel
  {
    private MiniViewBox miniViewBox;

    private MiniMapDisplay()
    {
      super();
      setOpaque(false);
      setLayout(new GridLayout(1,1));

      miniViewBox = new MiniViewBox(" ",frameWidth, frameHeight);

      miniViewBox.setPreferredSize(new Dimension(frameWidth/2, (frameHeight / 2)));
      this.add(miniViewBox);
    }

    private void updateMiniDisplay()
    {
      ArrayList<GUIRegion> grListTemp = new ArrayList<>();
      grListTemp.add(selectedCountry);
      miniViewBox.setTitle(selectedCountry.getName());
      miniViewBox.setAlph(0.0f);
      miniViewBox.setDrawableRegions(grListTemp);
      miniViewBox.getRegionView();
    }
  }
}