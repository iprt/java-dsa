package io.intellij.dsa.uf;

/**
 * 并查集是一种用于管理元素所属集合的数据结构
 *
 * @author tech@intellij.io
 */
public interface UnionFind<T> {

    // 是否为空集合
    default boolean isEmpty() {
        return this.getCount() == 0;
    }

    default boolean contains(T data) {
        return false;
    }

    // 获取集合的大小
    int getCount();

    // 添加元素到集合中
    boolean add(T data);

    // 将两个元素合并到同一个集合中
    boolean union(T x, T y);

    // 查看两个元素是否在同一个集合中
    boolean isConnected(T x, T y);

}
