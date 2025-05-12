package io.intellij.dsa.sort;

import io.intellij.dsa.sort.impl.BubbleSort;
import io.intellij.dsa.sort.impl.InsertSort;
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
        final int size = 10000;
        final int max = 10000;
        Assertions.assertTrue(
                new SortHelper<Integer>(new BubbleSort<>(), size, max)
                        .sort()
        );
    }

    @Test
    public void testInsertSort() {
        final int size = 10000;
        final int max = 10000;
        Assertions.assertTrue(
                new SortHelper<Integer>(new InsertSort<>(), size, max)
                        .sort()
        );
    }

}
