package main;

import gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * main.ButtonPanel houses all of the components above the navigation map
 * Has pause and settings capabilities
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/19/15
 */
public class ButtonPanel extends JPanel implements ActionListener
{
  /*
   * Components
   */
  JButton pause;
  JButton settings;
  /*
   * Variables
   */
  private int x;
  private int y;
  private int width;
  private int height;

  /**
   * Initializes a JPanel for all of the buttons to controls high level game
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param navY location of the navigation
   * @param frameWidth main frame's width
   * @param navWidth the desired calculated navWidth
   */
  public ButtonPanel(int navY, int frameWidth, int navWidth)
  {
    super();

    height = 60;
    width = navWidth/3;
    y = navY - height;
    x = frameWidth-width;

    setLocation(x,y);
    setSize(width, height);
    setBorder(new EmptyBorder(0, 0, 0, 0));

    setOpaque(false);
    setLayout(new FlowLayout(FlowLayout.RIGHT,0,0) );

    pause = new JButton("PAUSE");
    pause.setName("PAUSE");
    pause.setHorizontalAlignment(SwingConstants.LEFT);

    settings = new JButton("SETTINGS");
    settings.setName("SETTINGS");
    settings.setHorizontalAlignment(SwingConstants.RIGHT);

    pause.addActionListener(this);
    settings.addActionListener(this);

    add(pause);
    add(settings);
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
    String name = tempBtn.getName();

    if( name == "PAUSE" )
    {
      Game.pauseGame();
      Game.pause();
    }
    else
    {
      Game.settingsDisplay(true);
    }
  }
}
