package io.intellij.dsa.tree.bst;

import io.intellij.dsa.DSAUtils;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.Consumer;

import static io.intellij.dsa.DSAUtils.greater;
import static io.intellij.dsa.DSAUtils.less;

/**
 * BstUtils
 *
 * @author tech@intellij.io
 * @since 2025-05-15
 */
public class BstUtils {

    public static <K extends Comparable<K>, V> BstNode<K, V> get(BstNode<K, V> from, K key) {
        if (from == null) {
            return null;
        }

        if (DSAUtils.equals(from.getKey(), key)) {
            return from;
        } else if (less(key, from.getKey())) {
            return get(from.getLeft(), key);
        } else {
            return get(from.getRight(), key);
        }
    }

    public static <K extends Comparable<K>, V> BstNode<K, V> getMinOrMax(BstNode<K, V> node, boolean minOrMax) {
        if (node == null) {
            return null;
        }
        BstNode<K, V> tmp = node;
        while (tmp != null) {
            if (minOrMax) {
                if (tmp.getLeft() != null) {
                    tmp = tmp.getLeft();
                } else {
                    break;
                }
            } else {
                if (tmp.getRight() != null) {
                    tmp = tmp.getRight();
                } else {
                    break;
                }
            }
        }
        return tmp;
    }

    public static <K extends Comparable<K>, V> boolean isBST(BstNode<K, V> node) {
        if (node == null) {
            return true;
        }
        BstNode<K, V> left = node.getLeft();
        BstNode<K, V> right = node.getRight();
        if (left != null && greater(left.getKey(), node.getKey())) {
            return false;
        }
        if (right != null && less(right.getKey(), node.getKey())) {
            return false;
        }
        return isBST(left) && isBST(right);
    }

    public static <K extends Comparable<K>, V> void preorderTraversal(Consumer<BstNode<K, V>> action, BstNode<K, V> node) {
        if (node == null) {
            return;
        }
        action.accept(node);
        preorderTraversal(action, node.getLeft());
        preorderTraversal(action, node.getRight());
    }


    public static <K extends Comparable<K>, V> void inorderTraversal(Consumer<BstNode<K, V>> action, BstNode<K, V> node) {
        if (node == null) {
            return;
        }
        inorderTraversal(action, node.getLeft());
        action.accept(node);
        inorderTraversal(action, node.getRight());
    }

    public static <K extends Comparable<K>, V> void postorderTraversal(Consumer<BstNode<K, V>> action, BstNode<K, V> node) {
        if (node == null) {
            return;
        }
        postorderTraversal(action, node.getLeft());
        postorderTraversal(action, node.getRight());
        action.accept(node);
    }

    public static <K extends Comparable<K>, V> void levelOrderTraversal(Consumer<BstNode<K, V>> action, BstNode<K, V> node) {
        if (node == null) {
            return;
        }
        Queue<BstNode<K, V>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            BstNode<K, V> current = queue.poll();
            action.accept(current);
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }
    }

    public static <K extends Comparable<K>, V> void getLeafs(BstNode<K, V> node, List<BstNode<K, V>> leafs) {
        if (node == null) {
            return;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            leafs.add(node);
        }
        getLeafs(node.getLeft(), leafs);
        getLeafs(node.getRight(), leafs);
    }

    public static <K extends Comparable<K>, V> int getHeight(BstNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = getHeight(node.getLeft()) + 1;
        int rightHeight = getHeight(node.getRight()) + 1;
        return Math.max(leftHeight, rightHeight);
    }

}
