package evolcomp.strategy.ls;

import evolcomp.strategy.Strategy;
import static evolcomp.strategy.ls.LSType.STEEPEST;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import neighbours.InterRouteNeighbour;
import neighbours.NeighbourStrategy;

import java.util.*;
import neighbours.TwoEdgesExchangeNeighbour;

public class LocalSearch extends Strategy {
    private Strategy initialStrategy;
    private NeighbourStrategy intraRouteStrategy;
    private LSType lsType;

    private final Random rand = new Random(42);

    private TSPInstance tsp;
    private Cycle bestSolution;
    private Cycle intitialSolution;


    public LocalSearch(Cycle intitialSolution) {
        this.intraRouteStrategy = new TwoEdgesExchangeNeighbour();
        this.lsType = STEEPEST;
        this.intitialSolution = intitialSolution;
    }
    
    public void setIntitialSolution(Cycle solution) {
        this.intitialSolution = solution;
    }

    @Override
    public Cycle apply(TSPInstance tspInstance, int startNode) {
        this.tsp = tspInstance;
        bestSolution = intitialSolution;
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

        Set<Integer> solutionNodes = new HashSet<>(bestSolution.nodes());
        Set<Integer> otherNodes = new HashSet<>();
        for (int i=0; i<tsp.getHowManyNodes(); i++) {
            otherNodes.add(i);
        }
        otherNodes.removeAll(solutionNodes);

        // Add inter-route
        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
            for (int otherNode : otherNodes) {
                neighbours.add(new InterRouteNeighbour(tsp, bestSolution, i, otherNode));
            }
        }

        // Add intra-route
        for (int i=0; i<tsp.getRequiredCycleLength(); i++) {
            for (int j=0; j<tsp.getRequiredCycleLength(); j++) {
                if (i == j) {
                    continue;
                }
                if (intraRouteStrategy.isValid(i, j)) {
                    neighbours.add(intraRouteStrategy.construct(tsp, bestSolution, i, j));
                }
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