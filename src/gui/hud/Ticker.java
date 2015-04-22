package gui.hud;

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
public class Ticker extends JPanel implements ActionListener
{
  private static int width;
  private static int height;
  private static int x;
  private static int y;
  private static int stringLocation;
  private static int padding = 50;
  private static String stringToDraw = "";
  private static String stringSpacing = "                     ";

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
  private static final String SPACING = "   ";

  private String marquisStr;

  /* timer controls rate of scroll/repaint */
  private Timer timer;

  public Ticker(int x, int y, int width, int height)
  {
    super();
    marquisStr = new String();
    setMarquisText(Arrays.asList(testStrings));
    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    stringLocation = width - padding;

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

    timer = new Timer(50, this);
    start();
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
    for( String s: testStrings ) {
      stringToDraw += s;
      stringToDraw += stringSpacing;
    }
    // Calculate the adjusted ride height
    int rideHeight = (height + font.getSize() )/2;

    g2d.setFont(font);
    g2d.setColor( Color.WHITE  );
    g2d.drawString(stringToDraw,stringLocation,rideHeight);//stringLocation,rideHeight);

    g.drawImage(image, 0, -10, getWidth(), getHeight(), null);
  }

  /**
   clear the marquis
   */
  public void clearMarquisText()
  {
    marquisStr = "";
  }

  /**
   set the marquis source of text to the contents of a list
   @param newsList  list to source Strings from
   */
  public void setMarquisText(List<String> newsList)
  {
    clearMarquisText();
    addMarquisText(newsList);
  }

  /**
   Set the marquis text to a single String
   @param news    String to set marquis to
   */
  public void setMarquisText(String news)
  {
    clearMarquisText();
    addMarquisText(news);
  }

  /**
   add a String to the marquis
   @param news  String to add
   */
  public void addMarquisText(String news)
  {
    marquisStr += news + SPACING;
  }


  /**
   Add a list of Strings to the marquis
   @param newsList  list to source Strings from
   */
  public void addMarquisText(List<String> newsList)
  {
    for(String s : newsList) addMarquisText(s);
  }

  /**
   start marquis scrolling
   */
  public void start()
  {
    timer.start();
  }

  /**
   pause marquis scrolling
   */
  public void pause()
  {
    timer.stop();
  }

  /**
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
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
  }
}
