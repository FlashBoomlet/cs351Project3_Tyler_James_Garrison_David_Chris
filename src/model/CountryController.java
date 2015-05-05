package model;
import gui.GUIRegion;

/**
 * Created by garrison on 5/1/15.
 * General interface to represent both player and computer players.
 */
public interface CountryController {

    boolean proposeExport(CountryController other);
    boolean requestImport(CountryController other);

    boolean acceptExport(CountryController other);
    boolean acceptImport(CountryController other);

}
