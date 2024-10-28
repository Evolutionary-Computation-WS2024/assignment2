package neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.List;

public abstract class NeighbourStrategy {
    public int evaluationResult;
    public Cycle ThisNeighbor;
    protected final Cycle currentSolution;
    protected final TSPInstance instance;

    public NeighbourStrategy() {
        this.currentSolution = null;
        this.instance = null;
    }
    
    public NeighbourStrategy(TSPInstance instance, Cycle currentSolution) {
        this.instance = instance;
        this.currentSolution = currentSolution;
    }

    /** 
    * returns utility delta for this neighbour and sets it as evaluationResult
    * negative delta -> neighbour is better than current solution
    * positive delta -> neighbour is worse than current solution
    */
    public abstract int evaluate();

    /**
     * Actually returns this neighbour as a cycle object and sets it as ThisNeighbor
     */
    public abstract Cycle buildNeighbour();

    /**
    * returns utility delta for substituting the given node on the given place in a cycle
    */
    protected int getNodeInsertionDelta(int nodeID, int nodePositionIndexInRoute) {
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
        
        int gains = this.instance.getCostAt(nodesList.get(nodePositionIndexInRoute));
        int costs = this.instance.getCostAt(nodeID);
        
        gains += this.instance.getDistanceBetween(prevNodeID, nodesList.get(nodePositionIndexInRoute));
        gains += this.instance.getDistanceBetween(nextNodeID, nodesList.get(nodePositionIndexInRoute));
        
        costs +=  this.instance.getDistanceBetween(prevNodeID, nodeID);
        costs +=  this.instance.getDistanceBetween(nextNodeID, nodeID);
        
        int delta = costs - gains;
        this.evaluationResult = delta;
        return delta;
    }

    /**
     * Checks whether this combination of parameters is valid
     */
    public abstract boolean isValid(int first, int second);

    public abstract NeighbourStrategy construct(TSPInstance instance, Cycle currentSolution, int first, int second);
}