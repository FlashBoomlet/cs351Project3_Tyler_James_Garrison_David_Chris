package main;

import javax.swing.*;
import java.awt.*;

/**
 * FinishScreen creates the finish screen of the game for the end of the game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4/5/15
 */
public class FinishScreen extends JPanel
{
  /**
   * Constructor that initializes a JPanel for the finish screen with graphics to
   * be added later and "cool stuff"
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param frameWidth main frame's width
   * @param frameHeight main frame's height
   */
  public FinishScreen(int frameWidth, int frameHeight)
  {
    super();
    setBackground(new Color(0xF7C76D));
    setLocation(0,0);
    setSize(frameWidth,frameHeight);
  }

  /*
   * Create button that then calls main.Game.reset(); to reset the game on click of button
   */
}
