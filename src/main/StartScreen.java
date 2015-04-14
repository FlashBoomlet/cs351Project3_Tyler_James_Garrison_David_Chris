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
    setLayout(new FlowLayout());

    start = new JButton("START");
    start.setHorizontalAlignment(SwingConstants.RIGHT);

    textHolder = new JPanel();
    textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.PAGE_AXIS));


    text = new JTextArea();
    text.setBackground(new Color(0x333333));
    text.setPreferredSize(new Dimension((int) (getWidth() * (.67)), (int) (getHeight() * (.90))));
    text.setFont(ColorsAndFonts.GUI_FONT);
    text.setForeground(Color.white);
    text.setText(
      " \n * \"World Food Production and Land Management\" version 1.3 developed by: \n\n" +
        " \tTyler Lynch <lyncht@unm.edu>\n" +
        " \tJames Lawson <jlawso02@unm.edu> \n" +
        " \tGarrison Osteen <gosteen@unm.edu> \n" +
        " \tDavid Ringo <davidringo@unm.edu> \n" +
        " \tChris Salinas <casjr13@unm.edu> \n\n" +

        " \n with a version 1.2 foundation by: \n\n" +
        " \tTyler Lynch <lyncht@unm.edu>\n" +
        " \tTimothy Chavez <tchavez22@unm.edu> \n" +
        " \tDavid Matins <dmatins@unm.edu> \n\n" +

        "  and a version 1.1 foundation by:\n\n" +
        "  \tWinston Wriley <wriley@unm.edu>\n" +
        "  \tDavid Ringo <davidringo@unm.edu>\n\n\n" +
        "   Why:\n" +
        "      You are a policy Maker for the country. Your mission, should you choose to accept it, is to\n" +
        "      further advance the world while maintaining or improving upon the populations over all well being\n" +
        "      \n" +
        "      The overall well being of a country is defined by their happiness.\n" +
        "      \n" +
        "      What portion of the government should you represent?\n" +
        "      \n" +
        "      Difficulty levels based on external influences on your policies\n" +
        "      At the end of the game you will see how well you have done throughout the game\n" +
        "      \n" +
        "      As always, \"All models are wrong, but some are useful\" \n\n" +
        " To Play: \n" +
            "      Use 1-9 as well as q,w,e,r,t,y,u,i,o,p and a to filter overlays or select an overlay from the dropdown. \n\n" +
            "      Temperature and precipitation filters require zooming in to an extent before cell data will display.\n\n"+
            "      Hold Shift while clicking and dragging the mouse to select multiple countries. \n\n"+
        "      Adjust percentages of crops in the info panel after selecting a country. \n\n"+
            "      Adjust percentages of crops in the info panel after selecting a country. \n\n"+
            "      Use [Next Year] [-] [+] to adjust game speed. \n\n"+
            "      View World Population and well-being, as well as the Timeline/Progress at the top of the map panel. \n\n"+
        "      Press Pause to view this screen after starting the game. \n\n\n\n\n\n" +
        " ***Please note that the update crops in the sidebar will reflect once the year is over. \n\n\n\n"

    );

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

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "startaction");
    getActionMap().put("startaction", startAction);


    textHolder.add(version);

    JPanel realTextHolder = new JPanel();
    realTextHolder.add(text);

    JScrollPane scrollFrame = new JScrollPane(realTextHolder);
    realTextHolder.setAutoscrolls(true);
    scrollFrame.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.75))));
    textHolder.add(scrollFrame);
    textHolder.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.75))));
    add(textHolder);

    JPanel buttonCon = new JPanel();
    buttonCon.setPreferredSize(new Dimension((int) (getWidth() * (.70)), (int) (getHeight() * (.25))));
    buttonCon.setOpaque(false);
    buttonCon.add(start);
    add(buttonCon);

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
