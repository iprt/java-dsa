package io.intellij.dsa.tree.bst;

import static io.intellij.dsa.tree.bst.BstUtils.getNodeHeight;

/**
 * BSTNode
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public interface BstNode<K extends Comparable<K>, V> {
    int DEFAULT_HEIGHT = 1;

    int getHeight();

    BstNode<K, V> setHeight(int height);

    K getKey();

    BstNode<K, V> setKey(K key);

    V getValue();

    BstNode<K, V> setValue(V value);

    BstNode<K, V> getParent();

    BstNode<K, V> setParent(BstNode<K, V> parent);

    BstNode<K, V> getLeft();

    BstNode<K, V> setLeft(BstNode<K, V> left);

    BstNode<K, V> getRight();

    BstNode<K, V> setRight(BstNode<K, V> right);

    default boolean isLeaf() {
        return this.getLeft() == null && this.getRight() == null;
    }

    // 刷新高度
    default BstNode<K, V> refreshHeight() {
        return this.setHeight(Math.max(getNodeHeight(getLeft()), getNodeHeight(getRight())) + 1);
    }

}
