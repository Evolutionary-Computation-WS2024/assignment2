package evolcomp.misc;

import evolcomp.strategy.Strategy;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;

public final class Evaluator {
    private final TSPInstance instance;
    private final Strategy strategy;
    private final boolean verbose;

    private Cycle bestCycle;

    private int minValue = Integer.MAX_VALUE;
    private int maxValue = Integer.MIN_VALUE;
    private int averageValue;

    private long minTimeMs = Long.MAX_VALUE;
    private long maxTimeMs = Long.MIN_VALUE;
    private long averageTimeMs;

    public Evaluator(TSPInstance instance, Strategy strategy, boolean verbose) {
        this.instance = instance;
        this.strategy = strategy;
        this.verbose = verbose;
        execute();
    }

    public Evaluator(TSPInstance instance, Strategy strategy) {
        this.instance = instance;
        this.strategy = strategy;
        this.verbose = false;
        execute();
    }

    private void execute() {
        int totalValue = 0;
        updateProgressStatus(0, instance.getHowManyNodes(), true);
        for (int i=0; i<instance.getHowManyNodes(); i++) {
            updateProgressStatus(i, instance.getHowManyNodes(), false);

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

    private void updateProgressStatus(int progress, int total, boolean isStart) {
        // Do not evaluate when verbose flag is not set
        if (!verbose) {
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("Evaluating... ")
                .append(progress)
                .append("/")
                .append(total);

        if (!isStart) {
            sb.insert(0, "\r");
        }
        System.out.print(sb);
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
