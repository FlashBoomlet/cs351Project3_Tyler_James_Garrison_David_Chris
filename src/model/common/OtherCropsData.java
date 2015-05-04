package model.common;
/**
 *
 * TODO: Use this class or remove dependencies
 * Class is currently not used.  Only in project to allow compilation.
 * Class for storing each country's other crops climate requirements.
 *
 * @author jessica
 * @version 29-March-2015
 */
@Deprecated
public class OtherCropsData {
    public final float maxTemp;
    public final float minTemp;
    public final float dayTemp;
    public final float nightTemp;
    public final float maxRain;
    public final float minRain;

    private OtherCropsData(float maxTemp, float minTemp, float dayTemp, float nightTemp, float maxRain, float minRain) {
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.dayTemp = dayTemp;
        this.nightTemp = nightTemp;
        this.maxRain = maxRain;
        this.minRain = minRain;
    }
}