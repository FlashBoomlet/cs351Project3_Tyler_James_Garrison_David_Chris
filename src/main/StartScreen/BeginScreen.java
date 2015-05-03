package main.StartScreen;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import main.Trigger;
import model.PolicyData;
import model.Region;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Collection;

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
  Font FONT = new Font("Tahoma", Font.PLAIN, 14);
  private GUIRegion selectedCountry = null;
  private JLabel selectedCountryLabel;
  Collection<GUIRegion> guiRegions;
  Color backGround = new Color(0, 255, 254);
  private LowerLeft left;
  private RightLeft right;
  private MiniMapDisplay miniMap;


  /*
   * Components for the begin screen
   */
  private CountrySelect countrySelect;
  private CountryPreview countryPreview;
  private Trigger trigger;

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
  BeginScreen(Trigger trigger)
  {
    super();
    this.trigger = trigger;
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
    selectedCountryLabel = new JLabel("United States of America");
    buttonCon.add(selectedCountryLabel);
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

    trigger.setPlayer(selectedCountry);
    StartScreen.buttonAction(name);
  }

  /**
   *
   * @return a GUIRegion country that the player selects
   */
  private GUIRegion fetchCountry(String name)
  {
    for (GUIRegion r : guiRegions)
    {
      if( r.getName() == name ) return r;
    }
    return null;
  }

  /**
   *  Panel to select what country you would like
   */
  private class CountrySelect extends JPanel implements MouseListener
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
      int scrollW = 10;

      /*
       * Taking hacking of the positioning to a whole new level.
       */
      setBorder(new CompoundBorder(BorderFactory.createMatteBorder(y, x, frameHeight-(height+y),(frameWidth/2)-(width+x), BORDER_COL), PADDING_BORDER));

      guiRegions = WorldPresenter.getAllRegions();
      int regionCount = guiRegions.size();
      int labelH = (int) (FONT.getSize()*(1.2));
      int countryHeight = (int) (regionCount * (labelH) );
      JPanel countries = new JPanel();
      countries.setBackground(backGround);
      countries.setPreferredSize(new Dimension(width-scrollW, countryHeight));
      countries.setLayout(new BoxLayout(countries,BoxLayout.PAGE_AXIS));
      countries.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);

      setFont(FONT);
      for (GUIRegion r : guiRegions)
      {
        JLabel tempLabel = new JLabel(r.getName());

        if( r.getName().equalsIgnoreCase("United States of America" ) )
        {
          selectedCountry = r;
        }
        tempLabel.addMouseListener(this);
        countries.add( tempLabel );
      }

      JScrollPane scrollPane = new JScrollPane(countries);
      scrollPane.setOpaque(false);
      scrollPane.setLocation(0, 0);
      scrollPane.setAlignmentX(JScrollPane.LEFT_ALIGNMENT);
      scrollPane.setPreferredSize(new Dimension(width, height));
      scrollPane.getVerticalScrollBar().setPreferredSize(new Dimension(scrollW, 10));
      scrollPane.getHorizontalScrollBar().setPreferredSize(new Dimension(scrollW, 10));
      scrollPane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
      add(scrollPane );

    }

    @Override
    public void mouseClicked(MouseEvent e)
    {
      /* Do nothing */
    }

    @Override
    public void mousePressed(MouseEvent e)
    {
      /* Do nothing */
    }

    @Override
    public void mouseReleased(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();
      String name = temp.getText();

      selectedCountry = fetchCountry(name);
      selectedCountryLabel.setText( selectedCountry.getName() );
      refreshDisplay();
    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();
      String name = temp.getText();

      temp.setOpaque(true);
      temp.setBackground(Color.WHITE);
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      JLabel temp = (JLabel) e.getSource();
      String name = temp.getText();

      temp.setOpaque(false);
      temp.setBackground(backGround );
    }

  }

  private void refreshDisplay()
  {
    left.repaint();
    right.repaint();
    miniMap.repaint();
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
      miniMap = new MiniMapDisplay();
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
      left = new LowerLeft();
      lowerCon.add( left );

      /*
       * Lower right Container Panel to hold pie chars and data on your country/character that you choose
       *
       * Add a constructor for something to this
       */
      right = new RightLeft();
      lowerCon.add( right );

      add(lowerCon);
      refreshDisplay();
    }
  }

  private class LowerLeft extends JPanel
  {
    /**
     * Constructor
     */
    private LowerLeft()
    {
      super();
      setOpaque( true );
      setBackground(Color.CYAN);
    }

    @Override
    public void paintComponent(Graphics g)
    {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor( Color.BLACK );
      g2d.drawString(selectedCountry.getName(),10, 40 );
    }
  }

  private class RightLeft extends JPanel
  {
    /**
     * Constructor
     */
    private RightLeft()
    {
      super();
      setOpaque( true );
      setBackground(Color.PINK);
    }

    @Override
    public void paintComponent(Graphics g)
    {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor( Color.BLACK );
      g2d.drawString(selectedCountry.getName(),10, 40 );
    }
  }

  private class MiniMapDisplay extends JPanel
  {
    private MiniMapDisplay()
    {
      super();
      setOpaque(true);
      setBackground( Color.WHITE );
    }

    @Override
    public void paintComponent(Graphics g)
    {
      Graphics2D g2d = (Graphics2D) g;
      g2d.setColor( Color.BLACK );
      g2d.drawString(selectedCountry.getName(),10, 40 );
    }
  }
}