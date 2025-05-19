package io.intellij.dsa.graph;

/**
 * Node
 *
 * @author tech@intellij.io
 * @since 2025-05-17
 */
public record Vertex(int id, String name) {
    public Vertex {
        if (id < 0) {
            throw new IllegalArgumentException("Vertex id must be non-negative");
        }
    }

    public boolean same(Vertex other) {
        if (other == null) {
            return false;
        }
        return this.id == other.id;
    }

}
