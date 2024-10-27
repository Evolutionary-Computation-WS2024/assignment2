package neighbors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.List;
/**
 *
    * @author Jerzu
 */
abstract class NeighborStrategy {
    public int evaluationResult;
    public Cycle ThisNeighbor;
    protected final Cycle currentSolution;
    protected final TSPInstance instance;
    
    public NeighborStrategy(TSPInstance instance, Cycle currentSolution) {
        this.instance = instance;
        this.currentSolution = currentSolution;
    }
    /** 
     returns utility delta for this neighbour
     negative delta -> neighbour is better than current solution
     positive delta -> neighbour is worse than current solution
    */
    public abstract int evaluate();

    public abstract Cycle buildNeighbor();
    /** 
     returns utility delta for substituting the given node on the given place in a cycle
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
}