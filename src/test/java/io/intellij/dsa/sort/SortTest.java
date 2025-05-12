package io.intellij.dsa.sort;

import io.intellij.dsa.sort.impl.BubbleSort;
import io.intellij.dsa.sort.impl.InsertSort;
import io.intellij.dsa.sort.impl.MergeSort;
import io.intellij.dsa.sort.impl.QuickSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * SortTest
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class SortTest {

    final int size = 100000;
    final int max = 100000;

    @Test
    public void testBubbleSort() {
        SortHelper.SortResult sortResult = new SortHelper<Integer>(new BubbleSort<>(), size, max).sort();
        Assertions.assertTrue(sortResult.isSorted());
    }

    @Test
    public void testInsertSort() {
        SortHelper.SortResult sortResult = new SortHelper<Integer>(new InsertSort<>(), size, max).sort();
        Assertions.assertTrue(sortResult.isSorted());
    }

    @Test
    public void testMerge() {
        SortHelper.SortResult sortResult = new SortHelper<Integer>(new MergeSort<>(), size, max).sort();
        Assertions.assertTrue(sortResult.isSorted());
    }

    @Test
    public void testQuick() {
        SortHelper.SortResult sortResult = new SortHelper<Integer>(new QuickSort<>(), size, max).sort();
        Assertions.assertTrue(sortResult.isSorted());
    }

}
