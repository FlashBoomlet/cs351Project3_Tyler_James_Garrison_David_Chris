package gui.hud;

import IO.PolicyCSVParser;
import gui.ColorsAndFonts;
import main.Trigger;
import model.PolicyData;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

/**
 * CardSelector is a coverFlow framework for some really cool card selection in this game!
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.23.15
 */
public class CardSelector extends JPanel
  implements ActionListener, MouseListener, MouseMotionListener, MouseWheelListener
{
  private int x;
  private int y;
  private int width;
  private int height;
  private JPanel close;
  private JLabel closeLabel;
  private ArrayList<PolicyData> masterPolicyData = new ArrayList<>();
  private int policyMiddle;
  private int currentPol;
  private boolean hideRight = false;
  private boolean hideLeft = false;

  /*
   * Formatting for the Card
   */
  private int cardWidth;
  private int cardHeight;
  private int mx;
  private int my;
  private int lx;
  private int ly;
  private int rx;
  private int ry;
  private int realX;
  private int realWidth;
  private int realHeight;
  private int realY;

  /*
   * Foreground colors for the main playing card
   */
  private float fontR = 0.0f;
  private float fontG = 0.0f;
  private float fontB = 0.0f;

  /*
   * Overall transparency of everything pretty much
   */
  private float alpha = 1.0f;
  private float mAlpha = 1.0f;
  private float lAlpha = 0.5f;
  private float rAlpha = 0.5f;

  /*
   * Components for the main card
   */
  private JTextArea policyTA = new JTextArea();
  private JTextArea descriptionTA = new JTextArea();
  private JLabel pro = new JLabel( "PRO: ");
  private JLabel con = new JLabel( "CON: ");
  private JPanel sponsor;
  private JLabel sponsorLabel;
  private JPanel rightArrow;
  private JPanel leftArrow;
  private BufferedImage lArrow;
  private BufferedImage rArrow;
  private String lArrowPath = "resources/images/leftArrow.png";
  private String rArrowPath = "resources/images/rightArrow.png";
  private OpenerThread openerThread;
  private boolean nextSelect = false;
  private boolean previousSelect = false;
  private JPanel proGraph;
  private JPanel conGraph;

  /*
   * Rectangles/Cards
   */
  private Rectangle middleCard;
  private Rectangle leftCard;
  private Rectangle rightCard;
  private Rectangle leftDummyCard;
  private Rectangle rightDummyCard;

  /*
   * Random stuff that I am too lazy to really move -TLynch
   */
  private JPanel middleCon;
  private JPanel topCon;
  Point dragFrom;
  Font CLOSE_FONT = new Font("SansSerif", Font.BOLD, 14);
  Font CARD_FONT = new Font("SansSerif", Font.BOLD, 12);
  public static final EmptyBorder PADDING_BORDER = new EmptyBorder(2, 2, 2, 2);
  private final static Color BORDER_COL = ColorsAndFonts.GUI_TEXT_COLOR.darker();
  private Trigger trigger;

  /**
   * Class constructor
   */
  public CardSelector(int x, int y, int width, int height,String label, Trigger trigger)
  {
    super();
    this.trigger = trigger;
    new PolicyCSVParser(this);
    currentPol = policyMiddle = (masterPolicyData.size())/2;

    setOpaque(true);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setSize(new Dimension(width, height));
    setLocation(x,y);
    setLayout(new BorderLayout());

    // Fighting the map for who is superior so this fixes that
    addMouseListener(this);
    setName("MainContainer");
    //Ensure the key listener works for this panel and not the map

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    cardWidth = (int) ( width /2* (1.25));
    cardHeight = (int) (height /2* (1.25));
    mx = cardWidth/2;
    my = (int) (getHeight() * (.2));
    lx = mx - (cardWidth + (cardWidth*(5/8)));
    ly = my - 20;
    realWidth = cardWidth ;
    realHeight = cardHeight ;
    realX = (int) (mx * (.65));
    realY =  (my);
    rx = (realX+realWidth)-(lx+cardWidth-realX);
    ry = ly;

    middleCard = new Rectangle(realX, my, realWidth, realHeight);
    leftCard = new Rectangle(lx,ly,cardWidth,cardHeight);
    leftDummyCard = new Rectangle(lx,ly,cardWidth,cardHeight);
    rightCard = new Rectangle(rx,ry,cardWidth,cardHeight);
    rightDummyCard = new Rectangle(rx,ry,cardWidth,cardHeight);

    try
    {
      lArrow = ImageIO.read(new File(lArrowPath));
      rArrow = ImageIO.read(new File(rArrowPath));
    }
    catch(IOException ex)
    {
      System.out.println("ERROR: Cannot find policy selector arrows!");
    }

    topCon = new JPanel();
    topCon.setOpaque(false);
    topCon.setLocation(0,0);
    topCon.setSize(width,(int)(height*(.10)));
    JLabel title = new JLabel( "     " + label );
    title.setForeground(new Color(0xA0A0A0));
    title.setFont(CARD_FONT);
    title.setHorizontalAlignment(SwingConstants.LEFT);
    topCon.setBorder(new CompoundBorder(BorderFactory.createMatteBorder(0, 0, 2, 0, BORDER_COL), PADDING_BORDER));

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

    topCon.addMouseListener(this);
    topCon.addMouseMotionListener(this);
    topCon.setName("topCon");

    topCon.setLayout(new GridLayout(1,2));
    topCon.add(title);
    JPanel holder = new JPanel();
    holder.setOpaque(false);
    holder.setLayout(new FlowLayout(SwingConstants.RIGHT,0,0));
    holder.add(close);
    topCon.add(holder);
    add(topCon, BorderLayout.NORTH);

    // Really just a way of adding padding because I'm lazy
    middleCon = new JPanel();
    middleCon.setOpaque(false);
    middleCon.setLocation(0, topCon.getX() + topCon.getHeight());
    middleCon.setSize(width, (int) (height * (.90)));
    middleCon.addMouseWheelListener(this);
    middleCon.requestFocusInWindow();
    add(middleCon, BorderLayout.CENTER);

    /*
     * Setting up key-bindings for the card selection
     */
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("LEFT"), "left");
    getActionMap().put("left", leftNavigate);
    getInputMap(WHEN_IN_FOCUSED_WINDOW).put(KeyStroke.getKeyStroke("RIGHT"), "right");
    getActionMap().put("right", rightNavigate);

    repaint();
    drawCard();
  }

  /**
   * Handles drawing all of the components for the Main card that the user interacts with
   */
  private void drawCard()
  {
    middleCon.setLayout(null);

    policyTA.setLocation((int) (realX + middleCon.getWidth() * (.02)), (int) (realY * (.70)));
    policyTA.setSize((int) (realWidth * (.90)) / 2, (int) (realHeight * (.05)));
    policyTA.setOpaque(false);
    policyTA.setEditable(false);
    middleCon.add(policyTA);

    descriptionTA.setBounds(policyTA.getX(), policyTA.getHeight() + policyTA.getY() + 10, (int) (realWidth * (.90)), (int) (realHeight * (.20)));
    descriptionTA.setLineWrap(true);
    descriptionTA.setOpaque(false);
    descriptionTA.setEditable(false);
    middleCon.add(descriptionTA);

    pro.setBounds(descriptionTA.getX(), descriptionTA.getY() + descriptionTA.getHeight() + 10, (int) (realWidth * (.90)) / 2, (int) (realHeight * (.05)));
    pro.setOpaque(false);
    middleCon.add(pro);

    con.setBounds(pro.getX() + pro.getWidth(), pro.getY(), pro.getWidth(), pro.getHeight());
    con.setOpaque(false);
    middleCon.add(con);

    /*
     * Place graph of pro here
     */
    proGraph = new JPanel();
    proGraph.setBounds(pro.getX(), pro.getY() + pro.getHeight(), (int) (pro.getWidth()*(.90)), (int)(con.getWidth()*(0.9)));
    middleCon.add(proGraph);
    /*
     * Place graph of con here
     */
    conGraph = new JPanel();
    conGraph.setBounds(con.getX(),con.getY()+con.getHeight(),(int) (con.getWidth()*(.90)),proGraph.getHeight());
    middleCon.add(conGraph);

    /*
     * Component to sponsor the bill
     */
    sponsor = new JPanel();
    sponsor.setOpaque(false);
    sponsor.setBackground(new Color(0xD9D9D9));
    sponsor.setName("SPONSOR");
    sponsor.addMouseListener(this);
    sponsor.addMouseMotionListener(this);
    sponsor.setSize(100, 25);
    sponsor.setLocation((int) (middleCard.getX() + middleCard.getWidth() - sponsor.getWidth()), (int) (middleCard.getY() + middleCard.getHeight() - sponsor.getHeight() * 3));
    sponsorLabel = new JLabel("SPONSOR");
    sponsorLabel.setHorizontalAlignment(SwingConstants.CENTER);
    sponsor.add(sponsorLabel);
    middleCon.add( sponsor );

    /*
     * adding el arrows
     */
    int arrowW = width/16;
    int arrowH = height/4;
    int arrowY = realY+realHeight/8;
    rightArrow = new JPanel();
    rightArrow.setName(">");
    rightArrow.setBounds(realX + realWidth+(width*3 / 32), arrowY, arrowW, arrowH);
    rightArrow.addMouseListener(this);
    rightArrow.setBackground(Color.PINK);

    leftArrow = new JPanel();
    leftArrow.setName("<");
    leftArrow.setBounds((width / 32), arrowY, arrowW, arrowH);
    leftArrow.addMouseListener(this);
    leftArrow.setBackground(Color.PINK);

    middleCon.add(leftArrow);
    leftArrow.setOpaque(false);
    middleCon.add(rightArrow);
    rightArrow.setOpaque(false);

    setComponentForColor();

    PolicyData d = masterPolicyData.get(policyMiddle);
    updateCard(d);
  }

  /**
   * This helps with color transitions in the Cards on movement
   */
  private void setComponentForColor()
  {
    policyTA.setForeground(new Color(fontR, fontG, fontB, alpha));
    descriptionTA.setForeground(new Color(fontR, fontG, fontB, alpha));
    pro.setForeground(new Color(fontR, fontG, fontB, alpha));
    con.setForeground(new Color(fontR, fontG, fontB, alpha));
    sponsorLabel.setForeground(new Color(fontR, fontG, fontB, alpha));

    if( alpha < .4f)
    {
      proGraph.setVisible(false);
      conGraph.setVisible(false);
    }
    else
    {
      proGraph.setVisible(true);
      conGraph.setVisible(true);
    }
    Color proColor = new Color(0.67f,0.67f,0.67f,alpha);
    Color conColor = new Color(0.67f,0.67f,0.67f,alpha);
    proGraph.setBackground(proColor);
    conGraph.setBackground(conColor);
  }

  /**
   * Functions to help out with creating and bringing in the policy data
   */
  public void add(PolicyData d)
  {
    masterPolicyData.add(d);
  }

  /**
   * Lets make the cards shuffle/ moves!
   */
  private void fancyFlip()
  {
    if (openerThread == null || !openerThread.isAlive())
    {
      openerThread = new OpenerThread();
      openerThread.start();
    }
  }

  /**
  * Thread to make the cards shuffle.
  * It's like taking a water balloon and popping it while recording it in slow motion and watching it back
  *   its pretty fancy
  */
  private class OpenerThread extends Thread
  {
    /**
     * Overrides run for thread and runs a thread
     */
    @Override
    public void run()
    {
      int transitionSpeed = 4;
      // For the main alpha
      float adjustBy = .04f;
      int mod = 1;
      float adjustTo = 100f;
      float dy = Math.abs(ry - realY)/adjustTo * mod;
      // lx is off screen at a negative position
      float dx = Math.abs(rx - realX)/adjustTo * mod;
      float adjustBackAlpha = (1.0f-0.5f)/adjustTo;

      float tLX = (float) leftCard.getX();
      float tLY = (float) leftCard.getY();

      float tRX = (float) rightCard.getX();
      float tRY = (float) rightCard.getY();

      float tMX = (float) middleCard.getX();
      float tMY = (float) middleCard.getY();
          /*
       * Set the alpha's to zero
       */
      for( int i = 0; i < adjustTo ; i++ )
      {

        if (i < adjustTo / 4) alpha -= adjustBy;
        else if (i > (adjustTo - (adjustTo / 4))) alpha += adjustBy;

        if( i == adjustTo/2 )
        {
          PolicyData d = masterPolicyData.get(currentPol);
          updateCard(d);
        }
        //mAlpha = alpha;
        if (nextSelect)
        {
          if( i % mod == 0 ) {
            rightCard.setBounds((int) (tRX -= dx), (int) (tRY += dy), cardWidth, cardHeight);
            middleCard.setBounds((int) (tMX -= dx), (int) (tMY -= dy), cardWidth, cardHeight);
          }
          rAlpha += adjustBackAlpha;
        }
        else if( previousSelect )
        {
          if( i % mod == 0 ) {
            leftCard.setBounds((int) (tLX += dx), (int) (tLY += dy), cardWidth, cardHeight);
            middleCard.setBounds((int) (tMX += dx), (int) (tMY -= dy), cardWidth, cardHeight);
          }
          lAlpha += adjustBackAlpha;
        }
        repaint();
        setComponentForColor();
        try
        {
          this.sleep(transitionSpeed);
        }
        catch (InterruptedException e)
        {
          e.printStackTrace();
        }
      }
      //Relocate the cards to the original positions of the dummy
      resetCardLocations();


      nextSelect = false;
      previousSelect = false;
      this.interrupt();
    }
  }

  /**
   * Touch up to my slow motion film of a water balloon popping or my card movement
   */
  private void resetCardLocations()
  {
    alpha = 1.0f;
    mAlpha = 1.0f;
    lAlpha = 0.5f;
    rAlpha = 0.5f;

    leftCard.setBounds(lx, ly, cardWidth, cardHeight);
    rightCard.setBounds(rx, ry, cardWidth, cardHeight);
    middleCard.setBounds(realX, realY, cardWidth, cardHeight);
    repaint();
  }

  /**
   * Called to reset the location of the card
   */
  public void resetLocation()
  {
    setLocation(x,y);
  }

  /**
   * Update Card information
   * @param d -Current Policy for the Main card
   */
  private void updateCard(PolicyData d)
  {
    policyTA.setText(d.getPolicy());
    descriptionTA.setText(d.getDescription());
    sponsorLabel.setText(d.getSponsor());
    sponsor.setName(d.getSponsor());
  }

  /**
   * Override paint components
   * @param g graphics you wish to have
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    Graphics2D g2d = (Graphics2D) g;
    Color borderLColor = new Color(0.0f,0.0f,0.0f, lAlpha);
    Color borderMColor = new Color(0.0f,0.0f,0.0f, alpha);
    Color borderRColor = new Color(0.0f,0.0f,0.0f, rAlpha);

    /*
     * Draw the table like feature
     */
    g2d.setColor( new Color(0.24705882f, 0.24705882f, 0.24705882f, 1.0f));
    Rectangle tempTop = new Rectangle(0,((height*11)/16),width, ((height*1) / 6) );
    int tempTopEnd = (int) (tempTop.getY()+tempTop.getHeight());
    g2d.fill(tempTop);

    g2d.setColor( new Color(0.28627452f, 0.28627452f, 0.28627452f, 1.0f));
    g2d.fillRect(0,tempTopEnd,width,height-(tempTopEnd) );

    //g2d.setColor(new Color(0.67058825f, 0.67058825f, 0.67058825f,  0.1f));
    g2d.setColor(new Color(0.1764706f, 0.1764706f, 0.1764706f, 1.0f));
    g2d.drawLine(0,tempTopEnd,width,tempTopEnd);

    /*
     * Set up actual cards
     */
    if( policyMiddle >= 0 )
    {
      // Pop off left
      if( !hideLeft )
      {
        g2d.setColor(new Color(0.627451f, 0.627451f, 0.627451f, lAlpha));
        g2d.fill(leftCard);
        g2d.setColor( new Color(0.627451f, 0.627451f, 0.627451f,0.75f) );
        g2d.fill(leftDummyCard);
        g2d.setColor(borderLColor);
        g2d.draw(leftDummyCard);
      }
      else
      {
        g2d.setColor( new Color(0.627451f, 0.627451f, 0.627451f,0.0f) );
        g2d.fill(leftCard);
        g2d.fill(leftDummyCard);
      }

      // Pop off right
      if( !hideRight )
      {
        g2d.setColor(new Color(0.627451f, 0.627451f, 0.627451f,rAlpha) );
        g2d.fill(rightCard);
        g2d.setColor( new Color(0.627451f, 0.627451f, 0.627451f,0.75f) );
        g2d.fill(rightDummyCard);
        g2d.setColor(borderRColor);
        g2d.draw(rightDummyCard);
      }
      else
      {
        g2d.setColor( new Color(0.627451f, 0.627451f, 0.627451f,0.0f) );
        g2d.fill(rightCard);
        g2d.fill(rightDummyCard);
      }

      // Pop off middle /Main
      if( policyMiddle <  masterPolicyData.size() )
      {
        PolicyData d = masterPolicyData.get(currentPol);
        g2d.setColor( new Color(0.627451f, 0.627451f, 0.627451f, mAlpha) );
        g2d.fill(middleCard);
        g2d.setColor(borderMColor);
        g2d.draw(middleCard);
      }
    }
    g2d.setColor(Color.RED);
    // The images wouldn't line up with were the jpanel it is referencing is lining up....
    g2d.drawImage(lArrow,leftArrow.getX(),  (leftArrow.getY()+middleCon.getY()), leftArrow.getWidth(),  leftArrow.getHeight(), null);
    g2d.drawImage(rArrow,rightArrow.getX(),  (rightArrow.getY()+middleCon.getY()), rightArrow.getWidth(),  rightArrow.getHeight(), null);
  }

  /**
   * Override Mouse Clicked
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseClicked(MouseEvent e)
  {
    /* Do nothing */
  }

  /**
   * Override Mouse Pressed
   *
   * @param e MouseEvent
   */
  @Override
  public void mousePressed(MouseEvent e) {
    JPanel tempPnl = (JPanel) e.getSource();
    String name = tempPnl.getName();
    switch(name) {
      case "topCon":
        // Let make a custom pop up to annoy the user because, I can!
        dragFrom = e.getPoint();
        break;
      default:
        break;
    }
  }

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
      case "<":
        previous();
        break;
      case ">":
        next();
        break;
      case "SPONSOR":
        // Do something
        PolicyData d = masterPolicyData.get(currentPol);
        d.setSponsor("SPONSORED");

        sponsorLabel.setText(d.getSponsor());
        sponsor.setName(d.getSponsor());

        trigger.sponsoredBill(d);
        this.setVisible(false);
        break;
      default:
        break;
    }
  }

  /**
   * Move to the next card
   */
  private void next()
  {
    if( currentPol < masterPolicyData.size()-1 )
    {
      currentPol++;
      nextSelect = true;
      fancyFlip();
    }
    checkSides();
  }

  /**
   * Move to the previous card
   */
  private void previous()
  {
    if( currentPol > 0 )
    {
      currentPol--;
      previousSelect = true;
      fancyFlip();
    }
    checkSides();
  }

  /**
   * Check the sides to toggle whether or not the side(s) should be shown
   */
  private void checkSides()
  {
    if( currentPol < masterPolicyData.size()-1 )  hideRight = false;
    else hideRight = true;

    if( currentPol > 0 )  hideLeft = false;
    else hideLeft = true;
  }


  /**
   * Overridden mouseWheelMoved controls zooming on the map
   * @param e MouseWheelEvent fired by mouse wheel motion
   */
  @Override
  public void mouseWheelMoved(MouseWheelEvent e)
  {
    if( e.getPreciseWheelRotation() > 0 )
    {
      previous();
    }
    else
    {
      next();
    }
  }

  /**
   * Override Mouse Entered
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseEntered(MouseEvent e)
  {
    JPanel tempPnl = (JPanel) e.getSource();
    String name = tempPnl.getName();
    switch(name)
    {
      case "SPONSOR":
        sponsor.setOpaque(true);
        sponsor.setBackground(new Color(0xD9D9D9));
        repaint();
        break;
      default:
        break;
    }
  }

  /**
   * Override Mouse Exited
   *
   * @param e MouseEvent
   */
  @Override
  public void mouseExited(MouseEvent e)
  {
    JPanel tempPnl = (JPanel) e.getSource();
    String name = tempPnl.getName();
    switch(name)
    {
      case "SPONSOR":
        sponsor.setOpaque(false);
        repaint();
        break;
      default:
        break;
    }
  }

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
  public void mouseDragged(MouseEvent e)
  {
    double dx = (dragFrom.getX() - e.getPoint().getX() );
    double dy = (dragFrom.getY() - e.getPoint().getY() );
    getParent().repaint();
    setLocation( (int) (x-dx), (int) (y-dy) );
    getParent().repaint();
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
    switch(name) {
      default:
        break;
    }
  }

  /**
   * Key actions for the left Navigation
   */
  private Action leftNavigate = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      previous();
    }
  };

  /**
   * Key actions for the right Navigation
   */
  private Action rightNavigate = new AbstractAction()
  {
    @Override
    public void actionPerformed(ActionEvent e)
    {
      next();
    }
  };

}
