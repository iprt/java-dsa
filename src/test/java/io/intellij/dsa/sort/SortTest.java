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
        sortTest(new BubbleSort<>());
    }

    @Test
    public void testInsertSort() {
        sortTest(new InsertSort<>());
    }

    @Test
    public void testMerge() {
        sortTest(new MergeSort<>());
    }

    @Test
    public void testQuick() {
        sortTest(new QuickSort<>());
    }

    private void sortTest(Sort<Integer> sort) {
        SortHelper.SortResult sortResult = new SortHelper(sort, size, max).sort();
        System.out.println(sortResult);
        Assertions.assertTrue(sortResult.isIncrement());
        Assertions.assertTrue(sortResult.isSame());
    }

}
