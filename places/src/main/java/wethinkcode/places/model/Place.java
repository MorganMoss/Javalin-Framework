package wethinkcode.places.model;

import java.util.Objects;


/**
 * A Town represents any town, neighbourhood, populated area or settled place in the place-names
 * data.
 * <p>
 We assume that there is only one town with a given getName in each Province. (<em>In reality this
 * is simply not true</em> and we'd have to invent a more sophisticated model to deal with that. But
 * then we'd also need better data than we have access to... Since our mission is to explore
 * Distributed Systems and integration, our assumption is Good Enough.)
 */
public class Suburb
        implements Comparable<wethinkcode.places.model.Town>
{
    private final String name;

    private final String town;

    public Suburb( String aName, String aTown ){
        name = aName;
        town = aTown;
    }

    public String getName(){
        return name;
    }

    public String getTown(){
        return town;
    }

    @Override
    public int compareTo( wethinkcode.places.model.Town other ){
        if( other == null ) throw new NullPointerException();
        return getTown().equals(other.getProvince() )
                ? getName().compareTo(other.getName() )
                : getTown().compareTo(other.getProvince() );
    }

    @Override
    public int hashCode(){
        int hash = 5;
        hash = 97 * hash + Objects.hashCode( this.name );
        hash = 97 * hash + Objects.hashCode( this.town );
        return hash;
    }

    @Override
    public boolean equals( Object obj ){
        if( this == obj )return true;
        if( obj == null )return false;
        if( getClass() != obj.getClass() )return false;
        final wethinkcode.places.model.Suburb other = (wethinkcode.places.model.Suburb) obj;
        return this.town.equals( other.town )
                && this.name.equals( other.name );
    }

    @Override public String toString(){
        return "Suburb{"
                + getName()
                + ", "
                + getTown()
                + "}";
    }
}

