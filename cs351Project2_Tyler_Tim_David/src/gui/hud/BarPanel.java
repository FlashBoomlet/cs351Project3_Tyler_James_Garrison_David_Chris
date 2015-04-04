package gui.hud;

import IO.AttributeGenerator;
import gui.ColorsAndFonts;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.AlphaComposite;
import java.awt.event.*;

/**
 * Created by winston on 1/31/15.
 * Updated by Tyler Lynch on 3.20.15
 *
 * Draw a single bar chart graphic element (a bar with a label). This class is
 * used to build up the State panel objects of the GUI.
 */
public class BarPanel extends JPanel
{
  /* look and feel constants */
  private static final Font GUI_FONT = ColorsAndFonts.GUI_FONT;
  private static final Font OVERLAY_FONT = new Font("SansSerif", Font.PLAIN, 10);
  private static final Font POP_AND_HAPPY_FONT = new Font("SansSerif", Font.PLAIN, 11);
  private static final Color BAR_TEXT_C = Color.black;
  private static final Color TEXT_ROLLOVER_C = ColorsAndFonts.ACTIVE_REGION;
  private static final Color BAR_ROLLOVER_C = Color.gray;

  private final Color originalBarColor;
  private Color overLayTextColor;
  private Color chosenTextColor = BAR_TEXT_C;
  private Color barColor;
  private final JLabel label;
  private final double ratio;
  private String overLayText;
  private AdjustBox adjustBox;
  private JPanel dataPanel;
  private int adjustValue = 5;
  private BarPane barGraph;

  private static boolean showAdjust = false;
  private int animationStep = 0; /* used to start and stop animation */

  /**
   * Constructor for infoPanel BarPanels
   *
   * @param barColor    the barColor of the bar to be draw
   * @param ratio       a double between 0 and 1, 1 being 'full'.
   * @param labelText   String that will be displayGUIRegion labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the ratio passed in for example
   */
  public BarPanel(Color barColor, double ratio, String labelText, String overLayText, boolean adjustable)
  {
    showAdjust = adjustable;

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayTextColor = chosenTextColor;
    this.ratio = ratio;
    this.overLayText = overLayText;

    // 6000 is just to make things too big! fighting with swing.
    // 16 is height of each individual bar.
    Dimension size = new Dimension(6000, 50);
    setMaximumSize(size);

    label = new JLabel(labelText);
    barGraph = new BarPane();

    //config
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    label.setFont(GUI_FONT);
    label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setVerticalAlignment(SwingConstants.BOTTOM);

    if( showAdjust )
    {
      setLayout(new GridLayout(2,1) );

      adjustBox = new AdjustBox();
      JPanel top = new JPanel();
      top.setOpaque(false);
      top.setLayout(new GridLayout(1, 2));
      label.setFont( new Font("SansSerif", Font.PLAIN, 12) );
      top.add(label);
      top.add(adjustBox);
      add(top);

      JPanel lower = new JPanel();
      lower.setOpaque(false);
      lower.setLayout(new GridLayout(1, 2));
      lower.add(barGraph);
      //FORMATTER
      lower.add(new JLabel(""));
      add(lower);

    }
    else
    {
      setLayout(new GridLayout(1,2) );
      addMouseListener(getMouseListener());
      add(label);
      add(barGraph);
    }
  }

  /**
   * Constructor for class for use in World population and happiness
   *
   * @param barColor    the barColor of the bar to be draw
   * @param ratio       a double between 0 and 1, 1 being 'full'.
   * @param labelText   String that will be displayGUIRegion labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the ratio passed in for example
   */
  public BarPanel(Color barColor, double ratio, String labelText, String overLayText, int x, int y, int width, int height) //for use in World population and happiness
  {
    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    chosenTextColor = Color.white;
    this.overLayTextColor = chosenTextColor;
    this.ratio = ratio;
    this.overLayText = overLayText;

    // 6000 is just to make things too big! fighting with swing.
    // 16 is height of each individual bar.
    Dimension size = new Dimension(width, height);
    setMaximumSize(size);
    setLayout(new GridLayout(1,1));

    setLocation(x,y);

    label = new JLabel(labelText);
    barGraph = new PopAndHappyPane();

    dataPanel = new JPanel();
    dataPanel.setOpaque(false);
    dataPanel.setLayout(new GridLayout(1,2));
    dataPanel.add(label);
    dataPanel.add(barGraph);

    //config
    setBackground(ColorsAndFonts.OCEANS);

    //setPreferredSize(new Dimension(width, height));

    label.setFont(ColorsAndFonts.TOP_FONT);
    label.setForeground(chosenTextColor);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setVerticalAlignment(SwingConstants.TOP);
    addMouseListener(getMouseListener());

    add(dataPanel);
  }

  /**
   * set the label for the bar graph.
   * @param text
   */
  public void setLabelText(String text)
  {
    label.setText(text);
  }

  /**
   * creates a mouse listener to facilitate roll-over effects.
   */
  private MouseListener getMouseListener()
  {
    return new MouseAdapter()
    {
      @Override
      public void mouseEntered(MouseEvent e)
      {
        overLayTextColor = Color.white;
        barColor = BAR_ROLLOVER_C;
        label.setForeground(TEXT_ROLLOVER_C);
      }

      @Override
      public void mouseExited(MouseEvent e)
      {
        overLayTextColor = chosenTextColor;
        barColor = originalBarColor;
        label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      }
    };
  }

