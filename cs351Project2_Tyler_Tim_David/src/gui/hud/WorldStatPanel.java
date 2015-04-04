package gui.hud;

import gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * WorldStatPanel extends JPanel and creates the display for the current world population and happiness
 *
 * @author David Matins
 * @since 4/4/15
 */
public class WorldStatPanel extends JPanel
{
  BarPanel bar;

  /**
   * WorldStatPanel initializes all of the components for the world population and happiness
   *
   * @author David Matins
   * @since 4/4/15
   *
   * @param x relative location to the WorldFeedPanel
   * @param y relative location to the WorldFeedPanel
   * @param width desired width
   * @param height desired height
   */
  public WorldStatPanel(int x, int y, int width, int height)
  {
    super();
    setOpaque(false);
    setBackground(Color.BLUE);
    setLocation(x,y);
    setPreferredSize(new Dimension(width,height));
    //setLayout(new FlowLayout(SwingConstants.CENTER,0,0));

    bar = new BarPanel(ColorsAndFonts.NAV_MAP_OUTLINE, 0.5, "Population Happiness", "50% : 7,200,000,000", x, y, width, height);

    add(bar);
  }
}
