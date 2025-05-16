package io.intellij.dsa.tree.bst.avl;

import io.intellij.dsa.tree.bst.BstNode;

import static io.intellij.dsa.tree.bst.avl.AvlNode.getNodeMaxDistanceToLeaf;

/**
 * Rotate
 *
 * @author tech@intellij.io
 * @since 2025-05-16
 */
class Rotate {

    static <K extends Comparable<K>, V> BstNode<K, V> rr(BstNode<K, V> node) {
        /*
            n(-2)
             \
              r(-1) return node
               \
                x
         */
        BstNode<K, V> r = node.getRight();
        BstNode<K, V> rl = r.getLeft();

        if (rl != null) {
            // break n and r
            rl.setParent(node);
        }
        r.setParent(node.getParent());
        node.setRight(rl);
        r.setLeft(node);
        node.setParent(r);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );
        ((AvlNode<K, V>) r).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(r.getLeft()), getNodeMaxDistanceToLeaf(r.getRight())) + 1
        );
        return r;
    }


    static <K extends Comparable<K>, V> BstNode<K, V> rl(BstNode<K, V> node) {
        /*
            n(-2)
             \
              r(1)
             /
            x return node
         */
        BstNode<K, V> r = node.getRight();
        BstNode<K, V> x = r.getLeft();

        BstNode<K, V> xr = x.getRight();
        // 先处理  r 和 x 的旋转
        if (xr != null) {
            xr.setParent(r);
        }
        r.setLeft(xr);

        x.setParent(node);
        node.setRight(x);
        r.setParent(x);
        x.setRight(r);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) r).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(r.getLeft()), getNodeMaxDistanceToLeaf(r.getRight())) + 1
        );
        ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
        );

        // 再处理 n 和 x
        BstNode<K, V> xl = x.getLeft();

        if (xl != null) {
            xl.setParent(node);
        }
        node.setRight(xl);

        x.setParent(node.getParent());
        x.setLeft(node);
        node.setParent(x);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );
        ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
        );
        return x;
    }

    static <K extends Comparable<K>, V> BstNode<K, V> ll(BstNode<K, V> node) {
        /*
              n(2)
             /
            l(1) return node
           /
          x
         */
        BstNode<K, V> l = node.getLeft();
        BstNode<K, V> lr = l.getRight();

        if (lr != null) {
            lr.setParent(node);
        }
        node.setLeft(lr);

        l.setParent(node.getParent());
        l.setRight(node);
        node.setParent(l);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );
        ((AvlNode<K, V>) l).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(l.getLeft()), getNodeMaxDistanceToLeaf(l.getRight())) + 1
        );
        return l;
    }

    static <K extends Comparable<K>, V> BstNode<K, V> lr(BstNode<K, V> node) {
        /*
              n(2)
             /
            l(-1)
             \
               x <return node>
         */
        BstNode<K, V> l = node.getLeft();
        BstNode<K, V> x = l.getRight();
        BstNode<K, V> xl = x.getLeft();
        // 先处理 x 和 l 的旋转
        if (xl != null) {
            xl.setParent(l);
        }
        l.setRight(xl);

        x.setParent(node);
        node.setLeft(x);
        x.setLeft(l);
        l.setParent(x);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) l).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(l.getLeft()), getNodeMaxDistanceToLeaf(l.getRight())) + 1
        );
        ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
        );

        // 再处理x和n
        BstNode<K, V> xr = x.getRight();
        if (xr != null) {
            xr.setParent(node);
        }
        node.setLeft(xr);

        x.setParent(node.getParent());
        x.setRight(node);
        node.setParent(x);

        // 更新最大距离，从下往上更新
        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );
        ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
        );
        return x;
    }

}