package io.intellij.dsa.sort;

/**
 * SortUtils
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
public class SortUtils<E extends Comparable<E>> {

    /**
     * Swap two elements in the array.
     *
     * @param array the array
     * @param i     the index of the first element
     * @param j     the index of the second element
     */
    public static <E extends Comparable<E>> void swap(E[] array, int i, int j) {
        E temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }


    /**
     * Compares two elements and determines if the first is less than the second.
     *
     * @param a   the first element to compare
     * @param b   the second element to compare
     * @param <E> the type of elements being compared, which must implement {@code Comparable<E>}
     * @return {@code true} if the first element is less than the second; {@code false} otherwise
     */
    public static <E extends Comparable<E>> boolean less(E a, E b) {
        return a.compareTo(b) < 0;
    }

    /**
     * Compares two elements and determines if the first is less than or equal to the second.
     *
     * @param a   the first element to compare
     * @param b   the second element to compare
     * @param <E> the type of elements being compared, which must implement {@code Comparable<E>}
     * @return {@code true} if the first element is less than or equal to the second; {@code false} otherwise
     */
    public static <E extends Comparable<E>> boolean lessEquals(E a, E b) {
        return a.compareTo(b) <= 0;
    }

    /**
     * Compares two elements and determines if the first is greater than the second.
     *
     * @param a   the first element to compare
     * @param b   the second element to compare
     * @param <E> the type of elements being compared, which must implement {@code Comparable<E>}
     * @return {@code true} if the first element is greater than the second; {@code false} otherwise
     */
    public static <E extends Comparable<E>> boolean greater(E a, E b) {
        return a.compareTo(b) > 0;
    }

    /**
     * Compares two elements and determines if the first is greater than or equal to the second.
     *
     * @param a   the first element to compare
     * @param b   the second element to compare
     * @param <E> the type of elements being compared, which must implement {@code Comparable<E>}
     * @return {@code true} if the first element is greater than or equal to the second; {@code false} otherwise
     */
    public static <E extends Comparable<E>> boolean greaterEquals(E a, E b) {
        return a.compareTo(b) >= 0;
    }

}
