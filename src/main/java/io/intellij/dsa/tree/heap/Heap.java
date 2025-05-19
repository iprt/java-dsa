package io.intellij.dsa.tree.heap;

/**
 * Heap 最大堆的实现
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public interface Heap<T extends Comparable<T>> extends Iterable<T> {

    enum Type {
        MAX,
        MIN
    }

    int size();

    default boolean isEmpty() {
        return this.size() == 0;
    }

    void add(T element);

    T extract();

    T get();

    Type getType();

}
