/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.ls;

import evolcomp.strategy.Strategy;
import evolcomp.strategy.RandomStrategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

import neighbours.InterRouteNeighbour;
import neighbours.NeighbourStrategy;
import neighbours.TwoEdgesExchangeNeighbour;
import neighbours.TwoNodesExchangeNeighbour;
import java.util.Random;

/**
 *
 * @author User
 */
public class ILS extends Strategy {
    private final Random randomGenerator = new Random(42);
    private int noLsRuns;
    
    @Override
    public Cycle apply(TSPInstance tspInstance, final int startNode) {
        this.noLsRuns = 0;
        long start = System.currentTimeMillis();
        
        RandomStrategy randomSolutionGenerator = new RandomStrategy(42);
        LocalSearch LS = new LocalSearch(randomSolutionGenerator.apply(tspInstance,startNode));
        Cycle x = LS.apply(tspInstance, -1); //the int argument should be nothing, but couldn't assign null
        int scoreX = tspInstance.evaluate(x);
        
        while(System.currentTimeMillis() - start < 32699) {
            Cycle y = perturb(x, tspInstance);
            LS.setIntitialSolution(y);
            y = LS.apply(tspInstance, -1); //the int argument should be nothing, but couldn't assign null
            this.noLsRuns += 1;
            int scoreY = tspInstance.evaluate(y);
            if (scoreY<scoreX) {
                x = y;
                scoreX = scoreY;
            }
        }
        return x;
    }
    @Override
    public int getNoLsRuns() {
        return this.noLsRuns;
    }
    @Override
    public String toString() {
        return "ILS";
    }
    private Cycle perturb(Cycle x, TSPInstance tspInstance) {
        
        // apply a few two edges exchanges
        for(int i=0; i<7; i++) {
            boolean applied = false;
            while(!applied) { 
                int A1 = randomGenerator.nextInt(100);
                int A2 = randomGenerator.nextInt(100);
                if (Math.abs(A1 - A2)>=2) {
                    TwoEdgesExchangeNeighbour neighbour = new TwoEdgesExchangeNeighbour(tspInstance, x, A1, A2);
                    applied = true;
                    x = neighbour.buildNeighbour();
                }
            }
        }
        // apply a few two nodes exchanges
        for(int i=0; i<7; i++) {
            boolean applied = false;
            while(!applied) { 
                int node1 = randomGenerator.nextInt(100);
                int node2 = randomGenerator.nextInt(100);
                if (node1 != node2) {
                    TwoNodesExchangeNeighbour neighbour = new TwoNodesExchangeNeighbour(tspInstance, x, node1, node2);
                    applied = true;
                    x = neighbour.buildNeighbour();
                }
            }
        }
        return x;
    }
}
