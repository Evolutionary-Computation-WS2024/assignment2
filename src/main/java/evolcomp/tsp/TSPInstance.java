package evolcomp.tsp;

import evolcomp.misc.Exponentiation;

import java.util.*;

public final class TSPInstance {
    private final HashMap<Integer, Point> points;
    private final HashMap<Integer, HashMap<Integer, Integer>> distances;
    private final HashMap<Integer, Set<Integer>> nearestNeighbors;
    private final int hashMapCapacity;
    private String name;
    private final int howManyNodes;
    private final int requiredCycleLength;

    public TSPInstance(List<Point> points) {
        hashMapCapacity = Exponentiation.getClosest2ToThePowerOf(points.size());
        this.points = new HashMap<>(hashMapCapacity, 1.0f);
        this.distances = new HashMap<>(hashMapCapacity, 1.0f);
        this.nearestNeighbors = new HashMap<>(hashMapCapacity, 1.0f);
        this.howManyNodes = points.size();
        this.requiredCycleLength = (points.size() + 1) / 2;
        this.name = null;
        populatePoints(points);
        populateDistances();
        populateNearestNeighbors(10);
    }

    public TSPInstance(List<Point> points, String name) {
        hashMapCapacity = Exponentiation.getClosest2ToThePowerOf(points.size());
        this.points = new HashMap<>(hashMapCapacity, 1.0f);
        this.distances = new HashMap<>(hashMapCapacity, 1.0f);
        this.nearestNeighbors = new HashMap<>(hashMapCapacity, 1.0f);
        this.howManyNodes = points.size();
        this.requiredCycleLength = (points.size() + 1) / 2;
        this.name = name;
        populatePoints(points);
        populateDistances();
        populateNearestNeighbors(10);
    }

    public int evaluate(Cycle cycle) {
        int cycleSize = cycle.getNodes().size();
        if (cycleSize != requiredCycleLength) {
            throw new IllegalArgumentException("Provided cycle length (" + cycleSize + ") does not match instance's required cycle length (" + requiredCycleLength + ")!");
        }

        int nodesCost = 0;
        int edgesCost = 0;

        Iterator<Integer> i = cycle.getNodes().iterator();
        int prev = i.next();

        while (i.hasNext()) {
            // add cost of node
            nodesCost += getCostAt(prev);

            int next = i.next();

            // add cost of edge
            edgesCost += getDistanceBetween(prev, next);

            // update previous node
            prev = next;
        }

        // add cost of edge between first and last
        edgesCost += getDistanceBetween(cycle.getNodes().get(0), prev);

        // add cost of last node
        nodesCost += getCostAt(prev);

        int totalCost = nodesCost + edgesCost;

        return totalCost;
    }

    public int getDistanceBetween(final int x, final int y) {
        if (x < 0 || x >= howManyNodes) {
            throw new IllegalArgumentException("X must be between 0 and " + (howManyNodes - 1) + " (x=" + x + " was provided)");
        }
        if (y < 0 || y >= howManyNodes) {
            throw new IllegalArgumentException("Y must be between 0 and " + (howManyNodes - 1) + " (y=" + y + " was provided)");
        }

        return distances.get(x).get(y);
    }

    public int getCostAt(final int x) {
        assert 0 <= x && x < howManyNodes;
        return points.get(x).cost();
    }

    private void populatePoints(final List<Point> pointsToInclude) {
        int i = 0;
        for (Point p : pointsToInclude) {
            this.points.put(i++, p);
        }
    }

    private void populateDistances() {
        for (int i = 0; i < howManyNodes; i++) {
            distances.put(i, new HashMap<>(hashMapCapacity, 1.0f));
            distances.get(i).put(i, 0);
        }

        for (int i = 0; i < howManyNodes; i++) {
            Point a = points.get(i);

            for (int j = 1; j < howManyNodes; j++) {
                Point b = points.get(j);

                // Euclidean distance
                int distance = (int) Math.round(Math.sqrt(Math.pow(a.x() - b.x(), 2) + Math.pow(a.y() - b.y(), 2)));

                distances.get(i).put(j, distance);
                distances.get(j).put(i, distance);
            }
        }
    }
    
    private void populateNearestNeighbors(int neighborCount) {
        for (int i = 0; i < howManyNodes; i++) {
            // each element in pq is an int[] array, a[0] = neighbor’s index, a[1] = neighbor’s distance
            // a -> a[1] lambda expression idicates that distance will be used for comaprison
            PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingInt(a -> a[1]));
            // entrySet()returns a Set of Map.Entry objects
            for (Map.Entry<Integer, Integer> entry : distances.get(i).entrySet()) {
                int neighborIndex = entry.getKey();
                int distance = entry.getValue();
                int costAdjustedDistance = distance + getCostAt(neighborIndex);
                pq.add(new int[]{neighborIndex, costAdjustedDistance});
                if (pq.size() > neighborCount) {
                    pq.poll(); // Remove the farthest neighbor if we exceed neighborCount
                }
            }
            Set<Integer> nearestSet = new HashSet<>();
            for (int[] neighbor : pq) {
                nearestSet.add(neighbor[0]);
            }
            nearestNeighbors.put(i, nearestSet);
        }
    }
    
    public Set<Integer> getNearestNeighbors(int pointIndex) {
    // Validate input
    if (pointIndex < 0 || pointIndex >= howManyNodes) {
        throw new IllegalArgumentException("Point index must be between 0 and " + (howManyNodes - 1) + " (pointIndex=" + pointIndex + " was provided)");
    }
    return nearestNeighbors.get(pointIndex);
    }
    // Returns number of nodes for this TSP instance
    public int getHowManyNodes() {
        return howManyNodes;
    }

    // Returns number of nodes required to form a cycle
    public int getRequiredCycleLength() {
        return requiredCycleLength;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return name;
    }
}
