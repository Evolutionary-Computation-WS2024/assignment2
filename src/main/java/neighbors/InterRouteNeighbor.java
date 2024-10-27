/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package neighbors;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author Jerzu
 */
class InterRouteNeighbor extends NeighborStrategy {
    private final int nodePositionIndexInRoute;
    private final int newNodeId;

    // Constructor initializing the specific fields and passing currentSolution to the superclass
    public InterRouteNeighbor(TSPInstance instance, Cycle currentSolution, int nodePositionIndexInRoute, int newNodeId) {
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
    public Cycle buildNeighbor() {
        List<Integer> nodesListCopy = new ArrayList (this.currentSolution.nodes());
        nodesListCopy.set(this.nodePositionIndexInRoute, newNodeId);
        return new Cycle(nodesListCopy);
    }
}
