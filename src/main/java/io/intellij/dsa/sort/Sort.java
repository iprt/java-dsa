package io.intellij.dsa.sort;

import org.jetbrains.annotations.NotNull;

/**
 * Sort
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public interface Sort<E extends Comparable<E>> {

    /**
     * Sort the array in ascending order.
     *
     * @param array the array to be sorted
     */
    void sort(@NotNull E[] array);

}
