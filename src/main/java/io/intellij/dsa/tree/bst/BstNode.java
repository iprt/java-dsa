package io.intellij.dsa.tree.bst;

/**
 * BSTNode
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public interface BstNode<K extends Comparable<K>, V> {

    K getKey();

    BstNode<K, V> setKey(K key);

    V getValue();

    BstNode<K, V> setValue(V value);

    int getHeight();

    void setHeight(int height);

    void setParent(BstNode<K, V> parent);

    BstNode<K, V> getParent();

    BstNode<K, V> setLeft(BstNode<K, V> left);

    BstNode<K, V> getLeft();

    BstNode<K, V> setRight(BstNode<K, V> right);

    BstNode<K, V> getRight();

}
