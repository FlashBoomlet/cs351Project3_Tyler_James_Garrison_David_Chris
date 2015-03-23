package gui.hud;

import gui.ColorsAndFonts;
import gui.MapPane;
import gui.regionlooks.RegionViewFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * OverlaySelect extends JPanel and creates the guts for selecting
 * an overlay from a drop down menu
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 3/21/15
 */
public class OverlaySelect extends JPanel
{
  private static JComboBox<String> overlaySelector;
  private JLabel title;
  private static String[] filters= {
      "NONE",
      "HAPPINESS",
      "PLANTING_ZONE",
      "YEARLY_RAIN_FALL",
      "NOURISHMENT",
      "SOIL_TYPE",
      "POPULATION"
    };

  /**
   * OverlaySelect initializes all of the components for selecting
   * an overlay from a drop down menu and adds them to the JPanel.
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   * @since 3/21/15
   *
   * @param x relative location to the WorldFeedPanel
   * @param y relative location to the WorldFeedPanel
   * @param width desired width
   * @param height desired height
   */
  public OverlaySelect(int x, int y, int width, int height)
  {
    super();
    setOpaque(false);
    setBackground(Color.RED);
    setLocation(x,y);
    setPreferredSize(new Dimension(width,height));
    setLayout(new FlowLayout(SwingConstants.LEADING,0,0));

    title = new JLabel("Filter Selector:");
    title.setForeground(ColorsAndFonts.GUI_TEXT_COLOR);
    title.setFont(ColorsAndFonts.TOP_FONT);

    overlaySelector = new JComboBox(filters);
    overlaySelector.setSelectedIndex(0);
    overlaySelector.setFocusable(false);
    overlaySelector.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent e)
      {
        int key =  overlaySelector.getSelectedIndex();
        switch(key)
        {
          case 1:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.HAPPINESS);
            break;
          case 2:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.PLANTING_ZONE);
            break;
          case 3:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.YEARLY_RAIN_FALL);
            break;
          case 4:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.NOURISHMENT);
            break;
          case 5:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.SOIL_TYPE);
            break;
          case 6:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.POPULATION);
            break;
          default:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.NONE);
        }
      }
    });

    add(title);
    add(overlaySelector);
  }

  /**
   * Sets the index of the comboBox to the particular overlay
   * @param currentOverlay being use
   */
  public static void updateOverlaySelect(RegionViewFactory.Overlay currentOverlay)
  {
    String overlay = currentOverlay.toString();
    if( overlay == "HAPPINESS") overlaySelector.setSelectedIndex(1);
    else if( overlay == "PLANTING_ZONE") overlaySelector.setSelectedIndex(2);
    else if( overlay == "YEARLY_RAIN_FALL") overlaySelector.setSelectedIndex(3);
    else if( overlay == "NOURISHMENT" ) overlaySelector.setSelectedIndex(4);
    else if( overlay == "SOIL_TYPE" ) overlaySelector.setSelectedIndex(5);
    else if( overlay == "POPULATION" ) overlaySelector.setSelectedIndex(6);
    else overlaySelector.setSelectedIndex(0);
  }
}
