package evolcomp.strategy.ls;

import evolcomp.strategy.Strategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.neighbours.InterRouteNeighbour;
import evolcomp.neighbours.NeighbourStrategy;

import java.util.*;

public class LocalSearch extends Strategy {
    private Strategy initialStrategy;
    private NeighbourStrategy intraRouteStrategy;
    private LSType lsType;
    private boolean useCandidateMoves;
    private boolean useCache;

    private final Random rand = new Random(42);

    private TSPInstance tsp;
    private Cycle bestSolution;
    private List<NeighbourStrategy> neighbours;

    public LocalSearch(Strategy initialStrategy, NeighbourStrategy intraRouteStrategy, LSType lsType) {
        this.initialStrategy = initialStrategy;
        this.intraRouteStrategy = intraRouteStrategy;
        this.lsType = lsType;
        this.useCandidateMoves = false;
        this.useCache = false;
        this.neighbours = new ArrayList<>();
    }

    public LocalSearch(Strategy initialStrategy, NeighbourStrategy intraRouteStrategy, LSType lsType, boolean useCandidateMoves, boolean useCache) {
        this.initialStrategy = initialStrategy;
        this.intraRouteStrategy = intraRouteStrategy;
        this.lsType = lsType;
        this.useCandidateMoves = useCandidateMoves;
        this.useCache = useCache;
        this.neighbours = new ArrayList<>();
    }

    @Override
    public Cycle apply(TSPInstance tspInstance, int startNode) {
        this.tsp = tspInstance;
        this.neighbours = new ArrayList<>();
        bestSolution = initialStrategy.apply(tspInstance, startNode);

        boolean hasImproved = true;
        while (hasImproved) {
            getNeighbourhood();
            hasImproved = switch (lsType) {
                case STEEPEST -> useSteepest();
                case GREEDY -> useGreedy();
            };
        }

        return bestSolution;
    }

    private void getNeighbourhood() {
        neighbours = new ArrayList<>();

        Set<Integer> solutionNodes = new HashSet<>(bestSolution.getNodes());
        Set<Integer> otherNodes = new HashSet<>();
        for (int i=0; i<tsp.getHowManyNodes(); i++) {
            otherNodes.add(i);
        }
        otherNodes.removeAll(solutionNodes);

        if (useCandidateMoves) {
            getNeighbourhoodUsingCandidateMoves(solutionNodes, otherNodes);
        } else {
            getNeighbourhoodWithoutCandidateMoves(otherNodes);
        }
    }

    private void getNeighbourhoodWithoutCandidateMoves(Set<Integer> otherNodes) {
        // Add inter-route
        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
            for (int otherNode : otherNodes) {
                neighbours.add(new InterRouteNeighbour(tsp, bestSolution, i, otherNode));
            }
        }

        // Add intra-route
        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
            for (int j=0; j<tsp.getRequiredCycleLength(); j++) {
                if (i != j && intraRouteStrategy.isValid(i, j)) {
                    neighbours.add(intraRouteStrategy.construct(tsp, bestSolution, i, j));
                }
            }
        }
    }

    private void getNeighbourhoodUsingCandidateMoves(Set<Integer> solutionNodes, Set<Integer> otherNodes) {
        // Add inter-route
        for (int outsiderID : otherNodes) {
            Set<Integer> nearestInSolution = getNearestInSolution(outsiderID, otherNodes);
            if (!nearestInSolution.isEmpty()) {
                Set<Integer> uniqueCandidatesIndexes = new HashSet<>(); //declared as a set to avoid the samae insertions
                for (int nearestNeighbor : nearestInSolution) {
                    int candidateIndex = bestSolution.getIndexOfElement(nearestNeighbor);
                    uniqueCandidatesIndexes.add(bestSolution.getPreviousNodeIndex(candidateIndex));
                }
                for (int candidateIndex : uniqueCandidatesIndexes) {
                    neighbours.add(new InterRouteNeighbour(
                            tsp,
                            bestSolution,
                            candidateIndex,
                            outsiderID));
                }
            }
        }

        // Add intra-route
        Set<Pair> uniqueEdgesExchanges = new HashSet<>();
        for (int routeNodeID : solutionNodes) {
            Set<Integer> nearestInSolution = getNearestInSolution(routeNodeID, otherNodes);

            if (!nearestInSolution.isEmpty()) {
                int currentNodeIndex = bestSolution.getIndexOfElement(routeNodeID);
                for (int candidateNodeID : nearestInSolution) {
                    int candidateNodeIndex = bestSolution.getIndexOfElement(candidateNodeID);
                    uniqueEdgesExchanges.add(new Pair(
                            candidateNodeIndex,
                            currentNodeIndex));
                    uniqueEdgesExchanges.add(new Pair(
                            bestSolution.getPreviousNodeIndex(candidateNodeIndex),
                            bestSolution.getPreviousNodeIndex(currentNodeIndex)));
                }
            }
        }
        for (Pair pair : uniqueEdgesExchanges) {
            if (intraRouteStrategy.isValid(pair.first , pair.second)) {
                neighbours.add(
                        intraRouteStrategy.construct(
                                tsp,
                                bestSolution,
                                pair.first,
                                pair.second));
            }
        }
    }

    private Set<Integer> getNearestInSolution(int nodeID, Set<Integer> otherNodes) {
        Set<Integer> nearestNeighbors = tsp.getNearestNeighbors(nodeID);
        Set<Integer> nearestInSolution = new HashSet<>(nearestNeighbors);
        nearestInSolution.removeAll(otherNodes);
        return nearestInSolution;
    }

    private boolean useGreedy() {
        Collections.shuffle(neighbours, rand);
        for (NeighbourStrategy neighbour : neighbours) {
            int delta = neighbour.evaluate();
            if (delta < 0) {
                bestSolution = neighbour.buildNeighbour();
                return true;
            }
        }
        return false;
    }

    private boolean useSteepest() {
        int lowestDelta = 0;
        NeighbourStrategy bestNeighbour = null;

        for (NeighbourStrategy neighbour : neighbours) {
            int delta = neighbour.evaluate();
            if (delta < lowestDelta) {
                lowestDelta = delta;
                bestNeighbour = neighbour;
            }
        }

        if (bestNeighbour == null) {
            return false;
        }
        bestSolution = bestNeighbour.buildNeighbour();
        return true;
    }

    @Override
    public String toString() {
        return "Local Search (" +
                initialStrategy.toString() +
                ", " +
                lsType +
                ", " +
                intraRouteStrategy.toString() +
                ")";
    }

    public static final class Builder {
        private Strategy initialStrategy;
        private NeighbourStrategy intraRouteStrategy;
        private LSType lsType;
        private boolean useCandidateMoves = false;
        private boolean useCache = false;

        public Builder() {}

        public Builder initialStrategy(Strategy initialStrategy) {
            this.initialStrategy = initialStrategy;
            return this;
        }

        public Builder intraRouteStrategy(NeighbourStrategy intraRouteStrategy) {
            this.intraRouteStrategy = intraRouteStrategy;
            return this;
        }

        public Builder lsType(LSType lsType) {
            this.lsType = lsType;
            return this;
        }

        public Builder useCandidateMoves(boolean useCandidateMoves) {
            this.useCandidateMoves = useCandidateMoves;
            return this;
        }

        public Builder useCache(boolean useCache) {
            this.useCache = useCache;
            return this;
        }

        public LocalSearch build() {
            return new LocalSearch(initialStrategy, intraRouteStrategy, lsType, useCandidateMoves, useCache);
        }
    }
}
