package main;

import gui.Camera;
import gui.ColorsAndFonts;
import gui.WorldPresenter;
import gui.displayconverters.DisplayUnitConverter;
import gui.displayconverters.EquirectangularConverter;

import javax.swing.*;
import java.awt.*;
import java.util.Set;

/**
 * Creates and houses anything that is needed to make the
 * map scale on the lower right hand corner function
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.4.2015
 */
public class MapScale extends JPanel
{

  private WorldPresenter worldPresenter;
  private Camera cam;
  private DisplayUnitConverter converter = new DisplayUnitConverter();
  private double scale = 0;
  private String scale1 = "";
  private String scale2 = "" ;
  private double s2;
  private double s1;
  private static final Font FONT = new Font("SansSerif", Font.PLAIN, 10);

  /**
   * Constructor to create a JPanel to hold the MapScale and all components
   * drawn inside.
   *
   * @param x location
   * @param y location
   * @param width of component
   * @param height of component
   * @param cam that controls the view
   */
  MapScale(int x, int y, int width, int height, Camera cam)
  {
    super();
    this.cam = cam;
    s2 = (cam.getScale());
    setOpaque(false);

    setLocation(x, y);
    setSize(width, height);
  }

  /**
   * Updates the scale label's
   * Called by Game.java once you change your zoom
   */
  public void updateScale()
  {
    // get scale from world presenter
    s2 = (cam.getScale());
    //Convert to English Miles if appropriate
    if( !SettingsScreen.getUnits() ) s2 *= 0.60934;
    s1 = s2/2;
    repaint();
  }

  /**
   * Override paint components
   * @param g graphics you with to have
   */
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

    scale1 = String.format( "%.1f" + " " + converter.getScaleSymbol( SettingsScreen.getUnits() ) , s1);
    scale2 = String.format( "%.1f" + " " + converter.getScaleSymbol( SettingsScreen.getUnits() ) , s2);

    g2d.drawString(scale2, scale2X, scale2Y);
    g2d.drawString(scale1, scale1X, scale1Y);
  }

}
