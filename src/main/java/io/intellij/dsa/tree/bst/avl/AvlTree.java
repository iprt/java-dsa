package io.intellij.dsa.tree.bst.avl;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.tree.bst.BST;
import io.intellij.dsa.tree.bst.BstNode;
import io.intellij.dsa.tree.bst.BstUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.function.Consumer;

import static io.intellij.dsa.DSAUtils.less;
import static io.intellij.dsa.tree.bst.avl.AvlNode.DEFAULT_MAX_DISTANCE_TO_LEAF;

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
    public int height() {
        return BstUtils.getHeight(this.root);
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
            if (parent == null) {
                rtNode.setHeight(1);
            } else {
                rtNode.setHeight(parent.getHeight() + 1);
            }
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
        return this.rebalance(node, balanceFactor);
    }

    int getNodeMaxDistanceToLeaf(BstNode<K, V> node) {
        if (node == null) {
            return DEFAULT_MAX_DISTANCE_TO_LEAF - 1;
        }
        return ((AvlNode<K, V>) node).getMaxDistanceToLeaf();
    }

    // 计算平衡因子，左右高度差
    int getBalanceFactor(BstNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        return getNodeMaxDistanceToLeaf(node.getLeft()) - getNodeMaxDistanceToLeaf(node.getRight());
    }

    // 重新平衡，返回平衡后的节点
    BstNode<K, V> rebalance(BstNode<K, V> node, int balanceFactor) {
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
        if (balanceFactor == -2 && getBalanceFactor(node.getRight()) == -1) {
            /*
                n(-2)
                 \
                  r(-1 return node)
                   \
                    x
             */
            BstNode<K, V> r = node.getRight();
            BstNode<K, V> rl = r.getLeft();

            if (rl != null) {
                // break n and r
                rl.setParent(node);
            }
            node.setRight(rl);

            r.setParent(node.getParent());
            r.setLeft(node);
            node.setParent(r);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
            );
            ((AvlNode<K, V>) r).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(r.getLeft()), getNodeMaxDistanceToLeaf(r.getRight())) + 1
            );

            // 距离根节点的距离 -1
            r.setHeight(r.getHeight() - 1);
            this.updateHeight(r, null);
            return r;
        } else if (balanceFactor == -2 && getBalanceFactor(node.getRight()) == 1) {
            /*
                n(-2)
                 \
                  r(1)
                 /
                x (return node)
             */
            BstNode<K, V> r = node.getRight();
            BstNode<K, V> x = r.getLeft();
            BstNode<K, V> xr = x.getRight();

            // 先处理  r 和 x 的旋转
            if (xr != null) {
                xr.setParent(r);
            }
            r.setLeft(xr);

            x.setParent(node);
            node.setRight(x);
            r.setParent(x);
            x.setRight(r);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) r).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(r.getLeft()), getNodeMaxDistanceToLeaf(r.getRight())) + 1
            );
            ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
            );


            // 再处理 n 和 x
            BstNode<K, V> xl = x.getLeft();

            if (xl != null) {
                xl.setParent(node);
            }
            node.setRight(xl);

            x.setParent(node.getParent());
            x.setLeft(node);
            node.setParent(x);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
            );
            ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
            );

            x.setHeight(x.getHeight() - 2);
            updateHeight(x, null);

            return x;
        } else if (balanceFactor == 2 && getBalanceFactor(node.getLeft()) == 1) {
            /*
                  n(2)
                 /
                l(1 return node)
               /
              x
             */
            BstNode<K, V> l = node.getLeft();
            BstNode<K, V> lr = l.getRight();

            if (lr != null) {
                lr.setParent(node);
            }
            node.setLeft(lr);

            l.setParent(node.getParent());
            l.setRight(node);
            node.setParent(l);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
            );
            ((AvlNode<K, V>) l).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(l.getLeft()), getNodeMaxDistanceToLeaf(l.getRight())) + 1
            );

            l.setHeight(l.getHeight() - 1);

            updateHeight(l, null);
            return l;
        } else if (balanceFactor == 2 && getBalanceFactor(node.getLeft()) == -1) {
            /*
                  n(2)
                 /
                l(-1)
                 \
                   x (return node)
             */
            BstNode<K, V> l = node.getLeft();
            BstNode<K, V> x = l.getRight();
            BstNode<K, V> xl = x.getLeft();
            // 先处理 x 和 l 的旋转
            if (xl != null) {
                xl.setParent(l);
            }
            l.setRight(xl);

            x.setParent(node);
            node.setLeft(x);
            x.setLeft(l);
            l.setParent(x);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) l).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(l.getLeft()), getNodeMaxDistanceToLeaf(l.getRight())) + 1
            );
            ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
            );

            // 再处理x和n
            BstNode<K, V> xr = x.getRight();
            if (xr != null) {
                xr.setParent(node);
            }
            node.setLeft(xr);

            x.setParent(node.getParent());
            x.setRight(node);
            node.setParent(x);

            // 更新最大距离，从下往上更新
            ((AvlNode<K, V>) node).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(node.getLeft()), getNodeMaxDistanceToLeaf(node.getRight())) + 1
            );
            ((AvlNode<K, V>) x).setMaxDistanceToLeaf(
                    Math.max(getNodeMaxDistanceToLeaf(x.getLeft()), getNodeMaxDistanceToLeaf(x.getRight())) + 1
            );

            x.setHeight(x.getHeight() - 2);
            updateHeight(x, null);
            return x;
        }
        throw new IllegalStateException("Unbalanced Tree");
    }

    private void updateHeight(BstNode<K, V> node, BstNode<K, V> parent) {
        if (node == null) {
            return;
        }
        if (parent != null) {
            node.setHeight(parent.getHeight() + 1);
        }
        updateHeight(node.getLeft(), node);
        updateHeight(node.getRight(), node);
    }

    @Override
    public BstNode<K, V> delete(K key) {
        return null;
    }

    @Override
    public boolean update(K key, V value) {
        BstNode<K, V> find = BstUtils.get(this.root, key);
        if (find == null) {
            return false;
        }
        find.setValue(value);
        return true;
    }

    @Override
    public V get(K key) {
        BstNode<K, V> find = BstUtils.get(this.root, key);
        return find == null ? null : find.getValue();
    }

    @Override
    public BstNode<K, V> getMin() {
        return BstUtils.getMinOrMax(this.root, true);
    }

    @Override
    public BstNode<K, V> getMax() {
        return BstUtils.getMinOrMax(this.root, false);
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
