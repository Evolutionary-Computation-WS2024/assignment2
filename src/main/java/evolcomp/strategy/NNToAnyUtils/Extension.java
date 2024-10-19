/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.NNToAnyUtils;

import evolcomp.tsp.TSPInstance;

/**
 *
 * @author User
 */
public class Extension {
    public final int utility_difference;
    public final int extra_node_id;
    public final evolcomp.strategy.NNToAnyUtils.Node anchor;
    private final TSPInstance instance;
    public Extension(TSPInstance instance, evolcomp.strategy.NNToAnyUtils.Node anchor, int extra_node_id) {
        this.extra_node_id = extra_node_id;
        this.anchor = anchor;
        this.instance = instance;
        this.utility_difference = this.computeUtilityDifference();
    }
    private int computeUtilityDifference() {
        int costs = this.instance.getCostAt(extra_node_id);
        costs += this.instance.getDistanceBetween(anchor.point_id, extra_node_id);
        if (this.anchor.hasNext()) {
            costs += this.instance.getDistanceBetween(extra_node_id, anchor.getNext().point_id);
            int gains = this.instance.getDistanceBetween(anchor.point_id, anchor.getNext().point_id);
            return costs-gains;
        }
        return costs;
    }
    public void add_to_the_patch() {
        if (this.anchor.hasNext()) {
            Node new_node = new Node(extra_node_id);
            new_node.setNext(anchor.getNext());
            this.anchor.setNext(new_node);  
        } else {
            Node new_node = new Node(extra_node_id);
            this.anchor.setNext(new_node);  
        }   
    }
    //compare
    public boolean is_better(Extension other) {
        return this.utility_difference < other.utility_difference;
    }
}