package evolcomp.misc;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import evolcomp.strategy.Strategy;
import evolcomp.strategy.ls.LSType;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import neighbours.NeighbourStrategy;

public final class Evaluator {
    private final TSPInstance instance;
    private final Strategy strategy;

    private Cycle bestCycle;

    private int minValue = Integer.MAX_VALUE;
    private int maxValue = Integer.MIN_VALUE;
    private int averageValue;

    private long minTimeMs = Long.MAX_VALUE;
    private long maxTimeMs = Long.MIN_VALUE;
    private long averageTimeMs;
    
    private List<Cycle> solutions = new ArrayList<>();

    public Evaluator(TSPInstance instance, Strategy strategy) throws IOException {
        this.instance = instance;
        this.strategy = strategy;
        execute();
        this.computeAwerageSimilarity();
        this.computeSimilarityToBest(bestCycle);
        String path = this.instance.toString() + ".csv";
        exportToCsv(this.solutions, path);
    }

    private void execute() {
        int totalValue = 0;
        for (int i=0; i<1000; i++) {
            Random randomNumberGenerator = new Random();
            
            long start = System.nanoTime();
            Cycle currentCycle = strategy.apply(instance, randomNumberGenerator.nextInt(200));
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            
            this.solutions.add(currentCycle);
            
            averageTimeMs += elapsedMs;

            if (elapsedMs > maxTimeMs) {
                maxTimeMs = elapsedMs;
            }

            if (elapsedMs < minTimeMs) {
                minTimeMs = elapsedMs;
            }

            int currentValue = instance.evaluate(currentCycle);
            currentCycle.setScore(currentValue);
            if (currentValue > maxValue) {
                maxValue = currentValue;
            }
            else if (currentValue < minValue) {
                minValue = currentValue;
                bestCycle = currentCycle;
            }
            totalValue += currentValue;
        }
        averageValue = totalValue / instance.getHowManyNodes();
        averageTimeMs = averageTimeMs / instance.getHowManyNodes();
              
    }  

    public int getAverageValue() {
        return averageValue;
    }

    public int getMaxValue() {
        return maxValue;
    }

    public int getMinValue() {
        return minValue;
    }

    public Cycle getBestCycle() {
        return bestCycle;
    }

    public long getMinTimeMs() {
        return minTimeMs;
    }

    public long getMaxTimeMs() {
        return maxTimeMs;
    }

    public long getAverageTimeMs() {
        return averageTimeMs;
    }
    
    private void computeSimilarityToBest(Cycle bestSolution) {
        for(Cycle solution : solutions) {
            solution.setSimmilarityBestEdge(solution.computeSimmilarityEdges(bestSolution));
            solution.setSimmilarityBestNode(solution.computeSimmilarityNodes(bestSolution));
        }        
    }
    
    private void computeAwerageSimilarity() {
        for(int i = 0; i<this.solutions.size(); i++) {
            Cycle solution1 = solutions.get(i);
            float similaritesSumEdge = 0;
            float similaritesSumNode = 0;
            for(int j = 0; j<this.solutions.size(); j++) {
                if(i!=j) {
                    Cycle solution2 = solutions.get(j);
                    similaritesSumEdge += solution1.computeSimmilarityEdges(solution2);
                    similaritesSumNode += solution1.computeSimmilarityNodes(solution2);
                }
            }
            float avgEdge = (float) similaritesSumEdge/(solutions.size()-1);
            float avgNode = (float) similaritesSumNode/(solutions.size()-1);
            solution1.setSimmilarityAvgEdge(avgEdge);
            solution1.setSimmilarityAvgNode(avgNode);
        } 
    }
    public static void exportToCsv(List<Cycle> cycles, String filePath) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("score;simmilarityAvgEdge;simmilarityAvgNode;simmilarityBestEdge;simmilarityBestNode");
            writer.newLine();
            for (Cycle cycle : cycles) {
                writer.write(cycle.toCsvRow());
                writer.newLine();
            }
        }
    }
}
