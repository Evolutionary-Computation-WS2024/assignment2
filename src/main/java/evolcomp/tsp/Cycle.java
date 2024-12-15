package evolcomp.tsp;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Cycle {
    private final List<Integer> nodes;
    private final Map<Integer, Integer> tspNodeToCycleIdxMap;

    public Cycle(List<Integer> nodes) {
        this.nodes = nodes;
        this.tspNodeToCycleIdxMap = new HashMap<>();

        // Populate the map with node values as keys and their indices as values
        for (int i = 0; i < nodes.size(); i++) {
            tspNodeToCycleIdxMap.put(nodes.get(i), i);
        }

        // Check consistency
        if (tspNodeToCycleIdxMap.size() != nodes.size()) {
            throw new IllegalArgumentException("Number of distinct nodes is less than number of provided list (should be equal)");
        }
    }

    /**
     * Returns the ID of node in the cycle that is immediately before the given index.
     *
     * @param cycleIdx the index of the node for which to find the previous node
     * @return the previous node ID
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public Integer getPreviousNodeIndex(int cycleIdx) {
        if (cycleIdx < 0 || cycleIdx >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + cycleIdx + " provided)");
        }

        // Calculate the previous index (circular behavior)
        return (cycleIdx - 1 + nodes.size()) % nodes.size();
    }

     /**
     * Returns the ID of node in the cycle that is immediately before the given index.
     *
     * @param cycleIdx the index of the node for which to find the previous node
     * @return the previous node ID
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public Integer getNextNodeIndex(int cycleIdx) {
        if (cycleIdx < 0 || cycleIdx >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + cycleIdx + " provided)");
        }

        // Calculate the next index (circular behavior)
        return (cycleIdx + 1) % nodes.size();
    }

    /**
     * // Returns the index or null if the node doesn't exist
     * @param tspNode A node in TSP
     * @return An index of this element in the solution (or null if it does not exist)
     */
    public Integer getIndexOfElement(int tspNode) {
        return tspNodeToCycleIdxMap.get(tspNode);
    }

    public boolean areNodesAdjacentByCycleIdx(int firstCycleIdx, int secondCycleIdx) {
        Integer firstNodeIdx = tspNodeToCycleIdxMap.get(firstCycleIdx);
        Integer secondNodeIdx = tspNodeToCycleIdxMap.get(secondCycleIdx);

        if (firstNodeIdx == null || secondNodeIdx == null) {
            return false;
        }

        int diff = Math.abs(firstNodeIdx - secondNodeIdx);
        return diff == 1 || diff == nodes.size() - 1;
    }

    public boolean edgesContainSameReturns(int edgeStart1, int edgeEnd1, int edgeStart2, int edgeEnd2) {
        int firstNodeIdx = tspNodeToCycleIdxMap.get(edgeStart1);
        int secondNodeIdx = tspNodeToCycleIdxMap.get(edgeEnd1);
        boolean isFirstLeftToRight = isLeftToRight(firstNodeIdx, secondNodeIdx);

        firstNodeIdx = tspNodeToCycleIdxMap.get(edgeStart2);
        secondNodeIdx = tspNodeToCycleIdxMap.get(edgeEnd2);
        boolean isSecondLeftToRight = isLeftToRight(firstNodeIdx, secondNodeIdx);

        return isFirstLeftToRight == isSecondLeftToRight;
    }

    private boolean isLeftToRight(int firstCycleIdx, int secondCycleIdx) {
        return ((secondCycleIdx - firstCycleIdx + nodes.size()) % nodes.size()) == 1;
    }

    public List<Integer> getNodes() {
        return nodes;
    }

    @Override
    public String toString() {
        return String.join(",", nodes.stream().map(Object::toString).toList());
    }
}

