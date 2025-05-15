package io.intellij.dsa.tree;

import io.intellij.dsa.tree.bst.basic.BasicBST;
import org.junit.jupiter.api.Test;

/**
 * BSTTest
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class BSTTest {

    @Test
    public void testBasic() {
        Integer[] arr = {5, 5, 3, 7, 2, 4, 6, 8, 8};
        BasicBST<Integer, Integer> bst = new BasicBST<>();
        for (Integer i : arr) {
            bst.add(i, i);
        }
        System.out.println("bst.size() = " + bst.size() + ";" + " bst.height() = " + bst.height());
        for (Integer i : arr) {
            bst.delete(i);
        }
        System.out.println("bst.size() = " + bst.size() + ";" + " bst.height() = " + bst.height());
    }

}
