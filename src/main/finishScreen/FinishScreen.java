package main.finishScreen;

import gui.ColorsAndFonts;

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
  int frameWidth;
  int frameHeight;
  int marginH;
  int marginV;
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

    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setLocation(0, 0);
    setSize(frameWidth, frameHeight);

    this.frameWidth = frameWidth;
    this.frameHeight = frameHeight;
    marginH = (int) (frameWidth * 0.1);
    marginV = (int) (frameHeight * 0.1);

    reset = new JButton("Reset");
    reset.addActionListener(this);
    reset.setFocusable(false);
    reset.setVisible(true);

    add(reset);
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

  /**
   * Override paint components
   * @param g graphics you with to have
   */
  @Override
  public void paint(Graphics g)
  {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(Color.RED);
    g2d.fillRect(marginH, marginV, (int) (marginH * 3.5), (int) (marginV * 3.5));

    g2d.setColor(Color.GREEN);
    g2d.fillRect((int) (marginH * 5.5), marginV, (int) (marginH * 3.5), (int) (marginV * 3.5));

    g2d.setColor(Color.BLUE);
    g2d.fillRect(marginH, (int) (marginV * 5.5), (int) (marginH * 3.5), (int) (marginV * 3.5));

    g2d.setColor(Color.YELLOW);
    g2d.fillRect((int) (marginH * 5.5), (int) (marginV * 5.5), (int) (marginH * 3.5), (int) (marginV * 3.5));

    g2d.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    g2d.drawString("Summary", marginH+5, marginV+getFontMetrics(getFont()).getHeight()+5);


  }
}
