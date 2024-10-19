package evolcomp.strategy;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

import java.util.*;

public final class GreedyCycleStrategy extends Strategy {
    public GreedyCycleStrategy() {}

    private record CheapestNode(int node, int increase) {}

    private static class Edge implements Comparable<Edge> {
        final int start;
        final int end;
        final int cheapestNewNode;
        final int cheapestIncrease;

        Edge(int start, int end, CheapestNode cheapestNode) {
            this.start = start;
            this.end = end;
            this.cheapestNewNode = cheapestNode.node();
            this.cheapestIncrease = cheapestNode.increase();
        }

        @Override
        public int compareTo(Edge o) {
            return Integer.compare(this.cheapestIncrease, o.cheapestIncrease);
        }
    }

    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        Set<Integer> remaining = new HashSet<>();
        for (int i=0; i<tspInstance.getHowManyNodes(); i++) {
            remaining.add(i);
        }
        remaining.remove(startNode);

        List<Integer> cycle = getFirstCycle(tspInstance, startNode, remaining);
        int secondNode = cycle.get(1);
        remaining.remove(secondNode);

        Queue<Edge> queue = new PriorityQueue<>();

        Edge edge = generateNewEdge(tspInstance, remaining, startNode, secondNode);
        int recentNode = edge.cheapestNewNode;
        remaining.remove(recentNode);
        cycle.add(recentNode);

        queue.add(generateNewEdge(tspInstance, remaining, edge.start, edge.end));
        queue.add(generateNewEdge(tspInstance, remaining, edge.start, recentNode));
        queue.add(generateNewEdge(tspInstance, remaining, edge.end, recentNode));

        for (int i=3; i<tspInstance.getRequiredCycleLength(); i++) {
            while (true) {
                edge = queue.poll();
                if (edge == null) {
                    throw new RuntimeException("No edges");
                }
                if (remaining.contains(edge.cheapestNewNode)) {
                    break;
                } else {
                    queue.add(generateNewEdge(tspInstance, remaining, edge.start, edge.end));
                }
            }

            recentNode = edge.cheapestNewNode;
            remaining.remove(recentNode);

            queue.add(generateNewEdge(tspInstance, remaining, edge.start, recentNode));
            queue.add(generateNewEdge(tspInstance, remaining, edge.end, recentNode));

            // Check if to insert between first and last element
            int lastElementInCycle = cycle.get(cycle.size() - 1);
            int firstElementInCycle = cycle.get(0);
            boolean isEdgeOnBoundsOfCycle = ((firstElementInCycle == edge.start) && (lastElementInCycle == edge.end));
            isEdgeOnBoundsOfCycle |= ((firstElementInCycle == edge.end) && (lastElementInCycle == edge.start));

            // update cycle
            if (isEdgeOnBoundsOfCycle) {
                cycle.add(0, recentNode);
            } else {
                ListIterator<Integer> iter = cycle.listIterator();
                int val = iter.next();
                while (!(val == edge.start || val == edge.end)) {
                    val = iter.next();
                }
                iter.add(recentNode);

            }
        }
        return new Cycle(cycle);
    }

    private List<Integer> getFirstCycle(final TSPInstance tspInstance, final int startNode, Set<Integer> remaining) {
        int lowestIncrease = Integer.MAX_VALUE;
        int bestNode = -1;
        for (Integer candidate : remaining) {
            int increase = tspInstance.getCostAt(candidate) + 2*tspInstance.getDistanceBetween(candidate, startNode);
            if (increase < lowestIncrease) {
                lowestIncrease = increase;
                bestNode = candidate;
            }
        }
        List<Integer> res = new ArrayList<>();
        res.add(startNode);
        res.add(bestNode);
        return res;
    }

    private Edge generateNewEdge(final TSPInstance tspInstance,
                                 final Set<Integer> remaining,
                                 final int startNode,
                                 final int endNode) {
        CheapestNode cheapestNode = findCheapestNode(tspInstance, remaining, startNode, endNode);
        return new Edge(startNode, endNode, cheapestNode);
    }

    private CheapestNode findCheapestNode(final TSPInstance tspInstance,
                                          final Set<Integer> remaining,
                                          final int startNode,
                                          final int endNode) {

        int lowestIncrease = Integer.MAX_VALUE;
        int bestNode = -1;
        for (Integer candidate : remaining) {
            int increase = tspInstance.getCostAt(candidate);
            increase += tspInstance.getDistanceBetween(candidate, startNode);
            increase += tspInstance.getDistanceBetween(candidate, endNode);

            if (increase < lowestIncrease) {
                lowestIncrease = increase;
                bestNode = candidate;
            }
        }

        lowestIncrease -= tspInstance.getDistanceBetween(startNode, endNode);
        return new CheapestNode(bestNode, lowestIncrease);
    }

    @Override
    public String toString() {
        return "Greedy Cycle";
    }
}
