package main;

import IO.AreaXMLLoader;
import IO.CountryCSVParser;
import IO.WorldDataParser;
import gui.*;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import gui.hud.*;
import model.Region;
import model.World;
import IO.AttributeGenerator;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
  public static final String PRECIP_DATA = "resources/data/precip2010.txt";
  int frameWidth = 1200;
  int frameHeight = 700;
  final float NAV_HEIGHT_SCALE = (float) .25;
  final float NAV_WIDTH_SCALE = (float) 1.6;
  private final static int DEFAULT_TIME_SPEED = 2000;
  private int feedPanelHeight;
  private Collection<Region> xmlRegions = new LinkedList<>();

  /*
   * Frame panels
   */
  private static MapPane mapPane;
  private static InfoPanel infoPanel;
  private static NavMap navMap;
  private static WorldFeedPanel worldFeedPanel;
  private static ButtonPanel buttonPanel;
  private static StartScreen startPanel;
  private static SettingsScreen settingsScreen;
  private static JPanel defaultScreen;

  /*
   * Frame components
   */
  private static WorldPresenter worldPresenter;
  public static Timer worldTime;
  public static Timer gameLoop;
  public static JFrame frame;

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

    frame.setTitle("GAME");
    frame.setPreferredSize(new Dimension(frameWidth, frameHeight) );
    frame.setSize(frame.getPreferredSize());
    frame.setLocation(0,0);
    frame.setBackground(ColorsAndFonts.OCEANS);

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
    xmlRegions = new AreaXMLLoader().getRegions();

    startPanel = new StartScreen(frameWidth,frameHeight);

    settingsScreen = new SettingsScreen(frameWidth,frameHeight);

    defaultScreen = new JPanel();
    defaultScreen.setBounds(0,0,frameWidth,frameHeight);
    defaultScreen.setBackground(ColorsAndFonts.OCEANS);

    Random random = new Random(234);
    AttributeGenerator randoAtts = new AttributeGenerator();

    Collection<Region> background = initBackgroundRegions(random, randoAtts);
    Collection<Region> modelRegions = initModelRegions(random, randoAtts);

    List<Region> allRegions = new ArrayList<>(modelRegions);
    allRegions.addAll(background);
    World world = new World(allRegions);

    MapConverter converter = new EquirectangularConverter();
    WorldDataParser globalData = new WorldDataParser(world);

    worldPresenter = new WorldPresenter(converter, world);
    worldPresenter.setBackgroundRegions(background);
    worldPresenter.setModelRegions(modelRegions);

    feedPanelHeight = (int) (frameHeight/25);
    Camera cam = new Camera(converter);
    Dimension dim = new Dimension(frameWidth,(int) (frameHeight-feedPanelHeight) );
    mapPane = new MapPane(cam, worldPresenter,dim,feedPanelHeight);

    worldFeedPanel = new WorldFeedPanel(worldPresenter,frameWidth,feedPanelHeight);
    worldPresenter.addObserver(worldFeedPanel);

    infoPanel = new InfoPanel(frameWidth/(6),(frameHeight-feedPanelHeight),feedPanelHeight);
    infoPanel.setPresenter(worldPresenter);

    final int NAV_WIDTH = (int) Math.floor(frameWidth/4);
    final int NAV_HEIGHT = (int) Math.floor(NAV_WIDTH/2);
    final int NAV_X = frameWidth-NAV_WIDTH;
    final int NAV_Y = frameHeight-NAV_HEIGHT;
    navMap = new NavMap(NAV_X, NAV_Y, NAV_WIDTH, NAV_HEIGHT, frameWidth,frameHeight,cam, worldPresenter);

    buttonPanel = new ButtonPanel(NAV_Y,frameWidth,NAV_WIDTH);

    new CountryCSVParser(allRegions);
    initFrame();
    setupControlls();
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

    gameLoop = new Timer(20, new ActionListener()
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

//    inputMap.put(KeyStroke.getKeyStroke("8"), "defaultTime");
//    actionMap.put("defaultTime", new AbstractAction()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        worldTime.setDelay(DEFAULT_TIME_SPEED);
//      }
//    });
//
//    inputMap.put(KeyStroke.getKeyStroke("9"), "faster");
//    actionMap.put("faster", new AbstractAction()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        worldTime.setDelay(DEFAULT_TIME_SPEED / 3);
//      }
//    });
//
//    inputMap.put(KeyStroke.getKeyStroke("0"), "superfast");
//    actionMap.put("superfast", new AbstractAction()
//    {
//      @Override
//      public void actionPerformed(ActionEvent e)
//      {
//        worldTime.setDelay(DEFAULT_TIME_SPEED / 6);
//      }
//    });

    inputMap.put(KeyStroke.getKeyStroke("SPACE"), "pause");
    actionMap.put("pause", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        if (isRunning()) pause();
        else start();
      }
    });
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
    frame.setContentPane(new CenterPanel(mapPane, infoPanel, worldFeedPanel));

    frame.addKeyListener(mapPane);
    frame.pack();
    frame.setVisible(true);
  }

  /**
   * Loads all the model regions and sets their attributes according to the
   * given attribute generator.
   */
  private Collection<Region> initModelRegions(Random random,
                                              AttributeGenerator randoAtts)
  {
    Collection<Region> modelMap = new LinkedList<>();
    // adds XML regions for area folder...
    modelMap.addAll(xmlRegions);

    for (Region r : modelMap)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return modelMap;
  }

  /**
   * Loads and inits attributes for all background regions.
   */
  private Collection<Region> initBackgroundRegions(Random random,
                                                   AttributeGenerator randoAtts)
  {
    Collection<Region> BGRegions = new LinkedList<>();
    BGRegions.addAll(xmlRegions);
    for (Region r : BGRegions)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return BGRegions;
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
    CenterPanel(MapPane mapPane,InfoPanel infoPanel,WorldFeedPanel worldFeedPanel)
    {
      super();
      JLayeredPane layeredPane = frame.getLayeredPane();

      // Add Start Screen
      layeredPane.add(startPanel, new Integer(99) );

      // Add Settings Screen
      layeredPane.add(settingsScreen, new Integer(100) );

      layeredPane.add(defaultScreen, new Integer(0));

      mapPane.setBounds(0,0,frameWidth,frameHeight);
      layeredPane.add(mapPane, new Integer(1));

      worldFeedPanel.setBounds(0,0,frameWidth,feedPanelHeight);
      layeredPane.add(worldFeedPanel, new Integer(2));

      // Side panel with all information
      infoPanel.setBounds(0,feedPanelHeight,frameWidth/6,frameHeight-feedPanelHeight);
      layeredPane.add(infoPanel, new Integer(3)) ;
      infoPanel.setVisible(false);

      // Navigation in the lower right hand corner
      layeredPane.add(navMap, new Integer(3) );

      // Button panel in the lower right hand corner
      layeredPane.add(buttonPanel, new Integer(4) );

      pauseGame();
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
    infoPanel.setVisible(false);
    navMap.setVisible(false);
    buttonPanel.setVisible(false);
    startPanel.setVisible(true);
    settingsScreen.setVisible(false);
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

