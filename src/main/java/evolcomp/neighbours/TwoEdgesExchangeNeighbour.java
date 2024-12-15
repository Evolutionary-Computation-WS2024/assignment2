package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * O -- a1 -- b1 -- O
 * |                |
 * O -- b2 -- a2 -- O
 * 
 *         |
 *         V
 * 
 * O -- a1   b1 -- O
 * |       X       |
 * O -- b2   a2 -- O
 * 
 * @author Jerzu, Siemieniuk
 */
public final class TwoEdgesExchangeNeighbour
    extends NeighbourStrategy
        implements IntraRouteNeighbour {

    private final int a1;
    private final int a2;
    private final int b1;
    private final int b2;

    public TwoEdgesExchangeNeighbour() {
        super();
        this.a1 = -1;
        this.a2 = -1;
        this.b1 = -1;
        this.b2 = -1;
    }
    
    public TwoEdgesExchangeNeighbour(
            TSPInstance instance,
            Cycle currentSolution,
            int firstEdgeStartingNodePositionIndexInRoute,
            int secondEdgeStartingNodePositionIndexInRoute)
    {
        this(instance, currentSolution, firstEdgeStartingNodePositionIndexInRoute, secondEdgeStartingNodePositionIndexInRoute, true);
    }

    public TwoEdgesExchangeNeighbour(
            TSPInstance instance,
            Cycle currentSolution,
            int firstEdgeStartingNodePositionIndexInRoute,
            int secondEdgeStartingNodePositionIndexInRoute,
            boolean oppositeEdges)
    {
        super(instance);

        if (!isValid(firstEdgeStartingNodePositionIndexInRoute, secondEdgeStartingNodePositionIndexInRoute, instance.getRequiredCycleLength())) {
            throw new IllegalArgumentException("Minimal distance between two entries must be 2!");
        }

        int a1Index = firstEdgeStartingNodePositionIndexInRoute;
        int a2Index = secondEdgeStartingNodePositionIndexInRoute;
        int b1Index = (a1Index + 1)%currentSolution.getNodes().size();
        int b2Index = (a2Index + 1)%currentSolution.getNodes().size();

        if (oppositeEdges) {
            int swap = a1Index;
            a1Index = a2Index;
            a2Index = swap;
        }

        this.a1 = currentSolution.getNodes().get(a1Index);
        this.a2 = currentSolution.getNodes().get(a2Index);
        this.b1 = currentSolution.getNodes().get(b1Index);
        this.b2 = currentSolution.getNodes().get(b2Index);
    }

    @Override
    public int evaluate(Cycle solution) {
        int costs = 0;
        int gains = 0;
        costs += tsp.getDistanceBetween(a1, a2);
        costs += tsp.getDistanceBetween(b1, b2);

        gains += tsp.getDistanceBetween(a1, b1);
        gains += tsp.getDistanceBetween(a2, b2);

        delta = costs - gains;
        return delta;
    }

    @Override
    public Cycle buildNeighbour(Cycle previousSolution) {
        int b1Index = previousSolution.getIndexOfElement(b1);
        int a2Index = previousSolution.getIndexOfElement(a2);

        List<Integer> neighbourAsList = reverseSubList(previousSolution.getNodes(), b1Index, a2Index);

        return new Cycle(neighbourAsList);
    }

    @Override
    public boolean isValid(int first, int second, int cycleLength) {
        int distance = Math.abs(first - second);
        return distance >= 2 && distance <= cycleLength - 2;
    }

    /**
     * 
     * @param originalList - Original list
     * @param start - the last node of edge 1 (B1)
     * @param end  - the First node of edge 2 (A2)
     */
    private static List<Integer> reverseSubList(List<Integer> originalList, int start, int end) {
        List<Integer> newList = new ArrayList<>(originalList);
        int n = newList.size();

        while (start != end) {
            Collections.swap(newList, start, end);
            start = (start + 1) % n;
            if (start == end) break;
            end = (end - 1 + n) % n;
        }
        return newList;
    }

    @Override
    public boolean allEdgesExist(Cycle bestSolution) {
        boolean doesFirstEdgeExist = bestSolution.areNodesAdjacentByCycleIdx(a1, b1);
        boolean doesSecondEdgeExist = bestSolution.areNodesAdjacentByCycleIdx(a2, b2);
        return doesFirstEdgeExist && doesSecondEdgeExist;
    }

    @Override
    public boolean edgesContainSameReturns(Cycle solution) {
        return solution.edgesContainSameReturns(a1, b1, a2, b2);
    }

    @Override
    public NeighbourStrategy construct(TSPInstance tsp, Cycle currentSolution, int i, int j, boolean oppositeEdges) {
        return new TwoEdgesExchangeNeighbour(tsp, currentSolution, i, j, oppositeEdges);
    }

    @Override
    public String toString() {
        return "2-Edges";
    }

    public int getIdxA1(Cycle solution) {
        return solution.getIndexOfElement(a1);
    }

    public int getIdxA2(Cycle solution) {
        return solution.getIndexOfElement(a2);
    }

    public int getIdxB1(Cycle solution) {
        return solution.getIndexOfElement(b1);
    }

    public int getIdxB2(Cycle solution) {
        return solution.getIndexOfElement(b2);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TwoEdgesExchangeNeighbour that)) return false;

        return a1 == that.a1 && a2 == that.a2 && b1 == that.b1 && b2 == that.b2;
    }

    @Override
    public int hashCode() {
        int result = a1;
        result = 31 * result + a2;
        result = 31 * result + b1;
        result = 31 * result + b2;
        return result;
    }
}
