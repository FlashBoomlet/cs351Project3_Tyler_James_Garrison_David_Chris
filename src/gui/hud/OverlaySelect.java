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
      "% Corn",
      "% Wheat",
      "% Rice",
      "% Soy",
      "% Other",
      "% Organic",
      "% Conventional",
      "% GMO",
      "Average Temperature",
      "Annual High Temperature",
      "Annual Low Temperature",
      "Precipitation",
      "Happiness",
      "Nourishment",
      "Population",
      "Median Age",
      "Birth Rate",
      "Migration Rate",
      "Mortality Rate",
      "Crop Distribution"
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

    title = new JLabel("Overlay:");
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
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.CORN);
            break;
          case 2:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.WHEAT);
            break;
          case 3:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.RICE);
            break;
          case 4:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.SOY);
            break;
          case 5:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.OTHER);
            break;
          case 6:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.ORGANIC);
            break;
          case 7:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.CONVENTIONAL);
            break;
          case 8:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.GMO);
            break;
          case 9:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.AVG_TEMPERATURE);
            break;
          case 10:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.HIGH_TEMPERATURE);
            break;
          case 11:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.LOW_TEMPERATURE);
            break;
          case 12:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.PRECIPITATION);
            break;
          case 13:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.HAPPINESS);
            break;
          case 14:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.NOURISHMENT);
            break;
          case 15:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.POPULATION);
            break;
          case 16:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.AGE);
            break;
          case 17:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.BIRTHS);
            break;
          case 18:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.MIGRATION);
            break;
          case 19:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.MORTALITY);
            break;
          case 20:
            MapPane.presenter.setCurrentOverlay(RegionViewFactory.Overlay.CROPS);
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
    if( overlay == "CORN" ) overlaySelector.setSelectedIndex(1);
    else if( overlay == "WHEAT" ) overlaySelector.setSelectedIndex(2);
    else if( overlay == "RICE" ) overlaySelector.setSelectedIndex(3);
    else if( overlay == "SOY" ) overlaySelector.setSelectedIndex(4);
    else if( overlay == "OTHER" ) overlaySelector.setSelectedIndex(5);
    else if( overlay == "ORGANIC" ) overlaySelector.setSelectedIndex(6);
    else if( overlay == "CONVENTIONAL" ) overlaySelector.setSelectedIndex(7);
    else if( overlay == "GMO" ) overlaySelector.setSelectedIndex(8);
    else if( overlay == "AVG_TEMPERATURE" ) overlaySelector.setSelectedIndex(9);
    else if( overlay == "HIGH_TEMPERATURE" ) overlaySelector.setSelectedIndex(10);
    else if( overlay == "LOW_TEMPERATURE" ) overlaySelector.setSelectedIndex(11);
    else if( overlay == "PRECIPITATION") overlaySelector.setSelectedIndex(12);
    else if( overlay == "HAPPINESS") overlaySelector.setSelectedIndex(13);
    else if( overlay == "NOURISHMENT" ) overlaySelector.setSelectedIndex(14);
    else if( overlay == "POPULATION" ) overlaySelector.setSelectedIndex(15);
    else if( overlay == "AGE" ) overlaySelector.setSelectedIndex(16);
    else if( overlay == "BIRTHS" ) overlaySelector.setSelectedIndex(17);
    else if( overlay == "MIGRATION" ) overlaySelector.setSelectedIndex(18);
    else if( overlay == "MORTALITY" ) overlaySelector.setSelectedIndex(19);
    else if( overlay == "CROPS" ) overlaySelector.setSelectedIndex(20);
    else overlaySelector.setSelectedIndex(0);
  }
}
