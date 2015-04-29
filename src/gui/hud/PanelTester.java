package gui.hud;

import gui.hud.PieChart.ChartKey;
import gui.hud.PieChart.PieChart;
import gui.hud.PieChart.Slice;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Utilized to test JPanels without having to navigate through the main game
 * Also used to ensure modularity of all components being added to the game.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.28.15
 */
public class PanelTester
{
  private static int width = 700;
  private static int height = 700;
  private static JFrame f;

  /**
   * Constructor for the Panel Tester.
   * Basically just to add components to the JFrame created in main to unit test components
   */
  private PanelTester()
  {
    //f.add(new CardSelector(0, 0, width, height, "TEST"));

    // 73 Total
    Slice[] slices2 = { new Slice(5, Color.BLACK,"BLACK"),
      new Slice(33, Color.GREEN,"GREEN"),
      new Slice(20, Color.YELLOW,"YELLOW"), new Slice(15, Color.RED,"RED") };

    Slice[] slices = { new Slice(0.64, Color.GREEN, "Organic"),
      new Slice(52.6590008, Color.BLUE,  "Conventional" ),
    new Slice(46.7009992, Color.RED, "GMO" ) };

    ArrayList<Slice> sliceArray = new ArrayList<>();
    for( int i = 0; i < slices.length ; i++)
    {
      sliceArray.add( slices[i] );
    }
    f.add(new PieChart( f.getBounds(), sliceArray ) );
    Rectangle temp = new Rectangle(0,0,100,100);
    //f.add(new ChartKey(temp, sliceArray ));
  }

  /**
   * Main. We all know what this does.
   *
   * Creates a new JFrame for testing and then creates a new PanelTester
   * @param args from the command line
   */
  public static void main(String[] args)
  {
    f = new JFrame();

    f.setTitle("STARVATION EVASION");
    f.setPreferredSize(new Dimension(width, height));
    f.setSize(f.getPreferredSize());
    f.setLocation(0, 0);
    f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

    PanelTester tester = new PanelTester();
    tester.show();
  }


  /**
   * sets the main game container to visible.
   * Called by main should everything be set up properly
   */
  public void show()
  {
    f.setVisible(true);
  }
}
