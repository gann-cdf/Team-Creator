package teamCreator.core.merges;

import teamCreator.core.Edge;

public class EdgeSumMerge extends Edge {
    public EdgeSumMerge(int startingDistance) {
        super(startingDistance);
    }

    @Override
    public void merge(Edge other) {
        setDistance(getDistance() + other.getDistance());
    }
}
