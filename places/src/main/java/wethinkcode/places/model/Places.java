package wethinkcode.places.model;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * I am the "database" of place-names. You should write a class that implements me.
 * Your class might have other operations that I have not listed... that's OK.
 */
public interface Places
{
    Collection<Province> provinces();

    Collection<Municipality> municipalitiesIn(String province );

    Collection<Place> placesInMunicipality(String municipality);

    List<Place> placesInProvince(String province);

    Optional<Municipality> municipality(String name);

    Optional<Place> place(String name);

    Optional<Province> province(String name);

    int size();
}