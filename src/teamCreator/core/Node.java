package teamCreator.core;

import java.util.*;

public class Node {
    private static final String DISTANCE_SEPARATOR = " → ";
    private List<String> members;
    private Map<Node, Edge> edges;

    public Node(String originalMember) {
        members = new ArrayList<>();
        edges = new HashMap<>();
        members.add(originalMember);
    }

    public void addEdge(Node other, Edge distance) {
        edges.put(other, distance);
    }

    public void merge(Node other) {
        members.addAll(other.members);
        for (Map.Entry<Node, Edge> neighborDistancePair : other.edges.entrySet()) {
            Node neighbor = neighborDistancePair.getKey();
            Edge edgeToNeighbor = neighborDistancePair.getValue();
            if (neighbor != this) {
                if (edges.get(neighbor) != null) {
                    edges.get(neighbor).merge(edgeToNeighbor);
                } else {
                    addEdge(neighbor, edgeToNeighbor);
                }
            }
        }
    }

    public void remove(Node other) {
        edges.remove(other);
    }

    public ArrayList<Node> neighbors(int distance) {
        ArrayList<Node> nearbyNeighbors = new ArrayList<>();
        for (Map.Entry<Node, Edge> neighborDistancePair : edges.entrySet()) {
            Node neighbor = neighborDistancePair.getKey();
            Edge edgeToNeighbor = neighborDistancePair.getValue();
            if (edgeToNeighbor.getDistance() <= distance && neighbor.distanceTo(this) <= distance) {
                nearbyNeighbors.add(neighbor);
            }
        }
        return nearbyNeighbors;
    }

    private int distanceTo(Node other) {
        if (edges.containsKey(other)) {
            return edges.get(other).getDistance();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public String shortName() {
        List<String> names = new ArrayList<>();
        for (String member : members) {
            StringBuilder name = new StringBuilder();
            for (int i = 0; i < member.length(); i++) {
                if (i == 0 || member.charAt(i - 1) == ' ') {
                    name.append(member.charAt(i));
                }
            }
            names.add(name.toString());
        }
        return String.join(",", names);
    }

    public String toString() {
        String result = "[" + String.join(", ", members);
        ArrayList<String> distances = new ArrayList<>();
        for (Map.Entry<Node, Edge> pair : edges.entrySet()) {
            distances.add(pair.getValue().getDistance() + DISTANCE_SEPARATOR + pair.getKey().shortName());
        }
        distances.sort(Comparator.comparing(o -> Integer.valueOf(o.substring(0, o.indexOf(DISTANCE_SEPARATOR)))));
        return result + ": " + String.join(", ", distances) + "]";
    }

    public int size() {
        return members.size();
    }

    public List<String> getMembers() {
        return members;
    }

    public Map<Node, Edge> getEdges() {
        return edges;
    }

    public boolean isIsolated() {
        return edges.isEmpty();
    }
}
