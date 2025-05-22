package io.intellij.dsa.uf;

import java.util.Objects;
import java.util.TreeSet;
import java.util.function.Function;

/**
 * 基于元素索引的并查集实现
 *
 * @author tech@intellij.io
 */
public class IndexedUnionFind<T> implements UnionFind<T> {
    private final Function<T, Integer> indexFunction;
    // 存储元素
    private Object[] dataArr;
    // 存储元素的父节点索引
    private Integer[] parent;
    // 存储元素的子节点索引集合
    private TreeSet<Integer>[] children;

    private int count;

    @SuppressWarnings("unchecked")
    public IndexedUnionFind(Function<T, Integer> indexFunc) {
        if (indexFunc == null) {
            throw new IllegalArgumentException("indexFunc cannot be null");
        }
        this.indexFunction = indexFunc;
        this.count = 0;
        dataArr = new Object[2];
        parent = new Integer[2];
        children = new TreeSet[2];
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
        int index = getIndex(data);
        if (index >= dataArr.length) {
            return null;
        }
        return (T) this.dataArr[index];
    }

    @Override
    public boolean add(T data) {
        if (data == null) {
            return false;
        }
        return Objects.nonNull(add(data, true));
    }

    @SuppressWarnings("unchecked")
    private T add(T data, boolean replace) {
        if (data == null) {
            return null;
        }
        int dataIndex = indexFunction.apply(data);
        if (dataIndex < 0) {
            return null;
        }
        this.expand(dataIndex + 1);
        if (this.dataArr[dataIndex] != null) {
            if (replace) {
                this.dataArr[dataIndex] = data;
                return data;
            }
            return (T) this.dataArr[dataIndex];
        }

        this.dataArr[dataIndex] = data;
        parent[dataIndex] = dataIndex;
        children[dataIndex] = new TreeSet<>();
        count++;
        return data;
    }

    @Override
    public boolean union(T x, T y) {
        x = add(x, false);
        y = add(y, false);
        if (x == null || y == null) {
            return false;
        }
        this.union(getIndex(x), getIndex(y));
        return true;
    }

    private void union(int source, int current) {
        int sourceParent = getParent(source);
        int curParentIdx = getParent(current);
        // 已连接
        if (sourceParent == curParentIdx) {
            return;
        }
        // 连接
        parent[curParentIdx] = sourceParent;

        children[sourceParent].add(current);
        children[sourceParent].addAll(children[curParentIdx]);
        children[curParentIdx].forEach(child -> parent[child] = sourceParent);
        children[curParentIdx].clear();
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
        if (newSize > dataArr.length) {
            Object[] newData = new Object[newSize];
            Integer[] newParent = new Integer[newSize];
            TreeSet<Integer>[] newChildren = new TreeSet[newSize];
            System.arraycopy(dataArr, 0, newData, 0, dataArr.length);
            System.arraycopy(parent, 0, newParent, 0, parent.length);
            System.arraycopy(children, 0, newChildren, 0, children.length);
            dataArr = newData;
            parent = newParent;
            children = newChildren;
        }
    }

}
