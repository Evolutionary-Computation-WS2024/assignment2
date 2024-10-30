package evolcomp.tsp;

import java.util.List;

public record Cycle(List<Integer> nodes) {
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
    public Integer getPreviousNode(int index) {
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + index + " provided)");
        }

        // Calculate the previous index (circular behavior)
        int previousIndex = (index - 1 + nodes.size()) % nodes.size();

        return nodes.get(previousIndex);
    }
     /**
     * Returns the ID of node in the cycle that is immediately before the given index.
     * 
     * @param index the index of the node for which to find the previous node
     * @return the previous node ID
     * @throws IllegalArgumentException if the index is out of bounds
     */
    public Integer getNextNode(int index) {
        if (index < 0 || index >= nodes.size()) {
            throw new IllegalArgumentException("Index must be between 0 and " + (nodes.size() - 1) + " (index=" + index + " provided)");
        }

        // Calculate the previous index (circular behavior)
        int previousIndex = (index + 1) % nodes.size();

        return nodes.get(previousIndex);
    }
    public int getIndexOfElement(int element) {
        return nodes.indexOf(element);
    }
}

