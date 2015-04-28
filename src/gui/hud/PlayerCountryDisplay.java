package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by James Lawson on 4/27/2015.
 */
public class PlayerCountryDisplay extends JPanel
{

  private JPanel holderPanel;

  private Font title = new Font(Font.SANS_SERIF,Font.BOLD,18);
  private Border border = BorderFactory.createRaisedBevelBorder();

  private ArrayList<InfoBar> bars = new ArrayList<>();

  private GUIRegion playerCountry;

  public PlayerCountryDisplay(GUIRegion playerCountry,int width, int height)
  {
    this.playerCountry = playerCountry;
    this.setSize(width, height);


    this.setLayout(null);
    holderPanel = new JPanel();
    holderPanel.setBounds(0,21,this.getWidth(),this.getHeight()-21);
    holderPanel.setBackground(Color.GRAY);
    holderPanel.setBorder(border);

    this.add(holderPanel);


    setBars();
    addBars();
  }

  private void setBars()
  {
    CountryData data = playerCountry.getCountryData();
    int yPos = 50;


    InfoBar bar = new InfoBar("Population: " + (int) data.getPopulation(true), "population");

    Dimension barDim = new Dimension(this.getWidth(),20);

    bar.setPreferredSize(barDim);
    bar.setMaximumSize(barDim);
    bar.setMinimumSize(barDim);

    bars.add(bar);
  }

  private void addBars()
  {
    for (InfoBar bar:bars)
    {
      bar.repaint();
      holderPanel.add(bar);
    }
  }

  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D)g;

    g2.setColor(Color.GRAY);
    g2.fillRect(0, 0, this.getWidth(), this.getHeight());


    g2.setColor(Color.WHITE);

    CountryData playerData = playerCountry.getCountryData();

    g2.setFont(title);
    g2.drawString("United Sates of America", 10, 17);
    g2.drawLine(0, 20, getWidth(), 20);
  }




  private class InfoBar extends JPanel implements MouseListener
  {
    private String text;

    private boolean highlight = false;




    public InfoBar(String text,String tooltip)
    {

      this.text = text;

      this.setVisible(true);

      this.addMouseListener(this);
      this.setToolTipText(tooltip);

    }

    @Override
    public void paintComponent(Graphics g)
    {
      g.setFont(ColorsAndFonts.HUD_TITLE);


      if (!highlight)
      {


        g.setColor(Color.BLACK);
        g.fillRect(3, getY(), this.getWidth()-7, this.getHeight());

        g.setColor(Color.WHITE);
        g.drawString(text, getX()+3, getY()+11);

      }
      else
      {
        g.setColor(Color.WHITE);
        g.fillRect(3, getY(), this.getWidth()-7, this.getHeight());

        g.setColor(Color.BLACK);
        g.drawString(text, getX()+3, getY()+11);
      }
    }

    @Override
    public void mouseClicked(MouseEvent e) { }

    @Override
    public void mousePressed(MouseEvent e) { }

    @Override
    public void mouseReleased(MouseEvent e) { }

    @Override
    public void mouseEntered(MouseEvent e) { highlight = true;}

    @Override
    public void mouseExited(MouseEvent e) { highlight = false;}
  }
}
