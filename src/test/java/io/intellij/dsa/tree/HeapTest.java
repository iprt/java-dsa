package io.intellij.dsa.tree;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.heap.Heap;
import io.intellij.dsa.tree.heap.HeapImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.Comparator;

/**
 * HeapTest
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class HeapTest {

    @Test
    public void testMinHeap() {
        Integer[] array = DSAUtils.createRandomArray(1000000, 1000000);
        Integer[] copyArray = DSAUtils.copyArray(array);
        HeapImpl<Integer> heap = new HeapImpl<>();
        for (Integer i : array) {
            heap.add(i);
        }

        Arrays.sort(copyArray);
        for (Integer integer : copyArray) {
            Integer min = heap.extract();
            Assertions.assertEquals(integer, min);
        }
    }

    @Test
    public void testHeapify() {
        Integer[] array = DSAUtils.createRandomArray(1000000, 1000000);
        Integer[] copyArray = DSAUtils.copyArray(array);

        // 最大堆 反过来比较
        HeapImpl<Integer> heap = new HeapImpl<>(array, Heap.Type.MAX, Comparator.comparing(Integer::intValue).reversed());
        Arrays.sort(copyArray);
        for (Integer integer : copyArray) {
            Integer min = heap.extract();
            Assertions.assertEquals(integer, min);
        }
    }

}
