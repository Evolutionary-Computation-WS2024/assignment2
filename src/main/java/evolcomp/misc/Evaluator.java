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

    public Evaluator(TSPInstance instance, Strategy strategy) {
        this.instance = instance;
        this.strategy = strategy;
        execute();
    }

    private void execute() {
        int totalValue = 0;
        for (int i=0; i<instance.getHowManyNodes(); i++) {
            Cycle currentCycle = strategy.apply(instance, i);
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
}
