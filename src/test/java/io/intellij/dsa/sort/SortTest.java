package io.intellij.dsa.sort;

import io.intellij.dsa.sort.impl.BubbleSort;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * SortTest
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class SortTest {

    @Test
    public void testBubbleSort() {
        Integer[] array = SortHelper.createRandomArray(10000, 10000);
        new BubbleSort<Integer>().sort(array);
        boolean sorted = SortHelper.isSorted(array);
        Assertions.assertTrue(sorted);
    }

}
