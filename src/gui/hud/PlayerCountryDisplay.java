package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import main.Game;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by James Lawson on 4/27/2015.
 *
 * display class to show specific country info
 */
public class PlayerCountryDisplay extends JPanel
{

  private JPanel holderPanel;

  //JPanel prettiness
  private Font title = new Font(Font.SANS_SERIF,Font.BOLD,18);
  private Border border = BorderFactory.createRaisedBevelBorder();

  //holds the display bars
  private ArrayList<InfoBar> bars = new ArrayList<>();

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
    this.setSize(width, height);


    this.setLayout(null);
    holderPanel = new JPanel();
    holderPanel.setBounds(0,40,this.getWidth(),this.getHeight()-21);
    holderPanel.setBackground(Color.GRAY);
    holderPanel.setBorder(border);

    this.add(holderPanel);


    setBars();
    addBars();
  }

  private void setBars()
  {
    CountryData data = playerCountry.getCountryData();



    InfoBar pop = new InfoBar("Population: " + NumberFormat.getIntegerInstance().format((int) data.getPopulation(true)),
            "population");

    Dimension barDim = new Dimension(this.getWidth(),20);

    pop.setPreferredSize(barDim);
    pop.setMaximumSize(barDim);
    pop.setMinimumSize(barDim);

    bars.add(pop);

    InfoBar mal = new InfoBar("Malnourished: " + NumberFormat.getIntegerInstance().format((int) data.getUndernourish(true))+"%",
            "percent of pop that is malnourished");


    mal.setPreferredSize(barDim);
    mal.setMaximumSize(barDim);
    mal.setMinimumSize(barDim);

    bars.add(mal);

    InfoBar corn = new InfoBar("Corn: " + NumberFormat.getIntegerInstance().format((int) data.getCornTotal(true)),
            "In Metric Tons");

    corn.setPreferredSize(barDim);
    corn.setMaximumSize(barDim);
    corn.setMinimumSize(barDim);

    bars.add(corn);

    InfoBar wheat = new InfoBar("Wheat: " + NumberFormat.getIntegerInstance().format((int) data.getWheatTotal(true)),
            "In Metric Tons");

    wheat.setPreferredSize(barDim);
    wheat.setMaximumSize(barDim);
    wheat.setMinimumSize(barDim);

    bars.add(wheat);

    InfoBar rice = new InfoBar("Rice: " + NumberFormat.getIntegerInstance().format((int) data.getRiceTotal(true)),
            "In Metric Tons");

    rice.setPreferredSize(barDim);
    rice.setMaximumSize(barDim);
    rice.setMinimumSize(barDim);

    bars.add(rice);

    InfoBar soy = new InfoBar("Soy: " + NumberFormat.getIntegerInstance().format((int) data.getSoyTotal(true)),
            "In Metric Tons");

    soy.setPreferredSize(barDim);
    soy.setMaximumSize(barDim);
    soy.setMinimumSize(barDim);

    bars.add(soy);

    InfoBar other = new InfoBar("Other: " + NumberFormat.getIntegerInstance().format((int) data.getOtherTotal(true)),
            "In Metric Tons");

    other.setPreferredSize(barDim);
    other.setMaximumSize(barDim);
    other.setMinimumSize(barDim);

    bars.add(other);

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

    g2.drawImage(playerCountry.getFlag(),getWidth()/2-25,-4,null);

    g2.drawLine(0, 20, getWidth(), 20);
  }



//class to draw a very simple attribute bar
  private class InfoBar extends JPanel implements MouseListener
  {
    private String text;
    private String TTtext;

    private boolean highlight = false;




    public InfoBar(String text,String tooltip)
    {
      //System.out.println(text);

      this.text = text;
      TTtext = tooltip;
      this.setBorder(border);

      this.setVisible(true);

      this.addMouseListener(this);
      this.setToolTipText(tooltip);

    }

    public void setDisplayText(String text)
    {
      this.text = text;
    }

    public void setToolTip(String TTtext)
    {
      this.TTtext = TTtext;
    }

    @Override
    public void paintComponent(Graphics g)
    {
      g.setFont(ColorsAndFonts.HUD_TITLE);


      if (!highlight)
      {


        g.setColor(Color.BLACK);
        g.fillRect(3, 0, this.getWidth()-7, this.getHeight());

        g.setColor(Color.WHITE);
        g.drawString(text, 3, 14);

      }
      else
      {
        g.setColor(Color.WHITE);
        g.fillRect(3, 0, this.getWidth()-7, this.getHeight());

        g.setColor(Color.BLACK);
        g.drawString(text, 3, 14);
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
