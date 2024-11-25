package evolcomp.neighbours;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public sealed interface IntraRouteNeighbour
        permits TwoEdgesExchangeNeighbour, TwoNodesExchangeNeighbour {

    boolean allEdgesExist(Cycle bestSolution);
    boolean isValid(int a, int b);

    NeighbourStrategy construct(TSPInstance tsp, Cycle bestSolution, int i, int j);
}
