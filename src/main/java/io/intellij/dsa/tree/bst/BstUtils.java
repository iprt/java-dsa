package io.intellij.dsa.tree.bst;

import io.intellij.dsa.DSAUtils;
import org.jetbrains.annotations.NotNull;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.function.BiConsumer;

import static io.intellij.dsa.DSAUtils.greater;
import static io.intellij.dsa.DSAUtils.less;

/**
 * BstUtils
 *
 * @author tech@intellij.io
 * @since 2025-05-15
 */
public class BstUtils {

    public static <K extends Comparable<K>, V> BstNode<K, V> get(BstNode<K, V> from, K key) {
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

    public static <K extends Comparable<K>, V> BstNode<K, V> getMinOrMax(BstNode<K, V> node, @NotNull BstUtils.Type type) {
        if (node == null) {
            return null;
        }
        BstNode<K, V> tmp = node;
        while (tmp != null) {
            if (type == Type.MIN) {
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

    public enum Type {
        MIN, MAX
    }

    public static <K extends Comparable<K>, V> boolean isBST(BstNode<K, V> node) {
        if (node == null) {
            return true;
        }
        BstNode<K, V> left = node.getLeft();
        BstNode<K, V> right = node.getRight();
        if (left != null && greater(left.getKey(), node.getKey())) {
            return false;
        }
        if (right != null && less(right.getKey(), node.getKey())) {
            return false;
        }
        return isBST(left) && isBST(right);
    }

    public static <K extends Comparable<K>, V> void preorderTraversal(BiConsumer<K, V> action, BstNode<K, V> node) {
        if (action == null || node == null) {
            return;
        }
        action.accept(node.getKey(), node.getValue());
        preorderTraversal(action, node.getLeft());
        preorderTraversal(action, node.getRight());
    }


    public static <K extends Comparable<K>, V> void inorderTraversal(BiConsumer<K, V> action, BstNode<K, V> node) {
        if (action == null || node == null) {
            return;
        }
        inorderTraversal(action, node.getLeft());
        action.accept(node.getKey(), node.getValue());
        inorderTraversal(action, node.getRight());
    }

    public static <K extends Comparable<K>, V> void postorderTraversal(BiConsumer<K, V> action, BstNode<K, V> node) {
        if (action == null || node == null) {
            return;
        }
        postorderTraversal(action, node.getLeft());
        postorderTraversal(action, node.getRight());
        action.accept(node.getKey(), node.getValue());
    }

    public static <K extends Comparable<K>, V> void levelOrderTraversal(BiConsumer<K, V> action, BstNode<K, V> node) {
        if (action == null || node == null) {
            return;
        }
        Queue<BstNode<K, V>> queue = new LinkedList<>();
        queue.add(node);
        while (!queue.isEmpty()) {
            BstNode<K, V> current = queue.poll();
            action.accept(current.getKey(), current.getValue());
            if (current.getLeft() != null) {
                queue.add(current.getLeft());
            }
            if (current.getRight() != null) {
                queue.add(current.getRight());
            }
        }
    }

    public static <K extends Comparable<K>, V> void getLeafs(BstNode<K, V> node, List<BstNode<K, V>> leafs) {
        if (node == null) {
            return;
        }
        if (node.getLeft() == null && node.getRight() == null) {
            leafs.add(node);
        }
        getLeafs(node.getLeft(), leafs);
        getLeafs(node.getRight(), leafs);
    }

    public static <K extends Comparable<K>, V> int getHeight(BstNode<K, V> node) {
        if (node == null) {
            return 0;
        }
        int leftHeight = getHeight(node.getLeft()) + 1;
        int rightHeight = getHeight(node.getRight()) + 1;
        return Math.max(leftHeight, rightHeight);
    }


    public static <K extends Comparable<K>, V> void printTree(BstNode<K, V> root) {
        if (root == null) {
            System.out.println("空树");
            return;
        }

        int height = getHeight(root);
        int width = (int) Math.pow(2, height) - 1;
        String[][] matrix = new String[height * 2 - 1][width];

        // 初始化矩阵
        for (String[] row : matrix) {
            java.util.Arrays.fill(row, " ");
        }

        fillMatrix(matrix, root, 0, 0, width - 1);

        // 打印矩阵
        for (String[] row : matrix) {
            for (String cell : row) {
                System.out.print(cell);
            }
            System.out.println();
        }
    }

    private static <K extends Comparable<K>, V> void fillMatrix(String[][] matrix, BstNode<K, V> node,
                                                                int row, int left, int right) {
        if (node == null) {
            return;
        }

        int mid = (left + right) / 2;

        // 将节点值放入矩阵
        matrix[row][mid] = String.valueOf(node.getKey());

        // 处理左子树
        if (node.getLeft() != null) {
            // 绘制连接线
            for (int i = 1; i <= (mid - left) / 2; i++) {
                matrix[row + i][mid - i] = "/";
            }
            fillMatrix(matrix, node.getLeft(), row + (mid - left) / 2 + 1, left, mid - 1);
        }

        // 处理右子树
        if (node.getRight() != null) {
            // 绘制连接线
            for (int i = 1; i <= (right - mid) / 2; i++) {
                matrix[row + i][mid + i] = "\\";
            }
            fillMatrix(matrix, node.getRight(), row + (right - mid) / 2 + 1, mid + 1, right);
        }
    }

}
