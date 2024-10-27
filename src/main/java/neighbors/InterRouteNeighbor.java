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
        List<Integer> nodesList = this.currentSolution.nodes();
        int lastNodeIndex = nodesList.size() - 1;
        int prevNodeID, nextNodeID;
        
        if (nodePositionIndexInRoute != 0) {
            prevNodeID = nodesList.get(nodePositionIndexInRoute - 1);     
        } else {
            prevNodeID = nodesList.get(lastNodeIndex);
        }
        if (nodePositionIndexInRoute != lastNodeIndex) {
            nextNodeID = nodesList.get(nodePositionIndexInRoute + 1);     
        } else {
            nextNodeID = nodesList.get(0);
        }
        
        int gains = this.instance.getCostAt(nodesList.get(this.nodePositionIndexInRoute));
        int costs = this.instance.getCostAt(this.newNodeId);
        
        gains += this.instance.getDistanceBetween(prevNodeID, nodesList.get(this.nodePositionIndexInRoute));
        gains += this.instance.getDistanceBetween(nextNodeID, nodesList.get(this.nodePositionIndexInRoute));
        
        costs +=  this.instance.getDistanceBetween(prevNodeID, newNodeId);
        costs +=  this.instance.getDistanceBetween(nextNodeID, newNodeId);
        
        int delta = costs - gains;
        this.evaluationResult = delta;
        return delta;
    }

    @Override
    public Cycle buildNeighbor() {
        List<Integer> nodesListCopy = new ArrayList (this.currentSolution.nodes());
        nodesListCopy.set(this.nodePositionIndexInRoute, newNodeId);
        return new Cycle(nodesListCopy);
    }
}
