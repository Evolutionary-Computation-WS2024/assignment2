package evolcomp.tsp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Cycle {
    public final List<Integer> nodes;
    private int score;
    private float setSimmilarityAvgEdge;
    private float setSimmilarityAvgNode;
    private float simmilarityBestEdge;
    private float simmilarityBestNode;
    
    // Constructor
    public Cycle(List<Integer> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        this.nodes = List.copyOf(nodes); // Ensures immutability
    }
    public String toString() {
        return String.join(",", nodes.stream().map(Object::toString).collect(Collectors.toList()));
    }
    public void setScore(int a) {
        this.score = a;
    }
    public void setSimmilarityAvgEdge(float a) {
        this.setSimmilarityAvgEdge = a;
    }
    public void setSimmilarityAvgNode(float a) {
        this.setSimmilarityAvgNode = a;
    }
    public void setSimmilarityBestEdge(float a) {
        this.simmilarityBestEdge = a;
    }
    public void setSimmilarityBestNode(float a) {
        this.simmilarityBestNode = a;
    }
    public String toCsvRow() {
        return score + ";" + setSimmilarityAvgEdge + ";" + setSimmilarityAvgNode + ";" + simmilarityBestEdge + ";" + simmilarityBestNode;
    } 
    public float computeSimmilarityNodes(Cycle other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cycle cannot be null");
        }
        Set<Integer> thisNodesSet = new HashSet<>(this.nodes);
        Set<Integer> otherNodesSet = new HashSet<>(other.nodes);

        thisNodesSet.retainAll(otherNodesSet); // Retain only common nodes
        return (float) thisNodesSet.size()/otherNodesSet.size();
    }
    public float computeSimmilarityEdges(Cycle other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cycle cannot be null");
        }

        Set<String> thisEdgesSet = getEdgesSet(this.nodes);
        Set<String> otherEdgesSet = getEdgesSet(other.nodes);

        thisEdgesSet.retainAll(otherEdgesSet); // Retain only common edges
        return (float) thisEdgesSet.size()/otherEdgesSet.size();
    }

    private Set<String> getEdgesSet(List<Integer> nodes) {
        Set<String> edges = new HashSet<>();
        int size = nodes.size();

        for (int i = 0; i < size; i++) {
            int from = nodes.get(i);
            int to = nodes.get((i + 1) % size); // Wrap around to form a cycle

            edges.add(from < to ? from + "-" + to : to + "-" + from);
        }

        return edges;
    }
}
