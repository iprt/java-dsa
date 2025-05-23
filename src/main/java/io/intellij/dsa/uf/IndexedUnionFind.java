package io.intellij.dsa.uf;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

/**
 * 基于元素索引的并查集实现
 *
 * @author tech@intellij.io
 */
public class IndexedUnionFind<T> implements UnionFind<T> {
    private final Function<T, Integer> indexFunction;
    // 存储元素
    private Object[] storage;

    // 存储元素的父节点索引
    private Integer[] parent;
    // 存储元素的子节点索引集合
    private List<Integer>[] children;

    private int count;

    @SuppressWarnings("unchecked")
    public IndexedUnionFind(Function<T, Integer> indexFunc) {
        if (indexFunc == null) {
            throw new IllegalArgumentException("indexFunc cannot be null");
        }
        this.indexFunction = indexFunc;
        this.count = 0;
        storage = new Object[2];
        parent = new Integer[2];
        children = new ArrayList[2];
    }

    @Override
    public int getCount() {
        return this.count;
    }

    @Override
    public boolean contains(T data) {
        return Objects.nonNull(doContains(data));
    }

    @SuppressWarnings("unchecked")
    private T doContains(T data) {
        if (data == null) {
            return null;
        }
        int index = getIndex(data);
        if (index < 0 || index >= storage.length) {
            return null;
        }
        return (T) this.storage[index];
    }

    @Override
    public boolean add(T data) {
        return Objects.nonNull(doAdd(data, true));
    }

    @SuppressWarnings("unchecked")
    private T doAdd(T data, boolean replace) {
        if (data == null) {
            return null;
        }
        int dataIndex = indexFunction.apply(data);
        if (dataIndex < 0) {
            return null;
        }
        this.expand(dataIndex + 1);
        if (this.storage[dataIndex] != null) {
            if (replace) {
                this.storage[dataIndex] = data;
                return data;
            }
            return (T) this.storage[dataIndex];
        }

        storage[dataIndex] = data;
        parent[dataIndex] = dataIndex;
        children[dataIndex] = new ArrayList<>();
        count++;
        return data;
    }

    @Override
    public boolean union(T x, T y) {
        x = doAdd(x, false);
        y = doAdd(y, false);
        if (x == null || y == null) {
            return false;
        }
        this.union(getIndex(x), getIndex(y));
        return true;
    }

    private void union(int source, int current) {
        int srcParent = getParent(source);
        int curParent = getParent(current);
        // 已连接
        if (srcParent == curParent) {
            return;
        }

        // 树压缩
        parent[curParent] = srcParent;
        children[srcParent].add(curParent);
        children[srcParent].addAll(children[curParent]);
        children[curParent].forEach(child -> parent[child] = srcParent);
        children[curParent].clear();
    }

    private int getParent(int index) {
        return parent[index] != index ? getParent(parent[index]) : index;
    }

    @Override
    public boolean isConnected(T x, T y) {
        if (x == null || y == null) {
            return false;
        }
        int indexA = getIndex(x);
        int indexB = getIndex(y);
        if (indexA < 0 || indexB < 0) {
            return false;
        }
        return getParent(indexA) == getParent(indexB);
    }

    private int getIndex(T a) {
        Integer index = indexFunction.apply(a);
        if (index == null) {
            return -1;
        }
        return index;
    }

    // expand the data array to accommodate new elements
    @SuppressWarnings("unchecked")
    private void expand(int newSize) {
        if (newSize > storage.length) {
            Object[] newData = new Object[newSize];
            Integer[] newParent = new Integer[newSize];
            List<Integer>[] newChildren = new ArrayList[newSize];
            System.arraycopy(storage, 0, newData, 0, storage.length);
            System.arraycopy(parent, 0, newParent, 0, parent.length);
            System.arraycopy(children, 0, newChildren, 0, children.length);
            storage = newData;
            parent = newParent;
            children = newChildren;
        }
    }

}
