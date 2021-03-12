public class Route {
    private final String source;
    private final String destination;

    public Route(String source, String destination) {
        this.source = source;
        this.destination = destination;
    }
    public String getSource() {
        return source;
    }
    public String getDestination() {
        return destination;
    }

    @Override
    public String toString() {
        return "Route [source=" + source+ ", destination=" + destination + "]";
    }
}
