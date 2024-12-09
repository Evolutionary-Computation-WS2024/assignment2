package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.List;

public class InterRouteNeighbour extends NeighbourStrategy {
    private final int oldNodeValue;
    private final int newNodeValue;
    private final int prevNodeValue;
    private final int nextNodeValue;

    private InterRouteNeighbour() {
        super();
        this.oldNodeValue = 0;
        this.newNodeValue = 0;
        this.prevNodeValue = 0;
        this.nextNodeValue = 0;
    }

    // Constructor initializing the specific fields and passing currentSolution to the superclass
    public InterRouteNeighbour(TSPInstance instance, Cycle currentSolution, int oldNodeIdx, int newNodeValue) {
        super(instance);
        this.oldNodeValue = currentSolution.getNodes().get(oldNodeIdx);
        this.newNodeValue = newNodeValue;
        this.prevNodeValue = currentSolution.getNodes().get(currentSolution.getPreviousNodeIndex(oldNodeIdx));
        this.nextNodeValue = currentSolution.getNodes().get(currentSolution.getNextNodeIndex(oldNodeIdx));
    }

    @Override
    public int evaluate(Cycle solution) {
        int oldNodeIdx = solution.getIndexOfElement(oldNodeValue);
        delta = getNodeInsertionDelta(solution, newNodeValue, oldNodeIdx);
        return delta;
    }

    public boolean isConsistentWith(Cycle solution) {
        // If element was found, then it's not consistent
        if (getNewNodeIndex(solution) != null) {
            return false;
        }

        boolean firstEdgeExists = solution.areNodesNextToEachOther(prevNodeValue, oldNodeValue);
        boolean secondEdgeExists = solution.areNodesNextToEachOther(oldNodeValue, nextNodeValue);
        return firstEdgeExists && secondEdgeExists;
    }

    @Override
    public Cycle buildNeighbour(Cycle previousSolution) {
        List<Integer> nodesListCopy = new ArrayList<>(previousSolution.getNodes());
        int oldNodeIdx = previousSolution.getIndexOfElement(oldNodeValue);
        nodesListCopy.set(oldNodeIdx, newNodeValue);
        return new Cycle(nodesListCopy);
    }

    public InterRouteNeighbour construct(TSPInstance instance, Cycle currentSolution,
                                         int nodePositionIndexInRoute, int newNodeId) {
        return new InterRouteNeighbour(instance, currentSolution, nodePositionIndexInRoute, newNodeId);
    }

    public Integer getNewNodeIndex(Cycle solution) {
        return solution.getIndexOfElement(newNodeValue);
    }

    public Integer getPrevNodeIndex(Cycle solution) {
        return solution.getIndexOfElement(prevNodeValue);
    }

    public Integer getNextNodeValue(Cycle solution) {
        return solution.getIndexOfElement(nextNodeValue);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        InterRouteNeighbour that = (InterRouteNeighbour) o;
        return oldNodeValue == that.oldNodeValue && newNodeValue == that.newNodeValue && prevNodeValue == that.prevNodeValue && nextNodeValue == that.nextNodeValue;
    }

    @Override
    public int hashCode() {
        int result = oldNodeValue;
        result = 31 * result + newNodeValue;
        result = 31 * result + prevNodeValue;
        result = 31 * result + nextNodeValue;
        return result;
    }
}
