package io.intellij.dsa.tree.bst;

/**
 * BST
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public interface BST<K extends Comparable<K>, V> {

    int size();

    default boolean isEmpty() {
        return this.size() == 0;
    }

    int height();

    void add(K key, V value);

    V delete(K key);

}
