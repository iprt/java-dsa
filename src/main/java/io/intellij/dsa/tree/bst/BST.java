package io.intellij.dsa.tree.bst;

import java.util.function.BiConsumer;
import java.util.function.Consumer;

/**
 * BST 二分搜索树
 * <p>
 * 节点大于等于左子树，小于右子树
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public interface BST<K extends Comparable<K>, V> {

    int size();

    default boolean isEmpty() {
        return this.size() == 0;
    }

    BstNode<K, V> getRoot();

    int height();

    void add(K key, V value);

    V delete(K key);

    boolean update(K key, V value);

    V get(K key);

    BstNode<K, V> getMin();

    BstNode<K, V> getMax();

    // 是否是二分搜索树
    boolean isBST();

    // 前序遍历，
    // 先访问父节节点，再访问左子树，最后访问右子树
    void preorderTraversal(BiConsumer<K, V> action);

    // 中序遍历
    // 先访问左子树，再访问父节点，最后访问右子树
    void inorderTraversal(BiConsumer<K, V> action);

    // 后序遍历
    // 先访问左子树，再访问右子树，最后访问父节点
    void postorderTraversal(BiConsumer<K, V> action);

    // 层序遍历
    void levelOrderTraversal(BiConsumer<K, V> action);
}
