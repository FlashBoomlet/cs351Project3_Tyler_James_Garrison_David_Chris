package gui.hud;

import gui.ColorsAndFonts;
import gui.WorldPresenter;
import main.Game;
import main.Trigger;

import javax.swing.*;
import java.awt.*;
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
 private static DatePanel datePanel;
 private static OverlaySelect overlaySelect;
 private static GameplayControl gameplayControl;
 private static PlayerCountryDisplay playerCountryDisplay;
 private static PopulationAndHappiness popAndHap;

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
 public WorldFeedPanel(WorldPresenter presenter, int frameWidth, int height, Trigger trigger)
 {
  componentWidth = (int) (frameWidth*(.20));
  componentHeight = (int) (height*(0.95));
  /*
   * Component Initialization
   */
  datePanel = new DatePanel(0,0,(int) (frameWidth*(.20)),componentHeight);
  overlaySelect = new OverlaySelect(0,0,(int) (frameWidth*(.25)),componentHeight);
  popAndHap = new PopulationAndHappiness(0,0,(int)(frameWidth*(.25)), height);
  gameplayControl = new GameplayControl(0,0,(int) ((frameWidth*(.30))),componentHeight);
  playerCountryDisplay = new PlayerCountryDisplay(Game.userCountry, (int) ((frameWidth*(.30))),componentHeight);

  datePanel.setDate(presenter.getWorldDate());

  setBackground(ColorsAndFonts.OCEANS);

  initLayout();
  setLocation(0, 0);
  setPreferredSize(new Dimension(frameWidth, height));
  trigger.setPlayerCountryDisplay(playerCountryDisplay);
 }

 public void showAll(boolean trigger)
 {
  datePanel.setVisible(trigger);
  overlaySelect.setVisible(trigger);
  popAndHap.setVisible(trigger);
  playerCountryDisplay.setVisible(trigger);
  //gameplayControl.setVisible(trigger);
  this.setVisible(trigger);
 }

 public static void update()
 {
  popAndHap.update();
 }

 /*
  setup GroupLayout groups and orientation
  */
 private void initLayout()
 {
  setLayout(layout);

  add(overlaySelect);

  add(popAndHap);
  add(datePanel);
  add(playerCountryDisplay);
  //add(gameplayControl);

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

 public void resetDate()
 {
  datePanel.resetDate();
 }
}

