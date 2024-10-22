package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.strategy.NNToAnyUtils.Patch;    


public final class GreedyCycleWithRegret extends Strategy {
    public GreedyCycleWithRegret(int utility_weight, int regret_weight) {
        super(utility_weight,regret_weight);
    }
    
    //this are comments concern assignment 3
    // TODO: Implement
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {

        Patch patch = new Patch(startNode, tspInstance, this.regret_weight, this.utility_weight);
        
        for (int i = 1; i < tspInstance.getRequiredCycleLength(); i++) {
            patch.extend();
        }
        // solution = new Cycle(patch.toList());
        //start measuring time
        //apply local search to solution
        // end measuring time
        //return result of local search
        return new Cycle(patch.toList());
    }

    @Override
    public String toString() {
        String text = this.regret_weight+"Xregret_plus("+this.utility_weight+")Xbest_objective_function";
        return text;
    }
}
