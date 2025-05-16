package io.intellij.dsa.tree.bst;

import java.util.function.BiConsumer;

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

    default int height() {
        return BstUtils.getHeight(getRoot());
    }

    void add(K key, V value);

    V delete(K key);

    default boolean update(K key, V value) {
        BstNode<K, V> find = BstUtils.get(getRoot(), key);
        if (find == null) {
            return false;
        }
        find.setValue(value);
        return true;
    }

    default V get(K key) {
        BstNode<K, V> find = BstUtils.get(getRoot(), key);
        return find == null ? null : find.getValue();
    }

    default BstNode<K, V> getMin() {
        return BstUtils.getMinOrMax(getRoot(), BstUtils.Type.MIN);
    }

    default BstNode<K, V> getMax() {
        return BstUtils.getMinOrMax(getRoot(), BstUtils.Type.MAX);
    }

    // 是否是二分搜索树
    default boolean isBST() {
        return BstUtils.isBST(getRoot());
    }

    // 前序遍历，
    // 先访问父节节点，再访问左子树，最后访问右子树
    default void preorderTraversal(BiConsumer<K, V> action) {
        BstUtils.preorderTraversal(action, getRoot());
    }

    // 中序遍历
    // 先访问左子树，再访问父节点，最后访问右子树
    default void inorderTraversal(BiConsumer<K, V> action) {
        BstUtils.inorderTraversal(action, getRoot());
    }

    // 后序遍历
    // 先访问左子树，再访问右子树，最后访问父节点
    default void postorderTraversal(BiConsumer<K, V> action) {
        BstUtils.postorderTraversal(action, getRoot());
    }

    // 层序遍历
    default void levelOrderTraversal(BiConsumer<K, V> action) {
        BstUtils.levelOrderTraversal(action, getRoot());
    }

}
