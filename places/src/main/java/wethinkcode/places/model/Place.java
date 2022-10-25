package wethinkcode.places.model;


/**
 * A Municipality represents any town, neighbourhood, populated area or settled place in the place-names
 * data.
 * <p>
 * We assume that there is only one town with a given getName in each Province. (<em>In reality this
 * is simply not true</em> and we'd have to invent a more sophisticated model to deal with that. But
 * then we'd also need better data than we have access to... Since our mission is to explore
 * Distributed Systems and integration, our assumption is Good Enough.)
 */
public record Place(String name, String municipality) implements Comparable<Place> {

    @Override
    public int compareTo(Place other) {
        if (other == null) throw new NullPointerException();
        return municipality().equals(other.municipality)
                ? name().compareTo(other.name)
                : municipality().compareTo(other.municipality);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Place other = (Place) obj;
        return this.municipality.equals(other.municipality)
                && this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return "Place{"
                + name()
                + ", "
                + municipality()
                + "}";
    }
}

