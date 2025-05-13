package io.intellij.dsa.sort.impl;

import io.intellij.dsa.sort.Sort;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import static io.intellij.dsa.DSAUtils.less;

/**
 * MergeSort 自顶向下
 * <p>
 * 核心： divide(分解) conquer(解决) combine(合并)
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
@Slf4j
public class MergeSort<E extends Comparable<E>> implements Sort<E> {

    @Override
    public void sort(@NotNull E[] array) {
        if (array.length < 2) {
            return;
        }
        // [l...r]
        divide(array, 0, array.length - 1);
    }

    // [l...r]
    void divide(@NotNull E[] array, int l, int r) {
        if (l >= r) { // 只有一个元素
            return;
        }
        int mid = (l + r) / 2;

        // 1.1 -> 1.2 (分解)
        // 1.4 (回溯)
        divide(array, l, mid);

        // 1.1 -> 1.2 (分解)
        // 1.4 (回溯)
        divide(array, mid + 1, r);

        // how to understand this?
        // 递归最后的归并
        // 假设最终只有两个元素 arr[x] 和 arr[y]
        //    divide(array, x, mid); 会直接返回
        //    divide(array, mid + 1, y); 会直接返回
        //    调用 combine(array, x, mid, y); 进行归并，其中 l=x, mid=x, r=y
        // 回溯归并
        //    对于 [x,y] 已经有序
        //    作为一个子集， 下一次的mid则是y

        // 1.3 (最小合并)
        // 1.5 (合并)
        this.combine(array, l, mid, r);
        // 理解顺序  1.1 -> 1.2 -> 1.3 -> 1.4 -> 1.5
    }

    @SuppressWarnings("unchecked")
    void combine(@NotNull E[] arr, int l, int mid, int r) {
        E[] tmp = (E[]) new Comparable[r - l + 1];

        // e.g.  [5(l=0),6,7,8(mid=3)] [1(mid+1=4),2,3,4(r=7)]
        // tmp = [5,6,7,8,  1,2,3,4]

        // target_arr = [1,2,3,4,5,6,7,8]
        int x = l, y = mid + 1;
        int i = 0;

        // 处理左右两个子数组，直到其中一个处理完
        while (x <= mid && y <= r) {
            if (less(arr[x], arr[y])) {
                tmp[i++] = arr[x++];
            } else {
                tmp[i++] = arr[y++];
            }
        }
        // 处理左子数组剩余元素
        while (x <= mid) {
            tmp[i++] = arr[x++];
        }
        // 处理右子数组剩余元素
        while (y <= r) {
            tmp[i++] = arr[y++];
        }
        System.arraycopy(tmp, 0, arr, l, tmp.length);
    }

}
