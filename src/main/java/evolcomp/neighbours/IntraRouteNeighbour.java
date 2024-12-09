package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public sealed interface IntraRouteNeighbour
        permits TwoEdgesExchangeNeighbour, TwoNodesExchangeNeighbour {

    boolean allEdgesExist(Cycle bestSolution);
    boolean isValid(int a, int b, int cycleLength);

    NeighbourStrategy construct(TSPInstance tsp, Cycle currentSolution, int i, int j, boolean oppositeEdges);

    /**
     * Actually returns this neighbour as a cycle object and sets it as ThisNeighbor
     * @param previousSolution Previous solution
     */
    Cycle buildNeighbour(Cycle previousSolution);

    /**
     * Checks whether the return of edges are opposite
     */
    boolean edgesContainSameReturns(Cycle solution);
}
