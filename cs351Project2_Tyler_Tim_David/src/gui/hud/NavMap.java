package gui.hud;

import gui.ColorsAndFonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

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
  private BufferedImage navImage;
  private static double navX = 0;
  private static double navY = 0;
  private static double navW = 0;
  private static double navH = 0;
  private Color NAV_COLOR = Color.GREEN;

  /**
   * This Constructor creates the JPanel as well as sets its location, color,
   * and size. It reads in a buffered image.
   *
   * @param int x
   * @param int y
   * @param int width
   * @param int height
   */
  public NavMap(int x, int y, int width, int height)
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

    navW = width;
    navH = height;

    ClassLoader cl = getClass().getClassLoader();
    InputStream in = cl.getResourceAsStream("resources/images/world-map-background5.png");
    try
    {
      if( in != null )  image = ImageIO.read(in);
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find Navigation image!");
    }
    in = cl.getResourceAsStream("resources/images/navLeather.png");
    try
    {
      if( in != null )  navImage = ImageIO.read(in);
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find nav background image!");
    }
  }  //  End of Navigation constructor

  /**
   * This method sets all values for repainting. It updates the width and
   *   height, relative to the mouse location. It then update the x and y
   *   in proportion to the mouse position to zoom in on that location.
   *
   * This code zooms in on a point and zooms out on the same path.
   */
  static void updateLocation()
  {
    /*
    navX = width*( (Math.abs(Map.x)/Map.width) )*2;
    navY = height*( (Math.abs(Map.y)/Map.height) )*2;

    navW = width*(GameFrame.frameWidth/Map.width);
    navH = height*(GameFrame.frameHeight/Map.height);
    */
  }  //  End of updateLocation method

  /**
   * The paintComponent method overrides the paintComponent method in the
   * JComponent class.
   * @param Graphics g
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    g.drawImage(navImage, 0, 0, width, height, null);
    g.drawImage(image, 0, 0, width, height, null);
    g.setColor(NAV_COLOR);
    g.drawRect( (int) navX, (int) navY, (int) navW, (int) navH );
  } // End of paintComponent method
} // End of Navigation class







