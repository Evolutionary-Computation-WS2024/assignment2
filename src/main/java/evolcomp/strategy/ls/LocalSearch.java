package evolcomp.strategy.ls;

import evolcomp.neighbours.IntraRouteNeighbour;
import evolcomp.neighbours.TwoEdgesExchangeNeighbour;
import evolcomp.strategy.Strategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.neighbours.InterRouteNeighbour;
import evolcomp.neighbours.NeighbourStrategy;

import java.util.*;

public class LocalSearch extends Strategy {
    private final Strategy initialStrategy;
    private final IntraRouteNeighbour intraRouteStrategy;
    private final LSType lsType;
    private final boolean useCandidateMoves;
    private final boolean usePreviousDeltas;

    private final Random rand = new Random(42);

    private TSPInstance tsp;
    private Cycle bestSolution;
    private Set<NeighbourStrategy> lm;
    private List<NeighbourStrategy> neighbours;
    private NeighbourStrategy lastAppliedNeighbour = null;

    private Set<Integer> currentSolutionNodes = new HashSet<>();
    private Set<Integer> otherNodes = new HashSet<>();

    public LocalSearch(Strategy initialStrategy, IntraRouteNeighbour intraRouteStrategy, LSType lsType) {
        this.initialStrategy = initialStrategy;
        this.intraRouteStrategy = intraRouteStrategy;
        this.lsType = lsType;
        this.useCandidateMoves = false;
        this.usePreviousDeltas = false;
        this.neighbours = new ArrayList<>();
    }

    public LocalSearch(Strategy initialStrategy, IntraRouteNeighbour intraRouteStrategy, LSType lsType, boolean useCandidateMoves, boolean usePreviousDeltas) {
        this.initialStrategy = initialStrategy;
        this.intraRouteStrategy = intraRouteStrategy;
        this.lsType = lsType;
        this.useCandidateMoves = useCandidateMoves;
        this.usePreviousDeltas = usePreviousDeltas;
        this.neighbours = new ArrayList<>();
    }

