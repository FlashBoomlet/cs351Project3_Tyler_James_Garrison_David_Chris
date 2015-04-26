package gui.hud;

import IO.PolicyCSVParser;
import gui.ColorsAndFonts;
import model.PolicyData;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by Lyncht on 4/23/15.
 */
public class CardSelector extends JPanel implements ActionListener
{
  private int x;
  private int y;
  private int width;
  private int height;
  private JButton close;
  private ArrayList<PolicyData> masterPolicyData = new ArrayList<>();
  private int policyMiddle;

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
    policyMiddle = (masterPolicyData.size())/2;

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


    topCon = new JPanel();
    topCon.setOpaque(false);
    topCon.setSize(width,(int)(height*(.10)));
    JLabel title = new JLabel(label);
    topCon.add(title);
    close = new JButton("CLOSE");
    close.addActionListener(this);
    topCon.add(close);
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
    String name = tempBtn.getText();

    if( name == "CLOSE" )
    {
      this.setVisible(false);
    }
    else if ( name == "SPONSOR" )
    {
      // Do something
      sponsor.setText("SPONSORED");
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
        PolicyData d = masterPolicyData.get(policyMiddle);
        g.setColor( new Color(0.627451f, 0.627451f, 0.627451f, 1.0f) );
        g.fillRect(realX, my, realWidth, realHeight);
      }
    }
  }


  private void drawCard()
  {

    PolicyData d = masterPolicyData.get(policyMiddle);
    middleCon.setLayout(null);

    policyTA.setText(d.getPolicy());
    policyTA.setLocation((int) (realX+ realWidth*(.05)),realY);
    policyTA.setSize((int) (realWidth*(.90)) / 2, (int) (realHeight * (.05)));
    policyTA.setOpaque(false);
    middleCon.add(policyTA);

    descriptionTA.setText(d.getDescription());
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
  }


  /*
   * Functions to help out with creating and bringing in the policy data
   */
  public void add(PolicyData d)
  {
    masterPolicyData.add(d);
  }
}
