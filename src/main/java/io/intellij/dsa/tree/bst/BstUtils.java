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

    public static <K extends Comparable<K>, V> int getNodeHeight(BstNode<K, V> node) {
        if (node == null) {
            return BstNode.DEFAULT_HEIGHT - 1;
        }
        return node.getHeight();
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
        int rows = height * 2;
        int cols = (int) Math.pow(2, height + 1) - 1;

        String[][] matrix = new String[rows][cols];

        // 初始化矩阵
        for (String[] row : matrix) {
            java.util.Arrays.fill(row, " ");
        }

        // 填充矩阵
        fillMatrixSimplified(matrix, root, 0, 0, cols - 1);

        // 打印矩阵，跳过全空行
        for (String[] row : matrix) {
            boolean hasContent = false;
            for (String cell : row) {
                if (!cell.equals(" ")) {
                    hasContent = true;
                    break;
                }
            }

            if (hasContent) {
                for (String cell : row) {
                    System.out.print(cell);
                }
                System.out.println();
            }
        }
    }

    private static <K extends Comparable<K>, V> void fillMatrixSimplified(String[][] matrix,
                                                                          BstNode<K, V> node,
                                                                          int row,
                                                                          int left,
                                                                          int right) {
        if (node == null || row >= matrix.length) {
            return;
        }

        int mid = (left + right) / 2;

        // 放置当前节点
        if (mid >= 0 && mid < matrix[0].length) {
            matrix[row][mid] = String.valueOf(node.getKey());
        }

        // 间隔行用于放置连接线
        int nextRow = row + 2;

        // 处理左子树
        if (node.getLeft() != null) {
            // 计算左子节点的位置
            int leftChildMid = (left + mid - 1) / 2;

            // 绘制简化连接线 (只在中间位置放一个/)
            if (row + 1 < matrix.length) {
                int connectorPos = (mid + leftChildMid) / 2;
                if (connectorPos >= 0 && connectorPos < matrix[0].length) {
                    matrix[row + 1][connectorPos] = "/";
                }
            }

            // 递归填充左子树
            fillMatrixSimplified(matrix, node.getLeft(), nextRow, left, mid - 1);
        }

        // 处理右子树
        if (node.getRight() != null) {
            // 计算右子节点的位置
            int rightChildMid = (mid + 1 + right) / 2;

            // 绘制简化连接线 (只在中间位置放一个\)
            if (row + 1 < matrix.length) {
                int connectorPos = (mid + rightChildMid) / 2;
                if (connectorPos >= 0 && connectorPos < matrix[0].length) {
                    matrix[row + 1][connectorPos] = "\\";
                }
            }

            // 递归填充右子树
            fillMatrixSimplified(matrix, node.getRight(), nextRow, mid + 1, right);
        }
    }
}
