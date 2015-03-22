package main;

import gui.ColorsAndFonts;
import main.Game;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * StartScreen creates the start screen of the game for start up and paused game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/19/15
 */
public class StartScreen extends JPanel
{
  JButton start;

  /**
   * Initializes a JPanel for the start screen with graphics to be added later and cool stuff
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param frameWidth main frame's width
   * @param frameHeight main frame's height
   */
  public StartScreen(int frameWidth, int frameHeight)
  {
    super();

    setLocation(0,0);
    setSize(frameWidth, frameHeight);

    setBackground(ColorsAndFonts.OCEANS);

    start = new JButton("START");
    start.setHorizontalAlignment(SwingConstants.RIGHT);
    start.setFocusable(false);

    start.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          Game.startGame();
          Game.settingsDisplay(false);
        }
      }
    );
    add(start);
  }
}
