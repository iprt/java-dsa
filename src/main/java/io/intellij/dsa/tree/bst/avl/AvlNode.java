package io.intellij.dsa.tree.bst.avl;

import io.intellij.dsa.tree.bst.BstNode;
import io.intellij.dsa.tree.bst.basic.BasicNode;
import lombok.Getter;
import lombok.Setter;

/**
 * AvlNode
 *
 * @author tech@intellij.io
 * @since 2025-05-15
 */
public class AvlNode<K extends Comparable<K>, V> extends BasicNode<K, V> {
    // 计算的关键： 真实节点为1 null节点为 0
    static final int DEFAULT_MAX_DISTANCE_TO_LEAF = 1;

    // 距离叶子节点的最大距离
    // 新增这个变量的意义是简化BasicNode中高度的多次递归计算
    @Setter
    @Getter
    private int maxDistanceToLeaf;

    public AvlNode(K key, V value) {
        super(key, value);
        this.maxDistanceToLeaf = DEFAULT_MAX_DISTANCE_TO_LEAF;
    }

    static <K extends Comparable<K>, V> int getNodeMaxDistanceToLeaf(BstNode<K, V> node) {
        if (node == null) {
            return DEFAULT_MAX_DISTANCE_TO_LEAF - 1;
        }
        return ((AvlNode<K, V>) node).getMaxDistanceToLeaf();
    }

    // 计算平衡因子，左右高度差
    static <K extends Comparable<K>, V> int getBalanceFactor(BstNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        return getNodeMaxDistanceToLeaf(node.getLeft()) - getNodeMaxDistanceToLeaf(node.getRight());
    }

}
