package io.intellij.dsa.tree.bst.avl;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.bst.BST;
import io.intellij.dsa.tree.bst.BstNode;
import io.intellij.dsa.tree.bst.BstUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.BiConsumer;

import static io.intellij.dsa.DSAUtils.greater;
import static io.intellij.dsa.DSAUtils.less;
import static io.intellij.dsa.tree.bst.avl.AvlNode.getBalanceFactor;
import static io.intellij.dsa.tree.bst.avl.AvlNode.getNodeMaxDistanceToLeaf;

/**
 * AvlTree
 *
 * @author tech@intellij.io
 * @since 2025-05-13
 */
@Slf4j
public class AvlTree<K extends Comparable<K>, V> implements BST<K, V> {
    private BstNode<K, V> root;
    private int count;

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
        this.root = this.recursiveAdd(this.root, key, value, null);
    }

    // 基于插入的节点，返回插入的节点
    // 在递归的过程中，节点被创建和连接
    BstNode<K, V> recursiveAdd(BstNode<K, V> node, K k, V v, BstNode<K, V> parent) {
        if (node == null) {
            BstNode<K, V> rtNode = new AvlNode<>(k, v);
            rtNode.setParent(parent);
            this.count++;
            return rtNode;
        }
        // 如果key相等，直接更新值
        if (DSAUtils.equals(node.getKey(), k)) {
            node.setValue(v);
            return node;
        }

        if (less(k, node.getKey())) {
            node.setLeft(recursiveAdd(node.getLeft(), k, v, node));
        } else {
            node.setRight(recursiveAdd(node.getRight(), k, v, node));
        }

        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );

        int balanceFactor = getBalanceFactor(node);
        return this.rebalance(node, balanceFactor, Action.ADD);
    }

    // 重新平衡，返回平衡后的节点
    BstNode<K, V> rebalance(BstNode<K, V> node, int balanceFactor, Action action) {
        if (node == null) return null;
        log.info("node.key={}|balanceFactor = {}", node.getKey(), balanceFactor);
        if (balanceFactor == -1 || balanceFactor == 0 || balanceFactor == 1) {
            return node;
        }

        /*
        平衡的基础 有且只有三种，第一种情况下新增节点不会破坏平衡
                n
              /  \
             l    r

                n
              /
             l

                n
                 \
                  r
         */

        if (Action.ADD == action && balanceFactor == -2 && getBalanceFactor(node.getRight()) == -1) {
            /*
                n(-2)
                 \
                  r(-1 return node)
                   \
                    x
             */
            return Rotate.rr(node);
        } else if (Action.ADD == action && balanceFactor == -2 && getBalanceFactor(node.getRight()) == 1) {
            /*
                n(-2)
                 \
                  r(1)
                 /
                x (return node)
             */
            return Rotate.rl(node);
        } else if (Action.ADD == action && balanceFactor == 2 && getBalanceFactor(node.getLeft()) == 1) {
            /*
                  n(2)
                 /
                l(1 return node)
               /
              x
             */
            return Rotate.ll(node);
        } else if (Action.ADD == action && balanceFactor == 2 && getBalanceFactor(node.getLeft()) == -1) {
            /*
                  n(2)
                 /
                l(-1)
                 \
                   x (return node)
             */
            return Rotate.lr(node);
        }

        if (Action.DEL == action && balanceFactor == -2 &&
                (getBalanceFactor(node.getRight()) == -1 || getBalanceFactor(node.getRight()) == 0)) {
            return Rotate.rr(node);
        } else if (Action.DEL == action && balanceFactor == -2 && getBalanceFactor(node.getRight()) == 1) {
            return Rotate.rl(node);
        } else if (Action.DEL == action && balanceFactor == 2 &&
                (getBalanceFactor(node.getLeft()) == 1 || getBalanceFactor(node.getLeft()) == 0)) {
            return Rotate.ll(node);
        } else if (Action.DEL == action && balanceFactor == 2 && getBalanceFactor(node.getLeft()) == -1) {
            return Rotate.lr(node);
        }
        throw new IllegalStateException("Unbalanced Tree");
    }

    @Override
    public V delete(K key) {
        BstNode<K, V> targetNode = BstUtils.get(root, key);
        if (null == targetNode) {
            return null;
        }
        V value = targetNode.getValue();
        this.root = delete(this.root, key);
        return value;
    }

    // 删除逻辑的核心：寻找，替换，删除
    BstNode<K, V> delete(BstNode<K, V> node, K k) {
        if (node == null) {
            return null;
        }
        if (less(k, node.getKey())) {
            node.setLeft(delete(node.getLeft(), k));
        } else if (greater(k, node.getKey())) {
            node.setRight(delete(node.getRight(), k));
        } else {
            // 找到要删除的节点
            BstNode<K, V> left = node.getLeft();
            BstNode<K, V> right = node.getRight();
            if (left != null && right != null) {
                // 左右子树都不为空
                BstNode<K, V> rightMin = BstUtils.getMinOrMax(node.getRight(), BstUtils.Type.MIN);
                node.setKey(rightMin.getKey()).setValue(rightMin.getValue());
                node.setRight(
                        delete(node.getRight(), rightMin.getKey()));
            } else if (left == null && right == null) {
                // real delete 左右子树都为空 replace then delete
                this.count--;
                return null;
            } else if (left != null) {
                // 左子树不为空，右子树为空
                BstNode<K, V> leftMax = BstUtils.getMinOrMax(node.getLeft(), BstUtils.Type.MAX);
                node.setKey(leftMax.getKey()).setValue(leftMax.getValue());
                node.setLeft(
                        delete(node.getLeft(), leftMax.getKey())
                );
            } else {
                // 左子树为空，右子树不为空
                BstNode<K, V> rightMin = BstUtils.getMinOrMax(node.getRight(), BstUtils.Type.MIN);
                node.setKey(rightMin.getKey()).setValue(rightMin.getValue());
                node.setRight(
                        delete(node.getRight(), rightMin.getKey())
                );
            }
        }
        // 重新计算节点的 MaxDistanceToLeaf
        ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
        );
        int balanceFactor = getBalanceFactor(node);
        node = this.rebalance(node, balanceFactor, Action.DEL);
        return node;
    }

    private enum Action {
        ADD, DEL
    }

}
