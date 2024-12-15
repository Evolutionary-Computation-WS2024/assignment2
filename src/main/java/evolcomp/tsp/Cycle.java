package evolcomp.tsp;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class Cycle {
    public final List<Integer> nodes;
    private int score;
    private int simmilarityAvg;
    private int simmilarityBest;
    
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
    public void setSimmilarityAvg(int a) {
        this.simmilarityAvg = a;
    }
    public void setSimmilarityBest(int a) {
        this.simmilarityBest = a;
    }
    public String toCsvRow() {
        return simmilarityAvg + ";" + simmilarityBest;
    } 
    public int computeSimmilarityNodes(Cycle other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cycle cannot be null");
        }
        Set<Integer> thisNodesSet = new HashSet<>(this.nodes);
        Set<Integer> otherNodesSet = new HashSet<>(other.nodes);

        thisNodesSet.retainAll(otherNodesSet); // Retain only common nodes
        return thisNodesSet.size()/otherNodesSet.size();
    }
    public int computeSimmilarityEdges(Cycle other) {
        if (other == null) {
            throw new IllegalArgumentException("Other cycle cannot be null");
        }

        Set<String> thisEdgesSet = getEdgesSet(this.nodes);
        Set<String> otherEdgesSet = getEdgesSet(other.nodes);

        thisEdgesSet.retainAll(otherEdgesSet); // Retain only common edges
        return thisEdgesSet.size()/otherEdgesSet.size();
    }

    // Helper method to get a set of edges as strings
    private Set<String> getEdgesSet(List<Integer> nodes) {
        Set<String> edges = new HashSet<>();
        int size = nodes.size();

        for (int i = 0; i < size; i++) {
            int from = nodes.get(i);
            int to = nodes.get((i + 1) % size); // Wrap around to form a cycle

            // Store edges in a consistent order (smallest node first)
            edges.add(from < to ? from + "-" + to : to + "-" + from);
        }

        return edges;
    }
}
