/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package evolcomp.strategy.NNToAnyUtils;
import java.util.Comparator;

/**
 *
 * @author User
 */
public class ExtensionComparator implements Comparator<Extension>{
    public ExtensionComparator() {
    }
    @Override
    public int compare(Extension e1, Extension e2) {
        return e1.get_weighted_sum() - e2.get_weighted_sum();
    }
}
