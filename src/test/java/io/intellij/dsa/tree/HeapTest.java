package io.intellij.dsa.tree;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.heap.MaxHeap;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

/**
 * HeapTest
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class HeapTest {

    @Test
    public void testMaxHeap() {
        Integer[] array = DSAUtils.createRandomArray(1000000, 1000000);
        Integer[] copyArray = DSAUtils.copyArray(array);
        MaxHeap<Integer> heap = new MaxHeap<>();
        for (Integer i : array) {
            heap.add(i);
        }

        Arrays.sort(copyArray);
        for (int i = copyArray.length - 1; i >= 0; i--) {
            Integer max = heap.extractMax();
            if (!max.equals(copyArray[i])) {
                throw new RuntimeException("Heap is not sorted");
            }
        }
    }

}
