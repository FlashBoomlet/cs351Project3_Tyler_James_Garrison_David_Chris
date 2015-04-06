package main;

import gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * EndScreen to display credits
 *
 * @author David Matins
 * @since 4/5/15
 */
public class EndScreen extends JPanel
{
  JPanel textHolder;
  JTextArea text;

  /**
   * Initializes a JPanel for the end screen
   *
   * @author David Matins
   *
   * @param frameWidth main frame's width
   * @param frameHeight main frame's height
   */
  public EndScreen(int frameWidth, int frameHeight)
  {
    super();

    setLocation(0,0);
    setSize(frameWidth, frameHeight);

    setBackground(new Color(0x1A1A1A));
    setLayout(new FlowLayout());

    textHolder = new JPanel();
    textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.PAGE_AXIS));


    text = new JTextArea();
    text.setBackground(new Color(0x333333));
    text.setPreferredSize(new Dimension(500, 500));
    text.setFont(ColorsAndFonts.GUI_FONT);
    text.setForeground(Color.white);
    text.setText(" * \"World Food Production and Land Management\" version 1.2 developed by: \n" +
        "   Tyler Lynch, Timothy Chavez, and David Matins, with a version 1.1 foundation by: Winston Wriley and David Ringo \n\n" +
        "  Score: 8675309\n" +
        "      You fed valiantly, alas models only model so far... blah blah blah yada yada yada\n" +
        "      As always, \"All models are wrong, but some are useful\" \n\n");

    text.setEditable(false);

    textHolder.add(text);
    textHolder.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.75))));

    JScrollPane scrollFrame = new JScrollPane(textHolder);
    textHolder.setAutoscrolls(true);
    scrollFrame.setPreferredSize(new Dimension( 500,300));
    add(scrollFrame);

    //add(textHolder);

  }
}
