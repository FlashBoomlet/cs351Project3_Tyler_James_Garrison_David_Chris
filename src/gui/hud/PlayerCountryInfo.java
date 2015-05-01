package gui.hud;

import gui.GUIRegion;
import gui.hud.PieChart.ChartKey;
import gui.hud.PieChart.PieChart;
import gui.hud.PieChart.Slice;
import model.CountryData;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

/**
 * Created by James Lawson on 4/25/2015.
 *
 * class that will show info about the players country,
 * that they are playing as.
 */
public class PlayerCountryInfo extends JPanel implements ActionListener
{
  private final int STEP_SIZE = 10;
  private final int WSTEP_SIZE;

  private final int MIN_HEIGHT;
  private final int MAX_HEIGHT;
  private final int MIN_WIDTH;
  private final int MAX_WIDTH;

  private int currentHeight;
  private int currentWidth;


  private Border border = BorderFactory.createRaisedBevelBorder();


  // if true all info will be displayed
  //if false show a small collapsed version
  private boolean isOpen = false;

  private Timer timer;


  //display prettiness
  JPanel holder;

  Color[] cropColor = {new Color(125, 235, 232,255),
          new Color(255,153,0,255),
          new Color(230,230,230,255),
          new Color(194,163,133,255),
          new Color(255,102,255,255),
          new Color(102,255,51,255),
          new Color(153,102,51,255),
          new Color(255,80,80,255)

  };

  private ArrayList<Slice> cropArray = new ArrayList<>();
  Slice[] cropSlices = {
          new Slice(0, cropColor[0], "Corn"),
          new Slice(0, cropColor[1],  "Wheat" ),
          new Slice(0, cropColor[2], "Rice" ),
          new Slice(0, cropColor[3], "Soy"),
          new Slice(0, cropColor[4], "Other")
  };

  private ArrayList<Slice> landArray = new ArrayList<>();
  Slice[] landSlices = {
          new Slice(0, cropColor[5], "Organic"),
          new Slice(0, cropColor[6],  "Conventional" ),
          new Slice(0, cropColor[7], "GMO" )
  };

  //players country for access to information
  private GUIRegion playerCountry;

  /**
   *
   * @param playerCountry country the player is controlling
   */
  public PlayerCountryInfo(GUIRegion playerCountry,int x, int y, int Maxwidth, int Maxheight)
  {
    this.setLocation(x,y);

    this.playerCountry = playerCountry;

    MIN_WIDTH = 0;
    MAX_WIDTH = Maxwidth;

    MIN_HEIGHT = 0;
    MAX_HEIGHT = Maxheight;

    WSTEP_SIZE = (int)((double)STEP_SIZE/(((double)Maxheight/(double)Maxwidth)));

    currentWidth = MIN_WIDTH;
    currentHeight = MIN_HEIGHT;


    timer = new Timer(30, this);


    holder = new JPanel();
    holder.setLocation(0, 0);
    holder.setSize(MAX_WIDTH, MAX_HEIGHT);
    holder.setBackground(new Color(0, 0, 0, 100));
    holder.setBorder(border);
    holder.setLayout(new GridLayout(2,2));

    initCrops();
    initLand();

    this.setLayout(null);
    this.setSize(MIN_WIDTH, MIN_HEIGHT);
    this.setFocusable(true);
    this.add(holder);



  }




  public GUIRegion getPlayerCountry()
  {
    return playerCountry;
  }

  private void initCrops()
  {
    double cornTotal = 0;
    double wheatTotal = 0;
    double riceTotal = 0;
    double soyTotal = 0;
    double otherTotal = 0;

    CountryData cd = playerCountry.getCountryData();

      cornTotal = cd.getCornTotal(true);
      wheatTotal = cd.getWheatTotal(true);
      riceTotal = cd.getRiceTotal(true);
      soyTotal = cd.getSoyTotal(true);
      otherTotal = cd.getOtherTotal(true);



    // Update the slice information
    cropSlices[0].updateSlice(cornTotal,cropColor[0], "Corn");
    cropSlices[1].updateSlice(wheatTotal, cropColor[1],  "Wheat" );
    cropSlices[2].updateSlice(riceTotal, cropColor[2], "Rice" );
    cropSlices[3].updateSlice(soyTotal, cropColor[3], "Soy");
    cropSlices[4].updateSlice(otherTotal, cropColor[4], "Other");

    cropArray.clear();
    for( int i = 0; i < cropSlices.length ; i++)
    {
      cropArray.add(cropSlices[i]);
    }


    Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/2-5,MAX_WIDTH/2-5);
    Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/2,MAX_WIDTH/2);

    holder.add(new PieChart(landRect, cropArray ));
    holder.add(new ChartKey( keyRect, cropArray ));
  }


  private void initLand()
  {
    double organic = 0;
    double conventional = 0;
    double gmo = 0;



      CountryData cd = playerCountry.getCountryData();

      organic = cd.getOrganic(true);
      conventional = cd.getConventional(true);
      gmo = cd.getGmo(true);


    landSlices[0].updateSlice(organic*100,cropColor[5] , "Organic");
    landSlices[1].updateSlice(conventional*100,cropColor[6],  "Conventional" );
    landSlices[2].updateSlice(gmo*100, cropColor[7], "GMO" );

    landArray.clear();
    for( int i = 0; i < landSlices.length ; i++)
    {
      landArray.add(landSlices[i]);
    }


    Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/2-5,MAX_WIDTH/2-5);
    Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/2,MAX_WIDTH/2);

    holder.add(new PieChart(landRect, landArray));
    holder.add(new ChartKey(keyRect, landArray));
  }

  /**
   * call to start the timer
   */
  public void animate()
  {
    if (!timer.isRunning()) timer.start();
  }



  /**
   *
   * animates the window out
   *
   * @param e unused
   */
  @Override
  public void actionPerformed(ActionEvent e)
  {

    if (isOpen)
    {
      currentWidth -= WSTEP_SIZE;
      currentHeight -= STEP_SIZE;

      setLocation(getX()+WSTEP_SIZE,getY());

    }
    else
    {
      currentWidth += WSTEP_SIZE;
      currentHeight +=STEP_SIZE;

      setLocation(getX()-WSTEP_SIZE,getY());
    }

    if (currentHeight > MAX_HEIGHT)
    {
      currentHeight = MAX_HEIGHT;
      currentWidth = MAX_WIDTH;
      setLocation(getX()-WSTEP_SIZE,getY());
      setSize(MAX_WIDTH,MAX_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else if(currentHeight < MIN_HEIGHT)
    {
      currentHeight = MIN_HEIGHT;
      currentWidth = MIN_WIDTH;
      setLocation(getX()+WSTEP_SIZE,getY());
      setSize(MIN_HEIGHT,MIN_HEIGHT);
      //call to fix the jittering
      getParent().repaint();

      timer.stop();
      isOpen =! isOpen;
    }
    else
    {
      setSize(currentWidth, currentHeight);
      //call to fix the jittering
      getParent().repaint();
    }

  }
}
