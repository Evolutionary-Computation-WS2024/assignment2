/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.NNToAnyUtils;

import evolcomp.tsp.TSPInstance;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 *
 * @author User
 */
public class Patch {
    private Node starting_node;
    private Set<Integer> remaining_nodes;
    private final TSPInstance tspInstance;
    
    public Patch(int starting_point, TSPInstance tspInstance) {
        this.tspInstance = tspInstance;
        int no_nodes = tspInstance.getHowManyNodes();
        this.remaining_nodes = new HashSet<>();
        for (int i = 0; i < no_nodes; i++) {
            this.remaining_nodes.add(i); // Add numbers from 0 to n-1
        }
        
        this.starting_node = new Node(starting_point);
        this.remaining_nodes.remove(starting_point);
    }
    public void extend() {
        // initialize any solution for comaprison
        Iterator<Integer> remaining_nodes_iterator = remaining_nodes.iterator();
        Extension best_found_extension = new Extension(this.tspInstance, starting_node, remaining_nodes_iterator.next());
        
        Node current_node = starting_node;
        while (true) {
            remaining_nodes_iterator = remaining_nodes.iterator();
            while (remaining_nodes_iterator.hasNext()) {
                Extension candidate = new Extension(this.tspInstance, current_node, remaining_nodes_iterator.next());
                if (candidate.is_better(best_found_extension)) {
                    best_found_extension = candidate;
                }
            }
            if (!current_node.hasNext()) {
                break;
            }
            current_node = current_node.getNext();
        }
        // also check for extending at the begining
        remaining_nodes_iterator = remaining_nodes.iterator();
        int best_starting_node = 999999999;
        int best_utility_difference_for_starting_node = 999999999;
        while (remaining_nodes_iterator.hasNext()) {
            int starting_node_candidate = remaining_nodes_iterator.next();
            int utility_difference = this.tspInstance.getCostAt(starting_node_candidate);
            utility_difference += this.tspInstance.getDistanceBetween(starting_node_candidate, this.starting_node.point_id);
            if (utility_difference < best_utility_difference_for_starting_node) {
                best_starting_node = starting_node_candidate;
                best_utility_difference_for_starting_node = utility_difference;
            }
        }
        if (best_utility_difference_for_starting_node < best_found_extension.utility_difference) {
            Node new_starting_node = new Node(best_starting_node);
            new_starting_node.setNext(this.starting_node);
            this.starting_node = new_starting_node;
            this.remaining_nodes.remove(best_starting_node);
        } else {
            best_found_extension.add_to_the_patch();
            this.remaining_nodes.remove(best_found_extension.extra_node_id);
        }
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
 }
