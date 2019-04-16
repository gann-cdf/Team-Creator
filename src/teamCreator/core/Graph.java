package teamCreator.core;

import teamCreator.core.merges.EdgeAverageMerge;
import teamCreator.core.merges.EdgeMaxMerge;
import teamCreator.core.merges.EdgeSumMerge;
import teamCreator.core.merges.MergeType;

import java.util.*;

public class Graph {
    private List<Node> nodes;

    public Graph(Map<String, List<String>> data) {
        this(data, MergeType.Maximizing);
    }

    public Graph(Map<String, List<String>> data, MergeType merge) {
        Map<String, Node> map = new HashMap<>();
        Iterator<Map.Entry<String, List<String>>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> pair = iterator.next();
            if (pair.getKey().length() > 0) {
                Node n = getIfExists(map, pair.getKey());
                for (int distance = 0; distance < pair.getValue().size(); distance++) {
                    if (pair.getValue().get(distance) != null && !pair.getKey().equals(pair.getValue().get(distance))) {
                        Edge e = null;
                        switch (merge) {
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
                        n.addEdge(getIfExists(map, pair.getValue().get(distance)), e);
                    }
                }
            }
        }
        for (String key : map.keySet()) {
            if (map.get(key).isIsolated()) {
                for (String neighbor : map.keySet()) {
                    if (!key.equals(neighbor)) {
                        Edge e = null;
                        int distance = map.size() / 2;
                        switch (merge) {
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
                        map.get(key).addEdge(map.get(neighbor), e);
                    }
                }
            }
        }
        nodes = new ArrayList<>(map.values());
    }

    private Node getIfExists(Map<String, Node> map, String key) {
        key = key.trim();
        if (!map.containsKey(key)) {
            map.put(key, new Node(key));
        }
        return map.get(key);
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
                    if (n != neighbor && !purge.contains(neighbor) && n.size() + neighbor.size() <= maxClusterSize) {
                        purge.add(neighbor);
                        n.merge(neighbor);
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
