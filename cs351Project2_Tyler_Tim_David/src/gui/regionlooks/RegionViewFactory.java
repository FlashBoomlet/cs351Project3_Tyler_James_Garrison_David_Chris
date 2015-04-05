package gui.regionlooks;

import gui.Camera;


/**
 * Created by winston on 1/31/15.
 * <p/>
 * Class used to Generate various regions views. Basically a way to hide all the
 * specifics of this class from the calling context.
 *
 * Edited by David M on 3/19/2015
 *
 * For an overlay:
 * -create Region_View for that particular view to gather data and draw (in regionlooks folder)
 * -create Overlay enum corresponding to that view (at the end of this class)
 * -make changes in MapPane as well
 * -make changes in OverlaySelect too
 *
 *
 * TODO:
 * birth rate
 * migration?
 * mortality
 * conventional crops
 * gmo crops
 */
public class RegionViewFactory
{
  /* view currently correspond to camera angles */
  private final static RegionView DEFAULT_LOOK = new defaultLook();
  private final static RegionView PLANTING_VIEW = new PlantingZoneView();
  private final static RegionView HAPPINESS_VIEW = new RegionHappyView();
  private final static RegionView RAIN_VIEW = new RegionRainView();
  private final static RegionView NOURISHMENT_VIEW = new RegionMalnourishmentView();
  private final static RegionView SOIL_TYPE_VIEW = new RegionSoilTypeView();
  private final static RegionView POPULATION_VIEW = new RegionPopulationView();
  private final static RegionView AGE_VIEW = new RegionAgeView();
  private final static RegionView CORN_VIEW = new RegionCornView();
  private final static RegionView WHEAT_VIEW = new RegionWheatView();
  private final static RegionView RICE_VIEW = new RegionRiceView();
  private final static RegionView SOY_VIEW = new RegionSoyView();
  private final static RegionView OTHER_VIEW = new RegionOtherView();
  private final static RegionView ORGANIC_VIEW = new RegionOrganicView();
  private final static RegionView TEMPERATURE_VIEW = new RegionTemperatureView();
  private final static RegionView BIRTH_RATE_VIEW = new RegionBirthRateView();
  private final static RegionView MIGRATION_RATE_VIEW = new RegionMigrationRateView();
  private final static RegionView MORTALITY_RATE_VIEW = new RegionMortalityRateView();
  private final static RegionView CONVENTIONAL_VIEW = new RegionConventionalView();
  private final static RegionView GMO_VIEW = new RegionGMOView();
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

      case NOURISHMENT:
        return NOURISHMENT_VIEW;

      case SOIL_TYPE:
        return SOIL_TYPE_VIEW;

      case POPULATION:
        return POPULATION_VIEW;

      case AGE:
        return AGE_VIEW;

      case CORN:
        return CORN_VIEW;

      case WHEAT:
        return WHEAT_VIEW;

      case RICE:
        return RICE_VIEW;

      case SOY:
        return SOY_VIEW;

      case OTHER:
        return OTHER_VIEW;

      case ORGANIC:
        return ORGANIC_VIEW;

      case TEMPERATURE:
        return TEMPERATURE_VIEW;

      case BIRTHS:
        return BIRTH_RATE_VIEW;

      case MIGRATION:
        return MIGRATION_RATE_VIEW;

      case MORTALITY:
        return MORTALITY_RATE_VIEW;

      case CONVENTIONAL:
        return CONVENTIONAL_VIEW;

      case GMO:
        return GMO_VIEW;

      default:
        return DEFAULT_LOOK;
    }
  }

  public enum Overlay
  {
    NONE,
    PLANTING_ZONE,
    HAPPINESS,
    YEARLY_RAIN_FALL,
    NOURISHMENT,
    SOIL_TYPE,
    AGE,
    CORN,
    WHEAT,
    RICE,
    SOY,
    OTHER,
    ORGANIC,
    TEMPERATURE,
    BIRTHS,
    MIGRATION,
    MORTALITY,
    CONVENTIONAL,
    GMO,
    POPULATION
  }
}
