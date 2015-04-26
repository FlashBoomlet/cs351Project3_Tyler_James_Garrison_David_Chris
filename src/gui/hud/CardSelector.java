package gui.hud;

import IO.PolicyCSVParser;
import gui.ColorsAndFonts;
import model.PolicyData;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lyncht on 4/23/15.
 */
public class CardSelector extends JPanel implements ActionListener, MouseListener
{
  private int x;
  private int y;
  private int width;
  private int height;
  private JButton close;
  private ArrayList<PolicyData> masterPolicyData = new ArrayList<>();
  private int policyMiddle;
  private int currentPol;

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
   * Components for the main card
   */
  private JTextArea policyTA = new JTextArea();
  private JTextArea descriptionTA = new JTextArea();
  private JLabel pro = new JLabel( "PRO: ");
  private JLabel con = new JLabel( "CON: ");
  private JButton sponsor = new JButton("SPONSOR");
  private JPanel rightArrow;
  private JPanel leftArrow;
  private BufferedImage lArrow;
  private BufferedImage rArrow;
  private String lArrowPath = "resources/images/leftArrow.png";
  private String rArrowPath = "resources/images/rightArrow.png";
  /*
   *
   */
  private JPanel middleCon;
  private JPanel topCon;


  /**
   * Class constructor
   */
  public CardSelector(int x, int y, int width, int height,String label)
  {
    super();

    new PolicyCSVParser(this);
    currentPol = policyMiddle = (masterPolicyData.size())/2;

    setOpaque(true);
    setBackground(ColorsAndFonts.GUI_BACKGROUND);
    setSize(new Dimension(width, height));
    setLocation(x,y);
    setLayout(new BorderLayout());

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;


    cardWidth = width /2;
    cardHeight = height /2;
    mx = cardWidth/2;
    my = (int) (getHeight() * (.20));
    lx = mx - (cardWidth + (cardWidth*(5/8)));
    ly = my - 20;
    rx = mx + (cardWidth + cardWidth*(1/8));
    ry = my - 20;
    realWidth = (int) (cardWidth * (1.25));
    realHeight = (int) (cardHeight * (1.25));
    realX = (int) (mx * (.75));
    realY = (int) (my * (.65));

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
    topCon.setSize(width,(int)(height*(.10)));
    JLabel title = new JLabel(label);
    topCon.add(title);
    close = new JButton("CLOSE");
    close.setName("CLOSE");
    close.addActionListener(this);
    topCon.add(close);
    close.setLocation(width-close.getWidth(),0);
    add(topCon, BorderLayout.NORTH);

    // Really just a way of adding padding because I'm lazy
    middleCon = new JPanel();
    middleCon.setOpaque(false);
    middleCon.setSize(width, (int) (height * (.90)));
    add(middleCon, BorderLayout.CENTER);

    repaint();
    drawCard();
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
      case "CLOSE":
        this.setVisible(false);
        break;
      case "SPONSOR":
        // Do something
        sponsor.setText("SPONSORED");
        break;
      default:
    }
  }


  /**
   * Override paint components
   * @param g graphics you with to have
   */
  @Override
  public void paintComponent(Graphics g)
  {
    super.paintComponent(g);
    // Pop off left
    if( policyMiddle >= 0 )
    {
      g.setColor( new Color(1f,1f,1f,0.75f) );
      if( policyMiddle-1 >= 0 )
      {
        g.fillRect(lx,ly,cardWidth,cardHeight);
        //g.drawString(card.getDescription(),lx,ly);
      }

      // Pop off right
      if( policyMiddle+1 < masterPolicyData.size())
      {
        g.fillRect(rx,ry,cardWidth,cardHeight);
        //g.drawString(card.getDescription(),rx,ry);
      }

      g.setColor( new Color(.25f,.25f,.25f,0.75f) );
      g.fillRect(0,(int)(height*(.10)),width,(int)(height*(.90)));

      // Pop off middle /Main
      if( policyMiddle <  masterPolicyData.size() )
      {
        PolicyData d = masterPolicyData.get(currentPol);
        g.setColor( new Color(0.627451f, 0.627451f, 0.627451f, 1.0f) );
        g.fillRect(realX, my, realWidth, realHeight);
      }
    }
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.RED);
    // The images wouldn't line up with were the jpanel it is referencing is lining up....
    g2d.drawImage(lArrow,(int) leftArrow.getX(), (int) (leftArrow.getY()*(1.275)), (int) leftArrow.getWidth(), (int) leftArrow.getHeight(), null);
    g2d.drawImage(rArrow,(int) rightArrow.getX(), (int) (rightArrow.getY()*(1.275)), (int) rightArrow.getWidth(), (int) rightArrow.getHeight(), null);
  }


  private void drawCard()
  {

    PolicyData d = masterPolicyData.get(policyMiddle);
    updateCard(d);
    middleCon.setLayout(null);


    policyTA.setLocation((int) (realX+ realWidth*(.05)),realY);
    policyTA.setSize((int) (realWidth*(.90)) / 2, (int) (realHeight * (.05)));
    policyTA.setOpaque(false);
    middleCon.add(policyTA);


    descriptionTA.setBounds(policyTA.getX(), policyTA.getHeight() + policyTA.getY() + 10, (int) (realWidth*(.90)), (int) (realHeight * (.20)));
    descriptionTA.setLineWrap(true);
    descriptionTA.setOpaque(false);
    middleCon.add(descriptionTA);

    pro.setBounds(descriptionTA.getX(), descriptionTA.getY()+descriptionTA.getHeight() + 10, (int) (realWidth*(.90))/2, (int) (realHeight *(.05)));
    pro.setOpaque(false);
    middleCon.add(pro);

    con.setBounds(pro.getX()+pro.getWidth(), pro.getY(), pro.getWidth(), pro.getHeight());
    con.setOpaque(false);
    middleCon.add(con);

    /*
     * Place graph of pro here
     */
    JPanel proGraph = new JPanel();
    proGraph.setOpaque(true);
    proGraph.setBackground(Color.PINK);
    proGraph.setBounds(pro.getX(), pro.getY() + pro.getHeight(), pro.getWidth(), (int)(con.getWidth()*(1.0)));
    middleCon.add(proGraph);

    JPanel conGraph = new JPanel();
    conGraph.setOpaque(true);
    conGraph.setBackground(Color.CYAN);
    conGraph.setBounds(con.getX(),con.getY()+con.getHeight(),con.getWidth(),proGraph.getHeight());
    middleCon.add(conGraph);

    /*
     * Place graph of con here
     */
    sponsor.addActionListener(this);
    middleCon.add( sponsor );


    int arrowW = width/16;
    int arrowH = height/4;
    int arrowY = realY+realHeight/4;
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
  }

  private void updateCard(PolicyData d)
  {
    policyTA.setText(d.getPolicy());
    descriptionTA.setText(d.getDescription());
  }
  /*
   * Functions to help out with creating and bringing in the policy data
   */
  public void add(PolicyData d)
  {
    masterPolicyData.add(d);
  }


  @Override
  public void mouseClicked(MouseEvent e)
  {
    /* Do nothing */
  }

  @Override
  public void mousePressed(MouseEvent e) { /* Do nothing */ }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    JPanel tempPnl = (JPanel) e.getSource();
    String name = tempPnl.getName();
    switch(name) {
      case "<":
        if( currentPol > 0 ) currentPol--;
        break;
      case ">":
        if( currentPol < masterPolicyData.size()-1 ) currentPol++;
        break;
      default:
    }
    PolicyData d = masterPolicyData.get(currentPol);
    updateCard(d);
  }

  @Override
  public void mouseEntered(MouseEvent e) { /* Do nothing */ }

  @Override
  public void mouseExited(MouseEvent e) { /* Do nothing */ }
}
