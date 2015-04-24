package gui.hud;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created by Lyncht on 4/23/15.
 */
public class CardSelector extends JPanel implements ActionListener
{
  private int x;
  private int y;
  private int width;
  private int height;
  private JButton close;

  /**
   * Class constructor
   */
  public CardSelector(int x, int y, int width, int height,String label)
  {
    super();
    setOpaque(false);
    setSize(new Dimension(width, height));
    setLocation(x,y);
    setLayout(new BorderLayout());

    this.x = x;
    this.y = y;
    this.width = width;
    this.height = height;

    JPanel topCon = new JPanel();
    topCon.setBackground(Color.YELLOW);
    topCon.setSize(width,(int)(height*(.10)));
    JLabel title = new JLabel(label);
    topCon.add(title);
    add(topCon, BorderLayout.NORTH);

    JPanel middleCon = new JPanel();
    middleCon.setBackground(Color.GREEN);
    middleCon.setSize(width,(int)(height*(.80)));
    JLayeredPane layeredPane = new JLayeredPane();
    JScrollPane scrollPane = new JScrollPane();
    layeredPane.add(scrollPane);
    layeredPane.setSize(width,(int)(height*(.80)));
    middleCon.add(layeredPane);
    add(middleCon, BorderLayout.CENTER);

    JPanel bottomCon = new JPanel();
    bottomCon.setSize(width,(int)(height*(.10)));
    bottomCon.setBackground(Color.RED);
    close = new JButton("CLOSE");
    close.addActionListener(this);
    bottomCon.add(close);
    add(bottomCon, BorderLayout.SOUTH);

  }


  /**
   * Overrides action performed.
   * Detects which button is clicked and either pauses the game or shows the settings
   *
   * @author Tyler Lynch <lyncht@unm.edu>
   *
   * @param e
   */
  @Override
  public void actionPerformed(ActionEvent e) {
    JButton tempBtn = (JButton) e.getSource();
    String name = tempBtn.getText();

    if( name == "CLOSE" )
    {
      this.setVisible(false);
    }
  }

}
