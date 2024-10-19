package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public final class NNToEndStrategy extends Strategy {
    public NNToEndStrategy() {}

    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        List<Integer> nodes = new ArrayList<>();
        nodes.add(startNode);

        Set<Integer> remaining = new HashSet<>();
        for (int i = 0; i < tspInstance.getHowManyNodes(); i++) {
            remaining.add(i);
        }
        remaining.remove(startNode);

        int lastNode = startNode;
        for (int i = 1; i < tspInstance.getRequiredCycleLength(); i++) {
            int shortestDistance = Integer.MAX_VALUE;
            int closestNode = -1;

            for (int candidateNode : remaining) {
                int distance = tspInstance.getDistanceBetween(lastNode, candidateNode);
                distance += tspInstance.getCostAt(candidateNode);
                if (distance < shortestDistance) {
                    closestNode = candidateNode;
                    shortestDistance = distance;
                }
            }

            nodes.add(closestNode);
            remaining.remove(closestNode);
            lastNode = closestNode;
        }

        return new Cycle(nodes);
    }

    @Override
    public String toString() {
        return "Nearest Neighbor to end";
    }
}
