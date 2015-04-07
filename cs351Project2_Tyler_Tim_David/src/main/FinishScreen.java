package main;

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

    JPanel textHolder = new JPanel();
    textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.PAGE_AXIS));


    JTextArea text = new JTextArea();
    text.setBackground(new Color(0x333333));
    text.setPreferredSize(new Dimension((int) (getWidth() * (.67)), (int) (getHeight() * (.80))));
    text.setFont(ColorsAndFonts.GUI_FONT);
    text.setForeground(Color.white);
    text.setText("\n * \"World Food Production and Land Management\" version 1.2 developed by: \n\n" +
            " \tTyler Lynch <lyncht@unm.edu>\n" +
            " \tTimothy Chavez <tchavez22@unm.edu> \n" +
            " \tDavid Matins <dmatins@unm.edu> \n\n" +
            "   with a version 1.1 foundation by: Winston Wriley and David Ringo \n\n\n" +
            "      Congratulations, you have now destroyed the world! \n"+
            "      You fed valiantly, alas models only model so far... blah blah blah yada yada yada\n" +
            "      As always, \"All models are wrong, but some are useful\" \n\n"
    );

    text.setEditable(false);

    JTextArea title = new JTextArea();
    title.setBackground(new Color(0x800000));
    title.setPreferredSize(new Dimension(125,20));
    title.setFont(ColorsAndFonts.GUI_FONT);
    title.setForeground(Color.white);
    title.setText(" Congratulations!! Everyone has died!!");
    title.setEditable(false);

    textHolder.add(title);

    JPanel realTextHolder = new JPanel();
    realTextHolder.add(text);

    JScrollPane scrollFrame = new JScrollPane(realTextHolder);
    realTextHolder.setAutoscrolls(true);
    scrollFrame.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.75))));
    textHolder.add(scrollFrame);
    textHolder.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.75))));
    add(textHolder);

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
}
