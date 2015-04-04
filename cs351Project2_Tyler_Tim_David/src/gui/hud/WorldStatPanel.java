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
  JLabel popTitle;
  JLabel pop;
  JLabel happy;
  JLabel happyTitle;

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

    popTitle = new JLabel("World Population: ");
    popTitle.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    popTitle.setFont(ColorsAndFonts.TOP_FONT);
    pop = new JLabel("2?");
    pop.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    pop.setFont(ColorsAndFonts.TOP_FONT);

    happyTitle = new JLabel("Happiness: ");
    happyTitle.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    happyTitle.setFont(ColorsAndFonts.TOP_FONT);
    happy = new JLabel("good?");
    happy.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    happy.setFont(ColorsAndFonts.TOP_FONT);

    add(popTitle);
    add(pop);
    add(happyTitle);
    add(happy);
  }
}
