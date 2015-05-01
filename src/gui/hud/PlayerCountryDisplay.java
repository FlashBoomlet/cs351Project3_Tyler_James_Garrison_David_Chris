package gui.hud;

import gui.GUIRegion;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by James Lawson on 4/27/2015.
 *
 * display class to show specific country info
 */
public class PlayerCountryDisplay extends JPanel implements MouseListener
{

  boolean highlight = false;
  private Color highlightColor = new Color(255,255,255,100);

  private Font title = new Font(Font.SANS_SERIF,Font.BOLD,16);

  private JLabel nameLabel;

  //players country
  private GUIRegion playerCountry;

  /**
   * initializes everything needed
   *
   * @param playerCountry
   * @param width
   * @param height
   */
  public PlayerCountryDisplay(GUIRegion playerCountry,int width, int height)
  {
    this.playerCountry = playerCountry;
    this.setPreferredSize(new Dimension(width, height));
    this.setLocation(0, 0);
    this.addMouseListener(this);
    this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    this.setToolTipText("click for data");

    nameLabel = new JLabel(playerCountry.getName());
    nameLabel.setFont(title);
    nameLabel.setForeground(Color.WHITE);
    this.add(nameLabel);

  }

  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.DARK_GRAY);
    g2.fillRect(0, 0, this.getWidth(), this.getHeight());

    g2.drawImage(playerCountry.getFlag(), getWidth()-55, -8, null);

    if (highlight)
    {
      g2.setColor(highlightColor);
      g2.fillRect(0, 0, this.getWidth(), this.getHeight());
    }

  }


  @Override
  public void mouseClicked(MouseEvent e)
  {

  }

  @Override
  public void mousePressed(MouseEvent e)
  {

  }

  @Override
  public void mouseReleased(MouseEvent e)
  {
    //animate the panel
    Game.playerCountryInfo.animate();
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
