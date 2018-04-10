package teamCreator.core;

import teamCreator.core.merges.EdgeAverageMerge;
import teamCreator.core.merges.EdgeMaxMerge;
import teamCreator.core.merges.EdgeSumMerge;
import teamCreator.core.merges.MergeType;

import java.util.*;

public class Graph {
    private List<Node> nodes;
    private Map<String, Node> temp;

    ;

    public Graph(Map<String, List<String>> data) {
        this(data, MergeType.Maximizing);
    }

    public Graph(Map<String, List<String>> data, MergeType merge) {
        temp = new HashMap<>();
        Iterator<Map.Entry<String, List<String>>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> pair = iterator.next();
            Node n = getTemp(pair.getKey());
            for (int distance = 0; distance < pair.getValue().size(); distance++) {
                Edge e = null;
                switch(merge) {
                    case Summing:
                        e = new EdgeSumMerge(distance);
                        break;
                    case Maximizing:
                        e = new EdgeMaxMerge(distance);
                        break;
                    case Averaging:
                        e = new EdgeAverageMerge(distance);
                        break;
                }
                n.addEdge(getTemp(pair.getValue().get(distance)), e);
            }
        }
        nodes = new ArrayList<>(temp.values());
    }

    private Node getTemp(String key) {
        if (!temp.containsKey(key.trim())) {
            temp.put(key, new Node(key));
        }
        return temp.get(key);
    }

    private void remove(Node n) {
        for (Node m : nodes) {
            m.remove(n);
        }
        nodes.remove(n);
    }

    public void collapse(int distance, int maxClusterSize) {
        List<Node> purge = new ArrayList<>();
        for (Node n : nodes) {
            if (!purge.contains(n)) {
                for (Node neighbor : n.neighbors(distance)) {
                    if (!purge.contains(neighbor) && n.size() + neighbor.size() <= maxClusterSize) {
                        n.merge(neighbor);
                        purge.add(neighbor);
                    }
                }
            }
        }
        for(Node n : purge) {
            remove(n);
        }
    }

    public int count() {
        return nodes.size();
    }

    public String toString() {
        String result = count() + " teams:\n";
        for (Node n : nodes) {
            result += "\t" + n + "\n";
        }
        return result;
    }

    public List<Node> getNodes() {
        return nodes;
    }
}
