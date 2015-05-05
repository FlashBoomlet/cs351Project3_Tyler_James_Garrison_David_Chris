package gui.hud;

import IO.TreatyCSVParser;
import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.hud.PieChart.ChartKey;
import gui.hud.PieChart.PieChart;
import gui.hud.PieChart.Slice;
import main.Trigger;
import model.CountryData;
import model.TreatyData;

import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

/**
 * Tready Selector is a User Interface to selecte trade between countries
 *
 * Created by Lyncht on 5/4/15.
 */
public class TreatySelector extends JPanel
  implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
  int x;
  int y;
  int width;
  int height;
  Trigger trigger;
  String label = "";
  private GUIRegion selectedCountry;
  JPanel close;
  private JLabel closeLabel;
  JPanel request;
  private JLabel requestLabel;
  JLabel titleBar;
  private CountryPreview middleCon;
  private JPanel topCon;
  private JPanel bottomCon;
  Point dragFrom;
  Font CLOSE_FONT = new Font("SansSerif", Font.BOLD, 14);
  Font CARD_FONT = new Font("SansSerif", Font.BOLD, 12);
  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(2, 2, 2, 2);
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private ArrayList<TreatyData> masterTreatyData = new ArrayList<>();

  /**
   * Treaty Selector is a User Interface to select trade between countries
   *
   * CONSTRUCTOR
   *
   * @param area that you want the TreatySelector to be in within the main frame
   * @param label for the TreatySelector
   * @param trigger for all events
   */
  public TreatySelector(Rectangle area, String label, Trigger trigger)
  {
    super();
    x = (int) area.getX();
    y= (int) area.getY();
    width = (int) area.getWidth();
    height = (int) area.getHeight();

    this.trigger = trigger;
    new TreatyCSVParser(this);

    this.label = label;

    setOpaque(true);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setSize(new Dimension(width, height));
    setLocation(x,y);
    setLayout(new BorderLayout());

    topCon = new JPanel();
    topCon.setOpaque(false);
    topCon.setLocation(0,0);
    topCon.setSize(width,(int)(height*(.10)));
    topCon.setLayout(new GridLayout(1,2));
    JLabel title = new JLabel( "     " + label );
    title.setForeground(new Color(0xA0A0A0));
    title.setFont(CARD_FONT);
    title.setHorizontalAlignment(SwingConstants.LEFT);
    topCon.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL), PADDING_BORDER));

    topCon.addMouseListener(this);
    topCon.addMouseMotionListener(this);
    topCon.setName("topCon");

    /*
     * Make-shift close custom button so I can place it where ever I would like and make it look amazing
     */
    close = new JPanel();
    close.setOpaque(false);
    close.setBackground(Color.GRAY);
    close.setName("CLOSE");
    close.addMouseListener(this);
    close.setSize(75,25);
    close.setLocation(topCon.getWidth() - close.getWidth(), 0);

    closeLabel = new JLabel("X");
    closeLabel.setFont(CLOSE_FONT);
    Color closeColor = new Color(0xff0000);
    closeLabel.setForeground(closeColor);
    closeLabel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, closeColor), PADDING_BORDER));
    close.add(closeLabel);

    topCon.add(title);
    JPanel holder = new JPanel();
    holder.setOpaque(false);
    holder.setLayout(new FlowLayout(SwingConstants.RIGHT,0,0));
    holder.add(close);
    topCon.add(holder);
    add(topCon, BorderLayout.NORTH);


    // Really just a way of adding padding because I'm lazy
    middleCon = new CountryPreview();
    middleCon.addMouseWheelListener(this);
    middleCon.requestFocusInWindow();
    add(middleCon, BorderLayout.CENTER);


    /*
     * Make-shift close custom button so I can place it where ever I would like and make it look amazing
     */
    request = new JPanel();
    request.setOpaque(false);
    request.setBackground(Color.GRAY);
    request.setName("REQUEST");
    request.addMouseListener(this);
    request.setSize(75,25);
    request.setLocation(topCon.getWidth() - close.getWidth(), 0);

    requestLabel = new JLabel("REQUEST");
    requestLabel.setFont(CLOSE_FONT);
    Color requestColor = new Color(0xff0000);
    requestLabel.setForeground(closeColor);
    requestLabel.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, requestColor), PADDING_BORDER));
    request.add(requestLabel);


    bottomCon = new JPanel();
    bottomCon.setOpaque(false);
    bottomCon.setLocation(0,middleCon.getY()+middleCon.getHeight());
    bottomCon.setSize(width,(int)(height*(.10)));
    bottomCon.setLayout(new GridLayout(1,3));
    JLabel initial = new JLabel( "     " + "Initial: " );
    initial.setForeground(new Color(0xA0A0A0));
    initial.setFont(CARD_FONT);
    initial.setHorizontalAlignment(SwingConstants.RIGHT);
    bottomCon.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(2, 0, 0, 0, BORDER_COL), PADDING_BORDER));
    bottomCon.add(initial);
    bottomCon.add(new JTextArea());
    JPanel holder2 = new JPanel();
    holder2.setOpaque(false);
    holder2.setLayout(new FlowLayout(SwingConstants.RIGHT,0,0));
    holder2.add(request);
    bottomCon.add(holder2);
    add(bottomCon, BorderLayout.PAGE_END);

    repaint();
  }

  /**
   * Update the Country that you hope to trade with
   *
   * @param gr the Country that you hope to trade with
   */
  public void updateCountry(GUIRegion gr)
  {
    selectedCountry = gr;
    middleCon.intializeCharts();
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

    if( name == "CLOSE" )
    {
      this.setLocation(x,y);
      this.setVisible(false);
    }
  }

  /**
   * Override Mouse Clicked
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseClicked(MouseEvent e) { /* Do nothing */ }

  /**
   * Override Mouse Pressed
   *
   * @param e MouseEvent
   */
  @Override
  public void mousePressed(MouseEvent e)  { /* Do nothing */ }

  /**
   * Override Mouse Released
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseReleased(MouseEvent e)
  {
    JPanel tempPnl = (JPanel) e.getSource();
    String name = tempPnl.getName();

    switch(name) {
      case "CLOSE":
        this.setVisible(false);
        setLocation(x,y);
        break;
      case "REQUEST":
        trigger.signTreaty( masterTreatyData.get(0) );
        this.setLocation(x,y);
        this.setVisible(false);
        break;
      default:
        break;
    }
  }

  /**
   * Overridden mouseWheelMoved controls zooming on the map
   * @param e MouseWheelEvent fired by mouse wheel motion
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e) { /* Do nothing */ }

  /**
   * Override Mouse Entered
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseEntered(MouseEvent e) { /* Do nothing */ }

  /**
   * Override Mouse Exited
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseExited(MouseEvent e) { /* Do nothing */ }

  /**
   * Implementation of Mouse moved
   *
   * @param e MouseEvent
   */
  public void mouseMoved(MouseEvent e) { /* Do nothing */ }

  /**
   * Implementation of Mouse Dragged
   *
   * @param e MouseEvent
   */
  public void mouseDragged(MouseEvent e) { /* Do nothing */ }


  private class CountryPreview extends JPanel
  {
    JPanel landData;
    JPanel cropData;

    Color[] cropColor = {
      new Color(125, 235, 232,255),
      new Color(255,153,0,255),
      new Color(22, 152, 19,255),
      new Color(194,163,133,255),
      new Color(255,102,255,255),
      new Color(102,255,51,255),
      new Color(153,102,51,255),
      new Color(255,80,80,255),
      new Color(27, 35, 153,255),
      new Color(223, 22, 255,255)

    };

    private ArrayList<Slice> cropArray = new ArrayList<>();
    Slice[] cropSlices = {
      new Slice(0, cropColor[0], "Corn"),
      new Slice(0, cropColor[1],  "Wheat" ),
      new Slice(0, cropColor[2], "Rice" ),
      new Slice(0, cropColor[3], "Soy"),
      new Slice(0, cropColor[4], "Other")
    };

    private ArrayList<Slice> landArray = new ArrayList<>();
    Slice[] landSlices = {
      new Slice(0, cropColor[5], "Organic"),
      new Slice(0, cropColor[6],  "Conventional" ),
      new Slice(0, cropColor[7], "GMO" )
    };

    private final int MIN_HEIGHT;
    private final int MAX_HEIGHT;
    private final int MIN_WIDTH;
    private final int MAX_WIDTH;

    private CountryPreview()
    {
      super();
      setOpaque(false);
      setBackground(Color.RED);
      setLayout(new GridLayout(2,1));
      setLocation(0, topCon.getX() + topCon.getHeight());
      setSize(width, (int) (height * (.70)));

      MIN_WIDTH = 0;
      MAX_WIDTH = width;

      MIN_HEIGHT = 0;
      MAX_HEIGHT = height;

      landData = new JPanel();
      landData.setLayout(new GridLayout(1,3));
      landData.setOpaque(false);
      landData.setBackground(Color.GREEN);

      cropData = new JPanel();
      cropData.setLayout(new GridLayout(1,3));
      cropData.setOpaque(false);
      cropData.setBackground(Color.YELLOW);

      add(landData);
      add(cropData);

      if( selectedCountry != null ) intializeCharts();
    }

    private void intializeCharts()
    {
      landData.removeAll();
      cropData.removeAll();

      initCrops();
      initLand();


      /*
       * These fillers will house policies that will be set into place and actually utalized
       */
      JPanel filler1 = new JPanel();
      filler1.setOpaque(false);
      JPanel filler2 = new JPanel();
      filler2.setOpaque(false);

      landData.add(filler1);
      cropData.add(filler2);
    }

    private void initCrops()
    {
      double cornTotal = 0;
      double wheatTotal = 0;
      double riceTotal = 0;
      double soyTotal = 0;
      double otherTotal = 0;

      CountryData cd = selectedCountry.getCountryData();

      cornTotal = cd.getCornTotal(true);
      wheatTotal = cd.getWheatTotal(true);
      riceTotal = cd.getRiceTotal(true);
      soyTotal = cd.getSoyTotal(true);
      otherTotal = cd.getOtherTotal(true);

      // Update the slice information
      cropSlices[0].updateSlice(cornTotal,cropColor[0], "Corn");
      cropSlices[1].updateSlice(wheatTotal, cropColor[1],  "Wheat" );
      cropSlices[2].updateSlice(riceTotal, cropColor[2], "Rice" );
      cropSlices[3].updateSlice(soyTotal, cropColor[3], "Soy");
      cropSlices[4].updateSlice(otherTotal, cropColor[4], "Other");

      cropArray.clear();
      for( int i = 0; i < cropSlices.length ; i++)
      {
        cropArray.add(cropSlices[i]);
      }

      Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/3-5,MAX_WIDTH/3-5);
      Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/3,MAX_WIDTH/3);

      cropData.add(new PieChart(landRect, cropArray));
      cropData.add(new ChartKey(keyRect, cropArray));
    }


    private void initLand()
    {
      double organic = 0;
      double conventional = 0;
      double gmo = 0;

      CountryData cd = selectedCountry.getCountryData();

      organic = cd.getOrganic(true);
      conventional = cd.getConventional(true);
      gmo = cd.getGmo(true);

      landSlices[0].updateSlice(organic*100,cropColor[5] , "Organic");
      landSlices[1].updateSlice(conventional*100,cropColor[6],  "Conventional" );
      landSlices[2].updateSlice(gmo*100, cropColor[7], "GMO" );

      landArray.clear();
      for( int i = 0; i < landSlices.length ; i++)
      {
        landArray.add(landSlices[i]);
      }


      Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/3-5,MAX_WIDTH/3-5);
      Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/3,MAX_WIDTH/3);

      landData.add(new PieChart(landRect, landArray));
      landData.add(new ChartKey(keyRect, landArray));
    }
  }


  /**
   * Functions to help out with creating and bringing in the treaty data
   */
  public void add(TreatyData d)
  {
    masterTreatyData.add(d);
  }

}
