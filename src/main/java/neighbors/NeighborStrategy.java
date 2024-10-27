package neighbors;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
/**
 *
    * @author Jerzu
 */
abstract class NeighborStrategy {
    public int evaluationResult;
    public Cycle ThisNeighbor;
    protected final Cycle currentSolution;
    protected final TSPInstance instance;
    
    public NeighborStrategy(TSPInstance instance, Cycle currentSolution) {
        this.instance = instance;
        this.currentSolution = currentSolution;
    }
    /** 
     returns utility delta for this neighbour
     negative delta -> neighbour is better than current solution
     positive delta -> neighbour is worse than current solution
    */
    public abstract int evaluate();

    public abstract Cycle buildNeighbor();
}