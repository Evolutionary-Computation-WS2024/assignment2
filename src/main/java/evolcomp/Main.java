package evolcomp;

import evolcomp.io.CostTspReader;
import evolcomp.io.SolutionExporter;
import evolcomp.io.SolutionRow;
import evolcomp.misc.Evaluator;
import evolcomp.neighbours.IntraRouteNeighbour;
import evolcomp.strategy.*;
import evolcomp.strategy.ls.LSType;
import evolcomp.strategy.ls.LocalSearch;
import evolcomp.tsp.TSPInstance;
import evolcomp.neighbours.TwoEdgesExchangeNeighbour;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.*;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<TSPInstance> tspInstances = readTSPInstances();

        Strategy initialStrategy = new RandomStrategy(42);
        IntraRouteNeighbour neighStrategy = new TwoEdgesExchangeNeighbour();

        String delim = "|----------|-------------------------------------------|--------------------------------|--------------------------------|";
        System.out.println(delim);
        System.out.println("| Instance | Strategy                                  | f(x) [avg (min - max)]         | time in ms [avg (min - max)]   |");
        System.out.println(delim);

        String fmt = "\r| %-8s | %-41s | %-8d (%-8d - %-8d) | %-8d (%-8d - %-8d) |%n";
        List<SolutionRow> solutions = new ArrayList<>();

        for (TSPInstance tspInstance : tspInstances) {
            // FINAL VERSION
            Strategy ls = new LocalSearch.Builder()
                    .initialStrategy(initialStrategy)
                    .intraRouteStrategy(neighStrategy)
                    .lsType(LSType.STEEPEST)
                    .usePreviousDeltas(true)
                    .build();
//            Evaluator evaluator = new Evaluator(tspInstance, ls);
            Evaluator evaluator = new Evaluator(tspInstance, ls, true);

            String methodName = ls.toString();
            String instanceName = tspInstance.toString();
            String bestSolution = evaluator.getBestCycle().toString();

            int minValue = evaluator.getMinValue();
            int maxValue = evaluator.getMaxValue();
            int avgValue = evaluator.getAverageValue();

            long minTime = evaluator.getMinTimeMs();
            long avgTime = evaluator.getAverageTimeMs();
            long maxTime = evaluator.getMaxTimeMs();

            System.out.printf(fmt, instanceName, methodName, avgValue, minValue, maxValue, avgTime, minTime, maxTime);

            SolutionRow row = new SolutionRow(methodName, instanceName, bestSolution);
            solutions.add(row);
        }

        System.out.println(delim);
        System.out.println("Exporting solutions to solutions.csv...");
        SolutionExporter.export(solutions, "solution.csv");
        System.out.println("Solutions exported");
    }

    public static List<TSPInstance> readTSPInstances() throws IOException, URISyntaxException {
        List<TSPInstance> instances = new ArrayList<>(2);

        URL[] paths = {
                Main.class.getClassLoader().getResource("TSPA.csv"),
                Main.class.getClassLoader().getResource("TSPB.csv")
        };

        for (URL path : paths) {
            assert path != null;
            instances.add(CostTspReader.read(path));
        }

        return instances;
    }
}