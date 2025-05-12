package io.intellij.dsa.sort;

/**
 * SortHelper
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class SortHelper {

    public static Integer[] createRandomArray(int size, int max) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * max);
        }
        return array;
    }

    public static boolean isSorted(Integer[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

}
