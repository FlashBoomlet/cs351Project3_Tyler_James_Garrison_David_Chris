package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

/**
 * Created by jd505_000 on 4/27/2015.
 */
public class PlayerCountryDisplay extends JPanel
{

  private Font title = new Font(Font.SANS_SERIF,Font.BOLD,18);
  private Border border = BorderFactory.createRaisedBevelBorder();


  private GUIRegion playerCountry;

  public PlayerCountryDisplay(GUIRegion playerCountry,int width, int height)
  {
    this.playerCountry =playerCountry;
    this.setSize(width,height);
    this.setBorder(border);
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
    g2.drawString("United Sates of America", 10,  15);
    g2.drawLine(0,20,getWidth(),20);

    drawGeneralCountryInfo(g2, playerData);
  }

  private void drawGeneralCountryInfo( Graphics g, CountryData data)
  {
    int yPos = 35;
    g.setFont(ColorsAndFonts.HUD_TITLE);

    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Population: " + (int) (data.getPopulation(true)), 3, yPos);


    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Percent undernourished: " +  (data.getUndernourish(true))+"%", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Corn: " + (int) (data.getCornTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Wheat: " + (int) (data.getWheatTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Rice: " +  (int)(data.getRiceTotal(true))+" metric tons", 3, yPos);

    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Soy: " + (int) (data.getSoyTotal(true))+" metric tons", 3, yPos);


    yPos+=20;
    g.setColor(Color.BLACK);
    g.fillRect(0, yPos - 12, getWidth(), ColorsAndFonts.HUD_TITLE.getSize() + 4);

    g.setColor(Color.WHITE);
    g.drawString("Total Other: " +  (int)(data.getOtherTotal(true))+" metric tons", 3, yPos);

  }
}
