package evolcomp.tsp;

import java.util.List;
import java.util.stream.Collectors;

public class Cycle {
    public final List<Integer> nodes;
    

    // Constructor
    public Cycle(List<Integer> nodes) {
        if (nodes == null) {
            throw new IllegalArgumentException("Nodes cannot be null");
        }
        this.nodes = List.copyOf(nodes); // Ensures immutability
    }
    public String toString() {
        return String.join(",", nodes.stream().map(Object::toString).collect(Collectors.toList()));
    }
}
