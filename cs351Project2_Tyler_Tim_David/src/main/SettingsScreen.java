package main;

import gui.ColorsAndFonts;
import main.Game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * SettingsScreen creates a settings "pop up" display screen where the user can dominate the world
 * from! It handles everything settings for the user in a nice clean format so that they don't have
 * to memorize a bunch of settings.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/19/15
 */
public class SettingsScreen extends JPanel
{
  protected int x;
  protected int y;
  protected int width;
  protected int height;
  private final int SCALE_IN_FACTOR = 32;
  private JButton close;
  private SettingsCenterPanel center;
  private SettingsButtonPanel buttons;

  /**
   * Initializes a JPanel for all of the settings and adds the various components to it
   * This is the framework of the settings basically.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param frameWidth main frame's width
   * @param frameHeight main frame's height
   */
  public SettingsScreen(int frameWidth, int frameHeight)
  {
    super();
    x = frameWidth/SCALE_IN_FACTOR;
    y = frameHeight/SCALE_IN_FACTOR;
    width = frameWidth-(x*2);
    height = frameHeight-(y*2);

    setLayout(new BorderLayout(0,0));
    setLocation(x,y);
    setSize(width, height);
    setOpaque(false);

    buttons = new SettingsButtonPanel();
    add(buttons, BorderLayout.PAGE_END);

    center = new SettingsCenterPanel();
    add(center, BorderLayout.CENTER);
  }

  /**
   * Overrides the paint Components because I can, to set the opacity of the panel
   * The call to super is purposely left out because I am fighting with the side panel updating
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param g
   */
  @Override
  protected void paintComponent(Graphics g)
  {
    // Apply our own painting effect
    Graphics2D g2d = (Graphics2D) g.create();
    // 50% transparent Alpha
    g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.9f));
    g2d.setColor(ColorsAndFonts.OCEANS);
    g2d.fillRect(x,y,width,height);
  }

  /**
   * SettingsCenterPanel creates guts to the settings panel.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/20/15
   */
  private class SettingsCenterPanel extends JPanel
  {
    private int xC;
    private int yC;
    private int widthC;
    private int heightC;

    /**
     * Initializes a JPanel for all of the settings and adds the various components to it
     * This is the heart of the settings basically.
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     */
    SettingsCenterPanel()
    {
      super();
      widthC = (int) (width*(.90));
      heightC = (int) (height*(.90));
      this.xC = (width-widthC)/2;
      this.yC = (height-heightC)/2;

      setLocation(xC,yC);
      setSize(widthC, heightC);
      setBackground(Color.cyan);
    }
  }

  /**
   * SettingsButtonPanel creates guts to the button panel at the bottom of the screen.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/20/15
   */
  private class SettingsButtonPanel extends JPanel implements ActionListener
  {
    /**
     * Initializes a JPanel for all of the buttons in the bottom of the screen and formats them
     * This is the heart of the buttons basically.
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     */
    SettingsButtonPanel()
    {
      super();
      setLayout(new GridLayout(1, 5));
      setOpaque(false);
      close = new JButton("CLOSE");
      close.setName("CLOSE");
      close.addActionListener(this);

      for( int i = 0; i <4; i++)
      {
        JPanel tempPanel = new JPanel();
        tempPanel.setOpaque(false);
        add(tempPanel);
      }
      add(close);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
      JButton tempBtn = (JButton) e.getSource();
      String name = tempBtn.getName();

      if( name == "CLOSE" )
      {
        hideEverything();
        Game.settingsDisplay(false);
      }
    }
  }

  public void hideEverything()
  {
    close.setVisible(false);
    center.setVisible(false);
    buttons.setVisible(false);
    this.setVisible(false);
  }

  public void showEverything()
  {
    close.setVisible(true);
    center.setVisible(true);
    buttons.setVisible(true);
    this.setVisible(true);
  }

}
