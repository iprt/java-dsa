package io.intellij.dsa.graph;

import org.apache.commons.lang3.StringUtils;

/**
 * GraphAlgo
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public abstract class GraphCompute {
    protected Graph graph;

    public GraphCompute(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
    }

    public void setGraph(Graph graph) {
        if (graph == null) {
            throw new IllegalArgumentException("Graph cannot be null");
        }
        this.graph = graph;
    }

    public GraphCompute checkGraph() {
        if (graph.isEmpty()) {
            throw new IllegalArgumentException("Graph is empty");
        }
        return this;
    }

    public GraphCompute checkDirected(boolean expectedDirected) {
        if (graph.isDirected() != expectedDirected) {
            throw new IllegalArgumentException("Graph must be " + (expectedDirected ? "directed" : "undirected"));
        }
        return this;
    }

    public GraphCompute checkWeighted(boolean expectedWeighted) {
        if (graph.isWeighted() != expectedWeighted) {
            throw new IllegalArgumentException("Graph must be " + (expectedWeighted ? "weighted" : "unweighted"));
        }
        return this;
    }

    public Vertex checkVertex(String vertexName, boolean throwOrReturn) {
        if (StringUtils.isBlank(vertexName) && throwOrReturn) {
            throw new IllegalArgumentException("Vertex name cannot be null or empty");
        }
        Vertex vertex = graph.vertexIndex().getVertex(vertexName);
        if (vertex == null && throwOrReturn) {
            throw new IllegalArgumentException("Vertex not found in graph");
        }
        return vertex;
    }

    public void reset() {
    }

}
