package main;

import gui.GUIRegion;
import gui.hud.Ticker.News;
import gui.hud.Ticker.Ticker;

/**
 * Trigger is a system that you reference to update things.
 * Pass a reference of the trigger from game into a constructor to utilize.
 *
 * @author Tyler Lynch <lyncht@unm.edu>
 * @since 4.30.15
 */
public class Trigger
{
  private Notification notification = null;
  private Ticker ticker = null;

  /**
   * Constructor
   */
  public Trigger()
  {
    /* Do nothing */
  }

  /**
   * Setup the Notification System to be utilized.
   * @param notification system
   */
  public void setupNotifications(Notification notification)
  {
    this.notification = notification;
  }

  /**
   * Setup the Ticker System to be utilized.
   * @param ticker system
   */
  public void setupTicker(Ticker ticker)
  {
    this.ticker = ticker;
  }

  /**
   * Send an alert to the notification system
   *
   * @param type of Alert (String)
   * @param description of the Alert (String)
   * @return if an alert was sent
   */
  public boolean sendAlert(String type, String description)
  {
    if( notification != null )
    {
      notification.updateAlert(type,description);
      return true;
    }

    return false;
  }

  /**
   *
   * @param news that you would like to send out to the ticker
   * @return if the news was accepted and real news
   */
  public boolean newsAlert(News news)
  {
    if( news != null )
    {
      return true;
    }
    return false;
  }

  /**
   * Set the player of the game
   *
   * @param gRegion for the player to play as
   */
  public void setPlayer(GUIRegion gRegion)
  {
    System.out.println( "You have selected to play as: " + gRegion.getName() );
  }
}
