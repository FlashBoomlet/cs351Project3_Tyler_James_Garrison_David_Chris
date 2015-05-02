package gui.hud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by James Lawson on 5/1/2015.
 *
 * draws a bar with a gradient to show levels of a crop
 */
public class AvailableCropsBar extends JPanel implements MouseListener
{
  private String title;
  private Color color;
  private double cropTotal;
  private double cropNeeded;
  private double cropSpent;

  private int usedWidth = 0;
  private int spentWidth = 0;


  private LinearGradientPaint cropNeededPaint;
  private LinearGradientPaint cropSpentPaint;
  private Color highLightColor = new Color(255, 255, 255,100);

  private boolean highlight = true;

  private JLabel infoLabel = new JLabel();

  /**
   * sets the different values based on how much of the crop is needed, how
   * much has already been traded and how much is available to trade
   *
   * @param title
   * @param color
   * @param cropTotal
   * @param cropNeeded
   * @param cropSpent
   * @param width
   * @param height
   */
  public AvailableCropsBar(String title, Color color, double cropTotal, double cropNeeded,int cropSpent, int width, int height)
  {
    this.title = title;
    this.color = color;
    this.cropTotal = cropTotal;
    this.cropNeeded = cropNeeded;
    this.cropSpent = cropSpent;


    float[] frac = {0f,.5f,1.0f};
    Color[] colors = {Color.DARK_GRAY, Color.WHITE,Color.DARK_GRAY};
    cropNeededPaint = new LinearGradientPaint(0,0,width/20,0,frac,colors, MultipleGradientPaint.CycleMethod.REPEAT);
    Color[] colors2 = {color, Color.WHITE,color};
    cropSpentPaint = new LinearGradientPaint(0,0,width/20,0,frac,colors2, MultipleGradientPaint.CycleMethod.REPEAT);

    usedWidth = (int)((cropNeeded/cropTotal)*width);
    spentWidth = (int)((cropSpent/cropTotal)*width);

    infoLabel.setText(title+": used:"+(cropNeeded/cropTotal)*100+"%"+" traded:"+(cropSpent/cropTotal)*100+"%");
    infoLabel.setBounds(0, 20, width, 50);
    infoLabel.setForeground(Color.WHITE);

    this.addMouseListener(this);
    this.setPreferredSize(new Dimension(width, height));
    this.setSize(width, height);
    this.setLayout(null);
    this.add(infoLabel);

  }


  @Override
  public void paintComponent(Graphics g)
  {
    Graphics2D g2 = (Graphics2D) g;

    g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

    g2.setPaint(cropNeededPaint);
    g2.fillRect(0, 0, usedWidth, getHeight() - 50);

    g2.setPaint(cropSpentPaint);
    g2.fillRect(usedWidth, 0, usedWidth + spentWidth, getHeight() - 50);

    g2.setColor(color);
    g2.fillRect(usedWidth+spentWidth, 0, getWidth(), getHeight()-50);

    if (highlight)
    {
      g2.setColor(highLightColor);
      g2.fillRect(0,0,getWidth(),getHeight()-50);
    }

  }

  public void setCropVals(double cropTotal, double cropNeeded, double cropSpent)
  {
    this.cropTotal = cropTotal;
    this.cropNeeded = cropNeeded;
    this.cropSpent = cropSpent;

    this.repaint();
  }


  @Override
  public void mouseClicked(MouseEvent e) {

  }

  @Override
  public void mousePressed(MouseEvent e) {

  }

  @Override
  public void mouseReleased(MouseEvent e) {

  }

  @Override
  public void mouseEntered(MouseEvent e) {
      highlight = true;
  }

  @Override
  public void mouseExited(MouseEvent e) {
      highlight = false;
  }
}
