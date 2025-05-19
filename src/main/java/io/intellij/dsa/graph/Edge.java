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
    private Vertex source;
    private Vertex target;
    private double weight;

    public Edge(Vertex source, Vertex target) {
        this(source, target, Graph.DEFAULT_UNWEIGHTED_VALUE);
    }

    public Edge(Vertex source, Vertex target, double weight) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Source and target vertices cannot be null");
        }
        this.source = source;
        this.target = target;
        this.weight = weight;
    }

    // Returns the other vertex of the edge
    public Vertex another(Vertex v) {
        if (v == null) {
            throw new IllegalArgumentException("Vertex cannot be null");
        }
        int id = v.getId();
        if (source.getId() == id) {
            return target;
        } else if (target.getId() == id) {
            return source;
        } else {
            throw new IllegalArgumentException("Vertex is not part of the edge");
        }
    }

    // Checks if two edges are the same
    public boolean same(Edge other) {
        if (other == null) {
            return false;
        }
        return this.source.same(other.source) && this.target.same(other.target);
    }

}
