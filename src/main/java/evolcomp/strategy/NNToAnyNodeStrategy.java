package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.strategy.NNToAnyUtils.Patch;    


public final class NNToAnyNodeStrategy extends Strategy {
    public NNToAnyNodeStrategy(int utility_weight, int regret_weight) {
        super(utility_weight,regret_weight);
    }
    
    // TODO: Implement
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {

        Patch patch = new Patch(startNode, tspInstance, this.regret_weight, this.utility_weight);
        
        for (int i = 1; i < tspInstance.getRequiredCycleLength(); i++) {
            patch.extend();
        }
        return new Cycle(patch.toList());
    }

    @Override
    public String toString() {
        String text = this.regret_weight+"*regret+("+this.utility_weight+")*best_objective_function";
        return text;
    }
}
