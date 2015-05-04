package main;

import IO.AreaXMLLoader;
import IO.CountryCSVParser;
import gui.*;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import gui.hud.*;
import gui.hud.Ticker.Ticker;
import main.StartScreen.StartScreen;
import model.Region;
import model.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import javax.swing.JLayeredPane;

/**
 * Main entry point for the 'game'. Handles loading data and all configurations.
 * @author david winston
 *         created: 2015-02-04
 *
 * Heavily modified and restructured by
 * @author Tyler Lynch <lyncht@unm.edu>
 */
public class Game
{
  /*
   * Frame variables
   */
  public static final String MODEL_DATA_PATH = "resources/ne_10m_admin_1_states_provinces.kml";
  public static final String BG_DATA_PATH = "resources/countries_world.xml";
  public static int frameWidth = 1200;
  public static int frameHeight = 700;
  int tickerHeight;
  final float NAV_HEIGHT_SCALE = (float) .25;
  final float NAV_WIDTH_SCALE = (float) 1.6;
  private final static int DEFAULT_TIME_SPEED = 2000;
  private int feedPanelHeight;
  private static Collection<Region> xmlRegions = new LinkedList<>();
  public static Collection<Region> getXmlRegions()
  {
    return xmlRegions;
  }

  /*
   * Frame panels
   */
  private static MapPane mapPane;
  public static InfoPanel infoPanel;
  public static PlayerCountryInfo playerCountryInfo;
  private static NavMap navMap;
  private static Ticker ticker;
  private static WorldFeedPanel worldFeedPanel;
  public static WorldFeedPanel getWorldFeedPanel() { return worldFeedPanel; }
  private static ButtonPanel buttonPanel;
  private static StartScreen startPanel;
  private static FinishScreen finishPanel;
  private static SettingsScreen settingsScreen;
  private static JPanel defaultScreen;
  private static MapScale mapScale;
  private static Camera cam;
  private static Notification notification;
  private static Trigger trigger;
  public static Camera getCamera()
  {
    return cam;
  }
  public static void repaintMapScale()
  {
    mapScale.updateScale();
    mapScale.repaint();
  }

  /*
   * Frame components
   */
  private static WorldPresenter worldPresenter;
  public static WorldPresenter getWorldPresenter() { return worldPresenter; }
  public static Timer worldTime;
  public static Timer gameLoop;
  public static JFrame frame;
  public static World world;
  private static CardSelector policySelector;
  private static TreatySelector treatySelector;

  /*
   * users selected country
   */
  public static GUIRegion userCountry;

  /*
   * Stuff for the startUp image
   */
  private BufferedImage image;
  static final String IMAGE_PATH = "resources/images/Starvation_Evasion.png";

  /**
   * Constructor for game, handles all init logic.
   */
  public Game()
  {
    try {
      // Uses System setting for the look of the game
      //Go to http://docs.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html#steps for more info

      UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());

      // Sets the look of the game to "Metal", the normal look for windows
      //UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");

      //GTK theme
      // UIManager.setLookAndFeel("com.sun.java.swing.plaf.gtk.GTKLookAndFeel");
    }
    catch (UnsupportedLookAndFeelException e) {
      // handle exception
    }
    catch (ClassNotFoundException e) {
      // handle exception
    }
    catch (InstantiationException e) {
      // handle exception
    }
    catch (IllegalAccessException e) {
      // handle exception
    }

    frame = new JFrame();
    Dimension SCREEN_SIZE = Toolkit.getDefaultToolkit().getScreenSize();
    int SCREEN_WIDTH = (int) SCREEN_SIZE.getWidth();
    int SCREEN_HEIGHT = (int) SCREEN_SIZE.getHeight();

    if( frameWidth > SCREEN_WIDTH || frameHeight > SCREEN_HEIGHT )
    {
      frameWidth = (int) Math.floor(SCREEN_WIDTH*.93);
      frameHeight = (int) Math.floor(frameWidth*.50);
    }

