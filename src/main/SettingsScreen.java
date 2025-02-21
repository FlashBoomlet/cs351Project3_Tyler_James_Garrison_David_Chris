package main;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.hud.InfoPanel;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;

/**
 * SettingsScreen creates a settings "pop up" display screen where the user can dominate the world
 * from! It handles everything settings for the user in a nice clean format so that they don't have
 * to memorize a bunch of settings.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/19/15
 */
public class SettingsScreen extends JPanel
{
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  private final int SCALE_IN_FACTOR = 32;
  private JButton close;
  private JButton reset;
  private TabbedPanel center;
  private SettingsButtonPanel buttons;
  private algorithmInfluencePanel panelSimulator;
  private GeneralSettings generalSettings;
  private TradeInfluencePanel tradeInfluencePanel;
  private WorldPresenter worldPresenter;
  private Color fontColor = ColorsAndFonts.REGION_NAME_FONT_C;

  // J-Radio buttons
  private JRadioButton selectAll;
  private static JRadioButton lowestEvent;
  private static JRadioButton mediumEvent;
  private static JRadioButton highestEvent;
  private static JRadioButton lowestAccuracy;
  private static JRadioButton mediumAccuracy;
  private static JRadioButton highestAccuracy;
  private static JRadioButton units;

  /**
   * Initializes a JPanel for all of the settings and adds the various components to it
   * This is the framework of the settings basically.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param frameWidth main frame's width
   * @param frameHeight main frame's height
   */
  public SettingsScreen(int frameWidth, int frameHeight, WorldPresenter worldPresenter)
  {
    super();
    x = frameWidth/SCALE_IN_FACTOR;
    y = frameHeight/SCALE_IN_FACTOR;
    width = frameWidth-(x*4);
    height = frameHeight-(y*5);
    this.worldPresenter = worldPresenter;

    setLayout(new BorderLayout(0,0));
    y*=3;
    x*=2;
    setLocation(x,y);
    setSize(new Dimension(width, height));
    setOpaque(true);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    buttons = new SettingsButtonPanel();
    add(buttons, BorderLayout.PAGE_END);

    center = new TabbedPanel();
    add(center, BorderLayout.CENTER);
  }

  /**
   * SettingsCenterPanel creates guts to the settings TabbedPane.
   * <Tabbed panel
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/20/15
   */
  private class TabbedPanel extends JTabbedPane
  {
    private int xC;
    private int yC;
    private int widthC;
    private int heightC;

    /**
     * Initializes a JPanel for all of the settings and adds the various components to it
     * This is the heart of the settings basically.
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     */
    TabbedPanel()
    {
      super();

      setLocation(getX(), getY());
      setSize(new Dimension(getWidth(), getHeight()));
      setOpaque(false);

      JPanel container = new JPanel();
      container.setOpaque(true);
      container.setBackground(new Color(0xA9A9A9));

      panelSimulator = new algorithmInfluencePanel();
      generalSettings = new GeneralSettings();
      tradeInfluencePanel = new TradeInfluencePanel();
      container.add(panelSimulator);
      container.add(generalSettings);
      container.add(tradeInfluencePanel);

      JPanel DefinitionsPanel = new JPanel();
      DefinitionsPanel.setOpaque(false);

      addTab("World influences", null, container, "Influence Simulator");
      addTab("Definitions", null, DefinitionsPanel, "Does nothing");
    }
  }

  /**
   * SettingsButtonPanel creates guts to the button panel at the bottom of the screen.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/20/15
   */
  private class SettingsButtonPanel extends JPanel implements ActionListener
  {
    /**
     * Initializes a JPanel for all of the buttons in the bottom of the screen and formats them
     * This is the heart of the buttons basically.
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     */
    SettingsButtonPanel()
    {
      super();
      setLayout(new GridLayout(1, 5));
      setOpaque(false);
      close = new JButton("Close");
      close.setName("CLOSE");
      close.addActionListener(this);

      reset = new JButton("Reset");
      reset.setName("RESET");
      reset.addActionListener(this);

      for( int i = 0; i <3; i++)
      {
        JPanel tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        add(tempPanel);
      }
      add(reset);
      add(close);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JButton tempBtn = (JButton) e.getSource();
      String name = tempBtn.getName();

      if( name == "CLOSE" )
      {
        hideEverything();
        Game.settingsDisplay(false);
      }
      else if( name == "RESET" )
      {
        main.Game.reset();
        hideEverything();
        Game.settingsDisplay(false);
      }
    }
  }

