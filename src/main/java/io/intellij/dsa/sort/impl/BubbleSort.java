package io.intellij.dsa.sort.impl;

import io.intellij.dsa.sort.Sort;
import org.jetbrains.annotations.NotNull;

import static io.intellij.dsa.DSAUtils.less;
import static io.intellij.dsa.DSAUtils.swap;


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
                if (less(array[j], array[i])) {
                    swap(array, i, j);
                }
            }
        }
    }

}
