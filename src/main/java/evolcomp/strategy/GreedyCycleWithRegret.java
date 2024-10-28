package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.strategy.NNToAnyUtils.Path;


public final class GreedyCycleWithRegret extends Strategy {
    private final int utility_weight;
    private final int regret_weight;

    public GreedyCycleWithRegret(int utility_weight, int regret_weight) {
        this.utility_weight = utility_weight;
        this.regret_weight = regret_weight;
    }
    
    //this are comments concern assignment 3
    // TODO: Implement
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {

        Path path = new Path(startNode, tspInstance, this.regret_weight, this.utility_weight);
        
        for (int i = 1; i < tspInstance.getRequiredCycleLength(); i++) {
            path.extend();
        }
        // solution = new Cycle(patch.toList());
        //start measuring time
        //apply local search to solution
        // end measuring time
        //return result of local search
        return new Cycle(path.toList());
    }

    @Override
    public String toString() {
        return this.regret_weight+"Xregret_plus("+ this.utility_weight+")Xbest_objective_function";
    }
}
