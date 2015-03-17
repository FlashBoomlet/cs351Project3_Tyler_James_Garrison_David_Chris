import IO.AreaXMLLoader;
import IO.WorldDataParser;
import IO.XMLparsers.KMLParser;
import gui.*;
import gui.displayconverters.EquirectangularConverter;
import gui.displayconverters.MapConverter;
import gui.hud.InfoPanel;
import gui.hud.NavMap;
import gui.hud.WorldFeedPanel;
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
 *         <p/>
 *         description:
 */
public class Game
{
  public static final String MODEL_DATA_PATH = "resources/ne_10m_admin_1_states_provinces.kml";
  public static final String BG_DATA_PATH = "resources/countries_world.xml";
  public static final String PRECIP_DATA = "resources/data/precip2010.txt";
  private MapPane mapPane;
  private JPanel mapJPanel;
  private InfoPanel infoPanel;
  private JPanel worldFeedJPanel;
  private JPanel infoJPanel;
  private NavMap navMap;
  private WorldPresenter worldPresenter;
  private WorldFeedPanel worldFeedPanel;
  private Timer worldTime;
  private final static int DEFAULT_TIME_SPEED = 2000;
  private Timer gameLoop;
  private JFrame frame;
  int frameWidth = 1400;
  int frameHeight = 700;
  final float NAV_HEIGHT_SCALE = (float) .25;
  final float NAV_WIDTH_SCALE = (float) 1.6;

  /**
   * Constructor for game, handles all init logic.
   */
  public Game()
  {
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

    init();
  }

  /**
   * set it ALL up, I mean all of it.
   */
  private void init()
  {

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

    Camera cam = new Camera(converter);
    mapPane = new MapPane(cam, worldPresenter);

    infoPanel = new InfoPanel(frameWidth,frameHeight);
    infoPanel.setPresenter(worldPresenter);

    worldFeedPanel = new WorldFeedPanel(worldPresenter,frameWidth,frameHeight);
    worldPresenter.addObserver(worldFeedPanel);

    final int NAV_HEIGHT = (int) Math.floor(frameHeight*NAV_HEIGHT_SCALE);
    final int NAV_WIDTH = (int) Math.floor(NAV_HEIGHT*NAV_WIDTH_SCALE);
    final int NAV_X = frameWidth-NAV_WIDTH;
    final int NAV_Y = frameHeight-NAV_HEIGHT;
    navMap = new NavMap(NAV_X, NAV_Y, NAV_WIDTH, NAV_HEIGHT);

    initFrame();
    setupControlls();
  }

  /**
   * sets the main game container to visible.
   */
  public void show()
  {
    frame.setVisible(true);
  }

  /**
   * Starts the game timers.
   */
  public void start()
  {
    gameLoop.start();
    worldTime.start();
  }

  /**
   * pauses the game.
   */
  public void pause()
  {
    gameLoop.stop();
    worldTime.stop();
  }


  /**
   * @return true if the game loop is running.
   */
  public boolean isRunning()
  {
    return gameLoop.isRunning();
  }

  /**
   * init and configures the timers and key bindings for the game.
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

    inputMap.put(KeyStroke.getKeyStroke("8"), "defaultTime");
    actionMap.put("defaultTime", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("9"), "faster");
    actionMap.put("faster", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED / 3);
      }
    });

    inputMap.put(KeyStroke.getKeyStroke("0"), "superfast");
    actionMap.put("superfast", new AbstractAction()
    {
      @Override
      public void actionPerformed(ActionEvent e)
      {
        worldTime.setDelay(DEFAULT_TIME_SPEED / 6);
      }
    });

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
    Collection<Region> modelMap = KMLParser.getRegionsFromFile(MODEL_DATA_PATH);

    // adds XML regions for area folder...
    modelMap.addAll(new AreaXMLLoader().getRegions());
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
    Collection<Region> BGRegions = KMLParser.getRegionsFromFile(BG_DATA_PATH);
    for (Region r : BGRegions)
    {
      randoAtts.setRegionAttributes(r, random);
    }
    return BGRegions;
  }

  //*******
  // MAIN *
  //*******
  public static void main(String[] args)
  {
    Game gameManager = new Game();
    gameManager.show();
    gameManager.start();
  }


  /**
   * Creates the content panel for the JPanels to be added to in a layered fashion
   * Created by Lyncht on 3/17/15.
   *
   */
  class CenterPanel extends JPanel
  {
    CenterPanel(MapPane mapPane,InfoPanel infoPanel,WorldFeedPanel worldFeedPanel)
    {
      super();
      // Type cast panels to JPanels to be added to the Layered panel
      mapJPanel = (JPanel) mapPane;
      infoJPanel = (JPanel) infoPanel;
      worldFeedJPanel = (JPanel) worldFeedPanel;

      JLayeredPane layeredPane = frame.getLayeredPane();

      mapJPanel.setBounds(0,0,frameWidth,frameHeight);
      layeredPane.add(mapJPanel, new Integer(1));

      worldFeedJPanel.setBounds(0,0,frameWidth,(frameHeight/25));
      layeredPane.add(worldFeedJPanel, new Integer(2));

      // Side panel with all information
      infoJPanel.setBounds(0,(frameHeight/25),frameWidth/6,frameHeight-(frameHeight/25));
      layeredPane.add(infoJPanel, new Integer(3)) ;
      infoJPanel.setVisible(false);

      // Navigation in the lower right hand corner
      layeredPane.add(navMap, new Integer(3) );
    }
  }
}
