package wethinkcode.places.model;

import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * A Municipality represents any town, neighbourhood, populated area or settled place in the place-names
 * data.
 * <p>
 We assume that there is only one town with a given getName in each Province. (<em>In reality this
 * is simply not true</em> and we'd have to invent a more sophisticated model to deal with that. But
 * then we'd also need better data than we have access to... Since our mission is to explore
 * Distributed Systems and integration, our assumption is Good Enough.)
 */
public record Municipality(String name, String province) implements Comparable<Municipality>
{
    public String getName(){
        return name;
    }

    public String getProvince(){
        return province;
    }

    @Override
    public int compareTo(@NotNull Municipality other ){
        return getProvince().equals(other.getProvince() )
            ? getName().compareTo(other.getName() )
            : getProvince().compareTo(other.getProvince() );
    }

    @Override
    public int hashCode(){
        int hash = 5;
        hash = 97 * hash + Objects.hashCode( this.name );
        hash = 97 * hash + Objects.hashCode( this.province );
        return hash;
    }

    @Override
    public boolean equals( Object obj ){
        if( this == obj )return true;
        if( obj == null )return false;
        if( getClass() != obj.getClass() )return false;
        final Municipality other = (Municipality) obj;
        return this.province.equals( other.province )
            && this.name.equals( other.name );
    }

    @Override public String toString(){
        return "Municipality{"
            + getName()
            + ", "
            + getProvince()
            + "}";
    }
}