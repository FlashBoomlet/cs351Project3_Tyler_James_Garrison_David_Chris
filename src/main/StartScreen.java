package main;

import gui.ColorsAndFonts;
import main.Game;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * StartScreen creates the start screen of the game for start up and paused game
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/19/15
 */
public class StartScreen extends JPanel implements ActionListener
{
  JPanel textHolder;

  JButton start;
  JButton info;
  JButton back;
  JButton skip;
  JButton beginGame;

  JTextArea version;
  JTextArea text;

  private String startLabel = "\t\tSTART\t\t";
  private MoreInfo moreInfo;
  private StartUp startUp;
  private BeginScreen beginScreen;
  private int frameWidth;
  private int frameHeight;

  private BufferedImage image;
  static final String IMAGE_PATH = "resources/images/Starvation_Evasion.png";

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
    this.frameWidth = frameWidth;
    this.frameHeight = frameHeight;

    setLocation(0,0);
    setOpaque(true);
    setBackground(new Color(0x10749D));
    setSize(new Dimension(frameWidth, frameHeight));

    // Must be created before other components
    back = new JButton("BACK");
    back.addActionListener(this);

    startUp = new StartUp();
    moreInfo = new MoreInfo();
    beginScreen = new BeginScreen();

    add(moreInfo);
    moreInfo.setVisible(false);

    add(beginScreen);
    beginScreen.setVisible(false);


