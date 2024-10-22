package evolcomp;

import evolcomp.io.CostTspReader;
import evolcomp.io.SolutionExporter;
import evolcomp.io.SolutionRow;
import evolcomp.misc.Evaluator;
import evolcomp.strategy.*;
import evolcomp.tsp.TSPInstance;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

public class Main {
    public static void main(String[] args) throws IOException, URISyntaxException {
        List<TSPInstance> tspInstances = new ArrayList<>();

        URL[] paths = {
                Main.class.getClassLoader().getResource("TSPA.csv"),
                Main.class.getClassLoader().getResource("TSPB.csv")
        };

        for (URL path : paths) {
            assert path != null;
            tspInstances.add(CostTspReader.read(path));
        }

        List<Strategy> strategies = new ArrayList<>();
        strategies.add(new GreedyCycleWithRegret(-1,0));
        strategies.add(new GreedyCycleWithRegret(0,-1));
        strategies.add(new GreedyCycleWithRegret(-1,-1));
        strategies.add(new GreedyCycleWithRegret(-3,-1));
        strategies.add(new GreedyCycleWithRegret(-6,-1));
        strategies.add(new GreedyCycleWithRegret(-7,-1));
        
        List<SolutionRow> solutions = new ArrayList<>();

        String delim = "|----------|-------------------------------------|----------|----------|----------|";
        System.out.println(delim);
        System.out.println("| Instance | Strategy                            | Min f(x) | Avg f(x) | Max f(x) |");
        System.out.println(delim);

        String fmt = "| %-8s | %-35s | %-8d | %-8d | %-8d |";
        for (TSPInstance instance : tspInstances) {
            for (Strategy strategy : strategies) {
                long start = System.nanoTime();
                Evaluator evaluator = new Evaluator(instance, strategy);
                long elapsedMs = (System.nanoTime() - start) / 1_000_000;

                String methodName = strategy.toString();
                String instanceName = instance.toString();
                String bestSolution = evaluator.getBestCycle().toString();

                System.out.printf((fmt) + "%n", instanceName, methodName, evaluator.getMinValue(), evaluator.getAverageValue(), evaluator.getMaxValue());


                SolutionRow row = new SolutionRow(methodName, instanceName, bestSolution);
                solutions.add(row);
            }
        }

        System.out.println(delim);
        System.out.println("Exporting solutions to solutions.csv...");
        SolutionExporter.export(solutions, "solution.csv");
        System.out.println("Solutions exported");
    }
}