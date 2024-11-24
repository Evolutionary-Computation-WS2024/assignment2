package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.List;

public class InterRouteNeighbour extends NeighbourStrategy {
    private final int nodePositionIndexInRoute;
    private final int newNodeId;

    public InterRouteNeighbour() {
        super();
        nodePositionIndexInRoute = 0;
        newNodeId = 0;
    }

    // Constructor initializing the specific fields and passing currentSolution to the superclass
    public InterRouteNeighbour(TSPInstance instance, Cycle currentSolution, int nodePositionIndexInRoute, int newNodeId) {
        super(instance, currentSolution);
        this.nodePositionIndexInRoute = nodePositionIndexInRoute;
        this.newNodeId = newNodeId;
    }

    @Override
    public int evaluate() {
        this.evaluationResult = this.getNodeInsertionDelta(newNodeId, nodePositionIndexInRoute);
        return evaluationResult;
    }

    @Override
    public Cycle buildNeighbour() {
        List<Integer> nodesListCopy = new ArrayList<>(this.currentSolution.getNodes());
        nodesListCopy.set(this.nodePositionIndexInRoute, newNodeId);
        return new Cycle(nodesListCopy);
    }

    @Override
    public InterRouteNeighbour construct(TSPInstance instance, Cycle currentSolution, int nodePositionIndexInRoute, int newNodeId) {
        return new InterRouteNeighbour(instance, currentSolution, nodePositionIndexInRoute, newNodeId);
    }

    @Override
    public boolean isValid(int first, int second) {
        return first != second;
    }
}