    add(startUp);
    startUp.setVisible(true);

  }
  private void startMethod(){
    Game.startGame();
    Game.settingsDisplay(false);
  }
  private Action nextAction = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      buttonAction("START");
    }
  };
  private Action startAction = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      buttonAction("BEGIN");
    }
  };


  /**
   * Container for the initial start up screen
   */
  private class StartUp extends JPanel implements ActionListener
  {
    StartUp()
    {
      super();
      setLocation(0, 0);
      setSize(frameWidth, frameHeight);

      // I don't want a background color
      setOpaque(false);

      setLayout(new BorderLayout());

      start = new JButton(startLabel);
      start.setHorizontalAlignment(SwingConstants.RIGHT);

      info = new JButton("MORE INFORMATION");
      info.setHorizontalAlignment(SwingConstants.RIGHT);

      skip = new JButton("SKIP");
      skip.setHorizontalAlignment(SwingConstants.RIGHT);

      start.addActionListener(this);
      info.addActionListener(this);
      skip.addActionListener(this);
      start.setFocusable(false);
      info.setFocusable(false);
      skip.setFocusable(false);

      getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "nextAction");
      getActionMap().put("nextAction", nextAction);

      try
      {
        image = ImageIO.read(new File(IMAGE_PATH));
      }
      catch(IOException ex)
      {
        System.out.println("ERROR: Cannot find Start image!");
      }

      JPanel graphicPanel = new JPanel();
      graphicPanel.setOpaque(false);
      graphicPanel.setPreferredSize(new Dimension((int) (frameWidth), (int) (frameHeight * (.90))));
      add(graphicPanel, BorderLayout.NORTH);

      JPanel buttonCon = new JPanel();
      buttonCon.setPreferredSize(new Dimension((int) (frameWidth), (int) (frameHeight * (.10))));
      buttonCon.setOpaque(false);

      buttonCon.add(skip);
      buttonCon.add(info);
      buttonCon.add(start);
      add(buttonCon, BorderLayout.SOUTH);
    }

    /**
     * Overrides action performed.
     * Detects which button is clicked, gets the text and passes it along to buttonClicked
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      JButton tempBtn = (JButton) e.getSource();
      String name = tempBtn.getText();

      buttonAction(name);
    }

    /**
     * The paintComponent method overrides the paintComponent method in the
     * JComponent class.
     * @param g Graphics
     */
    @Override
    protected void paintComponent(Graphics g)
    {
      super.paintComponent(g);
      g.drawImage(image, 0, 0, getWidth(), getHeight(), null);
    } // End of paintComponent method
  }

  /**
   * Container for the more info and info on how to play the game
   */
  private class MoreInfo extends JPanel implements ActionListener
  {
    private JButton back;
    MoreInfo()
    {
      super();
      setLocation(0, 0);
      setOpaque(false);
      setSize(frameWidth, frameHeight);
      setLayout(new BorderLayout());

      textHolder = new JPanel();
      textHolder.setLayout(new BoxLayout(textHolder, BoxLayout.PAGE_AXIS));


      text = new JTextArea();
      text.setBackground(new Color(0x333333));
      text.setPreferredSize(new Dimension((int) (getWidth() ), (int) (getHeight() * (.90))));
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
          " \tDavid Matins <dmatins@unm.edu> \n" +

          "  and a version 1.1 foundation by:\n\n" +
          "  \tWinston Wriley <wriley@unm.edu>\n" +
          "  \tDavid Ringo <davidringo@unm.edu>\n\n" +
          "   Why:\n" +
          "      You are a policy Maker for the country. Your mission, should you choose to accept it, is to further advance the world \n" +
          "      while maintaining or improving upon the populations over all well being\n" +
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
      version.setText(" WFPaLM Version 1.3");
      version.setEditable(false);

      textHolder.add(version);

      JPanel realTextHolder = new JPanel();
      realTextHolder.add(text);

      JScrollPane scrollFrame = new JScrollPane(realTextHolder);
      realTextHolder.setAutoscrolls(true);
      scrollFrame.setSize((int) (getWidth()), (int) (getHeight() * (.90)));
      textHolder.add(scrollFrame);
      textHolder.setPreferredSize(new Dimension((int) (getWidth() ), (int) (getHeight() * (.90))));
      add(textHolder, BorderLayout.CENTER);

      JPanel buttonCon = new JPanel();
      buttonCon.setPreferredSize(new Dimension((int) (frameWidth), (int) (frameHeight * (.10))));
      buttonCon.setOpaque(false);
      back = new JButton("BACK");
      back.addActionListener(this);
      buttonCon.add(this.back);
      add(buttonCon, BorderLayout.SOUTH);
    }

    /**
     * Overrides action performed.
     * Detects which button is clicked, gets the text and passes it along to buttonClicked
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      JButton tempBtn = (JButton) e.getSource();
      String name = tempBtn.getText();

      buttonAction(name);
    }
  }

  /**
   * Container for the country selection and preview
   */
  private class BeginScreen extends JPanel implements ActionListener
  {
    BeginScreen()
    {
      super();
      setOpaque(false);
      setLocation(0,0);
      setSize(frameWidth, frameHeight);

      beginGame = new JButton("BEGIN");
      beginGame.addActionListener(this);

      getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "startaction");
      getActionMap().put("startaction", startAction);

      add(back);
      add(beginGame);
    }

    /**
     * Overrides action performed.
     * Detects which button is clicked, gets the text and passes it along to buttonClicked
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     *
     * @param e
     */
    @Override
    public void actionPerformed(ActionEvent e)
    {
      JButton tempBtn = (JButton) e.getSource();
      String name = tempBtn.getText();

      buttonAction(name);
    }
  }



  /**
   * Overrides action performed.
   * Detects which button is clicked, gets the text and passes it along to buttonClicked
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {
    JButton tempBtn = (JButton) e.getSource();
    String name = tempBtn.getText();

    buttonAction(name);
  }

  /**
   * Switches which screen is being showed
   * @param title of the button clicked
   */
  private void buttonAction(String title)
  {
    switch(title)
    {
      case "\t\tSTART\t\t":
        startUp.setVisible(false);
        moreInfo.setVisible(false);
        beginScreen.setVisible(true);
        break;
      case "MORE INFORMATION":
        startUp.setVisible(false);
        moreInfo.setVisible(true);
        beginScreen.setVisible(false);
        break;
      case "BACK":
        startUp.setVisible(true);
        moreInfo.setVisible(false);
        beginScreen.setVisible(false);
        break;
      default:
        // Very important to reset the screen so that pause works right
        startUp.setVisible(true);
        moreInfo.setVisible(false);
        beginScreen.setVisible(false);
        startMethod();
        break;
    }
  }
}