    @Override
    public Cycle apply(TSPInstance tspInstance, int startNode) {
        this.tsp = tspInstance;
        this.neighbours = new ArrayList<>();
        bestSolution = initialStrategy.apply(tspInstance, startNode);
        lastAppliedNeighbour = null;

        // TODO: Needs refactoring
        if (lsType.equals(LSType.STEEPEST) && usePreviousDeltas && !useCandidateMoves) {
            return useSteepestWithPreviousDeltas();
        }
        // ENDTODO

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

    private Cycle useSteepestWithPreviousDeltas() {
        lm = new TreeSet<>(Comparator.comparingInt(NeighbourStrategy::getDelta));
        boolean hasImproved = true;

        while (hasImproved) {
            hasImproved = false;
            addNewNeighbourhood();

            Iterator<NeighbourStrategy> it = lm.iterator();
            while (it.hasNext()) {
                lastAppliedNeighbour = it.next();
                if (lastAppliedNeighbour instanceof InterRouteNeighbour inter) {
                    it.remove();
                    if (inter.isConsistentWith(bestSolution)) {
                        bestSolution = inter.buildNeighbour(bestSolution);
                        hasImproved = true;
                        break;
                    }
                } else if (lastAppliedNeighbour instanceof IntraRouteNeighbour intra) {
                    // one of the edges was deleted
                    if (!intra.allEdgesExist(bestSolution)) {
                        it.remove();

                    // removed edges have opposite returns
                    } else if (intra.edgesContainSameReturns(bestSolution)) {
                        bestSolution = intra.buildNeighbour(bestSolution);
                        it.remove();
                        hasImproved = true;
                        break;
                    }
                    // CONTINUE - removed edges have the same return
                }
            }
        }
        return bestSolution;
    }

    private void addNewNeighbourhood() {
        populateSolutionAndOtherNodesSets();

        if (lastAppliedNeighbour instanceof InterRouteNeighbour inter) {
            int tspPos = inter.getNewNodeIndex(bestSolution);
            addInterRouteNeighboursOnPos(tspPos);
            addIntraRouteNeighboursFrom(tspPos);
        }
        else if (lastAppliedNeighbour instanceof TwoEdgesExchangeNeighbour intra) {
            int[] arr = {
                    intra.getIdxA1(bestSolution),
                    intra.getIdxA2(bestSolution),
                    intra.getIdxB1(bestSolution),
                    intra.getIdxB2(bestSolution)
            };
//
//            for (int i : arr) {
//                addInterRouteNeighboursOnPos(i);
//                addIntraRouteNeighboursFrom(i);
//                int prev = bestSolution.getPreviousNodeIndex(i);
//                addIntraRouteNeighboursFrom(prev);
//            }
            addInitialInterRouteNeighbours();
            addInitialIntraRouteNeighbours();
        }
        else {
            addInitialInterRouteNeighbours();
            addInitialIntraRouteNeighbours();
        }
    }

    private void populateSolutionAndOtherNodesSets() {
        currentSolutionNodes = new HashSet<>(bestSolution.getNodes());
        otherNodes = new HashSet<>();
        for (int i=0; i<tsp.getHowManyNodes(); i++) {
            otherNodes.add(i);
        }
        otherNodes.removeAll(currentSolutionNodes);
    }

    private void addInitialInterRouteNeighbours() {
        for (int innerNodeIdx=0; innerNodeIdx<tsp.getRequiredCycleLength(); innerNodeIdx++) {
            addInterRouteNeighboursOnPos(innerNodeIdx);
        }
    }

    private void addInterRouteNeighboursOnPos(int tspPos) {
        for (int otherNode : otherNodes) {
            NeighbourStrategy neighbour = new InterRouteNeighbour(tsp, bestSolution, tspPos, otherNode);
            int delta = neighbour.evaluate(bestSolution);
            if (delta < 0) {
                lm.add(neighbour);
            }
        }
    }

    private void addInitialIntraRouteNeighbours() {
        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
            addIntraRouteNeighboursFrom(i);
        }
    }

    private void addIntraRouteNeighboursFrom(int startIndex) {
        for (int j=0; j<tsp.getRequiredCycleLength(); j++) {
            addTwoIntraRouteNeighbours(startIndex, j);
        }
    }

    private void addTwoIntraRouteNeighbours(int i, int j) {
        if (intraRouteStrategy.isValid(i, j, tsp.getRequiredCycleLength())) {
            NeighbourStrategy neighbour1 = intraRouteStrategy.construct(tsp, bestSolution, i, j, true);
            int delta = neighbour1.evaluate(bestSolution);
            if (delta < 0) {
                lm.add(neighbour1);
            }

            NeighbourStrategy neighbour2 = intraRouteStrategy.construct(tsp, bestSolution, i, j, false);
            delta = neighbour2.evaluate(bestSolution);
            if (delta < 0) {
                lm.add(neighbour2);
            }
        }
    }

    private void getNeighbourhood() {
        neighbours = new ArrayList<>();

        populateSolutionAndOtherNodesSets();

        if (useCandidateMoves) {
            getNeighbourhoodUsingCandidateMoves(currentSolutionNodes, otherNodes);
        } else {
            getNeighbourhoodWithoutCandidateMoves(otherNodes);
        }
    }

    private void getNeighbourhoodWithoutCandidateMoves(Set<Integer> otherNodes) {
        // TODO: Refactor after changes
        // Add inter-route
//        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
//            for (int otherNode : otherNodes) {
////                neighbours.add(new InterRouteNeighbour(tsp, bestSolution, i, otherNode));
//                neighbours.add(new InterRouteNeighbour(tsp, i, otherNode));
//            }
//        }

        // Add intra-route
//        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
//            for (int j=0; j<tsp.getRequiredCycleLength(); j++) {
//                if (i != j && intraRouteStrategy.isValid(i, j)) {
////                    neighbours.add(intraRouteStrategy.construct(tsp, bestSolution, i, j));
//                    neighbours.add(intraRouteStrategy.construct(tsp, i, j));
//                }
//            }
//        }
    }

