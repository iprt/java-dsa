package io.intellij.dsa.sort.impl;

import io.intellij.dsa.sort.Sort;
import io.intellij.dsa.sort.SortUtils;
import org.jetbrains.annotations.NotNull;

/**
 * BubblingSort
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class BubbleSort<E extends Comparable<E>> implements Sort<E> {

    @Override
    public void sort(@NotNull E[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            for (int j = i + 1; j < array.length; j++) {
                if (SortUtils.less(array[j], array[i])) {
                    SortUtils.swap(array, i, j);
                }
            }
        }
    }

}
