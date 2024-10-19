package evolcomp.tsp;

import java.util.List;

public record Cycle(List<Integer> nodes) {
    @Override
    public String toString() {
        return String.join(",", nodes.stream().map(Object::toString).toList());
    }
}
