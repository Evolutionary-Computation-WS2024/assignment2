package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * O -- A1 -- B1 -- O
 * |                |
 * O -- B2 -- A2 -- O
 * 
 *         |
 *         V
 * 
 * O -- A1   B1 -- O
 * |       X       |
 * O -- B2   A2 -- O
 * 
 * @author Jerzu, Siemieniuk
 */
public final class TwoEdgesExchangeNeighbour
    extends NeighbourStrategy
        implements IntraRouteNeighbour {

    private final int A1Index;
    private final int A2Index;
    private final int B1Index;
    private final int B2Index;
    
    private final int A1;
    private final int A2;
    private final int B1;
    private final int B2;

    public TwoEdgesExchangeNeighbour() {
        super();
        this.A1Index = 0;
        this.A2Index = 0;
        this.B1Index = 0;
        this.B2Index = 0;
        this.A1 = -1;
        this.A2 = -1;
        this.B1 = -1;
        this.B2 = -1;
    }
    
    public TwoEdgesExchangeNeighbour(
            TSPInstance instance,
            Cycle currentSolution,
            int firstEdgeStartingNodePositionIndexInRoute,
            int secondEdgeStartingNodePositionIndexInRoute)
    {
        super(instance, currentSolution);

        if (!isValid(firstEdgeStartingNodePositionIndexInRoute, secondEdgeStartingNodePositionIndexInRoute)) {
            throw new IllegalArgumentException("Minimal distance between two entries must be 2!");
        }

        this.A1Index = firstEdgeStartingNodePositionIndexInRoute;
        this.A2Index = secondEdgeStartingNodePositionIndexInRoute;
        this.B1Index = (A1Index + 1)%currentSolution.getNodes().size();
        this.B2Index = (A2Index + 1)%currentSolution.getNodes().size();

        this.A1 = currentSolution.getNodes().get(A1Index);
        this.A2 = currentSolution.getNodes().get(A2Index);
        this.B1 = currentSolution.getNodes().get(B1Index);
        this.B2 = currentSolution.getNodes().get(B2Index);
    }

    @Override
    public int evaluate() {
        int costs = 0;
        int gains = 0;
        costs += instance.getDistanceBetween(A1, A2);
        costs += instance.getDistanceBetween(B1, B2);

        gains += instance.getDistanceBetween(A1, B1);
        gains += instance.getDistanceBetween(A2, B2);

        int delta = costs - gains;
        this.evaluationResult = delta;
        return delta;
    }

    @Override
    public Cycle buildNeighbour() {
        List<Integer> neighbourAsList = reverseSubList(this.currentSolution.getNodes(), B1Index, A2Index);
        return new Cycle(neighbourAsList);
    }

    // TODO: Implement
    @Override
    public boolean areEdgesOppositeTo(Cycle solution) {
        return false;
    }

    @Override
    public boolean isValid(int first, int second) {
        int distance = Math.abs(first - second);
        return distance >= 2;
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
    public TwoEdgesExchangeNeighbour construct(TSPInstance instance, Cycle currentSolution, int first, int second) {
        return new TwoEdgesExchangeNeighbour(instance, currentSolution, first, second);
    }

    @Override
    public String toString() {
        return "2-Edges";
    }

    // TODO: Implement
    @Override
    public boolean allEdgesExist(Cycle bestSolution) {
        return false;
    }
}
