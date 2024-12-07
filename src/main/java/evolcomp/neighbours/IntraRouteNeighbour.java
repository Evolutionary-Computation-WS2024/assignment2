package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public sealed interface IntraRouteNeighbour
        permits TwoEdgesExchangeNeighbour, TwoNodesExchangeNeighbour {

    boolean allEdgesExist(Cycle bestSolution);
    boolean isValid(int a, int b);

    NeighbourStrategy construct(TSPInstance tsp, Cycle bestSolution, int i, int j);

    /**
     * Actually returns this neighbour as a cycle object and sets it as ThisNeighbor
     */
    Cycle buildNeighbour();

    /**
     * Checks whether the return of edges are opposite
     */
    boolean areEdgesOppositeTo(Cycle solution);
}
