package gui.hud.Ticker;

import gui.ColorsAndFonts;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Ticker is the game component that gives news station a run for their
 * moeny
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 *   Based on the original version by David Ringo.
 * @since 04.20.2015
 */
public class Ticker extends JPanel
{
  private static int width;
  private static int height;
  private static int x;
  private static int y;
  private static int stringLocation;
  private static int padding = 50;
  private static String stringToDraw = "";
  private static String stringSpacing = "                     ";
  private OpenerThread openerThread;

  private BufferedImage image;
  static final String IMAGE_PATH = "resources/images/tickerOverlay.png";

  AffineTransform aTrans = new AffineTransform();
  FontRenderContext frc = new FontRenderContext(aTrans,true,true);
  //Convert to ColorsAndFonts when then font is set.
  Font font = new Font("Tahoma", Font.BOLD, 14);

  private final static String[] testStrings =
    {
      "Cost of Soylent Green Is Rising As Birthrates Fall In China",
      "Chocolate Chips Outlawed As Scone Ingredient; Scone Farmers Rejoice",
      "Lorem Ipsum Dolor Sit Amet"
    };
  private static List<String> marquisArray = new ArrayList<>();
  private static final String SPACING = "   ";

  private String marquisStr;


  public Ticker(int x, int y, int width, int height)
  {
    super();
    marquisStr = new String();
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    stringLocation = width - padding;

    for( int i = 0; i < testStrings.length; i++ )
    {
      marquisArray.add(testStrings[i]);
    }

    try
    {
      image = ImageIO.read(new File(IMAGE_PATH));
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find News Ticker Overlay!");
    }

    setOpaque(true);
    /* config */
    setLocation(x,y);
    setSize(new Dimension(width, height));

    openerThread = new OpenerThread();
    openerThread.start();
  }
  public static final int MARQUEE_SPEED_DIV = 5;
  public static final int REPAINT_WITHIN_MS = 5;
  /**
   * The paintComponent method overrides the paintComponent method in the
   * JComponent class.
   * @param g Graphics
   */
  @Override
  public void paint(Graphics g)
  {
    super.paint(g); // Do not touch this line
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor( ColorsAndFonts.GUI_BACKGROUND );
    g2d.fillRect(0, 0, width, height);

    stringToDraw = "";
    for( String s: marquisArray ) {
      stringToDraw += s;
      stringToDraw += stringSpacing;
    }
    // Calculate the adjusted ride height
    int rideHeight = (height + font.getSize() )/2;

    g2d.setFont(font);
    g2d.setColor( Color.WHITE  );
    g2d.drawString(stringToDraw,stringLocation,rideHeight);//stringLocation,rideHeight);

    g.drawImage(image, 0, 0, width, height, null);
  }

  /**
   clear the marquis
   */
  public void clearMarquisText()
  {
    marquisArray.clear();
  }


  /**
   add a String to the marquis
   @param news  String to add
   */
  public static void addMarquisText(String news)
  {
    marquisArray.add(news + SPACING);
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
      try
      {
        while( !this.isInterrupted() )
        {
          // Les calculate movement!
          int stringLength = (int)(font.getStringBounds(stringToDraw, frc).getWidth() );
          // Sets the corrected location to the end of the string
          int location = ( Math.abs(stringLocation + stringLength)  );
          if( location <= padding )
          {
            stringLocation = width;
          }
          else
          {
            stringLocation -= 2;
          }
          repaint();
          this.sleep(50);
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      //this.interrupt();
    }
  }
}
