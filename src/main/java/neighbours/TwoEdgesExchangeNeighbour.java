package neighbours;

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
public class TwoEdgesExchangeNeighbour extends NeighbourStrategy {
    private final int A1Index;
    private final int A2Index;
    private final int B1Index;
    private final int B2Index;
    
    private final int A1;
    private final int A2;
    private final int B1;
    private final int B2;
    
    public TwoEdgesExchangeNeighbour(
            TSPInstance instance,
            Cycle currentSolution,
            int firstEdgeStartingNodePositionIndexInRoute,
            int secondEdgeStartingNodePositionIndexInRoute)
    {
        super(instance, currentSolution);
        this.A1Index = firstEdgeStartingNodePositionIndexInRoute;
        this.A2Index = secondEdgeStartingNodePositionIndexInRoute;
        this.B1Index = (A1Index + 1)%currentSolution.nodes().size();
        this.B2Index = (A2Index + 1)%currentSolution.nodes().size();
        
        this.A1 = currentSolution.nodes().get(A1Index);
        this.A2 = currentSolution.nodes().get(A2Index);
        this.B1 = currentSolution.nodes().get(B1Index);
        this.B2 = currentSolution.nodes().get(B2Index);
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
        List<Integer> neighbourAsList = reverseSubList(this.currentSolution.nodes(),B1Index, A2Index);
        Cycle neighbour = new Cycle(neighbourAsList);
        this.ThisNeighbor = neighbour;
        return neighbour;
    }
    /**
     * 
     * @param originalList
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
}