  public static double getRandomizeBounds()
  {
    if( lowestEvent.isSelected() ) return 0.0;
    else if( mediumEvent.isSelected() ) return 0.5;
    else return 1.0;
  }
  private class algorithmInfluencePanel extends JPanel implements ActionListener
  {
    algorithmInfluencePanel()
    {
      super();
      setOpaque(false);
      setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
      setForeground(fontColor);

      lowestEvent = new JRadioButton("Low Trading Accuracy");
      lowestEvent.setForeground(fontColor);
      lowestEvent.setName("LTA");
      lowestEvent.addActionListener(this);

      mediumEvent = new JRadioButton("Normal Trading Accuracy");
      mediumEvent.setForeground(fontColor);
      mediumEvent.setName("MTA");
      mediumEvent.addActionListener(this);

      highestEvent = new JRadioButton("High Trading Accuracy");
      highestEvent.setForeground(fontColor);
      highestEvent.setName("HTA");
      highestEvent.addActionListener(this);

      add(lowestEvent);
      add(mediumEvent);
      add(highestEvent);

      lowestEvent.setSelected(true);
      mediumEvent.setSelected(false);
      highestEvent.setSelected(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JRadioButton tempBtn = (JRadioButton) e.getSource();
      String name = tempBtn.getName();

      if( name == "ALL" )
      {
        if( !tempBtn.isSelected() )
        {
          lowestEvent.setSelected(false);
          mediumEvent.setSelected(false);
          highestEvent.setSelected(false);
        }
        else
        {
          lowestEvent.setSelected(true);
          mediumEvent.setSelected(true);
          highestEvent.setSelected(true);
        }

      }
      else if( name == "LTA" )
      {
        lowestEvent.setSelected(true);
        mediumEvent.setSelected(false);
        highestEvent.setSelected(false);
      }
      else if( name == "MTA" )
      {
        lowestEvent.setSelected(false);
        mediumEvent.setSelected(true);
        highestEvent.setSelected(false);
      }
      else if( name == "HTA" )
      {
        lowestEvent.setSelected(false);
        mediumEvent.setSelected(false);
        highestEvent.setSelected(true);
      }
      for( GUIRegion gr : worldPresenter.getAllRegions())
      {
        if( gr.getCountryData() != null ) gr.getCountryData().randomizePercent();
      }
    }
  }


  public static int getTradeInfluence()
  {
    if( highestAccuracy.isSelected() ) return 2;
    else if( mediumAccuracy.isSelected() ) return 1;
    return 0;
  }

  private class TradeInfluencePanel extends JPanel implements ActionListener
  {
    TradeInfluencePanel()
    {
      super();
      setOpaque(false);
      setLayout(new BoxLayout(this,BoxLayout.PAGE_AXIS));
      setForeground(fontColor);

      selectAll = new JRadioButton("Select All");
      selectAll.setForeground(fontColor);
      selectAll.setName("ALL");
      selectAll.addActionListener(this);

      lowestAccuracy = new JRadioButton("Low Trading Accuracy");
      lowestAccuracy.setForeground(fontColor);
      lowestAccuracy.setName("LTA");
      lowestAccuracy.addActionListener(this);

      mediumAccuracy = new JRadioButton("Normal Trading Accuracy");
      mediumAccuracy.setForeground(fontColor);
      mediumAccuracy.setName("MTA");
      mediumAccuracy.addActionListener(this);

      highestAccuracy = new JRadioButton("High Trading Accuracy");
      highestAccuracy.setForeground(fontColor);
      highestAccuracy.setName("HTA");
      highestAccuracy.addActionListener(this);

      add(lowestAccuracy);
      add(mediumAccuracy);
      add(highestAccuracy);
      add(selectAll);
      selectAll.setVisible(false);

      lowestAccuracy.setSelected(true);
      mediumAccuracy.setSelected(false);
      highestAccuracy.setSelected(false);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JRadioButton tempBtn = (JRadioButton) e.getSource();
      String name = tempBtn.getName();

      if( name == "ALL" )
      {
        if( !tempBtn.isSelected() )
        {
          lowestAccuracy.setSelected(false);
          mediumAccuracy.setSelected(false);
          highestAccuracy.setSelected(false);
        }
        else
        {
          lowestAccuracy.setSelected(true);
          mediumAccuracy.setSelected(true);
          highestAccuracy.setSelected(true);
        }

      }
      else if( name == "LTA" )
      {
        lowestAccuracy.setSelected(true);
        mediumAccuracy.setSelected(false);
        highestAccuracy.setSelected(false);
        selectAll.setSelected(false);
      }
      else if( name == "MTA" )
      {
        lowestAccuracy.setSelected(false);
        mediumAccuracy.setSelected(true);
        highestAccuracy.setSelected(false);
        selectAll.setSelected(false);
      }
      else if( name == "HTA" )
      {
        lowestAccuracy.setSelected(false);
        mediumAccuracy.setSelected(false);
        highestAccuracy.setSelected(true);
        selectAll.setSelected(false);
      }
      for( GUIRegion gr : worldPresenter.getAllRegions())
      {
        if( gr.getCountryData() != null ) gr.getCountryData().randomizePercent();
      }
    }
  }







  public static void updateUnits(boolean show)
  {
    units.setSelected(show);
  }

  public static boolean getUnits()
  {
    return units.isSelected();
  }

  private class GeneralSettings extends JPanel implements ActionListener
  {

    GeneralSettings()
    {
      super();
      setOpaque(false);
      setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
      setForeground(fontColor);

      units = new JRadioButton("Metric Units");
      units.setForeground(fontColor);
      units.setName("UNIT");
      units.setSelected(true);
      units.addActionListener(this);

      add(units);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JRadioButton tempBtn = (JRadioButton) e.getSource();
      String name = tempBtn.getName();

      if( name == "UNIT" )
      {
        if( !tempBtn.isSelected() )
        {
          // Un-select
          units.setSelected(false);
          InfoPanel.setMetricUnits(false);
        }
        else
        {
          // Select
          units.setSelected(true);
          InfoPanel.setMetricUnits(true);
        }

      }
      else
      {
        //Do nothing
      }
    }
  }

  public void hideEverything()
  {
    close.setVisible(false);
    reset.setVisible(false);

    center.setVisible(false);
    buttons.setVisible(false);
    this.setVisible(false);
  }

  public void showEverything()
  {
    close.setVisible(true);
    reset.setVisible(true);

    center.setVisible(true);
    buttons.setVisible(true);
    this.setVisible(true);
  }

}
