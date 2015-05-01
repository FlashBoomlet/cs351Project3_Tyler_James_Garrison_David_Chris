package main.StartScreen;

import gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

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
    public final EmptyBorder PADDING_BORDER = new EmptyBorder(2, 2, 2, 2);
    // Null color basically
    private final Color BORDER_COL = new Color(0.0f,0.0f,0.0f,0.0f);

    private CountrySelect()
    {
      super();
      setOpaque(false);
      setBackground(Color.RED);

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) ((frameWidth/2)*(.5));
      int height = (int) (frameHeight*(.75));
      int x = ((frameWidth/4)-(width/2));
      int y = (frameHeight-height)/4;

      /*
       * Taking hacking of the positioning to a whole new level.
       */
      setBorder(new CompoundBorder(BorderFactory.createMatteBorder(y, x, frameHeight-(height+y),(frameWidth/2)-(width+x), BORDER_COL), PADDING_BORDER));


      JPanel countries = new JPanel();
      countries.setBackground(Color.CYAN);
      countries.setPreferredSize(new Dimension(width, height * 5));
      countries.setLayout(new FlowLayout());
      countries.add(new JLabel("Use Logic from Chart Key to            "));
      countries.add(new JLabel( "handle sizing & creating             \n " ));
      countries.add(new JLabel( " a custom list                  "));
      countries.add(new JLabel( "               "));
      for( int i = 0; i < 50; i ++ ) countries.add(new JLabel("Countries will go here"));

      JScrollPane scrollPane = new JScrollPane(countries);
      scrollPane.setOpaque(false);
      scrollPane.setLocation(0,0);
      scrollPane.setPreferredSize(new Dimension(width, height));
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(10, 10));
      scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(10, 10));
      //scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
      add(scrollPane );

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
      setOpaque(false);
      setLayout(new GridLayout(2,1));

      /*
       * Measurements for the JScrollPane
       */
      int width = (int) ((getWidth()/2)*(.5));
      int height = (int) (this.getHeight()*(.5));
      int x = ((getWidth()/2)-(width/2));
      int y = ((getWidth()/2)-(height/2));

      /*
       * Create a mini map view of the country for this area
       *
       * Add a constructor for something to this
       */
      JPanel miniMap = new JPanel();
      miniMap.setOpaque(true);
      miniMap.setBackground(Color.RED);
      add(miniMap);

      /*
       * Lower container Only for formatting
       */
      JPanel lowerCon = new JPanel();
      lowerCon.setOpaque(false);
      lowerCon.setLayout(new GridLayout(1,2));

      /*
       * Lower Left Container Panel to hold statistics on  your country/character that you choose
       *
       * Add a constructor for something to this
       */
      JPanel left = new JPanel();
      left.setBackground( Color.CYAN );
      lowerCon.add( left );

      /*
       * Lower right Container Panel to hold pie chars and data on your country/character that you choose
       *
       * Add a constructor for something to this
       */
      JPanel right = new JPanel();
      right.setBackground( Color.PINK );
      lowerCon.add( right );

      add(lowerCon);
    }
  }
}