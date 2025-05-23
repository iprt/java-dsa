package io.intellij.dsa.uf;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * UnionFindTest
 *
 * @author tech@intellij.io
 * @since 2025-05-22
 */
public class UnionFindTest {
    final Node a = new Node(1, "A");
    final Node b = new Node(2, "B");
    final Node c = new Node(3, "C");
    final Node d = new Node(4, "D");
    final Node e = new Node(5, "E");
    final Node f = new Node(6, "F");

    @Test
    public void testIndexedUF() {
        UnionFind<Node> uf = new IndexedUnionFind<>(Node::id);

        uf.union(a, b);
        uf.union(b, c);

        uf.union(d, e);
        uf.union(e, f);

        Assertions.assertTrue(uf.isConnected(a, c));
        Assertions.assertFalse(uf.isConnected(a, d));

        uf.union(a, f);
        Assertions.assertTrue(uf.isConnected(c, d));
    }

    @Test
    public void testTreeUF() {
        UnionFind<Node> uf = new TreeUnionFind<>(Node::name);

        uf.union(a, b);
        uf.union(b, c);

        uf.union(d, e);
        uf.union(e, f);

        Assertions.assertTrue(uf.isConnected(a, c));
        Assertions.assertFalse(uf.isConnected(a, d));

        uf.union(a, f);
        Assertions.assertTrue(uf.isConnected(c, d));
    }

    private record Node(int id, String name) {
    }

}
