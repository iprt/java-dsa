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
        this(source, target, 0.0);
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

}
