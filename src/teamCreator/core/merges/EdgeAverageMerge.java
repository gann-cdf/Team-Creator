package teamCreator.core.merges;

import teamCreator.core.Edge;

public class EdgeAverageMerge extends Edge {
    public EdgeAverageMerge(int startingDistance) {
        super(startingDistance);
    }

    @Override
    public void merge(Edge other) {
        setDistance((int) Math.ceil((double) getDistance() + other.getDistance() / 2));
    }
}
