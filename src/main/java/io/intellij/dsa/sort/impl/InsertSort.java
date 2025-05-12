package io.intellij.dsa.sort.impl;

import io.intellij.dsa.sort.Sort;
import org.jetbrains.annotations.NotNull;

import static io.intellij.dsa.sort.SortUtils.less;
import static io.intellij.dsa.sort.SortUtils.swap;

/**
 * InsertSort 插入排序的关键是相邻的两个元素比较和交换
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class InsertSort<E extends Comparable<E>> implements Sort<E> {

    @Override
    public void sort(@NotNull E[] array) {
        for (int i = 1; i < array.length; i++) {
            // Find the position where current should be inserted
            for (int j = i; j > 0; j--) {
                if (less(array[j], array[j - 1])) {
                    swap(array, j, j - 1);
                } else {
                    break;
                }
            }
        }
    }

}
