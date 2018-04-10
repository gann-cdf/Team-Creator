package teamCreator.core.merges;

import teamCreator.core.Edge;

public class EdgeMaxMerge extends Edge {
    public EdgeMaxMerge(int startingDistance) {
        super(startingDistance);
    }

    @Override
    public void merge(Edge other) {
        setDistance(Math.max(getDistance(), other.getDistance()));
    }
}
