package main;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * FinishScreen creates the finish screen of the game for the end of the game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4/5/15
 */
public class FinishScreen extends JPanel implements ActionListener
{
  JButton reset;
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
    setLocation(0, 0);
    setSize(frameWidth, frameHeight);
    reset = new JButton("Reset");
    reset.addActionListener(this);
    reset.setFocusable(false);
    add(reset);
    add(new JLabel("Congratulations, you have now destroyed the world!"));
  }

  /**
   * Overrides action performed.
   * Detects which button is clicked and either pauses the game or shows the settings
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    JButton tempBtn = (JButton) e.getSource();
    String name = tempBtn.getText();

    if( name == "Reset" ) {
      main.Game.pauseGame();
      main.Game.reset();
    }
  }
}
