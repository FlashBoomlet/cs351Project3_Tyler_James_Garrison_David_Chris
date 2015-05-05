package model;
import model.common.EnumCropType;

/**
 * Created by garrison on 5/1/15.
 * A class to control non-player countries, determine if trades will be accepted, etc.
 * Doesn't interact with game yet, just some structure.
 */
public class ComputerPlayer implements CountryController {

    gui.GUIRegion region;


    public ComputerPlayer(gui.GUIRegion region)
    {
        this.region = region;
    }

    /**
     * Communicate that this Country wishes to export food to other country
     *
     * @param other
     * @return
     */
    public boolean proposeExport(CountryController other)
    {
        // alert another country that we want to trade with it
        if(other.acceptImport(this))
        {
            return true;
        }
        return false;
    }

    /**
     * Communicate that this Country wishes to import FROM other country
     *
     * @param other
     * @return
     */
    public boolean requestImport(CountryController other)
    {
        if(other.acceptExport(this))
        {
            return true;
        }
        return false;
    }

    /**
     *
     * @param other
     * @return      true if this country is willing to export to other country
     */
    public boolean acceptExport(CountryController other)
    {
        if(other instanceof ComputerPlayer )
        {
            // check if we have food to export
            return true;
        }
        else
        {
            // Create a notification to the player that this country wishes to trade; await a response
            return false;
        }
    }

    /**
     *
     * @param other
     * @return      true if this country will accept an import from other country
     */
    public boolean acceptImport(CountryController other)
    {
        if(other instanceof ComputerPlayer )
        {
            // check if we have food to export
            return true;
        }
        else
        {
            // Create a notification to the player that this country wishes to trade; await a response
            return false;
        }
    }

}
