package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Date;
import java.util.Observable;
import java.util.Observer;

/**
 @author david
 created: 2015-02-05

 description:
 WorldFeedPanel holds a Ticker and DatePanel that (might) display world news events
 and world date respectively, to keep the player apprised of the state of the world
 and help maintain a constant sense of dread as time marches ever forward.
 */
public class WorldFeedPanel extends JPanel implements Observer
{
 /*
  * Panel Components
  */
 private DatePanel datePanel;
 private OverlaySelect overlaySelect;
 private ScalePanel scalePanel;
 private GameplayControl gameplayControl;

 private LayoutManager layout = new FlowLayout(FlowLayout.LEFT,0,0);
 private static final int PADDING = 0;
 private int componentWidth;
 private int componentHeight;

 /**
  Instantiate the WorldFeedPanel with a WorldPresenter to poll from upon
  receipt of update notifications
  @param presenter WorldPresenter to observe
  @param frameWidth width of main frame
  @param height height of this particular panel
  */
 public WorldFeedPanel(WorldPresenter presenter, int frameWidth, int height)
 {
  componentWidth = (int) (frameWidth*(.20));
  componentHeight = (int) (height*(0.95));
  /*
   * Component Initialization
   */
  datePanel = new DatePanel(0,0,(int) (frameWidth*(.15)),componentHeight);
  overlaySelect = new OverlaySelect(0,0,(int) (frameWidth*(.25)),componentHeight);
  scalePanel = new ScalePanel(0,0,(int) (frameWidth*(.15)),componentHeight);
  gameplayControl = new GameplayControl(0,0,(int) ((frameWidth*(.20))+(frameWidth*(.25))),componentHeight);

  datePanel.setDate(presenter.getWorldDate());

  setBackground(ColorsAndFonts.OCEANS);

  initLayout();
  setLocation(0, 0);
  setPreferredSize(new Dimension(frameWidth, height));
 }

 /*
  setup GroupLayout groups and orientation
  */
 private void initLayout()
 {
  setLayout(layout);

  add(scalePanel);
  add(overlaySelect);
  add(datePanel);
  add(gameplayControl);

  setBorder(BorderFactory.createMatteBorder(0, 0, 3, 0, ColorsAndFonts.GUI_TEXT_COLOR.darker()  ));
 }


 /**

  @param o
  @param arg
  */
 @Override
 public void update(Observable o, Object arg)
 {
  datePanel.setDate(((WorldPresenter)o).getWorldDate());
  
  /* update ticker text */
 }

 public void setDate(Date date)
 {
  datePanel.setDate(date);
 }
}
