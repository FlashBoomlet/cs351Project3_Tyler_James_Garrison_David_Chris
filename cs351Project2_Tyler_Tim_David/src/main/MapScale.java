package main;

import gui.Camera;
import gui.ColorsAndFonts;
import gui.WorldPresenter;
import gui.displayconverters.DisplayUnitConverter;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Created by Lyncht on 4/4/15.
 */
public class MapScale extends JPanel
{

  private WorldPresenter worldPresenter;
  private Camera cam;
  private DisplayUnitConverter converter = new DisplayUnitConverter();
  private double scale = 0;
  private String scale1 = "";
  private String scale2 = "" ;
  private int s2 = 100;
  private int s1 = s2/2;
  private static final Font FONT = new Font("SansSerif", Font.PLAIN, 10);

  MapScale(int x, int y, int width, int height, WorldPresenter worldPresenter)
  {
    super();
    this.worldPresenter = worldPresenter;
    setOpaque(false);

    setLocation(x, y);
    setSize(width, height);
  }

  public void updateScale()
  {
    // get scale from world presenter
    s1 = 100;
    s1 = s2/2;
  }

  @Override
  public void paint(Graphics g)
  {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.RED);

    g2d.drawRect( 0,0,getWidth()-1,getHeight()/2 );
    int scale2X = 70;
    int scale2Y = 10;
    g2d.fillRect( 50,0,100,getHeight()/2 );

    g2d.drawRect( 0,getHeight()/2,getWidth()-1,getHeight()/2 );
    int scale1X = 5;
    int scale1Y = (getHeight() / 2)+10;
    g2d.fillRect(0, getHeight() / 2, 50, getHeight() / 2);

    g2d.setColor(ColorsAndFonts.GUI_TEXT_COLOR);
    g2d.setFont(FONT);

    scale1 = s1 + " " + converter.getScaleSymbol( SettingsScreen.getUnits() ) ;
    scale2 = s2  + " " +  converter.getScaleSymbol( SettingsScreen.getUnits() ) ;

    g2d.drawString(scale2, scale2X, scale2Y);
    g2d.drawString(scale1, scale1X, scale1Y);
  }
}
