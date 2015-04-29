package gui.hud;

import gui.GUIRegion;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by James Lawson on 4/25/2015.
 *
 * class that will show info about the players country,
 * that they are playing as.
 */
public class PlayerCountryInfo extends JPanel implements ActionListener
{
  private final int STEP_SIZE = 10;
  private final int WSTEP_SIZE;

  private final int MIN_HEIGHT;
  private final int MAX_HEIGHT;
  private final int MIN_WIDTH;
  private final int MAX_WIDTH;

  private int currentHeight;
  private int currentWidth;


  private Border border = BorderFactory.createRaisedBevelBorder();


  // if true all info will be displayed
  //if false show a small collapsed version
  boolean isOpen = false;

  private Timer timer;

  //players country for access to information
  private GUIRegion playerCountry;

  /**
   *
   * @param playerCountry country the player is controlling
   */
  public PlayerCountryInfo(GUIRegion playerCountry,int x, int y, int Maxwidth, int Maxheight)
  {
    this.setLocation(x,y);

    this.playerCountry = playerCountry;

    MIN_WIDTH = 0;
    MAX_WIDTH = Maxwidth;

    MIN_HEIGHT = 0;
    MAX_HEIGHT = Maxheight;

    WSTEP_SIZE = (int)((double)STEP_SIZE/(((double)Maxheight/(double)Maxwidth)));

    currentWidth = MIN_WIDTH;
    currentHeight = MIN_HEIGHT;


    timer = new Timer(30, this);


    this.setLayout(null);
    this.setBorder(border);
    this.setSize(MIN_WIDTH, MIN_HEIGHT);
    this.setBackground(Color.GRAY);
    this.setFocusable(true);

  }


  public GUIRegion getPlayerCountry()
  {
    return playerCountry;
  }

  /**
   * call to start the timer
   */
  public void animate()
  {
    if (!timer.isRunning()) timer.start();
  }

  @Override
  public void paintComponent(Graphics g)
  {
    g.setColor(Color.DARK_GRAY);
    g.fillRect(0,0,getWidth(),getHeight());
  }


  /**
   *
   * animates the window out
   *
   * @param e unused
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {

    if (isOpen)
    {


      currentWidth -= WSTEP_SIZE;
      currentHeight -= STEP_SIZE;

      setLocation(getX()+WSTEP_SIZE,getY());

    }
    else
    {
      currentWidth += WSTEP_SIZE;
      currentHeight +=STEP_SIZE;

      setLocation(getX()-WSTEP_SIZE,getY());
    }

    if (currentHeight+STEP_SIZE > MAX_HEIGHT)
    {
      currentHeight = MAX_HEIGHT;
      currentWidth = MAX_WIDTH;
      setSize(MAX_WIDTH,MAX_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else if(currentHeight-STEP_SIZE < MIN_HEIGHT)
    {
      currentHeight = MIN_HEIGHT;
      currentWidth = MIN_WIDTH;
      setSize(MIN_HEIGHT,MIN_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else
    {
      setSize(currentWidth,currentHeight);
      //call to fix the jittering
      getParent().repaint();
    }
  }
}
