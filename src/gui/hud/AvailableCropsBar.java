package gui.hud;

import javax.swing.*;
import java.awt.*;

/**
 * Created by James Lawson on 5/1/2015.
 *
 * draws a bar with a gradient to show levels of a crop
 */
public class AvailableCropsBar extends JPanel
{
  private String title;
  private Color color;
  private double cropTotal;
  private double cropNeeded;

  private int usedWidth = 0;


  private LinearGradientPaint gp;

  public AvailableCropsBar(String title, Color color, double cropTotal, double cropNeeded, int width, int height)
  {
    this.title = title;
    this.color = color;
    this.cropTotal = cropTotal;
    this.cropNeeded = cropNeeded;


    float[] frac = {0f,.5f,1.0f};
    Color[] colors = {Color.DARK_GRAY, Color.WHITE,Color.DARK_GRAY};
    gp = new LinearGradientPaint(0,0,width/10,height/5,frac,colors, MultipleGradientPaint.CycleMethod.REPEAT);

    usedWidth = (int)((cropNeeded/cropTotal)*width);

    this.setPreferredSize(new Dimension(width, height));
    this.setSize(width, height);

  }


  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setPaint(gp);
    g2.fillRect(0, 0, usedWidth, getHeight());

    g2.setColor(color);
    g2.fillRect(usedWidth, 0, getWidth(), getHeight());

  }

  public void setCropVals(double cropTotal, double cropNeeded)
  {
    this.cropTotal = cropTotal;
    this.cropNeeded = cropNeeded;

    this.repaint();
  }


}
