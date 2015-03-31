package main;

import gui.ColorsAndFonts;
import main.Game;

import javax.swing.*;
import java.awt.*;
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
  JPanel textHolder;
  JButton start;
  JTextArea version;
  JTextArea text;

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

    setBackground(new Color(0x1A1A1A));

    start = new JButton("START");
    start.setHorizontalAlignment(SwingConstants.RIGHT);
    start.setFocusable(false);

    textHolder = new JPanel();
    textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.PAGE_AXIS));

    text = new JTextArea();
    text.setBackground(new Color(0x333333));
    text.setPreferredSize(new Dimension(500, 500));
    text.setFont(ColorsAndFonts.GUI_FONT);
    text.setForeground(Color.white);
    text.setText(" * \"World Food Production and Land Management\" version 1.2 developed by: \n" +
        "   Tyler Lynch, Timothy Chavez, and David Matins \n" +
        "   with a version 1.1 foundation by: Winston Wriley and David Ringo \n\n" +
        "TODO: why the game was made \n\n" +
        "TODO: what the game models \n\n" +
        "TODO: how to play the game");
    text.setEditable(false);

    version = new JTextArea();
    version.setBackground(new Color(0x800000));
    version.setPreferredSize(new Dimension(125,20));
    version.setFont(ColorsAndFonts.GUI_FONT);
    version.setForeground(Color.white);
    version.setText(" WFPaLM Version 1.2");
    version.setEditable(false);

    start.addActionListener(new ActionListener()
      {
        public void actionPerformed(ActionEvent e)
        {
          startMethod();
        }
      }
    );

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("Enter"), "startaction");
    getActionMap().put("startaction", startAction);

    add(start);
    textHolder.add(version);
    textHolder.add(text);
    add(textHolder);

  }
  private void startMethod(){
    Game.startGame();
    Game.settingsDisplay(false);
  }
  private Action startAction = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      startMethod();
    }
  };
}