    frame.setTitle("STARVATION EVASION");
    frame.setPreferredSize(new Dimension(frameWidth, frameHeight) );
    frame.setSize(frame.getPreferredSize());
    frame.setLocation(0,0);
    frame.setBackground(ColorsAndFonts.OCEANS);
    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    init();
  }

  /**
   * This function now calls to create all components and panels used by the frame and game
   * It then initializes the frame with all of this and sets up the controls after
   *
   * Modified heavily last by
   * @author Tyler Lynch <lyncht@unm.edu>
   */
  private void init()
  {
    //Load the startSceen Image so that the frame doesn't populate white for some reason
    try
    {
      image = ImageIO.read(new File(IMAGE_PATH));
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find Start image!");
    }

    new leParseStuff();

  }

  private class leParseStuff extends Thread
  {
    private Thread t;
    leParseStuff()
    {
      this.start();
    }

    @Override
    public void run()
    {
      try
      {
        tickerHeight = (int) (frameHeight*(.10));
        feedPanelHeight = (int) (frameHeight/25);
        final int NAV_WIDTH = (int) Math.floor(frameWidth/4);
        final int NAV_HEIGHT = (int) Math.floor(NAV_WIDTH/2);
        final int NAV_X = frameWidth-NAV_WIDTH;
        final int NAV_Y = frameHeight-NAV_HEIGHT;
        trigger = new Trigger();

        //Do stuff here
        xmlRegions = new AreaXMLLoader().getRegions();

        finishPanel = new FinishScreen(frameWidth,frameHeight);

        defaultScreen = new JPanel();
        defaultScreen.setBounds(0, 0, frameWidth, frameHeight);
        defaultScreen.setBackground(ColorsAndFonts.OCEANS);

        List<Region> allRegions = new ArrayList<>(xmlRegions);
        allRegions.addAll(xmlRegions);

        world = new World(allRegions);
        MapConverter converter = new EquirectangularConverter();

        worldPresenter = new WorldPresenter(converter, world);
        worldPresenter.setBackgroundRegions(xmlRegions);

        settingsScreen = new SettingsScreen(frameWidth,frameHeight,worldPresenter);
        new CountryCSVParser( worldPresenter.getAllRegions() );
        world.setAllFirstCrops();
        world.setPresenter(worldPresenter);

        cam = new Camera(converter);
        Dimension dim = new Dimension(frameWidth,(int) (frameHeight-feedPanelHeight) );
        mapPane = new MapPane(cam, worldPresenter,dim,feedPanelHeight);

        userCountry = worldPresenter.getSingleRegion("United States of America");

        worldFeedPanel = new WorldFeedPanel(worldPresenter,frameWidth,feedPanelHeight,trigger);
        worldPresenter.addObserver(worldFeedPanel);

        infoPanel = new InfoPanel(frameWidth/(6),(int) (frameHeight-feedPanelHeight-tickerHeight),(int) (feedPanelHeight));
        infoPanel.setPresenter(worldPresenter);

        playerCountryInfo = new PlayerCountryInfo(userCountry,frameWidth-15,feedPanelHeight,300,400);

        // Card Selectors
        policySelector = new CardSelector(275,75,600,500,"POLICY",trigger);
        infoPanel.setCardSelector(policySelector, "POLICY");
        treatySelector = new TreatySelector(new Rectangle(275,75,600,500),"TREATY REQUEST",trigger);
        infoPanel.setTreatySelector(treatySelector, "POLICY");

        navMap = new NavMap(NAV_X, NAV_Y, NAV_WIDTH, NAV_HEIGHT, frameWidth,frameHeight,cam, worldPresenter);

        ticker = new Ticker(0,frameHeight-tickerHeight-20,NAV_X,tickerHeight);

        buttonPanel = new ButtonPanel(NAV_Y,frameWidth,NAV_WIDTH);

        mapScale = new MapScale(NAV_X,NAV_Y-20,150,20,cam);

        int alertW = 150;
        int alertH = (int) ((ticker.getY()-mapScale.getY())*(.75));
        int alertX = mapScale.getX()-mapScale.getHeight()-alertW;
        Rectangle tempRect = new Rectangle(alertX,mapScale.getY(),alertW,alertH);
        notification =  new Notification(tempRect);

        startPanel = new StartScreen(frameWidth,frameHeight,image,trigger);
        trigger.setPlayCountryInfo(playerCountryInfo);

        initFrame();
        setupControlls();

        this.interrupt();
      }
      catch (Exception e) {}
    }
    /**
     * start overrides Thread's start
     * Creates new Thread and then starts said Thread
     */
    public void start()
    {
      if (t == null)
      {
        t = new Thread(this, (String) getName());
        t.start ();
      }
    }
  }

  /**
   * sets the main game container to visible.
   * Called by main should everything be set up properly
   */
  public void show()
  {
    frame.setVisible(true);
  }

  /**
   * Starts the game timers.
   */
  public static void start()
  {
    gameLoop.start();
    worldTime.start();
  }

  /**
   * pauses the game.
   */
  public static void pause()
  {
    gameLoop.stop();
    worldTime.stop();
  }

  /**
   * Reset the game
   */
  public static void reset()
  {
    gameLoop.restart();
    worldTime.restart();
    worldPresenter.resetWorldDate();
    worldFeedPanel.resetDate();
  }

  /**
   * @return true if the game loop is running.
   */
  public boolean isRunning()
  {
    return gameLoop.isRunning();
  }

  /**
   * init and configures the timers and key bindings for the main game controls.
   */
  private void setupControlls()
  {
    worldTime = new Timer(DEFAULT_TIME_SPEED, new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldPresenter.setWorldForward(1);
        worldFeedPanel.setDate(worldPresenter.getWorldDate());
      }
    });

    gameLoop = new Timer(30, new ActionListener()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        mapPane.repaint();   // for graphics
        mapPane.update();    // for controls
        infoPanel.repaint(); // again for graphics.
      }
    });


    InputMap inputMap = mapPane.getInputMap();
    ActionMap actionMap = mapPane.getActionMap();


    inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pause");
    actionMap.put("pause", new AbstractAction() {
              @Override
              public void actionPerformed(ActionEvent e) {
                if (isRunning()) pause();
                else start();
              }
            }
    );
    GameplayControl.updateDisplaySpeed();
  }

  /**
   * Handles constructing the frame and adding all the gui components in
   * their proper places.
   */
  private void initFrame() {
    frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

    frame.setResizable(false);
    frame.setPreferredSize(new Dimension(frameWidth, frameHeight));
    frame.setContentPane(new CenterPanel(mapPane, infoPanel, worldFeedPanel, playerCountryInfo));

    frame.addKeyListener(mapPane);
    // For future implementation should I choose to implement keyListener -TLynch 4.32.15
    //frame.addKeyListener(policySelector);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Main. Initializes and starts the game.
   * It also shows the game should everything start properly without error.
   *
   * @param args
   */
  public static void main(String[] args)
  {
    Game gameManager = new Game();
    gameManager.show();
  }

  /**
   * Creates the content panel for the JPanels to be added to in a layered fashion
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/17/15
   */
  class CenterPanel extends JPanel
  {
    CenterPanel(MapPane mapPane,InfoPanel infoPanel,WorldFeedPanel worldFeedPanel,PlayerCountryInfo playerCountryInfo)
    {
      super();
      JLayeredPane layeredPane = frame.getLayeredPane();

      // Add Start Screen
      layeredPane.add(startPanel, new Integer(99));

      // Add Start Screen
      layeredPane.add(finishPanel, new Integer(101) );
      finishPanel.setVisible(false);

      // Add Settings Screen
      layeredPane.add(settingsScreen, new Integer(100) );

      layeredPane.add(defaultScreen, new Integer(0));

      mapPane.setBounds(0, 0, frameWidth, frameHeight);
      layeredPane.add(mapPane, new Integer(1));

      worldFeedPanel.setBounds(0, 0, frameWidth, feedPanelHeight);
      layeredPane.add(worldFeedPanel, new Integer(2));

      // Side panel with all information
      infoPanel.setBounds(0,feedPanelHeight,frameWidth / 6, frameHeight - feedPanelHeight-tickerHeight);
      layeredPane.add(infoPanel, new Integer(6)) ;
      infoPanel.setVisible(false);

      layeredPane.add(playerCountryInfo, new Integer(6));


      // Navigation in the lower right hand corner
      layeredPane.add(navMap, new Integer(3));

      // Button panel in the lower right hand corner
      layeredPane.add(buttonPanel, new Integer(4) );

      // Map Scale in the lower right hand corner above the Nav Map
      layeredPane.add(mapScale, new Integer(5) );

      // Ticker at the bottom of the screen
      layeredPane.add(ticker, new Integer(7) );

      // Card selectors for Game play
      layeredPane.add(policySelector, new Integer(8) );
      layeredPane.add(treatySelector, new Integer(8) );

      layeredPane.add(notification, new Integer(9) );

      trigger.setupNotifications(notification);
      trigger.setupTicker(ticker);

      pauseGame();
      repaint();
    }
  }

  /**
   * Called by the startScreen to begin the game.
   * Hides the settings and the start screen and shows all others.
   *
   * WARNING! This does not actually start the game timer(s)
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   */
  public static void startGame()
  {
    mapPane.setVisible(true);
    worldFeedPanel.showAll(true);
    navMap.setVisible(true);
    buttonPanel.setVisible(true);
    startPanel.setVisible(false);
    settingsScreen.setVisible(false);
    mapScale.setVisible(true);
    notification.setVisible(true);
    ticker.setVisible(true);

  }

  /**
   * Called by World to signify that the simulation is done
   *
   * WARNING! This does end the game. No going back once it is called
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   */
  public static void gameFinished()
  {
    pauseGame();
    startPanel.setVisible(false);
    pause();
    finishPanel.setVisible(true);
  }

  /**
   * Called by the buttonPanel to pause the game.
   * Shows the startScreen and hides all others.
   *
   * WARNING! This does not actually pause the game timer(s)
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   */
  public static void pauseGame()
  {
    mapPane.setVisible(false);
    worldFeedPanel.showAll(false);
    infoPanel.hidePanel();
    navMap.setVisible(false);
    buttonPanel.setVisible(false);
    startPanel.setVisible(true);
    settingsScreen.setVisible(false);
    mapScale.setVisible(false);
    notification.setVisible(false);
    finishPanel.setVisible(false);
    ticker.setVisible(false);
  }

  /**
   * A function to pause and start the game after the settings panel is invoked or
   * the user is done messing with the settings. If invoked then it hides the map and
   * infoPanel so that their action listeners are not invoked by accident
   *
   * WARNING! This does toggle the game timer(s)
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @param display a boolean if you want to show the settings or not
   */
  public static void settingsDisplay(boolean display)
  {
    if( display ) {
      pause();
      pauseGame();
      settingsScreen.showEverything();
      settingsScreen.setVisible(true);
      startPanel.setVisible(false);
    }
    else
    {
      settingsScreen.hideEverything();
      settingsScreen.setVisible(false);
      start();
      startGame();
    }
  }
}

