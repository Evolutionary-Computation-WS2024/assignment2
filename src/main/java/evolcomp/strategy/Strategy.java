package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public abstract class Strategy {
    public final int utility_weight;
    public final int regret_weight;
    public Strategy(int utility_weight, int regret_weight) {
        this.regret_weight = regret_weight;
        this.utility_weight = utility_weight;
    }
    public abstract Cycle apply(final TSPInstance tspInstance, final int startNode);
}
