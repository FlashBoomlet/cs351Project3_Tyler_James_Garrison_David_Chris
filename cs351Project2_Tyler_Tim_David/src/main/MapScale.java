package main;

import gui.ColorsAndFonts;

import javax.swing.*;
import java.awt.*;

/**
 * Created by Lyncht on 4/4/15.
 */
public class MapScale extends JPanel
{

  MapScale(int x, int y, int width, int height)
  {
    super();

    setOpaque(false);

    setLocation(x, y);
    setSize(width, height);
  }

  @Override
  public void paint(Graphics g)
  {
    super.paint(g);
    Graphics2D g2d = (Graphics2D) g;
    g2d.setColor(Color.RED);

    g2d.drawRect( 0,0,getWidth()-1,getHeight()/2 );
    g2d.fillRect( 50,0,100,getHeight()/2 );

    g2d.drawRect( 0,getHeight()/2,getWidth()-1,getHeight()/2 );
    g2d.fillRect(0, getHeight() / 2, 50, getHeight() / 2);
  }
}
