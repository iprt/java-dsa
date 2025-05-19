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
import static io.intellij.dsa.DSAUtils.less;
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
public class HeapImpl<T extends Comparable<T>> implements Heap<T> {
    private static final int DEFAULT_CAPACITY = 7;
    private final Type type;

    private T[] data;
    private int count;
    private int capacity;

    public HeapImpl() {
        this(DEFAULT_CAPACITY, Type.MIN);
    }

    public HeapImpl(Type type) {
        this(DEFAULT_CAPACITY, type);
    }

    public HeapImpl(@NotNull T[] array, Type type) {
        this(array.length, type);
        this.heapify(array);
    }

    @SuppressWarnings("unchecked")
    public HeapImpl(int initCap, Type type) {
        if (initCap < 0) {
            throw new IllegalArgumentException("Capacity must be non-negative");
        }
        this.type = Objects.requireNonNullElse(type, Type.MIN);
        int realCapacity = DEFAULT_CAPACITY;
        while (initCap > realCapacity) {
            realCapacity = realCapacity * 2 + 1;
        }

        this.capacity = realCapacity;

        this.count = 0;
        this.data = (T[]) new Comparable[realCapacity];
    }

    @Override
    public int size() {
        return this.count;
    }

    // 自下而上的堆化
    // 这种方法从数组的最后一个非叶子节点开始，依次向前处理每个节点，对每个节点执行下沉（sift-down）操作。
    private void heapify(T[] array) {
        // 复制数组到堆中
        System.arraycopy(array, 0, this.data, 0, array.length);
        this.count = array.length;

        // 从最后一个非叶子节点开始，依次向前执行下沉操作
        for (int i = (count - 2) / 2; i >= 0; i--) {
            siftDown(i);
        }
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
    public T extract() {
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
    public T get() {
        if (this.count == 0) {
            return null;
        }
        return data[0];
    }

    @Override
    public Type getType() {
        return this.type;
    }

    // parent index = (index - 1) / 2
    private void siftUp(int index) {
        // 当前节点 > 0 代表有父节点
        int current = index, parent = (index - 1) / 2;
        while (current > 0 && elementCompare(data[current], data[parent])) {
            // 交换当前节点和父节点
            swap(data, current, parent);
            // 更新当前节点为父节点
            current = parent;
            parent = (current - 1) / 2;
        }
    }

    // left child index = 2 * index + 1
    // right child index = 2 * index + 2
    private void siftDown(int index) {
        // 能交换的前提是当前节点有子节点,直接先看左子节点
        int current = index, left = 2 * index + 1;
        while (left < count) {
            int chooseChild = left;
            if (left + 1 < count && elementCompare(data[left + 1], data[left])) {
                // left + 1 < count 存在右子节点
                // 右子节点大于左子节点
                chooseChild = left + 1;
            }
            if (elementCompare(data[chooseChild], data[current])) {
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

    private boolean elementCompare(T a, T b) {
        if (this.type == Type.MAX) {
            return greater(a, b);
        } else {
            return less(a, b);
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
