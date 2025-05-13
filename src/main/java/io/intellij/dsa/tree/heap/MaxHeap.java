package io.intellij.dsa.tree.heap;

import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;

import static io.intellij.dsa.DSAUtils.greater;
import static io.intellij.dsa.DSAUtils.swap;

/**
 * MaxHeap
 * <p>
 * 定义：节点值大于左右节点
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
@Slf4j
public class MaxHeap<T extends Comparable<T>> implements Heap<T> {
    private static final int DEFAULT_CAPACITY = 7;

    private T[] data;
    private int count;
    private int capacity;

    public MaxHeap() {
        this(DEFAULT_CAPACITY);
    }

    @SuppressWarnings("unchecked")
    public MaxHeap(int capacity) {
        this.capacity = capacity;
        this.count = 0;
        this.data = (T[]) new Comparable[capacity];
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public void add(T element) {
        if (this.count == this.capacity) {
            this.expand();
        }
        data[count++] = element;
        siftUp(count - 1);
    }


    @Override
    public T extractMax() {
        if (this.count == 0) {
            return null;
        }
        T max = data[0];
        // 将最后一个元素放到根节点
        data[0] = data[count - 1];
        data[count - 1] = null;
        count--;
        if (count > 0 && capacity > DEFAULT_CAPACITY && count == capacity / 2) {
            // 缩容
            this.reduce();
        }
        siftDown(0);
        return max;
    }

    @Override
    public T getMax() {
        if (this.count == 0) {
            return null;
        }
        return data[0];
    }

    // parent index = (index - 1) / 2
    private void siftUp(int index) {
        // 当前节点 > 0 代表有父节点
        while (index > 0 && greater(data[index], data[(index - 1) / 2])) {
            // 交换当前节点和父节点
            swap(data, index, (index - 1) / 2);
            // 更新当前节点为父节点
            index = (index - 1) / 2;
        }
    }

    // left child index = 2 * index + 1
    // right child index = 2 * index + 2
    private void siftDown(int index) {
        // 能交换的前提是当前节点有子节点,直接先看左子节点
        int current = index;
        int left = 2 * index + 1;
        while (left < count) {
            int chooseChild = left;
            if (left + 1 < count && greater(data[left + 1], data[left])) {
                // left + 1 < count 存在右子节点
                // 右子节点大于左子节点
                chooseChild = left + 1;
            }
            if (greater(data[chooseChild], data[current])) {
                // 子节点的值大于当前节点,交换当前节点和子节点
                swap(data, chooseChild, current);
                // 更新当前节点为子节点
                current = chooseChild;
                left = 2 * current + 1;
            } else {
                // 当前节点大于子节点
                break;
            }
        }
    }

    // 扩容
    @SuppressWarnings("unchecked")
    private void expand() {
        // 树添加一层
        int newCapacity = this.capacity * 2 + 1;
        T[] newData = (T[]) new Comparable[newCapacity];
        // 复制数据
        System.arraycopy(this.data, 0, newData, 0, this.count);
        log.info("expand heap from {} to {}", this.capacity, newCapacity);
        this.data = newData;
        this.capacity = newCapacity;

    }

    // 缩容
    @SuppressWarnings("unchecked")
    private void reduce() {
        // 树减少一层
        int newCapacity = this.capacity / 2;
        T[] newData = (T[]) new Comparable[newCapacity];
        // 复制数据
        System.arraycopy(this.data, 0, newData, 0, this.count);
        log.info("reduce heap from {} to {}", this.capacity, newCapacity);
        this.data = newData;
        this.capacity = newCapacity;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        return new Iterator<T>() {
            private int currentIndex = 0;

            @Override
            public boolean hasNext() {
                return currentIndex < size();
            }

            @Override
            public T next() {
                if (!hasNext()) {
                    throw new NoSuchElementException("没有更多元素");
                }
                return data[currentIndex++];
            }
        };
    }

    @Override
    public void forEach(Consumer<? super T> action) {
        Objects.requireNonNull(action);
        for (int i = 0; i < count; i++) {
            action.accept(data[i]);
        }
    }

    @Override
    public Spliterator<T> spliterator() {
        return Spliterators.spliterator(
                data, 0, count,
                Spliterator.SIZED | Spliterator.ORDERED | Spliterator.NONNULL
        );
    }

}
