package io.intellij.dsa.tree.bst.basic;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.bst.BST;
import io.intellij.dsa.tree.bst.BstNode;
import io.intellij.dsa.tree.bst.BstUtils;

import java.util.function.BiConsumer;

import static io.intellij.dsa.DSAUtils.greater;
import static io.intellij.dsa.DSAUtils.less;

/**
 * BasicBST
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
public class BasicBST<K extends Comparable<K>, V> implements BST<K, V> {
    private BstNode<K, V> root;
    private int count;

    public BasicBST() {
        this.root = null;
        this.count = 0;
    }

    @Override
    public int size() {
        return this.count;
    }

    @Override
    public BstNode<K, V> getRoot() {
        return this.root;
    }

    @Override
    public void add(K key, V value) {
        recursiveAdd(key, value);
    }

    void recursiveAdd(K k, V v) {
        this.root = this.recursiveAdd(this.root, k, v, null, 1);
    }

    // 基于插入的节点，返回插入的节点
    // 在递归的过程中，节点被创建和连接
    BstNode<K, V> recursiveAdd(BstNode<K, V> node, K k, V v,
                               BstNode<K, V> parent, int height) {
        if (node == null) {
            BstNode<K, V> rtNode = new BasicNode<>(k, v);
            rtNode.setParent(parent);
            this.count++;
            return rtNode;
        }
        // 如果key相等，直接更新值
        if (DSAUtils.equals(node.getKey(), k)) {
            return node.setValue(v);
        } else if (less(k, node.getKey())) {
            return node.setLeft(recursiveAdd(node.getLeft(), k, v, node, height + 1));
        } else {
            return node.setRight(recursiveAdd(node.getRight(), k, v, node, height + 1));
        }
    }

    void whileAdd(K k, V v) {
        BstNode<K, V> node = new BasicNode<>(k, v);
        this.whileAdd(node);
    }

    // 插入的操作一定是插入在叶子节点上
    void whileAdd(BstNode<K, V> addNode) {
        BstNode<K, V> tmp = this.root;
        if (tmp == null) {
            this.root = addNode;
            addNode.setParent(null);
            this.count++;
            return;
        }
        while (tmp != null) {
            // 向左寻找
            if (less(addNode.getKey(), tmp.getKey())) {
                if (tmp.getLeft() == null) {
                    tmp.setLeft(addNode);
                    addNode.setParent(tmp);
                    this.count++;
                    break;
                } else {
                    tmp = tmp.getLeft();
                }
            } else if (greater(addNode.getKey(), tmp.getKey())) {
                if (tmp.getRight() == null) {
                    tmp.setRight(addNode);
                    addNode.setParent(tmp);
                    this.count++;
                    break;
                } else {
                    tmp = tmp.getRight();
                }
            } else {
                tmp.setValue(addNode.getValue());
                break;
            }
        }
    }

    @Override
    public V delete(K key) {
        BstNode<K, V> targetNode = BstUtils.get(root, key);
        if (targetNode == null) {
            return null;
        }
        V value = targetNode.getValue();
        this.root = delete(this.root, key);
        return value;
    }

    /**
     * 删除节点
     *
     * @param from 从哪里开始递归删除
     * @param key  删除的节点的key
     * @return BstNode<K, V> 返回删除后的树(以from为基准)
     */
    BstNode<K, V> delete(BstNode<K, V> from, K key) {
        if (from == null) {
            return null;
        }
        if (less(key, from.getKey())) {
            return from.setLeft(delete(from.getLeft(), key));
        } else if (greater(key, from.getKey())) {
            return from.setRight(delete(from.getRight(), key));
        } else {
            // 找到要删除的节点 并判断左右子树的情况

            // 左子树和右子树都为空
            if (from.getLeft() == null && from.getRight() == null) {
                // 理解: 真正的删除操作就是替换和删除叶子节点
                this.count--;
                return null;
            }

            // 剩余情况 左子树 和 右子树一定有一个不为空
            // 左子树不为空
            if (from.getLeft() != null) {
                // 寻找左子树最大节点
                BstNode<K, V> leftMax = BstUtils.getMinOrMax(from.getLeft(), BstUtils.Type.MAX);
                from.setKey(leftMax.getKey()).setValue(leftMax.getValue());

                return from.setLeft(
                        delete(from.getLeft(), leftMax.getKey())
                );
            }

            // 右子树不为空
            if (from.getRight() != null) {
                BstNode<K, V> rightMin = BstUtils.getMinOrMax(from.getRight(), BstUtils.Type.MIN);
                from.setKey(rightMin.getKey()).setValue(rightMin.getValue());
                return from.setRight(
                        delete(from.getRight(), rightMin.getKey())
                );
            }

        }
        throw new IllegalStateException("Unreachable");
    }


}
