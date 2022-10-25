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
public record Province(String name) implements Comparable<Province> {

    @Override
    public int compareTo(Province other) {
        if (other == null) throw new NullPointerException();
        return name().compareTo(other.name);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        final Province other = (Province) obj;
        return this.name.equals(other.name);
    }

    @Override
    public String toString() {
        return "Province{"
                + name()
                + "}";
    }
}

