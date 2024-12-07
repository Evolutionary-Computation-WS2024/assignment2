package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public abstract class Strategy{
    public abstract Cycle apply(final TSPInstance tspInstance, final int startNode);
    public int getNoMainLoopRuns() {
        return 0;
    }
}
