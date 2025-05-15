package io.intellij.dsa.tree.bst.basic;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.bst.BST;
import io.intellij.dsa.tree.bst.BstNode;
import io.intellij.dsa.tree.bst.BstUtils;

import java.util.function.Consumer;

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
    public int height() {
        return BstUtils.getHeight(this.root);
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
            if (parent == null) {
                rtNode.setHeight(1);
            } else {
                rtNode.setHeight(parent.getHeight() + 1);
            }
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
            addNode.setHeight(1);
            this.count++;
            return;
        }
        while (tmp != null) {
            // 向左寻找
            if (less(addNode.getKey(), tmp.getKey())) {
                if (tmp.getLeft() == null) {
                    tmp.setLeft(addNode);
                    addNode.setParent(tmp);
                    addNode.setHeight(tmp.getHeight() + 1);

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
    public BstNode<K, V> delete(K key) {
        BstNode<K, V> targetNode = get(root, key);
        if (targetNode == null) {
            return null;
        }
        this.root = delete(this.root, key);
        return targetNode;
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
            from.setLeft(delete(from.getLeft(), key));
        } else if (greater(key, from.getKey())) {
            from.setRight(delete(from.getRight(), key));
        } else {
            // 找到要删除的节点 并判断左右子树的情况
            // 左子树和右子树都不为空
            if (from.getLeft() != null && from.getRight() == null) {
                // 寻找左子树最大节点
                BstNode<K, V> leftMax = getMinOrMax(from.getLeft(), false);
                from.setKey(leftMax.getKey())
                        .setValue(leftMax.getValue());
                // 删除 左子树最大节点
                from.setLeft(
                        delete(from.getLeft(), leftMax.getKey())
                );
            } else if (from.getLeft() == null && from.getRight() == null) {
                // 左子树和右子树都为空
                // 理解: 真正的删除操作就是替换和删除叶子节点
                from = null;
                this.count--;
            } else if (from.getLeft() != null) {
                // 左子树不为空，右子树为空
                BstNode<K, V> leftMax = getMinOrMax(from.getLeft(), false);
                from.setKey(leftMax.getKey())
                        .setValue(leftMax.getValue());
                // 删除 左子树最大节点
                from.setLeft(
                        delete(from.getLeft(), leftMax.getKey())
                );
            } else {
                // 左子树为空，右子树不为空
                BstNode<K, V> rightMin = getMinOrMax(from.getRight(), true);
                from.setKey(rightMin.getKey())
                        .setValue(rightMin.getValue());
                // 删除 右子树最小节点
                from.setRight(
                        delete(from.getRight(), rightMin.getKey())
                );
            }
        }
        // 返回节点本身
        return from;
    }

    @Override
    public boolean update(K key, V value) {
        BstNode<K, V> find = get(root, key);
        if (find == null) {
            return false;
        }
        find.setValue(value);
        return true;
    }

    @Override
    public V get(K key) {
        return null;
    }

    BstNode<K, V> get(BstNode<K, V> from, K key) {
        if (from == null) {
            return null;
        }

        if (DSAUtils.equals(from.getKey(), key)) {
            return from;
        } else if (less(key, from.getKey())) {
            return get(from.getLeft(), key);
        } else {
            return get(from.getRight(), key);
        }
    }

    @Override
    public BstNode<K, V> getMin() {
        return getMinOrMax(this.root, true);
    }

    @Override
    public BstNode<K, V> getMax() {
        return getMinOrMax(this.root, false);
    }

    private BstNode<K, V> getMinOrMax(BstNode<K, V> node, boolean minOrMax) {
        if (node == null) {
            return null;
        }
        BstNode<K, V> tmp = node;
        while (tmp != null) {
            if (minOrMax) {
                if (tmp.getLeft() != null) {
                    tmp = tmp.getLeft();
                } else {
                    break;
                }
            } else {
                if (tmp.getRight() != null) {
                    tmp = tmp.getRight();
                } else {
                    break;
                }
            }
        }
        return tmp;
    }

    @Override
    public boolean isBST() {
        return BstUtils.isBST(this.root);
    }

    @Override
    public void preorderTraversal(Consumer<BstNode<K, V>> action) {
        if (action == null) {
            return;
        }
        BstUtils.preorderTraversal(action, this.root);
    }

    @Override
    public void inorderTraversal(Consumer<BstNode<K, V>> action) {
        if (action == null) {
            return;
        }
        BstUtils.inorderTraversal(action, this.root);
    }

    @Override
    public void postorderTraversal(Consumer<BstNode<K, V>> action) {
        if (action == null) {
            return;
        }
        BstUtils.postorderTraversal(action, this.root);
    }

    @Override
    public void levelOrderTraversal(Consumer<BstNode<K, V>> action) {
        if (action == null) {
            return;
        }
        BstUtils.levelOrderTraversal(action, this.root);
    }
}
