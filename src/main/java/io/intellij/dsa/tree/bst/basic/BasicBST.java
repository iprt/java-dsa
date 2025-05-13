package io.intellij.dsa.tree.bst.basic;

import io.intellij.dsa.tree.bst.BST;

/**
 * BasicBST
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class BasicBST<K extends Comparable<K>, V> implements BST<K, V> {
    @Override
    public int size() {
        return 0;
    }

    @Override
    public int height() {
        return 0;
    }

    @Override
    public void add(K key, V value) {

    }

    @Override
    public V delete(K key) {
        return null;
    }
}
