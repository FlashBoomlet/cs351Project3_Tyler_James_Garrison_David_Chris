package gui.hud;

import gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * ScalePanel extends JPanel and creates the display for the zoom level
 * that the user is at. Gives the user a scale.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/21/15
 */
public class ScalePanel extends JPanel
{
  JLabel title;
  JLabel scale;
  JLabel unit;

  /**
   * ScalePanel initializes all of the components for the zoom level
   * that the user is at and adds them to the JPanel. Gives the user a scale.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/21/15
   *
   * @param x relative location to the WorldFeedPanel
   * @param y relative location to the WorldFeedPanel
   * @param width desired width
   * @param height desired height
   */
  public ScalePanel(int x, int y, int width, int height)
  {
    super();
    setOpaque(false);
    setBackground(Color.BLUE);
    setLocation(x,y);
    setPreferredSize(new Dimension(width,height));

    title = new JLabel("Scale: ");
    title.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    title.setFont(ColorsAndFonts.TOP_FONT);
    scale = new JLabel("xx");
    scale.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    scale.setFont(ColorsAndFonts.TOP_FONT);
    unit = new JLabel(" (km)");
    unit.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    unit.setFont(ColorsAndFonts.TOP_FONT);

    add(title);
    add(scale);
    add(unit);
  }
}
