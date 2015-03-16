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

 private static final int PADDING = 0;
 private DatePanel datePanel;
 private Ticker ticker;
 private GroupLayout layout;
 private int localFrameWidth = 0;

 /**
  Instantiate the WorldFeedPanel with a WorldPresenter to poll from upon
  receipt of update notifications
  @param presenter WorldPresenter to observe
  @param frameWidth width of main frame
  @param frameHeight height of main frame
  */
 public WorldFeedPanel(WorldPresenter presenter, int frameWidth, int frameHeight)
 {
  localFrameWidth = frameWidth;
  datePanel = new DatePanel();
  datePanel.setDate(presenter.getWorldDate());
  ticker = new Ticker();

  setBackground(ColorsAndFonts.OCEANS);

  initLayout();
  int prefH = datePanel.getPreferredSize().height + PADDING*2;
  int prefW = datePanel.getPreferredSize().width + ticker.getPreferredSize().width+PADDING*2;
  setLocation(0,0);
  setPreferredSize(new Dimension(frameWidth, frameHeight/(25) ));

 }

 /*
  setup GroupLayout groups and orientation
  */
 private void initLayout()
 {
  layout = new GroupLayout(this);
  setLayout(layout);

  layout.setAutoCreateGaps(true);

  layout.setHorizontalGroup(
    layout.createSequentialGroup()
      .addComponent(ticker, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
      .addComponent(datePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  );

  layout.setVerticalGroup(
    layout.createParallelGroup(GroupLayout.Alignment.BASELINE, false)
      .addComponent(ticker, GroupLayout.DEFAULT_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
      .addComponent(datePanel, GroupLayout.PREFERRED_SIZE, GroupLayout.DEFAULT_SIZE, GroupLayout.PREFERRED_SIZE)
  );
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
