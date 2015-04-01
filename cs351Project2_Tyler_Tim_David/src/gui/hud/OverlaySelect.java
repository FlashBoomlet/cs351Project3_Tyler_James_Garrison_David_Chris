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
      "None",
      "Happiness",
      "Planting Zone",
      "Annual Rain Fall",
      "Nourishment",
      "Soil Type",
      "Population",
      "Median Age",
      "% Corn",
      "% Wheat",
      "% Rice",
      "% Soy",
      "% Other",
      "% Organic",
      "Annual Temperature"
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
    setLayout(new FlowLayout(SwingConstants.NORTH_EAST,0,0));

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
          case 7:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.AGE);
            break;
          case 8:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.CORN);
            break;
          case 9:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.WHEAT);
            break;
          case 10:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.RICE);
            break;
          case 11:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.SOY);
            break;
          case 12:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.OTHER);
            break;
          case 13:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.ORGANIC);
            break;
          case 14:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.TEMPERATURE);
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
    else if( overlay == "AGE" ) overlaySelector.setSelectedIndex(7);
    else if( overlay == "CORN" ) overlaySelector.setSelectedIndex(8);
    else if( overlay == "WHEAT" ) overlaySelector.setSelectedIndex(9);
    else if( overlay == "RICE" ) overlaySelector.setSelectedIndex(10);
    else if( overlay == "SOY" ) overlaySelector.setSelectedIndex(11);
    else if( overlay == "OTHER" ) overlaySelector.setSelectedIndex(12);
    else if( overlay == "ORGANIC" ) overlaySelector.setSelectedIndex(13);
    else if( overlay == "TEMPERATURE" ) overlaySelector.setSelectedIndex(14);
    else overlaySelector.setSelectedIndex(0);
  }
}
