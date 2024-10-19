package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public final class RandomStrategy extends Strategy {
    private final Random rand;

    public RandomStrategy() {
        this.rand = new Random();
    }

    public RandomStrategy(int seed) {
        this.rand = new Random(seed);
    }

    // TODO: Implement
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        
        int no_nodes = tspInstance.getHowManyNodes();
        int half_no_nodes = (int) Math.ceil(no_nodes / 2.0);
        
        List<Integer> all_nodes = new ArrayList<>();
        for (int i = 0; i < no_nodes; i++) {
            if (i != startNode) {
                all_nodes.add(i);
            }
        } 
        Collections.shuffle(all_nodes, rand);
        all_nodes = all_nodes.subList(0, half_no_nodes-1);
        all_nodes.add(startNode);
        
        return new Cycle(all_nodes);
    }

    @Override
    public String toString() {
        return "Random";
    }
}