    private void getNeighbourhoodUsingCandidateMoves(Set<Integer> solutionNodes, Set<Integer> otherNodes) {
        // Add inter-route
        for (int outsiderID : otherNodes) {
            Set<Integer> nearestInSolution = getNearestInSolution(outsiderID, otherNodes);
            if (!nearestInSolution.isEmpty()) {
                Set<Integer> uniqueCandidatesIndexes = new HashSet<>(); //declared as a set to avoid the same insertions
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
        // TODO: refactor after changes
//        for (Pair pair : uniqueEdgesExchanges) {
//            if (intraRouteStrategy.isValid(pair.first , pair.second)) {
//                neighbours.add(
//                        intraRouteStrategy.construct(
//                                tsp,
//                           pair : uniqueEdgesExchanges) {
//            if (intraRouteStrategy.isValid(pair.first , pair.second)) {
//                neighbours.add(
//                        intraRouteStrategy.construct(
//                                tsp,
//                                bestSolution,
//                                pair.first,
//                                pair.second));
//            }
//        }      bestSolution,
//                                pair.first,
//                                pair.second));
//            }
//        }
    }

    private Set<Integer> getNearestInSolution(int nodeID, Set<Integer> otherNodes) {
        Set<Integer> nearestNeighbors = tsp.getNearestNeighbors(nodeID);
        Set<Integer> nearestInSolution = new HashSet<>(nearestNeighbors);
        nearestInSolution.removeAll(otherNodes);
        return nearestInSolution;
    }

    // Randomization is inspired on Fisher-Yates approach
    private boolean useGreedy() {
        // TODO: Refactor
//        List<Integer> indices = new ArrayList<>();
//        for (int i = 0; i < neighbours.size(); i++) {
//            indices.add(i);
//        }
//
//        for (int i = 0; i < indices.size(); i++) {
//            int randomIndex = i + rand.nextInt(indices.size() - i);
//            Collections.swap(indices, i, randomIndex);
//            lastAppliedNeighbour = neighbours.get(indices.get(i));
//
//            int delta = lastAppliedNeighbour.evaluate();
//            if (delta < 0) {
//                bestSolution = lastAppliedNeighbour.buildNeighbour();
//                return true;
//            }
//        }
        return false;
    }

    private boolean useSteepest() {
        // TODO: Refactor after changes
//        int lowestDelta = 0;
//        NeighbourStrategy bestNeighbour = null;
//
//        for (NeighbourStrategy neighbour : neighbours) {
//            int delta = neighbour.evaluate();
//            if (delta < lowestDelta) {
//                lowestDelta = delta;
//                bestNeighbour = neighbour;
//            }
//        }
//
//        if (bestNeighbour == null) {
//            return false;
//        }
//        bestSolution = bestNeighbour.buildNeighbour();
//        lastAppliedNeighbour = bestNeighbour;
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
        private IntraRouteNeighbour intraRouteStrategy;
        private LSType lsType;
        private boolean useCandidateMoves = false;
        private boolean usePreviousDeltas = false;

        public Builder() {}

        public Builder initialStrategy(Strategy initialStrategy) {
            this.initialStrategy = initialStrategy;
            return this;
        }

        public Builder intraRouteStrategy(IntraRouteNeighbour intraRouteStrategy) {
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

        public Builder usePreviousDeltas(boolean usePreviousDeltas) {
            this.usePreviousDeltas = usePreviousDeltas;
            return this;
        }

        public LocalSearch build() {
            return new LocalSearch(initialStrategy, intraRouteStrategy, lsType, useCandidateMoves, usePreviousDeltas);
        }
    }
}
