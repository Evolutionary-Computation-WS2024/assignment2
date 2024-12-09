package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.List;

public abstract class NeighbourStrategy {
//    protected final Cycle currentSolution;
    protected final TSPInstance tsp;
    protected int delta = 0;

    public NeighbourStrategy() {
//        this.currentSolution = null;
        this.tsp = null;
    }

//    public NeighbourStrategy(TSPInstance instance, Cycle currentSolution) {
    public NeighbourStrategy(TSPInstance tsp) {
        this.tsp = tsp;
//        this.currentSolution = currentSolution;
    }

    /** 
    * returns utility delta for this neighbour and sets it as evaluationResult
    * negative delta -> neighbour is better than current solution
    * positive delta -> neighbour is worse than current solution
    */
    public abstract int evaluate(Cycle solution);

    /**
     * Actually returns this neighbour as a cycle object and sets it as ThisNeighbor
     */
    public abstract Cycle buildNeighbour(Cycle previousSolution);

    /**
    * returns utility delta for substituting the given node on the given place in a cycle
    */
    protected final int getNodeInsertionDelta(Cycle solution, int nodeTsp, int nodeSolutionIdx) {
        List<Integer> nodesList = solution.getNodes();
        int lastNodeIndex = nodesList.size() - 1;
        int nodeSolutionTsp = nodesList.get(nodeSolutionIdx);

        int prevNodeTsp;
        if (nodeSolutionIdx != 0) {
            prevNodeTsp = nodesList.get(nodeSolutionIdx - 1);
        } else {
            prevNodeTsp = nodesList.get(lastNodeIndex);
        }

        int nextNodeTsp;
        if (nodeSolutionIdx != lastNodeIndex) {
            nextNodeTsp = nodesList.get(nodeSolutionIdx + 1);
        } else {
            nextNodeTsp = nodesList.get(0);
        }


        int gains = tsp.getCostAt(nodeSolutionTsp);
        gains += tsp.getDistanceBetween(prevNodeTsp, nodeSolutionTsp);
        gains += tsp.getDistanceBetween(nextNodeTsp, nodeSolutionTsp);

        int costs = tsp.getCostAt(nodeTsp);
        costs +=  tsp.getDistanceBetween(prevNodeTsp, nodeTsp);
        costs +=  tsp.getDistanceBetween(nextNodeTsp, nodeTsp);

        delta = costs - gains;
        return delta;
    }

    public int getDelta() {
        return delta;
    }
}