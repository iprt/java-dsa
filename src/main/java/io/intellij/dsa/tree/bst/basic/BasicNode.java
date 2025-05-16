package io.intellij.dsa.tree.bst.basic;

import io.intellij.dsa.tree.bst.BstNode;

/**
 * BasicNode
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class BasicNode<K extends Comparable<K>, V> implements BstNode<K, V> {
    protected K key;
    protected V value;

    protected BstNode<K, V> parent;
    protected BstNode<K, V> left;
    protected BstNode<K, V> right;

    // 距离根节点的高度
    protected int height;

    public BasicNode(K key, V value) {
        this.key = key;
        this.value = value;
        this.parent = null;
        this.left = null;
        this.right = null;
        this.height = 0;
    }

    @Override
    public K getKey() {
        return this.key;
    }

    @Override
    public BstNode<K, V> setKey(K key) {
        this.key = key;
        return this;
    }

    @Override
    public BstNode<K, V> setValue(V value) {
        this.value = value;
        return this;
    }

    @Override
    public V getValue() {
        return this.value;
    }

    @Override
    public int getHeight() {
        return this.height;
    }

    @Override
    public void setHeight(int height) {
        this.height = height;
    }

    @Override
    public void setParent(BstNode<K, V> parent) {
        this.parent = parent;
    }

    @Override
    public BstNode<K, V> getParent() {
        return this.parent;
    }

    @Override
    public BstNode<K, V> setLeft(BstNode<K, V> left) {
        this.left = left;
        return this;
    }

    @Override
    public BstNode<K, V> getLeft() {
        return this.left;
    }

    @Override
    public BstNode<K, V> setRight(BstNode<K, V> right) {
        this.right = right;
        return this;
    }

    @Override
    public BstNode<K, V> getRight() {
        return this.right;
    }
}
