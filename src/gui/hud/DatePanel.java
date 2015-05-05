package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 @author david
 created: 2015-02-06

 description: DatePanel is a Swing component that can be instructed to display
 a Date in a simple format.  It is meant to be driven by a parent container
 (WorldFeedPanel in this case)
 */
public class DatePanel extends JPanel
{
  private static final int INSET = 5;
  private static final String DATE_PATTERN = "MMM, YYYY";
  private static final Font DATE_FONT = ColorsAndFonts.TOP_FONT;
  private static final Color guiBackground = ColorsAndFonts.OCEANS;
  private static Calendar currentDate = Calendar.getInstance();
  private static double baseYear;
  private static double yearRatio;

  private SimpleDateFormat formatter;
  private Date date;
  private static double ratio = 0;


  /**
   Instantiates a DatePanel whose Dimension is dependent on FontMetrics and a
   default GUI font (see gui.ColorsAndFonts)
   */
  public DatePanel(int x, int y, int width, int height)
  {
    formatter = new SimpleDateFormat(DATE_PATTERN);
    baseYear = currentDate.get(Calendar.YEAR);
    setOpaque(true);
    setBackground(guiBackground);
    setLocation(x,y);
    setPreferredSize(new Dimension(width,height));
    setBorder(BorderFactory.createMatteBorder(0, 3, 1, 3, ColorsAndFonts.GUI_TEXT_COLOR.darker()  ));

    FontMetrics metrics = getFontMetrics(DATE_FONT);
  }

  /**
   Overridden paintComponent draws the Date with pleasant insets, locating
   itself according to FontMetrics
   @param g Graphics context to draw to
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);

    Graphics2D g2 = (Graphics2D) g;

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.25f));

    g2.setColor(new Color(0x00AE14));
    g2.fillRect(0,0,(int) Math.round(getWidth()*yearRatio),getHeight()/2);


    g2.setColor(new Color(0xFFFFFF));
    g2.fillRect(0,getHeight()/2,(int) Math.round(getWidth()*ratio),getHeight()/2);

    g2.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));

    g2.setRenderingHint(
      RenderingHints.KEY_TEXT_ANTIALIASING,
      RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

    g2.setColor(guiBackground);
    g2.setFont(ColorsAndFonts.TOP_FONT);

    String s = getDateString().toUpperCase();
    FontMetrics metrics = g2.getFontMetrics();

    int w = metrics.stringWidth(s);
    int h = (int) metrics.getLineMetrics(s, g2).getHeight();

  /* position given to Graphics context is lower left hand corner of text */
    int x = (getWidth() - w) / 2;
    int y = (getHeight() + h) / 2;

    g2.setColor(ColorsAndFonts.REGION_NAME_FONT_C);
    g2.drawString(s, x, y);
  }

  public static void updateRatio(double newRatio, double currentYear)
  {
    yearRatio = (currentYear-baseYear)/(2050.0-baseYear);
    ratio = newRatio;
  }

  /**
   Set the date to display
   @param d   Date to display in panel
   */
  public void setDate(Date d)
  {
    date = d;
    repaint();
  }

  /* wraps getDateString base with the member variable date as an arg*/
  private String getDateString()
  {
    return getDateString(date);
  }

  /* construct a string using the SimpleDateFormat for the passed Date object */
  private String getDateString(Date d)
  {
    StringBuffer s = new StringBuffer();
    formatter.format(d, s, new FieldPosition(DateFormat.FULL));
    return s.toString();
  }

  public static void resetDate()
  {

  }
}
