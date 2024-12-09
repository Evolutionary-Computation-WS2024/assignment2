package evolcomp.tsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cycle {
    private final List<Integer> nodes;

    // key - node value
    // value - location on the cycle (index in the array)
    private final Map<Integer, Integer> nodeIndexMap;

    public Cycle(List<Integer> nodes) {
        this.nodes = nodes;
        this.nodeIndexMap = new HashMap<>();

        // Populate the map with node values as keys and their indices as values
        for (int i = 0; i < nodes.size(); i++) {
            nodeIndexMap.put(nodes.get(i), i);
        }

        // Check consistency
        if (nodeIndexMap.size() != nodes.size()) {
            throw new IllegalArgumentException("Number of distinct nodes is less than number of provided list (should be equal)");
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

    public Integer getIndexOfElement(int node) {
        return nodeIndexMap.get(node);  // Returns the index or null if the node doesn't exist
    }

    public boolean areNodesNextToEachOther(int node1, int node2) {
        Integer firstNodeIdx = nodeIndexMap.get(node1);
        Integer secondNodeIdx = nodeIndexMap.get(node2);

        if (firstNodeIdx == null || secondNodeIdx == null) {
            return false;
        }

        int diff = Math.abs(firstNodeIdx - secondNodeIdx);
        return diff == 1 || diff == nodes.size() - 1;
    }

    public boolean edgesContainSameReturns(int edgeStart1, int edgeEnd1, int edgeStart2, int edgeEnd2) {
        int firstNodeIdx = nodeIndexMap.get(edgeStart1);
        int secondNodeIdx = nodeIndexMap.get(edgeEnd1);
        boolean isFirstLeftToRight = isLeftToRight(firstNodeIdx, secondNodeIdx);

        firstNodeIdx = nodeIndexMap.get(edgeStart2);
        secondNodeIdx = nodeIndexMap.get(edgeEnd2);
        boolean isSecondLeftToRight = isLeftToRight(firstNodeIdx, secondNodeIdx);

        return isFirstLeftToRight == isSecondLeftToRight;
    }

    private boolean isLeftToRight(int firstIdx, int secondIdx) {
        return ((secondIdx - firstIdx + nodes.size()) % nodes.size()) == 1;
    }

    public List<Integer> getNodes() {
        return nodes;
    }
}

