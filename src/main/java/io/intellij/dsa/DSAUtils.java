package io.intellij.dsa;

import org.jetbrains.annotations.NotNull;

/**
 * AlgoUtils
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class DSAUtils {

    /**
     * Creates an array of random integers with the specified size and maximum value.
     *
     * @param size the size of the array to be created
     * @param max  the maximum value (exclusive) for the random integers in the array
     * @return an array of random integers with the specified size and maximum value
     */
    public static Integer @NotNull [] createRandomArray(int size, int max) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * max);
        }
        return array;
    }

    public static Integer[] copyArray(@NotNull Integer[] array) {
        Integer[] copy = new Integer[array.length];
        System.arraycopy(array, 0, copy, 0, array.length);
        return copy;
    }

    /**
     * Determines whether the given array is sorted in non-decreasing order.
     *
     * @param array the array to be checked, where each element is an Integer
     * @return {@code true} if the array is sorted in non-decreasing order, {@code false} otherwise
     */
    public static boolean isIncrement(Integer @NotNull [] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Determines whether the given array is sorted in non-increasing order.
     *
     * @param array the array to be checked, where each element is an Integer
     * @return {@code true} if the array is sorted in non-increasing order, {@code false} otherwise
     */
    public static boolean isDecrement(Integer @NotNull [] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] < array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    /**
     * Swap two elements in the array.
     *
     * @param array the array
     * @param i     the index of the first element
     * @param j     the index of the second element
     */
    public static <E extends Comparable<E>> void swap(E @NotNull [] array, int i, int j) {
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
    public static <E extends Comparable<E>> boolean less(@NotNull E a, @NotNull E b) {
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
    public static <E extends Comparable<E>> boolean lessEquals(@NotNull E a, @NotNull E b) {
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
    public static <E extends Comparable<E>> boolean greater(@NotNull E a, @NotNull E b) {
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
    public static <E extends Comparable<E>> boolean greaterEquals(@NotNull E a, @NotNull E b) {
        return a.compareTo(b) >= 0;
    }

    /**
     * Compares two elements and determines if they are equal.
     *
     * @param a   the first element to compare; must not be null
     * @param b   the second element to compare; must not be null
     * @param <E> the type of elements being compared, which must implement {@code Comparable<E>}
     * @return {@code true} if the two elements are equal according to their natural ordering;
     *         {@code false} otherwise
     */
    public static <E extends Comparable<E>> boolean equals(@NotNull E a, @NotNull E b) {
        return a.compareTo(b) == 0;
    }

}
