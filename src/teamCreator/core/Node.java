package teamCreator.core;

import java.util.*;

public class Node {
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
        Iterator<Map.Entry<Node, Edge>> iterator = edges.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Node, Edge> pair = iterator.next();
            if (pair.getKey() != other) {
                pair.getValue().merge(other.edges.get(pair.getKey()));
            }
        }
    }

    public void remove(Node other) {
        edges.remove(other);
    }

    public ArrayList<Node> neighbors(int distance) {
        ArrayList<Node> neighbors = new ArrayList<>();
        Iterator<Map.Entry<Node,Edge>> iterator = edges.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Node, Edge> pair = iterator.next();
            if (pair.getValue().getDistance() <= distance && pair.getKey().distanceTo(this) <= distance) {
                neighbors.add(pair.getKey());
            }
        }
        return neighbors;
    }

    private int distanceTo(Node other) {
        if (edges.containsKey(other)) {
            return edges.get(other).getDistance();
        } else {
            return Integer.MAX_VALUE;
        }
    }

    public String shortName() {
        String result = "";
        for (String member : members) {
            result += member.substring(0, 1);
        }
        return result;
    }

    public String toString() {
        String result = "[" + String.join(", ", members);
        ArrayList<String> distances = new ArrayList<>();
        Iterator<Map.Entry<Node, Edge>> iterator = edges.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Node, Edge> pair = iterator.next();
            distances.add(pair.getValue().getDistance() + " → " + pair.getKey().shortName());
        }
        return result + ": " + String.join(", ", distances) + "]";
    }

    public int size() {
        return members.size();
    }

    public List<String> getMembers() {
        return members;
    }
}
