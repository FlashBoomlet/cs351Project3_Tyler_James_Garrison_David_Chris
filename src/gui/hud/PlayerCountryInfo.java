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

  PlayerCountryDisplay playerCountryDisplay;


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
    clickPanel.setCursor(new Cursor(Cursor.HAND_CURSOR));
    clickPanel.setBackground(Color.GRAY);
    clickPanel.setToolTipText("click");

    clickLabel = new JLabel();
    clickLabel.setText("Click for United States Of America");
    clickLabel.setBackground(Color.GRAY);


    clickPanel.add(clickLabel);



    playerCountryDisplay = new PlayerCountryDisplay(playerCountry,STANDARD_WIDTH,MAX_HEIGHT-MIN_HEIGHT);
    playerCountryDisplay.setLocation(0,MIN_HEIGHT);

    this.setLayout(null);
    this.setBorder(border);
    this.setSize(width, MIN_HEIGHT);

    this.add(clickPanel);
    this.add(playerCountryDisplay);

  }



  //===========================================LISTENERS============================================


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
  public void mouseEntered(MouseEvent e)
  {
    clickPanel.setBackground(Color.LIGHT_GRAY);
  }

  @Override
  public void mouseExited(MouseEvent e)
  {
    clickPanel.setBackground(Color.GRAY);
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
