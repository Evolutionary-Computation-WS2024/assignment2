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
class TwoNodesExchangeNeighbor extends NeighborStrategy {
    private int firstNodePositionIndexInRoute;
    private int secondNodePositionIndexInRoute;

    public TwoNodesExchangeNeighbor(TSPInstance instance, Cycle currentSolution, int firstNodePositionIndexInRoute, int secondNodePositionIndexInRoute) {
        super(instance, currentSolution);
        this.firstNodePositionIndexInRoute = firstNodePositionIndexInRoute;
        this.secondNodePositionIndexInRoute = secondNodePositionIndexInRoute;
    }

    @Override
    public int evaluate() {
        // Implementation of the evaluate logic specific to TwoNodesExchangeNeighbor
        return 0; // Placeholder for actual logic
    }

    @Override
    public Cycle buildNeighbor() {
        // Implementation of the neighbor building logic specific to TwoNodesExchangeNeighbor
        return null; // Placeholder for actual logic
    }
}
