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

    JPanel topCon = new JPanel();
    topCon.setOpaque(false);
    topCon.setSize(width,(int)(height*(.10)));
    JLabel title = new JLabel(label);
    topCon.add(title);
    close = new JButton("CLOSE");
    close.addActionListener(this);
    topCon.add(close);
    add(topCon, BorderLayout.NORTH);

    // Really just a way of adding padding because I'm lazy
    JPanel middleCon = new JPanel();
    middleCon.setOpaque(false);
    middleCon.setSize(width, (int) (height * (.90)));
    PolicyData d = masterPolicyData.get(policyMiddle);
    //middleCon.add(new JTextArea(d.getDescription()));
    add(middleCon, BorderLayout.CENTER);


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
  }


  /**
   * Override paint components
   * @param g graphics you with to have
   */
  @Override
  public void paint(Graphics g)
  {
    super.paint(g);
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
        g.setColor( new Color(1f,1f,1f,1.0f) );
        g.fillRect((int) (mx * (.75)), my, (int) (cardWidth * (1.25)), (int) (cardHeight * (1.25)));
        g.setColor(Color.RED);
        g.drawString(d.getDescription(),mx,my+20);
      }
    }

  }


  private void drawCard(Graphics g, PolicyData card, int x, int y)
  {
    super.paint(g);

    g.setColor( Color.white );
    g.fillRect(x,y,cardWidth,cardHeight);
    //g.drawString(card.getDescription(),x,y);
  }


  /*
   * Functions to help out with creating and bringing in the policy data
   */
  public void add(PolicyData d)
  {
    masterPolicyData.add(d);
  }
}
