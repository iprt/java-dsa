package io.intellij.dsa.tree.bst.avl;

import io.intellij.dsa.tree.bst.BstNode;

/**
 * Rotate
 *
 * @author tech@intellij.io
 * @since 2025-05-16
 */
class Rotate {

    private static <K extends Comparable<K>, V> BstNode<K, V> rotateNodeAndRight(BstNode<K, V> node) {
        /*
            n(-2)
             \
              r(-1) return node
               \
                x
         */
        BstNode<K, V> right = node.getRight();
        BstNode<K, V> rightLeft = right.getLeft();
        if (rightLeft != null) {
            rightLeft.setParent(node);
        }

        right.setParent(node.getParent());
        node.setRight(rightLeft);
        right.setLeft(node);
        node.setParent(right);

        node.refreshHeight();
        right.refreshHeight();

        return right;
    }

    private static <K extends Comparable<K>, V> BstNode<K, V> rotateNodeAndLeft(BstNode<K, V> node) {
        /*
              n(2)
             /
            l(1) return node
           /
          x
         */
        BstNode<K, V> left = node.getLeft();
        BstNode<K, V> leftRight = left.getRight();
        if (leftRight != null) {
            leftRight.setParent(node);
        }

        left.setParent(node.getParent());
        node.setLeft(leftRight);
        left.setRight(node);
        node.setParent(left);

        node.refreshHeight();
        left.refreshHeight();

        return left;
    }

    static <K extends Comparable<K>, V> BstNode<K, V> rr(BstNode<K, V> node) {
        /*
            n(-2)
             \
              r(-1) return node
               \
                x
         */
        return rotateNodeAndRight(node);
    }

    static <K extends Comparable<K>, V> BstNode<K, V> ll(BstNode<K, V> node) {
        /*
              n(2)
             /
            l(1) return node
           /
          x
         */
        return rotateNodeAndLeft(node);
    }


    static <K extends Comparable<K>, V> BstNode<K, V> rl(BstNode<K, V> node) {
        /*
            n(-2)
             \
              r(1)
             /
            x return node
         */
        node.setRight(
                rotateNodeAndLeft(node.getRight())
        );
        // 旋转后，node的右子树变成了x，node的高度也需要更新
        node.refreshHeight();

        // 旋转node
        return rotateNodeAndRight(node);
    }

    static <K extends Comparable<K>, V> BstNode<K, V> lr(BstNode<K, V> node) {
        /*
              n(2)
             /
            l(-1)
             \
               x <return node>
         */
        node.setLeft(
                rotateNodeAndRight(node.getLeft())
        );
        // 旋转后，node的左子树变成了x，node的高度也需要更新
        node.refreshHeight();
        // 旋转node
        return rotateNodeAndLeft(node);
    }

}