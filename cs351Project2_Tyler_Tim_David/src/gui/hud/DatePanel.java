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
  private static final String DATE_PATTERN = "EEE, MMM d, YYYY";
  private static final Color guiBackground = ColorsAndFonts.OCEANS;

  private BarPanel bar;
  private SimpleDateFormat formatter;
  private Date date;


  /**
   Instantiates a DatePanel whose Dimension is dependent on FontMetrics and a
   default GUI font (see gui.ColorsAndFonts)
   */
  public DatePanel(int x, int y, int width, int height)
  {
    bar = new BarPanel(Color.cyan, 0.5, "", "MMM d, YYYY", x, y, width, height);
    bar.setPreferredSize(new Dimension(width-10, height));
    formatter = new SimpleDateFormat(DATE_PATTERN);

    setOpaque(true);
    setPreferredSize(new Dimension(width,height));
    setBackground(guiBackground);
    setBorder(BorderFactory.createMatteBorder(0, 3, 1, 3, ColorsAndFonts.GUI_TEXT_COLOR.darker()  ));

    add(bar);

  }

  /**
   Set the date to display
   @param d   Date to display in panel
   */
  public void setDate(Date d)
  {
    date = d;
    bar.updateRatio(0.05);
    bar.setOverLayText(getDateString(d).toUpperCase());
   // repaint();
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
