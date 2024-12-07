/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy;

import evolcomp.strategy.ls.LocalSearch;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

/**
 *
 * @author User
 */
public class LNSwithLS extends LNS {
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        this.noMainLoopRuns = 0;
        long start = System.currentTimeMillis();
        
        RandomStrategy randomSolutionGenerator = new RandomStrategy(42);
        LocalSearch LS = new LocalSearch(randomSolutionGenerator.apply(tspInstance,startNode));
        Cycle x = LS.apply(tspInstance, -1); //the int argument should be nothing, but couldn't assign null
        int scoreX = tspInstance.evaluate(x);
        
        while(System.currentTimeMillis() - start < 32699) {
            Cycle y = destroy(x);
            y = repair(y, tspInstance);
            LS.setIntitialSolution(y);
            y = LS.apply(tspInstance, -1); //the int argument should be nothing, but couldn't assign null
            this.noMainLoopRuns += 1;
            int scoreY = tspInstance.evaluate(y);
            if (scoreY<scoreX) {
                x = y;
                scoreX = scoreY;
            }
        }
        return x;
    }
    @Override
    public String toString() {
        String text = "LNS with LS";
        return text;
    }
}
