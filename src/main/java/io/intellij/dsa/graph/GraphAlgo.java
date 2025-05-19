package io.intellij.dsa.graph;

/**
 * GraphAlgo
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public abstract class GraphAlgo {
    protected final Graph graph;

    protected GraphAlgo(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
    }

    protected void reset() {
    }

}
