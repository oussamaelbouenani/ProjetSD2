public class Route {
    private final Country source;
    private final Country destination;

    public Route(Country source, Country destination) {
        this.source = source;
        this.destination = destination;
    }
    public Country getSource() {
        return source;
    }
    public Country getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Route [source=" + source.getCca3() + ", destination=" + destination.getCca3() + "]";
    }
}
