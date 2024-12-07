/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.NNToAnyUtils;

import evolcomp.tsp.Cycle;
import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.Collections;

public class Path {
    public Node starting_node;
    private Set<Integer> remaining_nodes;
    private final TSPInstance tspInstance;
    private final ExtensionComparator comparator;
    public final int regret_weight;
    public final int utility_weight;
    
    // TODO
    public Path(Cycle brokenSolution, TSPInstance tspInstance) {
        this.tspInstance = tspInstance;
        int no_nodes = tspInstance.getHowManyNodes();
        this.remaining_nodes = new HashSet<>();
        for (int i = 0; i < no_nodes; i++) {
            this.remaining_nodes.add(i); // Add numbers from 0 to n-1
        }
        readCycle(brokenSolution);
        this.comparator = new ExtensionComparator();
        this.regret_weight  =  -1;
        this.utility_weight = -1;
    }
    public void extend() {
        // initialize any solution for comaprison
        Iterator<Integer> remaining_nodes_iterator = remaining_nodes.iterator();
        //WARNING it may be wrong
        
        List<Extension> best_extensions_for_every_remaining_node = new ArrayList<>();
        
        Extension best_found_extension = null;
        Extension second_best_found_extension = null;
        
        remaining_nodes_iterator = remaining_nodes.iterator();
        while (remaining_nodes_iterator.hasNext()){
            int extra_node_id = remaining_nodes_iterator.next();
            Node current_node = starting_node;
            best_found_extension = null;
            second_best_found_extension = null;
            while (true) {
                Extension candidate = new Extension(this.tspInstance, current_node, extra_node_id, this);
                if (candidate.is_better(best_found_extension)) {
                    best_found_extension = candidate;
                }
                else if (candidate.is_better(second_best_found_extension)) {
                    second_best_found_extension = candidate;
                }                                
                if (!current_node.hasNext()) {
                    break;
                }
                current_node = current_node.getNext();
            }
            best_extensions_for_every_remaining_node.add(best_found_extension);
            best_found_extension.compute_2_regret(second_best_found_extension);
        }
        Extension selected_extension = Collections.max(best_extensions_for_every_remaining_node, this.comparator);
        selected_extension.add_to_the_patch();
        this.remaining_nodes.remove(selected_extension.extra_node_id);
    }
    public List<Integer> toList() {
        List<Integer> list = new ArrayList<>();
        Node current_node = this.starting_node;
        while (true) {
            list.add(current_node.point_id);
            if (!current_node.hasNext()) {
                break;
            }
            current_node = current_node.getNext();
        }
        return list;
    }
    private void readCycle(Cycle cycle) {
        List<Integer> cycleNodes = cycle.nodes();
        int firstNodeId = cycleNodes.get(0);
        this.remaining_nodes.remove(firstNodeId);
        this.starting_node = new Node(firstNodeId);
        Node prev = this.starting_node;
        for (int i = 1; i < cycleNodes.size(); i++) {
            int nexNodeId = cycleNodes.get(i);
            this.remaining_nodes.remove(nexNodeId);
            Extension extension = new Extension(this.tspInstance, prev, nexNodeId, this);
            extension.add_to_the_patch();
            prev = prev.getNext();
        }
        //System.out.println("transcripted");
        //System.out.println(this.toList());
        //prev.setNext(this.starting_node);
    }
 }