package gui.hud;

import gui.ColorsAndFonts;
import gui.MapPane;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * GameplayControl extends JPanel and creates the guts for adjusting the
 * game clock and all of the components that go in it.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/21/15
 */
public class GameplayControl extends JPanel implements ActionListener
{
  /*
   * Panel Components
   */
  private JButton next;
  private JLabel speedControlLabel;
  private JButton faster;
  private JButton slower;
  private static JLabel speed;
  private final double TIMER_ADJUST = 100;

  /**
   * GameplayControl initializes all of the components and adds them
   * to a JPanel.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/21/15
   *
   * @param x relative location to the WorldFeedPanel
   * @param y relative location to the WorldFeedPanel
   * @param width desired width
   * @param height desired height
   */
  public GameplayControl(int x, int y, int width, int height)
  {
    super();
    setOpaque(false);
    setBackground(Color.YELLOW);
    setLocation(x,y);
    setPreferredSize(new Dimension(width,height));
    setLayout(new FlowLayout(SwingConstants.RIGHT,0,0));

    next = new JButton("Next Year");
    next.addActionListener(this);
    faster = new JButton("+");
    faster.addActionListener(this);
    slower = new JButton("-");
    slower.addActionListener(this);

    speed = new JLabel();
    speed.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    speed.setFont(ColorsAndFonts.TOP_FONT);
    speedControlLabel = new JLabel("Speed Controls:");
    speedControlLabel.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    speedControlLabel.setFont(ColorsAndFonts.TOP_FONT);

    add(next);
    add(speedControlLabel);
    add(slower);
    add(speed);
    add(faster);
    next.setFocusable(false);
    slower.setFocusable(false);
    faster.setFocusable(false);
  }

  /**
   * showAll is a public function to hide inner components of GamePlayControl
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @param show true to display and false to hide
   */
  public void showAll(boolean show)
  {
    next.setVisible(show);
    speedControlLabel.setVisible(show);
    slower.setVisible(show);
    speed.setVisible(show);
    faster.setVisible(show);
    this.setVisible(show);
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

    if( (main.Game.worldTime.getDelay()-(TIMER_ADJUST)) > 0.0 )
    {
      if( name == "Next Year" )
      {
        MapPane.presenter.setWorldForward(365);
      }
      else if( name == "-" )
      {
        main.Game.worldTime.setDelay( (int) (main.Game.worldTime.getDelay()+(TIMER_ADJUST)) );
      }
      else if( name == "+" )
      {
        main.Game.worldTime.setDelay( (int) (main.Game.worldTime.getDelay()-(TIMER_ADJUST)) );
      }
      updateDisplaySpeed();
    }
    else speed.setText("MAX" + " (sec./Day)");
  }

  /**
   * updateDisplaySpeed is a public function to updateTheSpeed of the timer.
   * Called when the timers are created. Also called when user uses keys to
   * update the speed of the timer.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   */
  public static void updateDisplaySpeed()
  {
    double oneSecond = 1000;
    speed.setText( Double.toString(((main.Game.worldTime.getDelay())/oneSecond)) + " (sec./Day)" );
  }
}
