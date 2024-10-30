package neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TwoNodesExchangeNeighbour extends NeighbourStrategy {
    private int firstNodePositionIndexInRoute;
    private int secondNodePositionIndexInRoute;

    public TwoNodesExchangeNeighbour() {
        super();
    }

    public TwoNodesExchangeNeighbour(
            TSPInstance instance,
            Cycle currentSolution,
            int firstNodePositionIndexInRoute,
            int secondNodePositionIndexInRoute)
    {
        super(instance, currentSolution);
        // ensure that the first node is actually before the second (required for case 1 in evaluate)
        if (firstNodePositionIndexInRoute == 0 && secondNodePositionIndexInRoute == this.currentSolution.getNodes().size()-1) {
            this.firstNodePositionIndexInRoute = secondNodePositionIndexInRoute;
            this.secondNodePositionIndexInRoute = firstNodePositionIndexInRoute;
        } else if (firstNodePositionIndexInRoute == this.currentSolution.getNodes().size()-1 && secondNodePositionIndexInRoute == 0) {
            this.firstNodePositionIndexInRoute = firstNodePositionIndexInRoute;
            this.secondNodePositionIndexInRoute = secondNodePositionIndexInRoute;
        } else if (firstNodePositionIndexInRoute > secondNodePositionIndexInRoute) {
            this.firstNodePositionIndexInRoute = secondNodePositionIndexInRoute;
            this.secondNodePositionIndexInRoute = firstNodePositionIndexInRoute;
        } else {
            this.firstNodePositionIndexInRoute = firstNodePositionIndexInRoute;
            this.secondNodePositionIndexInRoute = secondNodePositionIndexInRoute;
        }
    }

    @Override
    public int evaluate() {
        List<Integer> nodesList = this.currentSolution.getNodes();
        int nextNodeID, prevNodeID, delta;
        int indexesDifference = Math.abs(firstNodePositionIndexInRoute - secondNodePositionIndexInRoute);
        
        int costs = 0;
        int gains = 0;
        
        //case 1 swap two consecutive nodes
        // O--O--A--B--O
        if (indexesDifference == 1 ||  indexesDifference == nodesList.size()-1) {
            nextNodeID = nodesList.get((secondNodePositionIndexInRoute + 1)% (this.currentSolution.getNodes().size()));
            if (firstNodePositionIndexInRoute == 0) {
                prevNodeID = nodesList.get(this.currentSolution.getNodes().size()-1);
            } else {
                prevNodeID = nodesList.get(firstNodePositionIndexInRoute-1);
            }
            costs += this.instance.getDistanceBetween(prevNodeID, nodesList.get(secondNodePositionIndexInRoute));
            costs += this.instance.getDistanceBetween(nextNodeID, nodesList.get(firstNodePositionIndexInRoute));
            gains += this.instance.getDistanceBetween(prevNodeID, nodesList.get(firstNodePositionIndexInRoute));
            gains += this.instance.getDistanceBetween(nextNodeID, nodesList.get(secondNodePositionIndexInRoute));
            delta = costs - gains;
        } 
        //case 2 swap two non consecutive nodes
        // A--O--O--B--O
        else {
            int firstNodeID = nodesList.get(firstNodePositionIndexInRoute);
            int secondNodeID = nodesList.get(secondNodePositionIndexInRoute);
            delta = this.getNodeInsertionDelta(firstNodeID,secondNodePositionIndexInRoute);
            delta += this.getNodeInsertionDelta(secondNodeID,firstNodePositionIndexInRoute);
        } 
        this.evaluationResult = delta;
        return delta;
    }

    @Override
    public Cycle buildNeighbour() {
        List<Integer> nodesListCopy = new ArrayList<>(this.currentSolution.getNodes());
        Collections.swap(nodesListCopy, this.firstNodePositionIndexInRoute, this.secondNodePositionIndexInRoute);
        return new Cycle(nodesListCopy);
    }

    @Override
    public TwoNodesExchangeNeighbour construct(TSPInstance instance, Cycle currentSolution, int first, int second) {
        return new TwoNodesExchangeNeighbour(instance, currentSolution, first, second);
    }

    @Override
    public boolean isValid(int first, int second) {
        return first != second;
    }

    @Override
    public String toString() {
        return "2-Nodes";
    }
}
