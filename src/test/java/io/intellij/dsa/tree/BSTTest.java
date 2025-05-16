package io.intellij.dsa.tree;

import io.intellij.dsa.tree.bst.BST;
import io.intellij.dsa.tree.bst.BstUtils;
import io.intellij.dsa.tree.bst.avl.AvlTree;
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
        BST<Integer, Integer> bst = new BasicBST<>();
        for (Integer i : arr) {
            bst.add(i, i);
        }
        System.out.println("bst.size() = " + bst.size() + ";" + " bst.height() = " + bst.height());
        for (Integer i : arr) {
            System.out.println(bst.delete(i));
        }
        System.out.println("bst.size() = " + bst.size() + ";" + " bst.height() = " + bst.height());
    }

    @Test
    public void testAVL() {
        Integer[] arr = {1, 2, 3, 4, 5, 6, 7};
        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : arr) {
            avl.add(i, i);
        }
        System.out.println(avl);
    }

    @Test
    public void testAvlRR() {
        Integer[] arr = {1, 2, 3};
        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : arr) {
            avl.add(i, i);
        }
        System.out.println(avl);
    }

    @Test
    public void testAvlLL() {
        Integer[] arr = {3, 2, 1};
        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : arr) {
            avl.add(i, i);
        }
        System.out.println(avl);
    }

    @Test
    public void testAvlRL() {
        Integer[] arr = {1, 3, 2};
        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : arr) {
            avl.add(i, i);
        }
        System.out.println(avl);
    }

    @Test
    public void testAvlLR() {
        Integer[] arr = {3, 1, 2};
        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : arr) {
            avl.add(i, i);
        }
        System.out.println(avl);
    }

    @Test
    public void testAvlDelete() {
        Integer[] add = {1, 2, 3, 4, 5, 6, 7};
        Integer[] delete = {1, 2, 3};

        BST<Integer, Integer> avl = new AvlTree<>();
        for (Integer i : add) {
            avl.add(i, i);
        }
        System.out.println(avl);

        for (Integer i : delete) {
            avl.delete(i);
            BstUtils.printTree(avl.getRoot());
        }
    }

}
