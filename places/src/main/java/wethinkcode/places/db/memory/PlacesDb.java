package wethinkcode.places.db.memory;

import java.util.Collection;

import wethinkcode.places.model.Places;
import wethinkcode.places.model.Town;

/**
 * TODO: javadoc PlacesDb
 */
public class PlacesDb implements Places
{
    @Override
    public Collection<String> provinces(){
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public Collection<Town> townsIn( String aProvince ){
        throw new UnsupportedOperationException( "Not supported yet." );
    }

    @Override
    public int size(){
        throw new UnsupportedOperationException( "Not supported yet." );
    }
}