package io.intellij.dsa.sort.impl;

import io.intellij.dsa.sort.Sort;
import org.jetbrains.annotations.NotNull;

import static io.intellij.dsa.sort.SortUtils.greaterEquals;
import static io.intellij.dsa.sort.SortUtils.less;
import static io.intellij.dsa.sort.SortUtils.swap;

/**
 * QuickSort
 * <p>
 * 基准（pivot）、分区、递归
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class QuickSort<E extends Comparable<E>> implements Sort<E> {

    @Override
    public void sort(@NotNull E[] array) {
        if (array.length < 2) {
            return;
        }
        // [l...r]
        this.quickSort(array, 0, array.length - 1);
    }

    // [l...r]
    void quickSort(@NotNull E[] arr, int l, int r) {
        if (l >= r) {
            return;
        }
        // 获取中间位置
        int partition = onePartition(arr, l, r);
        quickSort(arr, l, partition);
        quickSort(arr, partition + 1, r);
    }

    // [l...r] 双路减少交换
    int partition(@NotNull E[] arr, int l, int r) {
        E pivot = arr[l];
        int start = l + 1, end = r;
        while (true) {
            while (start <= end && less(arr[start], pivot)) {
                // arr[start] 小于 pivot,直接自增
                start++;
            }

            while (end >= start && greaterEquals(arr[end], pivot)) {
                // arr[end] 大于等于 pivot,直接自减
                end--;
            }
            // 极限情况
            // case1: start=r+1 pivot 全小于 arr[l+1...r]
            // case2: end=l     pivot 全大于等于 arr[l+1...r]
            // 优先判断是否需要 break
            if (start > end) {
                break;
            }
            // arr[start] 大于 pivot 交换
            swap(arr, start, end);
        }
        if (l == end) {
            // 需要比较的元素 都比 pivot 大，直接返回l,不交换
            return l;
        }
        // 最终分割的数组的本质
        // [l,end] [end+1,r]
        swap(arr, l, end);
        return end;
    }


    // [l...r] 单路多次交换
    int onePartition(@NotNull E[] arr, int l, int r) {
        E pivot = arr[l];
        int start = l + 1, end = r;
        while (start <= end) {
            if (less(arr[start], pivot)) {
                start++;
            } else {
                swap(arr, start, end);
                end--;
            }

            // 极限情况
            // case1: start=r+1 pivot 全小于 arr[l+1...r]
            // case2: end=l     pivot 全大于等于 arr[l+1...r]
            // 终止的本质是 start=end+1
            if (start > end) {
                break;
            }
        }
        if (l == end) {
            // 需要比较的元素 都比 pivot 大，直接返回l,不交换
            return l;
        }
        // 最终分割的数组的本质
        // [l,end] [end+1,r]
        swap(arr, l, end);
        return end;
    }

}
