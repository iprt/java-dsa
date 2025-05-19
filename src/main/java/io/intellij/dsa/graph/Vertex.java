package io.intellij.dsa.graph;

import lombok.Data;

/**
 * Node
 *
 * @author tech@intellij.io
 * @since 2025-05-17
 */
@Data
public class Vertex {
    private int id;
    private String name;

    public Vertex(int id, String name) {
        if (id < 0) {
            throw new IllegalArgumentException("Vertex id must be non-negative");
        }
        this.id = id;
        this.name = name;
    }

    public boolean same(Vertex other) {
        if (other == null) {
            return false;
        }
        return this.id == other.id;
    }

}
