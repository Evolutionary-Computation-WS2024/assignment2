package evolcomp.misc;

import evolcomp.strategy.Strategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

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

    public Evaluator(TSPInstance instance, Strategy strategy) {
        this.instance = instance;
        this.strategy = strategy;
        execute();
    }

    private void execute() {
        int totalValue = 0;
        for (int i=0; i<instance.getHowManyNodes(); i++) {
            long start = System.nanoTime();
            Cycle currentCycle = strategy.apply(instance, i);
            long elapsedMs = (System.nanoTime() - start) / 1_000_000;
            averageTimeMs += elapsedMs;

            if (elapsedMs > maxTimeMs) {
                maxTimeMs = elapsedMs;
            }

            if (elapsedMs < minTimeMs) {
                minTimeMs = elapsedMs;
            }

            int currentValue = instance.evaluate(currentCycle);
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
}
