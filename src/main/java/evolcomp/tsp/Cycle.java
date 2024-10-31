package evolcomp.tsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cycle {
    private final List<Integer> nodes;
    private final Map<Integer, Integer> nodeIndexMap;

    public Cycle(List<Integer> nodes) {
        this.nodes = nodes;
        this.nodeIndexMap = new HashMap<>();
        
        // Populate the map with node values as keys and their indices as values
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i), i);
        }
    }

    @Override
    public String toString() {
        return String.join(",", nodes.stream().map(Object::toString).toList());
    }
    /**
     * Returns the ID of node in the cycle that is immediately before the given index.
     *
     * @param index the index of the node for which to find the previous node
     * @return the previous node ID
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public Integer getPreviousNodeIndex(int index) {
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + index + " provided)");
        }

        // Calculate the previous index (circular behavior)
        int previousIndex = (index - 1 + nodes.size()) % nodes.size();

        return previousIndex;
    }
     /**
     * Returns the ID of node in the cycle that is immediately before the given index.
     *
     * @param index the index of the node for which to find the previous node
     * @return the previous node ID
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public Integer getNextNodeIndex(int index) {
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + index + " provided)");
        }

        // Calculate the previous index (circular behavior)
        int nextIndex = (index + 1) % nodes.size();

        return nextIndex;
    }
    
    public int getIndexOfElement(int node) {
        return nodeIndexMap.get(node);  // Returns the index or null if the node doesn't exist
    }

    public List<Integer> getNodes() {
        return nodes;
    }
}