  /**
   * Generates an inner class to handle the custom drawing of the
   * bar.
   */
  private class PopAndHappyPane extends BarPane
  {
    int length = 0;
    PopAndHappyPane()
    {
      length = (int) (ratio * 130);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
      if (animationStep < length) animationStep += 2;
      Graphics2D g2d = (Graphics2D)g;
      g2d.setColor(Color.darkGray);
      g2d.drawRect(9,0,131,16);

      g2d.setColor(barColor);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.25f));

      // position info, animation, and seize of bar, should correlate to font
      // size.
      g2d.fillRect(10, 1, animationStep, 15);

      // if over lay text has been specified => draw it.
      if (overLayText != null)
      {
        g2d.setRenderingHint(
            RenderingHints.KEY_TEXT_ANTIALIASING,
            RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 1.0f));
        g2d.setColor(overLayTextColor);
        g2d.setFont(POP_AND_HAPPY_FONT);
        g2d.drawString(overLayText, 12, 12);
      }
    }
  }


  /**
   * Generates an inner class to handle the custom drawing of the
   * bar.
   */
  private class BarPane extends JPanel
  {
    int length = 0;
    BarPane()
    {
      length = (int) (ratio * 85);
    }

    @Override
    protected void paintComponent(Graphics g)
    {
      if (animationStep < length) animationStep += 2;
      Graphics2D g2d = (Graphics2D)g;

      //Based on GUI_BACKGROUND COLOR IN COLORSANDFONTS BUT LIGHTER
      g2d.setColor(new Color(0x6C6A6A) );
      g2d.fillRect(9, 1, 86, 13);
      //Based on GUI_BACKGROUND COLOR IN COLORSANDFONTS BUT DARKER
      g2d.setColor(new Color(0x5B5959) );
      g2d.drawRect(9, 1, 86, 13);

      g2d.setColor(barColor);
      g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,0.25f));

      // position info, animation, and seize of bar, should correlate to font
      // size.
      g2d.fillRect(10, 2, animationStep, 12);

      // if over lay text has been specified => draw it.
      if (overLayText != null)
      {
        g2d.setRenderingHint(
          RenderingHints.KEY_TEXT_ANTIALIASING,
          RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

        g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER,1.0f));
        g2d.setColor(overLayTextColor);
        g2d.setFont(OVERLAY_FONT);
        g2d.drawString(overLayText, 12, 12);
      }
    }
  }


  /**
   * AdjustBox creates guts adjusting the variable tied with it.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/21/15
   */
  private class AdjustBox extends JPanel
  {
    private Color buttonColor = Color.WHITE;
    private Color buttonRollover = Color.GREEN;
    private Color labelColor = Color.WHITE;
    JLabel title;
    JLabel down;
    JLabel up;

    /**
     * Initializes a JPanel for all of the adjustment features
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     */
    AdjustBox()
    {
      super();
      setOpaque(false);
      setLayout(new GridLayout(1,2));

      down = new JLabel("|-| ");
      down.setFont(GUI_FONT);
      down.setVerticalAlignment(SwingConstants.TOP);
      down.setHorizontalAlignment(SwingConstants.RIGHT);
      down.setName("-");
      down.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      down.setForeground(buttonColor);
      down.setOpaque(true);
      down.addMouseListener(getMouseListener());

      up = new JLabel(" |+|");
      up.setFont(GUI_FONT);
      up.setVerticalAlignment(SwingConstants.TOP);
      up.setHorizontalAlignment(SwingConstants.LEFT);
      up.setName("+");
      up.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      up.setForeground(buttonColor);
      up.setOpaque(true);
      up.addMouseListener(getMouseListener());

      add(down);
      add(up);
    }
    /**
     * Creates a mouse listener to add roll over effects and treat the JLabels like
     * they are buttons. They are JLabels because I was having to fight the buttons too
     * much and nothings was coming of it.
     *
     * @author Tyler Lynch <lyncht@unm.edu>
     *
     */
    private MouseListener getMouseListener()
    {
      return new MouseAdapter()
      {
        @Override
        public void mouseEntered(MouseEvent e)
        {
          JLabel tempBtn = (JLabel) e.getSource();
          String name = tempBtn.getName();
          if( name == "+" )
          {
            up.setForeground(buttonRollover);
            up.setOpaque(true);
          }
          else if( name == "-" )
          {
            down.setForeground(buttonRollover);
            down.setOpaque(true);
          }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
          JLabel tempBtn = (JLabel) e.getSource();
          String name = tempBtn.getName();
          if( name == "+" )
          {
            up.setForeground(buttonColor);
            up.setOpaque(true);
          }
          else if( name == "-" )
          {
            down.setForeground(buttonColor);
            down.setOpaque(true);
          }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
          JLabel tempBtn = (JLabel) e.getSource();
          String name = tempBtn.getName();
          double changeBy = 0.05;

          if( name == "+" ) {
            InfoPanel.adjustCrop(changeBy, label.getText() );
          }
          else if( name == "-" )
          {
            InfoPanel.adjustCrop( (-changeBy), label.getText() );
          }
          barGraph.repaint();
        }
      };
    }

  }
}
