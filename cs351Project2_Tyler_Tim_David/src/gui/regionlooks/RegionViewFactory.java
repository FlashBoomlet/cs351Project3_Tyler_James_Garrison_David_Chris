package gui.regionlooks;

import gui.Camera;


/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 *
 * Edited by David M on 3/19/2015
 * For an overlay:
 * -create Region_View for that particular view to gather data and draw (in regionlooks folder)
 * -create Overlay enum corresponding to that view (at the end of this class)
 */
public class RegionViewFactory
{

  /* view currently correspond to camera angles */
  private final static RegionView DEFAULT_LOOK = new defaultLook();
  private final static RegionView PLANTING_VIEW = new PlantingZoneView();
  private final static RegionView HAPPINESS_VIEW = new RegionHappyView();
  private final static RegionView RAIN_VIEW = new RegionRainView();
  private Overlay currentOverlay;

  /**
   * Constructor for class. World Presenter class relies on this object for
   * region presentation logic.
   */
  public RegionViewFactory()
  {
    this.currentOverlay = Overlay.NONE;
  }

  public Overlay getCurrentOverlay()
  {
    return currentOverlay;
  }

  public void setCurrentOverlay(Overlay currentOverlay)
  {
    this.currentOverlay = currentOverlay;
  }

  public RegionView getBackgroundMapView()
  {
    return DEFAULT_LOOK;
  }


  public RegionView getViewFromDistance(Camera.CAM_DISTANCE distance)
  {
    switch (currentOverlay)
    {
      case PLANTING_ZONE:
        return PLANTING_VIEW;

      case HAPPINESS:
        return HAPPINESS_VIEW;

      case YEARLY_RAIN_FALL:
        return RAIN_VIEW;

      default:
        return DEFAULT_LOOK;
    }
  }


  public enum Overlay
  {
    NONE,
    PLANTING_ZONE,
    HAPPINESS,
    YEARLY_RAIN_FALL
  }
}
