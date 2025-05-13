package io.intellij.dsa.sort;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;

import static io.intellij.dsa.DSAUtils.createRandomArray;
import static io.intellij.dsa.DSAUtils.isIncrement;

/**
 * SortHelper
 *
 * @author tech@intellij.io
 * @since 2025-05-12
 */
@Slf4j
public class SortHelper<E extends Comparable<Integer>> implements Sort<Integer> {
    private final Sort<Integer> sortMethod;
    private final Integer[] array;
    private final Integer[] arrayCopy;
    private final int size;

    public SortHelper(Sort<Integer> sortMethod, int size, int max) {
        this.sortMethod = sortMethod;
        this.size = size;
        this.array = createRandomArray(size, max);
        arrayCopy = new Integer[size];
        System.arraycopy(array, 0, arrayCopy, 0, size);
    }

    public SortResult sort() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        this.sortMethod.sort(this.array);
        stopWatch.stop();
        log.info("sort method = {}|size={}|sort time= {}ms", sortMethod.getClass().getSimpleName(), this.size, stopWatch.getTime());

        // after sort compare
        return new SortResult(isIncrement(this.array), afterSortCompare(), stopWatch.getTime());
    }

    @Override
    public void sort(@NotNull Integer[] array) {
        this.sortMethod.sort(array);
    }

    /**
     * Compares the original array with a sorted copy of itself to check if they are identical.
     * This method sorts a copy of the original array and compares each element of the original
     * array against the corresponding element in the sorted copy.
     *
     * @return {@code true} if the original array matches its sorted copy, indicating that the
     * array was already sorted; {@code false} otherwise.
     */
    private boolean afterSortCompare() {
        Arrays.sort(arrayCopy);
        // compare the sorted array with the original
        for (int i = 0; i < array.length; i++) {
            if (!array[i].equals(arrayCopy[i])) {
                return false;
            }
        }
        return true;
    }

    public record SortResult(boolean isIncrement, boolean isSame, long costMs) {
    }
}
