package gui.hud;

import gui.GUIRegion;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.util.Random;

/**
 * Created by Lyncht on 4/5/15.
 */
public class PopulationAndHappiness extends JPanel {

  private static WorldPresenter worldPresenter = main.Game.getWorldPresenter();
  private static double worldPop = 0;
  private static double worldWellBeing = 0;

  public PopulationAndHappiness()
  {
    super();
    setOpaque(false);
  }

  public static void update()
  {
    worldPop = 0;
    Random rand = new Random(100);
    worldWellBeing = rand.nextInt();

    for( GUIRegion gr : worldPresenter.getAllRegions())
    {
      if( gr.getCountryData() != null )
      {
        worldPop += gr.getCountryData().getPopulation(true);
      }
    }

  }

  @Override
  public void paint(Graphics g) {
    super.paint(g);
  }
}
