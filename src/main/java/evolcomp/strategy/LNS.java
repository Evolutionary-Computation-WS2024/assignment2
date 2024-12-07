/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy;

import evolcomp.strategy.NNToAnyUtils.Path;
import evolcomp.strategy.ls.LocalSearch;
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 *
 * @author User
 */
public class LNS extends Strategy{
    protected final Random randomGenerator = new Random(42);
    protected int noMainLoopRuns;
    @Override
    public Cycle apply(final TSPInstance tspInstance, final int startNode) {
        this.noMainLoopRuns = 0;
        long start = System.currentTimeMillis();
        
        RandomStrategy randomSolutionGenerator = new RandomStrategy(42);
        Cycle x = randomSolutionGenerator.apply(tspInstance,startNode);
        int scoreX = tspInstance.evaluate(x);
        
        while(System.currentTimeMillis() - start < 32699) {
            Cycle y = destroy(x);
            y = repair(y, tspInstance);
            this.noMainLoopRuns += 1;
            int scoreY = tspInstance.evaluate(y);
            if (scoreY<scoreX) {
                x = y;
                scoreX = scoreY;
            }
        }
        return x;
    }
    @Override
    public int getNoMainLoopRuns () {
        return this.noMainLoopRuns;
    }
    protected Cycle repair(Cycle y, TSPInstance tspInstance) {
        Path path = new Path(y, tspInstance);
        for (int i = y.nodes().size(); i < tspInstance.getRequiredCycleLength(); i++) {
            path.extend();
        }
        //System.out.println("reapired");
        //System.out.println(path.toList());
        return new Cycle(path.toList());
    }
    protected Cycle destroy(Cycle x) {
        List<Integer> nodesList = new ArrayList<>(x.nodes());
        //System.out.println("inital");
        //System.out.println(nodesList);
        removeRandomSublist(nodesList,0.25);
        //System.out.println("broken");
        //System.out.println(nodesList);
        return new Cycle(nodesList);
    }
    protected void removeRandomSublist(List<Integer> cycleList, double percentage) {
        int size = cycleList.size();
        int sublistSize = (int) Math.ceil(size * percentage);
        int startIndex = this.randomGenerator.nextInt(size);
        
        List<Integer> newList = new ArrayList<>();

        for (int i = 0; i < size; i++) {
            if (i < startIndex || i >= (startIndex + sublistSize) % size) {
                newList.add(cycleList.get(i));
            }
        }
        cycleList.clear();
        cycleList.addAll(newList);
    }
    @Override
    public String toString() {
        String text = "LNS without LS";
        return text;
    }
}
