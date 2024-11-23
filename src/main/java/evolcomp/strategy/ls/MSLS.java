/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.ls;

import evolcomp.strategy.Strategy;
import evolcomp.strategy.RandomStrategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

/**
 *
 * @author User
 */
public class MSLS extends Strategy {
    
    @Override
    public Cycle apply(TSPInstance tspInstance, int random_seed) {
        RandomStrategy randomSolutionsGenerator = new RandomStrategy(random_seed);
        Cycle bestSolution = null;
        int bestScore = Integer.MAX_VALUE;
            
        for (int i=0; i<200; i++) {
            Cycle startingSolution = randomSolutionsGenerator.apply(tspInstance, i);
            Strategy localSearch = new LocalSearch(startingSolution);
            Cycle currentSolution = localSearch.apply(tspInstance, -1); //the int argument should be nothing, but couldn't assign null
            int currentScore = tspInstance.evaluate(currentSolution);
            if (currentScore<bestScore) {
                bestSolution = currentSolution;
                bestScore = currentScore;
            }
        }
        return bestSolution;
    }
    @Override
    public String toString() {
        return "MSLS";
    }
}
