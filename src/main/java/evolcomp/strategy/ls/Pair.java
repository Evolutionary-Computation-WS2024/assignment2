/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.ls;
import java.util.Objects;

/**
 * Utility class to store unique pairs of integers in arbitrary order
 * pairs (a,b) and (b,a) will be considered identical, no matter the order
 * @author Jerzu
 */
public class Pair {
    public final int first;
    public final int second;

    public Pair(int first, int second) {
        // Ensure the first element is always less than or equal to the second
        if (first <= second) {
            this.first = first;
            this.second = second;
        } else {
            this.first = second;
            this.second = first;
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Pair)) return false;
        Pair pair = (Pair) o;
        return first == pair.first && second == pair.second;
    }

    @Override
    public int hashCode() {
        return Objects.hash(first, second);
    }

    @Override
    public String toString() {
        return "(" + first + ", " + second + ")";
    }
}
