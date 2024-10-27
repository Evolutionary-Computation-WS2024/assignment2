/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package neighbors;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

/**
 *
 * @author Jerzu
 */
class TwoEdgesExchangeNeighbor extends NeighborStrategy {
    private int firstEdgeStartingNodePositionIndexInRoute;
    private int secondEdgeStartingNodePositionIndexInRoute;

    public TwoEdgesExchangeNeighbor(TSPInstance instance, Cycle currentSolution, int firstEdgeStartingNodePositionIndexInRoute, int secondEdgeStartingNodePositionIndexInRoute) {
        super(instance, currentSolution);
        this.firstEdgeStartingNodePositionIndexInRoute = firstEdgeStartingNodePositionIndexInRoute;
        this.secondEdgeStartingNodePositionIndexInRoute = secondEdgeStartingNodePositionIndexInRoute;
    }

    @Override
    public int evaluate() {
        // Implementation of the evaluate logic specific to TwoEdgesExchangeNeighbor
        return 0; // Placeholder for actual logic
    }

    @Override
    public Cycle buildNeighbor() {
        // Implementation of the neighbor building logic specific to TwoEdgesExchangeNeighbor
        return null; // Placeholder for actual logic
    }
}