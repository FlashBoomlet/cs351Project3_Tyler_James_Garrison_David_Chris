package main;

import gui.GUIRegion;
import gui.WorldPresenter;
import gui.hud.PlayerCountryDisplay;
import gui.hud.PlayerCountryInfo;
import gui.hud.Ticker.News;
import gui.hud.Ticker.Ticker;
import model.PolicyData;
import model.RandomEventData;
import model.TreatyData;

import java.util.HashMap;


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
  private GUIRegion r;
  private String name = "";
  private PlayerCountryInfo playerCountryInfo;
  private PlayerCountryDisplay playerCountryDisplay;

  /**
   * Constructor
   */
  public Trigger() { /* Do nothing */ }

  /**
   * Set the playerCountryInfo Object
   *
   * @param playerCountryInfo object that holds the info
   */
  public void setPlayCountryInfo(PlayerCountryInfo playerCountryInfo)
  {
    this.playerCountryInfo = playerCountryInfo;
  }

  /**
   * Set the playerCountryDisplay Object
   *
   * @param playerCountryDisplay object that holds the display name and flag
   */
  public void setPlayerCountryDisplay(PlayerCountryDisplay playerCountryDisplay)
  {
    this.playerCountryDisplay = playerCountryDisplay;
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
   * Called by the cardSelector to Sponsor a bill
   *
   * @param policyData that is being sponsored
   */
  public void sponsoredBill(PolicyData policyData)
  {
    if( r != null ) {
      // Make a call to adjust the data.
      sendAlert("News", policyData.getPolicy());
      newsAlert(new News("Breaking! In " + name + " they have signed a bill that " + policyData.getDescription()));

      r.signBill(policyData);
    }
    else
    {
      System.out.println( "Please Select Country" );
    }
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
      Ticker.addMarquisText(news.getNews());
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
    r = gRegion;
    name = r.getName();
    r.setAsPlayer();
    playerCountryInfo.updatePlayerCountry(r);
    if( playerCountryDisplay != null ) playerCountryDisplay.updatePlayerCountry(r);
    System.out.println("You have selected to play as: " + name);

    HashMap<GUIRegion, Integer> rel = new HashMap<>();
    for (GUIRegion gr: WorldPresenter.getAllRegions()) {
      if (gr.getOfficialCountry() && !gr.getPlayerStatus()) {
        rel.put(gr, 1);
      }
    }
    r.setRelationshipMap(rel);
  }

  /**
   * Alter data for a random event
   *
   * @param red (Random Event Data) that occured
   */
  public void randomEvent(RandomEventData red)
  {
    if( r != null )
    {
      r.randomEvent(red);
      sendAlert( "ALERT!", red.getDescription() );
    }
    else
    {
      System.out.println("Please Select Country");
    }
  }

  public void signTreaty(TreatyData treatyData)
  {
    if( r != null ) {
      // Make a call to adjust the data.
      sendAlert("News", treatyData.getTreaty());
      newsAlert(new News("Breaking! In " + name + " they have signed a bill that " + treatyData.getDescription()));

      r.signTreaty(treatyData);
    }
    else
    {
      System.out.println( "Please Select Country" );
    }
  }
}
