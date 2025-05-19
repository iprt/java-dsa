package io.intellij.dsa.graph;

import lombok.Data;

/**
 * Edge
 *
 * @author tech@intellij.io
 * @since 2025-05-17
 */
@Data
public class Edge {
    private Vertex from;
    private Vertex to;
    private double weight;

    public Edge(Vertex from, Vertex to) {
        this(from, to, Graph.DEFAULT_UNWEIGHTED_VALUE);
    }

    public Edge(Vertex from, Vertex to, double weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Source and target vertices cannot be null");
        }
        this.from = from;
        this.to = to;
        this.weight = weight;
    }

    // Returns the other vertex of the edge
    public Vertex another(Vertex v) {
        if (v == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        int id = v.id();
        if (from.id() == id) {
            return to;
        } else if (to.id() == id) {
            return from;
        } else {
            throw new IllegalArgumentException("Vertex is not part of the edge");
        }
    }

    // Checks if two edges are the same
    public boolean same(Edge other) {
        if (other == null) {
            return false;
        }
        return this.from.same(other.from) && this.to.same(other.to);
    }

}
