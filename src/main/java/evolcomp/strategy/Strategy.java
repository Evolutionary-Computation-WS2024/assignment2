package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public abstract class Strategy{
    public abstract Cycle apply(final TSPInstance tspInstance, final int random_seed);
    public int getNoLsRuns() {
        return 0;
    }
}
