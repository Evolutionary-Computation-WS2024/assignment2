package evolcomp.strategy.ls;

import evolcomp.strategy.Strategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import evolcomp.neighbours.InterRouteNeighbour;
import evolcomp.neighbours.NeighbourStrategy;

import java.util.*;

public class CandidateLocalSearch extends Strategy {
    private Strategy initialStrategy;
    private NeighbourStrategy intraRouteStrategy;
    private LSType lsType;

    private final Random rand = new Random(42);

    private TSPInstance tsp;
    private Cycle bestSolution;


    public CandidateLocalSearch(Strategy initialStrategy, NeighbourStrategy intraRouteStrategy, LSType lsType) {
        this.initialStrategy = initialStrategy;
        this.intraRouteStrategy = intraRouteStrategy;
        this.lsType = lsType;
    }

    @Override
    public Cycle apply(TSPInstance tspInstance, int startNode) {
        this.tsp = tspInstance;
        bestSolution = initialStrategy.apply(tspInstance, startNode);
        boolean hasImproved = true;

        while (hasImproved) {
            List<NeighbourStrategy> neighbors = getNeighbourhood();

            hasImproved = switch (lsType) {
                case STEEPEST -> useSteepest(neighbors);
                case GREEDY -> useGreedy(neighbors);
            };
        }

        return bestSolution;
    }

    public List<NeighbourStrategy> getNeighbourhood() {
        List<NeighbourStrategy> neighbours = new ArrayList<>();

        Set<Integer> solutionNodes = new HashSet<>(bestSolution.getNodes());
        Set<Integer> otherNodes = new HashSet<>();
        for (int i=0; i<tsp.getHowManyNodes(); i++) {
            otherNodes.add(i);
        }
        otherNodes.removeAll(solutionNodes);

        // Add inter-route       
        for (int outsiderID : otherNodes) {
            Set<Integer> nearestNeighbors = tsp.getNearestNeighbors(outsiderID);
            Set<Integer> nearestInSolution = new HashSet<>(nearestNeighbors);
            nearestInSolution.removeAll(otherNodes);
            if (!nearestInSolution.isEmpty()) {
                Set<Integer> uniqueCandidatesIndexes = new HashSet<>(); //declared as a set to avoid the samae insertions
                for (int nearestNeighbor : nearestInSolution) {
                    int candidateIndex = bestSolution.getIndexOfElement(nearestNeighbor);
                    uniqueCandidatesIndexes.add(bestSolution.getPreviousNodeIndex(candidateIndex));
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
            
            Set<Integer> nearestNeighbors = tsp.getNearestNeighbors(routeNodeID);
            Set<Integer> nearestInSolution = new HashSet<>(nearestNeighbors);
            nearestInSolution.removeAll(otherNodes);
            
            if (!nearestInSolution.isEmpty()) {
                for (int candidateNodeID : nearestInSolution) {
                    int candidateNodeIndex = bestSolution.getIndexOfElement(candidateNodeID);
                    int currentNodeIndex = bestSolution.getIndexOfElement(routeNodeID);
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
        return neighbours;
    }

    public boolean useGreedy(List<NeighbourStrategy> neighbours) {
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

    public boolean useSteepest(List<NeighbourStrategy> neighbours) {
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

    public void setInitialStrategy(Strategy initialStrategy) {
        this.initialStrategy = initialStrategy;
    }

    public void setIntraRouteStrategy(NeighbourStrategy intraRouteStrategy) {
        this.intraRouteStrategy = intraRouteStrategy;
    }

    public void setLsType(LSType lsType) {
        this.lsType = lsType;
    }
}
