package main;

import javax.swing.*;
import java.awt.*;

/**
 *
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.30.15
 */
public class Notification extends JComponent
{
  private Rectangle area;
  private float alpha = 0.0f;
  private float alphaMax = 0.75f;
  private String title = "fhsfnfgn";
  private String description = "cvnxnxcv";
  private OpenerThread openerThread;

  //Convert to ColorsAndFonts when then font is set.
  Font font = new Font("Tahoma", Font.BOLD, 14);
  private String frontPadding = " ";
  private JLabel blankTest;

  /**
   * Get Notifications
   *
   * WARNING! You must only set location and size off of Area parameter. Everything else must
   * then reference the container. Especially important when adding components to this.
   *
   * @param area -Location of the Container and the deminsions of the container.
   */
  public Notification(Rectangle area)
  {
    super();

    this.area = new Rectangle(area.getBounds());
    setOpaque(false);
    setLocation((int) area.getX(), (int) area.getY());
    setSize((int) area.getWidth(), (int) area.getHeight());

    blankTest = new JLabel("jhgjhgjhgjhgj");
    blankTest.setForeground(Color.RED);
    add(blankTest);
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

    g2d.setColor( new Color(1.0f,0.6f,0.0f,alpha));
    g2d.fillRect(0, 0,getWidth(),getHeight() );
    g2d.setColor( new Color(0.0f,0.0f,0.0f,alpha));
    int fontY = font.getSize();
    g2d.drawString(frontPadding + title, 0, fontY + getHeight()/4 );
    g2d.drawString(frontPadding + description, 0, fontY + getHeight()/2  );
  }

  /* More to come soon with this sexy beast
   * Apple watch taptic engine ain't got nothing on this notification system
   *
   * Take the parameter of an alert object in the future.
   */

  /**
   * Create a new news alert to be sent out
   *
   * @param title of the alert
   * @param description of the alert
   */
  public void updateAlert(String title, String description)
  {
    this.title = title;
    this.description = description;
    /*
     * create thread for silky smooth open and close
     */
    if (openerThread == null || !openerThread.isAlive())
    {
      openerThread = new OpenerThread();
      openerThread.start();
    }
  }

  /*
  * Secret feature of the updateAlert... Where the real magic happen. Not Disney World.
  */
  private class OpenerThread extends Thread
  {

    //I just played with the values until I got something that felt right
    @Override
    public void run()
    {
      float adjustBy = .005f;
      int mod = 10;
      float adjustTo = 150f;

      getParent().repaint();
      try
      {
        /*
         * Set the alpha to one
         */
        for (int i = 0; i < adjustTo; i++) {

          alpha += adjustBy;
          if( alpha == alphaMax ) break;
          this.sleep(mod);
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }



      // Pause! So the user can see the alert
      try
      {
        this.sleep(10000);
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }


      getParent().repaint();
      try
      {
        /*
         * Set the alpha to zero
         */
        for (int i = 0; i < adjustTo; i++) {

          if( alpha-adjustBy >= 0 ) alpha -= adjustBy;

          this.sleep(mod);
        }
      }
      catch (InterruptedException e)
      {
        e.printStackTrace();
      }
      this.interrupt();
    }
  }

}
