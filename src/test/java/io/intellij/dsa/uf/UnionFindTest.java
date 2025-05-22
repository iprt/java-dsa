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

    @Test
    public void testIndexed() {
        UnionFind<Node> uf = new IndexedUnionFind<>(Node::id);

        Node a = new Node(1, "A");
        Node b = new Node(2, "B");
        Node c = new Node(3, "C");
        Node d = new Node(4, "D");
        Node e = new Node(5, "E");
        Node f = new Node(6, "F");

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
