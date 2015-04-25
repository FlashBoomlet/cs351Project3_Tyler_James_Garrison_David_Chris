package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by James Lawson on 4/25/2015.
 *
 * class that will show info about the players country,
 * that they are playing as.
 */
public class PlayerCountryInfo extends JPanel implements MouseListener
{

  private final int MIN_HEIGHT = 20;
  private final int MAX_HEIGHT = 300;
  private final int STANDARD_WIDTH;

  private OpenerThread openerThread;

  // if true all info will be displayed
  //if false show a small collapsed version
  boolean isOpen = false;

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


    this.setFocusable(true);
    this.setSize(width, MIN_HEIGHT);
    this.addMouseListener(this);
  }


  @Override
  protected void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;

    g2.setFont(ColorsAndFonts.HUD_TITLE);

    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(0, 0, this.getWidth(), this.getHeight());

    g2.setColor(Color.WHITE);

    g2.drawString("click for "+playerCountry.getName(),1, 12);
    g2.drawLine(0,19,this.STANDARD_WIDTH,19);

  }

  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

    if (openerThread == null || !openerThread.isAlive())
    {
      openerThread = new OpenerThread();
      openerThread.start();
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

  /*
   * runs to set the size of the panel through an
   * animation, a thread is used so that players
   * can continue to manipulate the map while the resizing is happening
   */
  private class OpenerThread extends Thread
  {

    //I just played with the values until I got something that felt right
    @Override
    public void run()
    {
      if (isOpen)
      {
        for (int i = MAX_HEIGHT; i>= MIN_HEIGHT;i-=5)
        {
          setSize(STANDARD_WIDTH,i);

          try {
            this.sleep(4);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      else
      {
        for (int i = MIN_HEIGHT; i<=MAX_HEIGHT; i+=5) {
          setSize(STANDARD_WIDTH, i);

          try {
            this.sleep(4);
          }
          catch (InterruptedException e) {
            e.printStackTrace();
          }
        }
      }
      isOpen = !isOpen;
      this.interrupt();
    }
  }
}
