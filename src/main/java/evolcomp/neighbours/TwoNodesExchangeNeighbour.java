package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public final class TwoNodesExchangeNeighbour
    extends NeighbourStrategy
        implements IntraRouteNeighbour {

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
//        super(instance, currentSolution);
        super(instance);
        // ensure that the first node is actually before the second (required for case 1 in evaluate)
        if (firstNodePositionIndexInRoute == 0 && secondNodePositionIndexInRoute == currentSolution.getNodes().size()-1) {
            this.firstNodePositionIndexInRoute = secondNodePositionIndexInRoute;
            this.secondNodePositionIndexInRoute = firstNodePositionIndexInRoute;
        } else if (firstNodePositionIndexInRoute == currentSolution.getNodes().size()-1 && secondNodePositionIndexInRoute == 0) {
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
    public int evaluate(Cycle solution) {
        List<Integer> nodesList = solution.getNodes();
        int nextNodeID, prevNodeID;
        int indexesDifference = Math.abs(firstNodePositionIndexInRoute - secondNodePositionIndexInRoute);

        int costs = 0;
        int gains = 0;

        //case 1 swap two consecutive nodes
        // O--O--A--B--O
        if (indexesDifference == 1 ||  indexesDifference == nodesList.size()-1) {
            nextNodeID = nodesList.get((secondNodePositionIndexInRoute + 1)% (solution.getNodes().size()));
            if (firstNodePositionIndexInRoute == 0) {
                prevNodeID = nodesList.get(solution.getNodes().size()-1);
            } else {
                prevNodeID = nodesList.get(firstNodePositionIndexInRoute-1);
            }
            costs += tsp.getDistanceBetween(prevNodeID, nodesList.get(secondNodePositionIndexInRoute));
            costs += tsp.getDistanceBetween(nextNodeID, nodesList.get(firstNodePositionIndexInRoute));
            gains += tsp.getDistanceBetween(prevNodeID, nodesList.get(firstNodePositionIndexInRoute));
            gains += tsp.getDistanceBetween(nextNodeID, nodesList.get(secondNodePositionIndexInRoute));
            delta = costs - gains;
        } 
        //case 2 swap two non consecutive nodes
        // A--O--O--B--O
        else {
            int firstNodeID = nodesList.get(firstNodePositionIndexInRoute);
            int secondNodeID = nodesList.get(secondNodePositionIndexInRoute);
            delta = this.getNodeInsertionDelta(solution, firstNodeID, secondNodePositionIndexInRoute);
            delta += this.getNodeInsertionDelta(solution, secondNodeID, firstNodePositionIndexInRoute);
        }
        return delta;
    }

    @Override
    public Cycle buildNeighbour(Cycle previousSolution) {
        List<Integer> nodesListCopy = new ArrayList<>(previousSolution.getNodes());
        Collections.swap(nodesListCopy, this.firstNodePositionIndexInRoute, this.secondNodePositionIndexInRoute);
        return new Cycle(nodesListCopy);
    }

    // TODO: Implement
    @Override
    public boolean edgesContainSameReturns(Cycle solution) {
        return true;
    }

    @Override
    public boolean isValid(int first, int second, int cycleLength) {
        return first != second;
    }

    @Override
    public NeighbourStrategy construct(TSPInstance tsp, Cycle currentSolution, int i, int j, boolean oppositeEdges) {
        return new TwoNodesExchangeNeighbour(tsp, currentSolution, i, j);
    }

    @Override
    public String toString() {
        return "2-Nodes";
    }

    // TODO: Implement
    @Override
    public boolean allEdgesExist(Cycle bestSolution) {
        return false;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;

        TwoNodesExchangeNeighbour that = (TwoNodesExchangeNeighbour) o;
        return firstNodePositionIndexInRoute == that.firstNodePositionIndexInRoute && secondNodePositionIndexInRoute == that.secondNodePositionIndexInRoute;
    }

    @Override
    public int hashCode() {
        int result = firstNodePositionIndexInRoute;
        result = 31 * result + secondNodePositionIndexInRoute;
        return result;
    }
}
