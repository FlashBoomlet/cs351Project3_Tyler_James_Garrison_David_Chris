package gui.hud;

import gui.ColorsAndFonts;
import gui.GUIRegion;
import gui.WorldPresenter;
import main.SettingsScreen;

import javax.swing.*;
import java.awt.*;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Random;

/**
 * PopulationAndHappiness extends JPanel and creHUD for all of the world
 * Population stats and the over all well being display
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4/5/15
 */
public class PopulationAndHappiness extends JPanel {

  private static WorldPresenter worldPresenter = main.Game.getWorldPresenter();
  private static int fontSize = 12;
  private static final Font FONT = new Font("SansSerif", Font.CENTER_BASELINE, fontSize);
  private static double worldPop = 0;
  private static int worldWellBeing = 0;
  private String pop = "";
  private String wellB = "";

  public PopulationAndHappiness(int x, int y, int width, int height)
  {
    super();
    setOpaque(false);
    update();
    setLocation(x,y);
    setPreferredSize(new Dimension(width, height));
    setLayout(new FlowLayout(SwingConstants.RIGHT,0,0));
  }

  public void update()
  {
    worldPop = 0;
    Random rand = new Random(43);
    worldWellBeing = rand.nextInt(100);

    for( GUIRegion gr : worldPresenter.getAllRegions())
    {
      if( gr.getCountryData() != null )
      {
        worldPop += gr.getCountryData().getPopulation(true);
      }
    }
    repaint();
  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);

    Graphics2D g2d = (Graphics2D) g;

    g2d.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    g2d.setFont(FONT);

    NumberFormat formatter = new DecimalFormat("#.##");

    pop = "Population: " + formatter.format(worldPop/1000000000) + " B";
    wellB = "Well-Being: " + (worldWellBeing) + "%";

    g2d.drawString(pop, 0, fontSize+5);
    g2d.drawString(wellB, getWidth()/2, fontSize+5);
  }
}
