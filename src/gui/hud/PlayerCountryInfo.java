package gui.hud;

import gui.GUIRegion;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Created by James Lawson on 4/25/2015.
 *
 * class that will show info about the players country,
 * that they are playing as.
 */
public class PlayerCountryInfo extends JPanel implements ActionListener
{
  private final int STEP_SIZE = 10;
  private final int WSTEP_SIZE = (int)((double)STEP_SIZE/1.66666d);

  private final int MIN_HEIGHT;
  private final int MAX_HEIGHT;
  private final int MIN_WIDTH;
  private final int MAX_WIDTH;

  private int currentHeigtht;
  private int currentWidth;



  private ClickPanel clickPanel;


  private Border border = BorderFactory.createRaisedBevelBorder();

  private PlayerCountryDisplay playerCountryDisplay;

  private BufferedImage flagIMG;


  // if true all info will be displayed
  //if false show a small collapsed version
  boolean isOpen = false;
  boolean useAnimation = true;


  private Timer timer;

  //players country for access to information
  private GUIRegion playerCountry;

  /**
   *
   * @param playerCountry country the player is controlling
   */
  public PlayerCountryInfo(GUIRegion playerCountry, int Maxwidth)
  {
    this.playerCountry = playerCountry;
    flagIMG = playerCountry.getFlag();

    MIN_WIDTH = flagIMG.getWidth();
    MAX_WIDTH = Maxwidth;

    MIN_HEIGHT = flagIMG.getHeight();
    MAX_HEIGHT = 400;

    currentWidth = MIN_WIDTH;
    currentHeigtht = MIN_HEIGHT;


    timer = new Timer(30, this);

    clickPanel = new ClickPanel();



    playerCountryDisplay = new PlayerCountryDisplay(playerCountry,MAX_WIDTH,MAX_HEIGHT-MIN_HEIGHT);
    playerCountryDisplay.setLocation(0, MIN_HEIGHT);


    this.setLayout(null);
    this.setBorder(border);
    this.setSize(MIN_WIDTH, MIN_HEIGHT);
    this.setBackground(Color.GRAY);
    this.setFocusable(true);


    this.add(clickPanel);
    this.add(playerCountryDisplay);

  }


  public GUIRegion getPlayerCountry()
  {
    return playerCountry;
  }

  public void paintComponent(Graphics g)
  {
    g.setColor(Color.GRAY);
    g.fillRect(0,0,getWidth(),getHeight());

    g.setColor(Color.WHITE);
    g.drawString(playerCountry.getName(), 70, 20);
  }

  @Override
  public void actionPerformed(ActionEvent e)
  {



    if (isOpen)
    {
      currentHeigtht -= STEP_SIZE;
      if (currentWidth > MIN_WIDTH)
      {
        currentWidth -= WSTEP_SIZE;
        setLocation(getX()+WSTEP_SIZE, getY());
      }
      else
      {
        currentWidth = MIN_WIDTH;
      }
    }
    else
    {
      currentHeigtht+= STEP_SIZE;
      if (currentWidth < MAX_WIDTH)
      {
        currentWidth += WSTEP_SIZE;
        setLocation(getX()-WSTEP_SIZE, getY());
      }
      else
      {
        currentWidth = MAX_WIDTH;
      }
    }

    if (currentHeigtht > MAX_HEIGHT)
    {
      currentHeigtht = MAX_HEIGHT;
      currentWidth = MAX_WIDTH;
      setSize(MAX_WIDTH,MAX_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else if(currentHeigtht < MIN_HEIGHT)
    {
      currentHeigtht = MIN_HEIGHT;
      currentWidth = MIN_WIDTH;
      setSize(MIN_HEIGHT,MIN_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else
    {
      setSize(currentWidth,currentHeigtht);
      //call to fix the jittering
      getParent().repaint();
    }
  }


  private class ClickPanel extends JPanel implements MouseListener
  {
    private boolean highlight = false;

    private Color highlightColor = new Color(255,255,255,100);

    public ClickPanel()
    {
      this.setBounds(0, 0, MIN_WIDTH, MIN_WIDTH);
      this.setCursor(new Cursor(Cursor.HAND_CURSOR));
      this.addMouseListener(this);
      this.setToolTipText("click");
    }


    public void paintComponent(Graphics g)
    {
      g.drawImage(flagIMG,0,-7,null);

      if (highlight) {
        g.setColor(highlightColor);
        g.fillRect(0,0,getWidth(),getHeight()-15);
      }
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
          setSize(MIN_WIDTH, MIN_HEIGHT);
        }
        else
        {
          setSize(MIN_HEIGHT, MAX_HEIGHT);
        }
        isOpen =!isOpen;
      }


    }

    @Override
    public void mouseEntered(MouseEvent e)
    {
      highlight = true;
    }

    @Override
    public void mouseExited(MouseEvent e)
    {
      highlight = false;
    }


  }


}
