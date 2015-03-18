package gui.hud;

import gui.Camera;
import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.regionlooks.RegionNameDraw;
import model.World;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.Random;

/**
 * This class is the implementation for the Navigational Map on the bottom
 * right corner of the game. There is logic to update the location of which
 * you are in relative to the Map JPanel everytime the user scrolls in
 * GameFrame.
 *
 * The original leather pattern came from:
 *   http://www.bannerelkbarra.com/wp-content/uploads/2013/12/black-leather.jpg
 * I then change the original photo and created what I needed from it.
 *
 * The original world map pattern came from:
 *   http://www.world.tisparkle.com/assets/2D_map-mobile.jpg
 * I then used it to create a template and created what I needed from it.
 *
 * @Author Tyler Lynch <lyncht@unm.edu>
 * @Author Christopher Salinas <casjr13@unm.edu>
 * @Version 1.3.2
 * @Since 2015-02-10
 */
public class NavMap extends JPanel
{
  private static int width;
  private static int height;
  private static int x;
  private static int y;
  private BufferedImage image;
  private static double baseX = 0;
  private static double baseY = 0;
  private static double baseW = 0;
  private static double baseH = 0;
  private static Color NAV_COLOR = Color.CYAN;
  private static int frameWidth = 0;
  private static int frameHeight = 0;
  private WorldPresenter presenter;
  private Camera cam;
  private boolean dynamicNameDrawing;
  private final static double MAP_VISIBILITY_SCALE = 100;
  private static Rectangle2D localRect;
  private static JLabel varsLabel;
  private static boolean setBaseVars = true;
  static final String IMAGE_PATH = "resources/images/world-map-background5.png";

  /**
   * This Constructor creates the JPanel as well as sets its location, color,
   * and size. It reads in a buffered image.
   *
   * @param x location of navigation panel
   * @param y location of navigation panel
   * @param width of navigation panel
   * @param height of navigation panel
   * @param height of Game Frame
   * @param height of Game Frame
   */
  public NavMap(int x, int y, int width, int height,int frameWidth, int frameHeight, Camera cam, WorldPresenter presenter)
  {
    super();

    //setBackground(Color.decode("#242424"));
    setBackground( ColorsAndFonts.OCEANS );

    setLocation(x,y);
    setSize(width, height);
    setBorder(BorderFactory.createLineBorder(ColorsAndFonts.GUI_TEXT_COLOR.darker()));

    this.width = width;
    this.height = height;
    this.x = 1;
    this.y = 1;
    this.frameHeight = frameHeight;
    this.frameWidth = frameWidth;
    this.cam = cam;
    this.presenter = presenter;
    dynamicNameDrawing = true;

    /*
     * This label is for check values
     * Remove once it has served its purpose
     */
    varsLabel = new JLabel("");
    varsLabel.setForeground(Color.WHITE);
    add(varsLabel);

    ClassLoader cl = this.getClass().getClassLoader();
    InputStream in = cl.getResourceAsStream(IMAGE_PATH);
    try
    {
      image = ImageIO.read(in);
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find Navigation image!");
    }
  }  //  End of Navigation constructor

  /**
   * This method sets the local rect to the view area on the map.
   *
   * If being initialized, it will save the dimensions for reference later in determining ratios
   *
   * @param rect as a Rectangle2D
   */
  public static void updateLocation(Rectangle2D rect)
  {
    localRect = rect;
    if( setBaseVars )
    {
      baseX = (double) localRect.getX();
      baseY = (double) localRect.getY();
      baseW = (double) localRect.getWidth();
      baseH = (double) localRect.getHeight();
      setBaseVars = false;
    }
  }  //  End of updateLocation method

  public static void updateLabel(String str)
  {
    varsLabel.setText(str);
  }

  /**
   * The paintComponent method overrides the paintComponent method in the
   * JComponent class.
   * @param g Graphics
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    g.drawImage(image, 0, 0, width, height, null);

    Graphics2D g2 = (Graphics2D) g;
    g2.setColor(NAV_COLOR);
    double localx = (double) width*Math.abs( localRect.getX()-baseX )/Math.abs(baseW);
    double localy = (double) height*Math.abs( localRect.getY()-baseY )/Math.abs(baseH);
    double localw = (double) width*(localRect.getWidth()/baseW);
    double localh = (double) height*(localRect.getHeight()/baseH);
    g2.drawRect((int) localx,(int) localy,(int) localw,(int) localh);
  } // End of paintComponent method
} // End of Navigation class







