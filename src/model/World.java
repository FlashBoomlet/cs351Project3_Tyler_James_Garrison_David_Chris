package model;


import IO.LandDataIO;
import gui.GUIRegion;
import gui.WorldPresenter;
import gui.hud.DatePanel;
import main.SettingsScreen;

import java.util.*;


/**
 Created by winston on 1/23/15.
 Phase_01
 CS 351 spring 2015
 <p/>
 The world is everything that is the case.
 The world is the totality of facts, not of things.
 The facts in logical space are the world.
 <p/>
 - L. W.
 */
public class World
{

  private static int totalUpdates = 0;
  private Collection<Region> world;
  private WorldPresenter worldPresenter;
  private Calendar currentDate;
  private TileManager tileManager;


  /**
   Class constructor. To build a world one must have a collection of regions.

   @param world
   collection of regions that we represent the world
   */
  public World(Collection<Region> world)
  {
    this(world, Calendar.getInstance());
  }


  /**
   One can also build a world starting at a particular date.

   @param world
   */
  public World(Collection<Region> world, Calendar cal)
  {
    this.world = world;
    System.out.println(world.size());
    this.currentDate = cal;
    loadWorldArray();
  }


  private void loadWorldArray()
  {
    new Thread(new Runnable()
    {
      @Override
      public void run()
      {
        try
        {
          tileManager = LandDataIO.parseFile(LandDataIO.DEFAULT_FILE, world);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }).start();
  }


  public int getCurrentYear()
  {
    return getCurrentDate().get(Calendar.YEAR);
  }


  /**
   Get the current time of this particular world.

   @return a calendar object, with the date and time in side.
   */
  public Calendar getCurrentDate()
  {
    return currentDate;
  }


  /**
   Set the world time to the given calendar date.

   @param currentDate
   date the world will be after calling this method.
   */
  public void setCurrentDate(Calendar currentDate)
  {
    this.currentDate = currentDate;
  }


  /**
   @return an array of all country names
   */
  public String[] getCountryNames()
  {
    String[] names = new String[world.size()];
    int count = 0;

    for (Region r : world)
    {
      r.getName();
      names[count] = r.getName();
      count++;
    }

    return names;
  }



  /**
   Sets the regions so that the country data can be updated every year

   @param worldPresenter
   */
  public void setPresenter(WorldPresenter worldPresenter)
  {
    this.worldPresenter = worldPresenter;
  }


  /**
   Advances the world forward by the given number of days. Every new month
   the worlds attributes are randomly modulated.

   @param numOfDays
   number of days to travel in the future too.

   @return true - the world was change, false otherwise.
   */
  public boolean setByDays(int numOfDays)
  {
    int previousYear = currentDate.get(Calendar.YEAR);
    currentDate.add(Calendar.DATE, numOfDays);

    DatePanel.updateRatio(currentDate.get(Calendar.DAY_OF_YEAR) / 365.0, currentDate.get(
        Calendar.YEAR));

    if (currentDate.get(Calendar.YEAR) == 2050)
    {
      //Call to finish game
      main.Game.gameFinished();
      return false;
    }

    boolean isPreviousYear = previousYear != currentDate.get(Calendar.YEAR);
    if (isPreviousYear)
    {
      for (GUIRegion gr : worldPresenter.getAllRegions())
      {
        //Check to ensure it should be updated so that we don't waste time
        if (gr.getOfficialCountry()) gr.iterateYear();
      }
      while (true)
      {
        if (doneUpdate())
        {
          //Call to trade
          tradeLeWorldAway();
          break;
        }
      }
      tileManager.updateTiles();
      main.Game.getWorldFeedPanel().update();
    }

    return isPreviousYear;
  }


  public static synchronized boolean doneUpdate()
  {
    if (totalUpdates == 193)
    {
      totalUpdates = 0;
      return true;
    }
    totalUpdates++;
    return false;
  }


  private void tradeLeWorldAway()
  {
    //Set level equal to settings panel eventually so that it dictates the difficulty.
    int level = SettingsScreen.getTradeInfluence();
    ArrayList<GUIRegion> canExport = new ArrayList<>();
    ArrayList<GUIRegion> needImport = new ArrayList<>();
    GUIRegion bestFit;
    double bestDiff;
    for (GUIRegion gr : worldPresenter.getAllRegions())
    {
      if (gr.getOfficialCountry())
      {
        gr.exportedTo.clear();
        gr.importedFrom.clear();
        if (gr.canExport())
        {
          canExport.add(gr);
        }
        else
        {
          needImport.add(gr);
        }
      }
    }
    switch (level)
    {
      case 2:
        //Hard Algorithm
        for (GUIRegion grX : canExport)
        {
          if (needImport.size() > 0)
          {
            bestFit = needImport.get(0);
            // Set to something crazy and unreachable hopefully
            bestDiff = 1_000_000_000;
            for (GUIRegion grI : needImport)
            {
              double diff =
                  grX.getCountryData().getTotalExport() - grI.getCountryData().getTotalImport();
              if (diff < bestDiff && diff > 0)
              {
                /*
                 * Add check for Trade Distance between countries.
                 * Add preferred trading with countries that you have treaties with
                 */
                if (grX.preferedTrade.contains(grI)) bestFit = grI;
              }
            }
            grX.exportedTo.add(bestFit);
            bestFit.importedFrom.add(grX);
            needImport.remove(bestFit);
          }
        }
        break;
      case 1:
        //Medium Algorithm
        for (GUIRegion grX : canExport)
        {
          if (needImport.size() > 0)
          {
            bestFit = needImport.get(0);
            // Set to something crazy and unreachable hopefully
            bestDiff = 1_000_000_000;
            for (GUIRegion grI : needImport)
            {
              double diff =
                  grX.getCountryData().getTotalExport() - grI.getCountryData().getTotalImport();
              if (diff < bestDiff && diff > 0)
              {
                /*
                 * Add check for Trade Distance between countries.
                 */
                bestFit = grI;
              }
            }
            grX.exportedTo.add(bestFit);
            bestFit.importedFrom.add(grX);
            needImport.remove(bestFit);
          }
        }
        break;
      default:
        //Easy Algorithm
        for (GUIRegion grX : canExport)
        {
          if (needImport.size() > 0)
          {
            bestFit = needImport.get(0);
            // Set to something crazy and unreachable hopefully
            bestDiff = 1_000_000_000;
            for (GUIRegion grI : needImport)
            {
              double diff =
                  grX.getCountryData().getTotalExport() - grI.getCountryData().getTotalImport();
              if (diff < bestDiff && diff > 0) bestFit = grI;
            }
            grX.exportedTo.add(bestFit);
            bestFit.importedFrom.add(grX);
            needImport.remove(bestFit);
          }
        }
    }
  }


  /**
   Sets the initial crops of a country
   */
  public void setAllFirstCrops()
  {
    for (Region area : world)
    {
      area.setFirstCrops();
    }
  }


}
