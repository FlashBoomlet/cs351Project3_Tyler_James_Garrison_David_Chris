package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by James Lawson on 4/25/2015.
 *
 * class that will show info about the players country,
 * that they are playing as.
 */
public class PlayerCountryInfo extends JPanel implements MouseListener, ActionListener
{
  private final int STEP_SIZE = 10;

  private final int MIN_HEIGHT = 30;
  private final int MAX_HEIGHT = 400;
  private final int STANDARD_WIDTH;

  private int currentHeigtht = MIN_HEIGHT;

  private JPanel clickPanel;
  private JLabel clickLabel;


  Border border = BorderFactory.createRaisedBevelBorder();

  private Font tile = new Font(Font.SANS_SERIF,Font.BOLD,18);


  // if true all info will be displayed
  //if false show a small collapsed version
  boolean isOpen = false;
  boolean useAnimation = true;


  Timer timer;

  //players country for access to information
  private GUIRegion playerCountry;

  /**
   *
   * @param playerCountry country the player is controlling
   */
  public PlayerCountryInfo(GUIRegion playerCountry, int width)
  {
    this.playerCountry = playerCountry;
    STANDARD_WIDTH = width;

    timer = new Timer(30, this);

    clickPanel = new JPanel();
    clickPanel.setBorder(border);
    clickPanel.setBounds(0, 0, width, MIN_HEIGHT);
    clickPanel.addMouseListener(this);

    clickLabel = new JLabel();
    clickLabel.setText("Click for data");
    clickLabel.setVerticalTextPosition(0);

    clickPanel.add(clickLabel);




    this.setLayout(null);
    this.setBorder(border);
    this.setSize(width, MIN_HEIGHT);

    this.add(clickPanel);

  }


  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.GRAY);
    g2.fillRect(0, 0, this.getWidth(), this.getHeight());


    g2.setColor(Color.WHITE);

     CountryData playerData = playerCountry.getCountryData();

    g.setFont(tile);
    g.drawString("United Sates of America", 10, MIN_HEIGHT + 15);
    g.drawLine(0,MIN_HEIGHT+20,STANDARD_WIDTH,MIN_HEIGHT+20);

    drawGeneralCountryInfo(g, playerData);

  }

  private void drawGeneralCountryInfo( Graphics g, CountryData data)
  {
    int yPos = MIN_HEIGHT +35;
    g.setFont(ColorsAndFonts.HUD_TITLE);

    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Population: " + (int) (data.getPopulation(true)), 3, yPos);


    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Percent undernourished: " +  (data.getUndernourish(true))+"%", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Corn: " + (int) (data.getCornTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Wheat: " + (int) (data.getWheatTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Rice: " +  (int)(data.getRiceTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Soy: " + (int) (data.getSoyTotal(true))+" metric tons", 3, yPos);


    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, STANDARD_WIDTH, ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Other: " +  (int)(data.getOtherTotal(true))+" metric tons", 3, yPos);





  }


  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

    if (useAnimation)
    {
      timer.start();
    }
    else
    {
      if (isOpen)
      {
        setSize(STANDARD_WIDTH, MIN_HEIGHT);
      }
      else
      {
        setSize(STANDARD_WIDTH, MAX_HEIGHT);
      }
      isOpen =!isOpen;
    }


  }

  @Override
  public void mouseEntered(MouseEvent e) {

  }

  @Override
  public void mouseExited(MouseEvent e) {

  }

  public GUIRegion getPlayerCountry()
  {
    return playerCountry;
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {

    if (isOpen)
    {
      currentHeigtht -= STEP_SIZE;
    }
    else
    {
      currentHeigtht+= STEP_SIZE;
    }

    if (currentHeigtht > MAX_HEIGHT)
    {
      currentHeigtht = MAX_HEIGHT;
      setSize(STANDARD_WIDTH,MAX_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else if(currentHeigtht < MIN_HEIGHT)
    {
      currentHeigtht = MIN_HEIGHT;
      setSize(STANDARD_WIDTH,MIN_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else
    {
      setSize(STANDARD_WIDTH,currentHeigtht);
      //call to fix the jittering
      getParent().repaint();
    }
  }


}
