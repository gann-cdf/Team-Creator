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
        Map<String, Node> nameDirectory = new HashMap<>();
        Iterator<Map.Entry<String, List<String>>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, List<String>> namePreferencesPair = iterator.next();
            String myName = namePreferencesPair.getKey();
            List<String> myPreferences = namePreferencesPair.getValue();
            if (myName.length() > 0) {
                Node n = createIfNotExisting(nameDirectory, myName);
                for (int ranking = 0; ranking < myPreferences.size(); ranking++) {
                    String neighborName = myPreferences.get(ranking);
                    if (neighborName != null && !myName.equals(neighborName)) {
                        n.addEdge(createIfNotExisting(nameDirectory, neighborName), createEdge(merge, ranking));
                    }
                }
            }
        }
        nodes = new ArrayList<>(nameDirectory.values());
        completeConnections(merge);
    }

    private Edge createEdge(MergeType merge, int distance) {
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
        return e;
    }

    /**
     * Make the graph completely connected
     * <p>
     * The pre-existing median edge length will be used for all newly created edges
     *
     * @param merge The type of edge-merge operation requested
     */
    private void completeConnections(MergeType merge) {
        int arbitraryDistance = getMedianEdge();
        for (Node n : nodes) {
            for (Node neighbor : nodes) {
                if (neighbor != n && !n.getEdges().keySet().contains(neighbor)) {
                    n.addEdge(neighbor, createEdge(merge, arbitraryDistance));
                }
            }
        }
    }

    /**
     * @return The longest edge in the graph
     */
    public int getLongestEdge() {
        int maxDistance = 0;
        for (Node n : nodes) {
            for (Edge e : n.getEdges().values()) {
                if (e.getDistance() > maxDistance) {
                    maxDistance = e.getDistance();
                }
            }
        }
        return maxDistance;
    }

    /**
     * @return The median edge value in the graph
     */
    public int getMedianEdge() {
        List<Integer> distances = new ArrayList<>();
        for (Node n : nodes) {
            for (Edge e : n.getEdges().values()) {
                distances.add(e.getDistance());
            }
        }
        distances.sort(Integer::compareTo);
        return distances.get(distances.size() / 2);
    }

    /**
     * Ensure that the requested key exists in the map and return it
     *
     * @param map The map to examine
     * @param key The key to look for
     * @return The node (now in the map) for the key
     */
    private Node createIfNotExisting(Map<String, Node> map, String key) {
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
        List<Node> merged = new ArrayList<>();
        for (Node n : nodes) {
            if (!merged.contains(n)) {
                for (Node neighbor : n.neighbors(distance)) {
                    if (n != neighbor && !merged.contains(neighbor) && n.size() + neighbor.size() <= maxClusterSize) {
                        n.merge(neighbor);
                        merged.add(neighbor);
                    }
                }
            }
        }
        for (Node n : merged) {
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
