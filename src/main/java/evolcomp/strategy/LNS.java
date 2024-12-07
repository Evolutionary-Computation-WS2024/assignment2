/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy;

import evolcomp.strategy.ls.LocalSearch;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.Random;

/**
 *
 * @author User
 */
public class LNS extends Strategy{
    protected final Random randomGenerator = new Random(42);
    protected int noMainLoopRuns;
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        this.noMainLoopRuns = 0;
        long start = System.currentTimeMillis();
        
        RandomStrategy randomSolutionGenerator = new RandomStrategy(42);
        Cycle x = randomSolutionGenerator.apply(tspInstance,startNode);
        int scoreX = tspInstance.evaluate(x);
        
        while(System.currentTimeMillis() - start < 100) {
            Cycle y = destroy(x);
            y = repair(y, tspInstance);
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
    public int getNoMainLoopRuns () {
        return this.noMainLoopRuns;
    }
    protected Cycle repair(Cycle y, TSPInstance tspInstance) {
        return y;
    }
    protected Cycle destroy(Cycle x) {
        return x;
    }
    @Override
    public String toString() {
        String text = "LNS without LS";
        return text;
    }
}
