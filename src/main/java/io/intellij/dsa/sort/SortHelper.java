package io.intellij.dsa.sort;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;
import org.jetbrains.annotations.NotNull;

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
    private final int size;

    public SortHelper(Sort<Integer> sortMethod, int size, int max) {
        this.sortMethod = sortMethod;
        this.size = size;
        this.array = createRandomArray(size, max);
    }

    public SortResult sort() {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        this.sortMethod.sort(this.array);
        stopWatch.stop();
        log.info("sort method = {}|size={}|sort time= {}ms", sortMethod.getClass().getSimpleName(), this.size, stopWatch.getTime());
        return new SortResult(isSorted(this.array), stopWatch.getTime());
    }

    @Override
    public void sort(@NotNull Integer[] array) {
        this.sortMethod.sort(array);
    }

    public Integer[] createRandomArray(int size, int max) {
        Integer[] array = new Integer[size];
        for (int i = 0; i < size; i++) {
            array[i] = (int) (Math.random() * max);
        }
        return array;
    }

    public boolean isSorted(Integer[] array) {
        for (int i = 0; i < array.length - 1; i++) {
            if (array[i] > array[i + 1]) {
                return false;
            }
        }
        return true;
    }

    public record SortResult(boolean isSorted, long costMs) {
    }
}
