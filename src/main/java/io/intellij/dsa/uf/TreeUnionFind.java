package io.intellij.dsa.uf;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import java.util.function.Function;

/**
 * TreeUnionFind
 *
 * @author tech@intellij.io
 * @since 2025-05-23
 */
public class TreeUnionFind<T, ID extends Comparable<ID>> implements UnionFind<T> {

    private final Function<T, ID> indentifierFunction;
    private final Map<ID, Node> storage;

    public TreeUnionFind(Function<T, ID> indentifierFunction) {
        if (indentifierFunction == null) {
            throw new IllegalArgumentException("Identifier function cannot be null");
        }
        this.indentifierFunction = indentifierFunction;
        this.storage = new TreeMap<>();
    }

    @Override
    public boolean contains(T data) {
        return doContains(data);
    }

    private boolean doContains(T data) {
        ID id = getIdentifier(data);
        if (id == null) {
            return false;
        }
        return storage.containsKey(id);
    }

    @Override
    public int getCount() {
        return storage.size();
    }

    @Override
    public boolean add(T data) {
        ID identifier = getIdentifier(data);
        if (identifier == null) {
            return false;
        }
        return doAdd(new Node(identifier), true) != null;
    }

    private Node doAdd(Node addNode, boolean check) {
        ID id = addNode.id;
        if (storage.containsKey(id)) {
            if (check) {
                return null;
            } else {
                return storage.get(id);
            }
        }
        storage.put(id, addNode);
        return addNode;
    }

    @Override
    public boolean union(T x, T y) {
        Node xNode = this.doAdd(new Node(getIdentifier(x)), false);
        Node yNode = this.doAdd(new Node(getIdentifier(y)), false);
        if (xNode == null || yNode == null) {
            return false;
        }
        this.union(xNode, yNode);
        return true;
    }

    private void union(@NotNull Node src, @NotNull Node cur) {

        Node srcParent = getParent(src);
        Node curParent = getParent(cur);

        // 已经连接
        if (idEquals(srcParent.id, curParent.id)) {
            return;
        }

        // 树压缩
        curParent.parent = srcParent;
        srcParent.children.add(curParent);

        srcParent.children.addAll(curParent.children);
        curParent.children.forEach(child -> child.parent = srcParent);
        curParent.children.clear();

    }

    private Node getParent(@NotNull Node node) {
        return node.parent == null ? node : getParent(node.parent);
    }

    @Override
    public boolean isConnected(T x, T y) {
        if (!contains(x) || !contains(y)) {
            return false;
        }
        return idEquals(getParent(this.storage.get(getIdentifier(x))).id, getParent(this.storage.get(getIdentifier(y))).id);
    }

    private ID getIdentifier(T data) {
        if (data == null) {
            return null;
        }
        return indentifierFunction.apply(data);
    }

    // 先比较 equals 再比较 compareTo
    private boolean idEquals(@NotNull ID id1, @NotNull ID id2) {
        if (id1.equals(id2)) {
            return true;
        }
        return id1.compareTo(id2) == 0;
    }

    private class Node {
        Node parent;
        ID id;
        List<Node> children;

        public Node(ID id) {
            this.id = id;
            this.parent = null;
            this.children = new ArrayList<>();
        }
    }

}
