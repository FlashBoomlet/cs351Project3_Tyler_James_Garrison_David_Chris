package main.StartScreen;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Container for the country selection and preview
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 04/30/15
 * */
class BeginScreen extends JPanel implements ActionListener
{
  private JButton beginGame;
  private int frameWidth;
  private int frameHeight;

  /*
   * Components for the begin screen
   */
  private CountrySelect countrySelect;
  private CountryPreview countryPreview;

  private Action startAction = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e) {
      StartScreen.buttonAction("BEGIN");
    }
  };

  /**
   * Constructor
   */
  BeginScreen()
  {
    super();

    this.frameWidth = main.Game.frameWidth;
    this.frameHeight = main.Game.frameHeight;

    setOpaque(false);
    setLocation(0, 0);

    setSize(frameWidth, frameHeight);
    setLayout(new BorderLayout());

    beginGame = new JButton("BEGIN");
    beginGame.addActionListener(this);

    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("ENTER"), "startaction");
    getActionMap().put("startaction", startAction);

    JPanel contentHolder = new JPanel();
    contentHolder.setOpaque(false);
    contentHolder.setPreferredSize(new Dimension((int) (getWidth()), (int) (getHeight() * (.90))));
    contentHolder.setLayout(new GridLayout(1,2));
    /*
     * Add components to the content holder
     */
    countrySelect = new CountrySelect();
    countryPreview = new CountryPreview();
    contentHolder.add(countrySelect);
    contentHolder.add(countryPreview);
    add(contentHolder, BorderLayout.NORTH);

    JPanel buttonCon = new JPanel();
    buttonCon.setPreferredSize(new Dimension((int) (frameWidth), (int) (frameHeight * (.10))));
    buttonCon.setOpaque(false);
    buttonCon.add(StartScreen.back);
    buttonCon.add(beginGame);
    add( buttonCon, BorderLayout.SOUTH);
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

    StartScreen.buttonAction(name);
  }

  /**
   *  Panel to select what country you would like
   */
  private class CountrySelect extends JPanel
  {
    private CountrySelect()
    {
      super();
      setOpaque(true);
      setBackground(Color.RED);
      setBounds(0,0,getWidth(),getHeight());

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) (getWidth()*(.75));
      int height = (int) (getHeight()*(.75));
      int x = (getWidth()-(width/2));
      int y = (getHeight()-(height/2));
      JScrollPane scrollPane = new JScrollPane();
      scrollPane.setLocation(x, y);
      scrollPane.setSize(width, height);
      scrollPane.setOpaque(true);
      scrollPane.setBackground(Color.CYAN);
      add(scrollPane);
    }
  }

  /**
   * Information about the selected country that you have selected
   */
  private class CountryPreview extends JPanel
  {
    private CountryPreview()
    {
      super();
      setOpaque(true);
      setBackground(Color.CYAN);
      setBounds(0,0,getWidth(),getHeight());

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) (getWidth()*(.75));
      int height = (int) (getHeight()*(.75));
      int x = (getWidth()-(width/2));
      int y = (getHeight()-(height/2));
      JPanel infoPanel = new JPanel();
      infoPanel.setLocation(x, y);
      infoPanel.setSize(width, height);
      infoPanel.setOpaque(true);
      infoPanel.setBackground(Color.RED);
      add(infoPanel);
    }
  }
}