package teamCreator.core;

public abstract class Edge {
    private int distance;

    public Edge(int startingDistance) {
        distance = startingDistance;
    }

    public abstract void merge(Edge other);

    public int getDistance() {
        return distance;
    }

    protected void setDistance(int distance) {
        this.distance = distance;
    }

    public String toString() {
        return Integer.toString(distance);
    }
}
