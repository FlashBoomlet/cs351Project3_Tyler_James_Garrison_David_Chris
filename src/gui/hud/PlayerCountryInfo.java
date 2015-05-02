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
  private JPanel pieHolder;
  private JPanel availableCrops;
  private JPanel holder;
  private JScrollPane holderSCR;

  Color[] cropColor = {
          new Color(125, 235, 232,255),
          new Color(255,153,0,255),
          new Color(22, 152, 19,255),
          new Color(194,163,133,255),
          new Color(255,102,255,255),
          new Color(102,255,51,255),
          new Color(153,102,51,255),
          new Color(255,80,80,255),
          new Color(27, 35, 153,255),
          new Color(223, 22, 255,255)

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

  private ArrayList<Slice> hungryArray = new ArrayList<>();
  Slice[] hungrySlices = {
          new Slice(0, cropColor[8], "Nourished"),
          new Slice(0, cropColor[9],  "Hungry" )
  };

  private ArrayList<Slice> popArray = new ArrayList<>();
  Slice[] popSlices = {
          new Slice(0, cropColor[8], "Stable"),
          new Slice(0, cropColor[9],  "New" ),
          new Slice(0, cropColor[9],  "Gone" )

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

    availableCrops = new JPanel();

    availableCrops.setSize(MAX_WIDTH, MAX_HEIGHT);
    availableCrops.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT));
    availableCrops.setLayout(new GridLayout(5, 1, 0, 0));
    availableCrops.setBackground(Color.DARK_GRAY);

    initAvailableCrops();

    pieHolder = new JPanel();
    pieHolder.setLocation(0, MAX_HEIGHT);
    pieHolder.setSize(MAX_WIDTH, MAX_HEIGHT * 2);
    pieHolder.setPreferredSize(new Dimension(MAX_WIDTH, MAX_HEIGHT * 2));
    pieHolder.setLayout(new GridLayout(4, 2));
    pieHolder.setBackground(Color.DARK_GRAY);

    initCrops();
    initLand();
    initHungry();
    initPop();

    holder = new JPanel();
    holder.setPreferredSize(new Dimension(MAX_WIDTH,MAX_HEIGHT*3));
    holder.setBackground(Color.DARK_GRAY);
    holder.add(availableCrops);
    holder.add(pieHolder);

    holderSCR = new JScrollPane(holder);
    holderSCR.setBounds(0, 0, MAX_WIDTH, MAX_HEIGHT);
    holderSCR.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    holderSCR.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    holderSCR.getVerticalScrollBar().setPreferredSize(new Dimension(5, holderSCR.getHeight()));
    holderSCR.setBackground(Color.DARK_GRAY);

    this.setLayout(null);
    this.setSize(MIN_WIDTH, MIN_HEIGHT);
    //this.setFocusable(true);
    this.add(holderSCR);
  }




  public GUIRegion getPlayerCountry()
  {
    return playerCountry;
  }

  //======
  private void initAvailableCrops()
  {
    CountryData cd = playerCountry.getCountryData();

    double corntotal = cd.getCornTotal(true);
    double cornneed = cd.getPerCapitaConsumptionCorn(true);


    AvailableCropsBar cornBar = new AvailableCropsBar("Corn",cropColor[0], 100,65,0,MAX_WIDTH-20,MAX_HEIGHT);

    availableCrops.add(cornBar);

    double wheattotal = cd.getWheatTotal(true);
    double wheatneed = cd.getCountryConsumptionWheat(true);

    AvailableCropsBar wheatBar = new AvailableCropsBar("Wheat",cropColor[1], 100,20,34,MAX_WIDTH-20,MAX_HEIGHT);

    availableCrops.add(wheatBar);

    double ricetotal = cd.getRiceTotal(true);
    double riceneed = cd.getCountryConsumptionRice(true);

    AvailableCropsBar riceBar = new AvailableCropsBar("Rice",cropColor[2], 100,90,2,MAX_WIDTH-20,MAX_HEIGHT);

    availableCrops.add(riceBar);

    double soytotal = cd.getCornTotal(true);
    double soyneed = cd.getCountryConsumptionCorn(true);


    AvailableCropsBar soyBar = new AvailableCropsBar("Soy",cropColor[3], 100,60,15,MAX_WIDTH-20,MAX_HEIGHT);

    availableCrops.add(soyBar);

    double othertotal = cd.getOtherTotal(true);
    double otherneed = cd.getCountryConsumptionOther(true);


    AvailableCropsBar otherBar = new AvailableCropsBar("Other",cropColor[4], 100,50,45,MAX_WIDTH-20,MAX_HEIGHT);

    availableCrops.add(otherBar);
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

    pieHolder.add(new PieChart(landRect, cropArray));
    pieHolder.add(new ChartKey(keyRect, cropArray));
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

    pieHolder.add(new PieChart(landRect, landArray));
    pieHolder.add(new ChartKey(keyRect, landArray));
  }

  //
  private void initHungry()
  {
    CountryData cd = playerCountry.getCountryData();

    double pop = cd.getPopulation(true);
    double unnur = cd.getUndernourish(true);

    double totUnourished = pop*(unnur/1000);
    double full = pop - totUnourished;

    hungrySlices[0].updateSlice((int) full, cropColor[8], "Nourished");
    hungrySlices[1].updateSlice((int)totUnourished,cropColor[9],  "Hungry" );

    hungryArray.clear();
    for( int i = 0; i < hungrySlices.length ; i++)
    {
      hungryArray.add(hungrySlices[i]);
    }


    Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/2-5,MAX_WIDTH/2-5);
    Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/2,MAX_WIDTH/2);

    pieHolder.add(new PieChart(landRect, hungryArray));
    pieHolder.add(new ChartKey(keyRect, hungryArray));
  }



  private void initPop()
  {
    CountryData cd = playerCountry.getCountryData();

    double pop = cd.getPopulation(true);
    double newPop = cd.getBirthRate(true) + cd.getMigration(true);
    double deadPop = cd.getMortality(true);

    double deadNum = pop*(deadPop/1000);
    double newNum = pop*(newPop/1000);
    double stable = pop +newNum -deadNum;

    popSlices[0].updateSlice(stable,cropColor[7],"Stable pop.");
    popSlices[1].updateSlice(newNum,cropColor[8],"New pop.");
    popSlices[2].updateSlice(deadNum,cropColor[9],"Dead pop.");


    popArray.clear();
    for( int i = 0; i < popSlices.length ; i++)
    {
      popArray.add(popSlices[i]);
    }


    Rectangle landRect = new Rectangle(0,0,MAX_WIDTH/2-5,MAX_WIDTH/2-5);
    Rectangle keyRect = new Rectangle(0,0,MAX_WIDTH/2,MAX_WIDTH/2);

    pieHolder.add(new PieChart(landRect, popArray));
    pieHolder.add(new ChartKey(keyRect, popArray));
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
