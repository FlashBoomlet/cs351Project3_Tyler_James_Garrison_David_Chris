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
  private static final Color BAR_TEXT_C = Color.black;
  private static final Color TEXT_ROLLOVER_C = ColorsAndFonts.ACTIVE_REGION;
  private static final Color BAR_ROLLOVER_C = Color.gray;

  private final Color originalBarColor;
  private Color overLayTextColor;
  private Color barColor;
  private final JLabel label;
  private final double ratio;
  private final String overLayText;
  private AdjustBox adjustBox;
  private JPanel dataPanel;
  private int adjustValue = 5;

  private static boolean showAdjust = false;
  private int animationStep = 0; /* used to start and stop animation */


  /**
   * Constructor for class.
   *
   * @param barColor  the barColor of the bar to be draw
   * @param ratio     a double between 0 and 1, 1 being 'full'.
   * @param labelText String that will be displayGUIRegion labeling the bar
   */
  public BarPanel(Color barColor, double ratio, String labelText)
  {
    this(barColor, ratio, labelText, null);
  }

  /**
   * Constructor for class.
   *
   * @param barColor    the barColor of the bar to be draw
   * @param ratio       a double between 0 and 1, 1 being 'full'.
   * @param labelText   String that will be displayGUIRegion labeling the bar
   * @param overLayText String that will be displayed on top of the bar.
   *                    (to show the ratio passed in for example
   */
  public BarPanel(Color barColor, double ratio, String labelText, String overLayText)
  {
    if( AttributeGenerator.isAdjustableCrop(labelText) )
    {
      showAdjust = true;
    }

    //init
    this.originalBarColor = barColor;
    this.barColor = barColor;
    this.overLayTextColor = BAR_TEXT_C;
    this.ratio = ratio;
    this.overLayText = overLayText;

    // 6000 is just to make things too big! fighting with swing.
    // 16 is height of each individual bar.
    Dimension size = new Dimension(6000, 50);
    setMaximumSize(size);
    if( showAdjust ) setLayout(new GridLayout(2,1) );
    else setLayout(new GridLayout(1,1));

    label = new JLabel(labelText);
    Component barGraph = getBarPane();

    dataPanel = new JPanel();
    dataPanel.setOpaque(false);
    dataPanel.setLayout(new GridLayout(1,2));
    dataPanel.add(label);
    dataPanel.add(barGraph);

    adjustBox = new AdjustBox();

    //config
    setBackground(ColorsAndFonts.GUI_BACKGROUND);

    label.setFont(GUI_FONT);
    label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    label.setHorizontalAlignment(SwingConstants.LEFT);
    label.setVerticalAlignment(SwingConstants.TOP);
    addMouseListener(getMouseListener());

    if( showAdjust )
    {
      add(dataPanel, BorderLayout.NORTH);
      add(adjustBox, BorderLayout.SOUTH);
    }
    else
    {/*
      add(label);
      add(barGraph);
      */
      add(dataPanel);
    }
    showAdjust = false;
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
        overLayTextColor = BAR_TEXT_C;
        barColor = originalBarColor;
        label.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
      }
    };
  }

  /**
   * Generates an inner class to handle the custom drawing of the
   * bar.
   */
  private Component getBarPane()
  {
    return new JPanel()
    {
      @Override
      protected void paintComponent(Graphics g)
      {
        int length = (int) (ratio * 100);

        if (animationStep < length) animationStep += 2;

        Graphics2D g2d = (Graphics2D)g;
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
    };
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
      setLayout(new GridLayout(1,4));

      title = new JLabel("Adjust:");
      title.setHorizontalAlignment(SwingConstants.RIGHT);
      title.setFont(new Font("Serif", Font.PLAIN, 12));
      title.setForeground(buttonColor);

      down = new JLabel("<");
      down.setHorizontalAlignment(SwingConstants.RIGHT);
      down.setFont(new Font("Serif", Font.BOLD, 14));
      down.setName("-");
      down.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      down.setForeground(buttonColor);
      down.setOpaque(true);
      down.addMouseListener(getMouseListener());

      JLabel variable = new JLabel();
      variable.setText(Integer.toString(adjustValue) + " %");
      variable.setHorizontalAlignment(SwingConstants.CENTER);
      variable.setForeground(labelColor);

      up = new JLabel(">");
      up.setHorizontalAlignment(SwingConstants.LEFT);
      up.setFont(new Font("Serif", Font.BOLD, 14));
      up.setName("+");
      up.setBackground(ColorsAndFonts.GUI_BACKGROUND);
      up.setForeground(buttonColor);
      up.setOpaque(true);
      up.addMouseListener(getMouseListener());

      add(title);
      add(down);
      add(variable);
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
            up.setText(">>");
          }
          else if( name == "-" )
          {
            down.setForeground(buttonRollover);
            down.setOpaque(true);
            down.setText("<<");
          }
        }

        @Override
        public void mouseExited(MouseEvent e)
        {
          JLabel tempBtn = (JLabel) e.getSource();
          String name = tempBtn.getName();
          System.out.println(name);
          if( name == "+" )
          {
            up.setForeground(buttonColor);
            up.setOpaque(true);
            up.setText(">");
          }
          else if( name == "-" )
          {
            down.setForeground(buttonColor);
            down.setOpaque(true);
            down.setText("<");
          }
        }

        @Override
        public void mouseClicked(MouseEvent e)
        {
          JLabel tempBtn = (JLabel) e.getSource();
          String name = tempBtn.getName();

          if( name == "+" )
          {

          }
          else if( name == "-" )
          {

          }
        }
      };
    }
  }
}
