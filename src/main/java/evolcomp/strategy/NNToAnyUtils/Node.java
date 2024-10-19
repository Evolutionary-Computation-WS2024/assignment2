/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.NNToAnyUtils;

/**
 *
 * @author User
 */
public class Node {
    private Node next;
    public final int point_id;
    public Node(int point_id) {
        this.point_id = point_id;
    }
    public void setNext(Node next) {
        this.next = next; 
    } 
    public Node getNext() {
        return this.next;
    }
    public boolean hasNext() {
        return this.next != null;
    }
}