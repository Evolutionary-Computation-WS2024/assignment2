package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.strategy.NNToAnyUtils.Path;


public final class NNToAnyNodeStrategy extends Strategy {
    private final int utility_weight;
    private final int regret_weight;

    public NNToAnyNodeStrategy(int utility_weight, int regret_weight) {
        this.utility_weight = utility_weight;
        this.regret_weight = regret_weight;
    }

    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {

        Path path = new Path(startNode, tspInstance, this.regret_weight, this.utility_weight);
        
        for (int i = 1; i < tspInstance.getRequiredCycleLength(); i++) {
            path.extend();
        }
        return new Cycle(path.toList());
    }

    @Override
    public String toString() {
        String text = this.regret_weight+"*regret+("+this.utility_weight+")*best_objective_function";
        return text;
    }
}
