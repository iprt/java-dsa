package io.intellij.dsa.graph.algo;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphAlgo;
import io.intellij.dsa.graph.Vertex;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Components
 * <p>
 * 图的联通分量
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class Components extends GraphAlgo {
    private int count;
    private final Map<Integer, Integer> visited;

    public Components(Graph graph) {
        super(graph);
        this.count = 0;
        this.visited = new HashMap<>();
        if (graph.isDirected()) {
            throw new IllegalArgumentException("Graph must be undirected");
        }
        this.compute();
    }

    @Override
    protected void reset() {
        this.count = 0;
        this.visited.clear();
    }

    public int componentsCount() {
        return count;
    }

    // 计算联通分量
    public void compute() {
        this.reset();
        List<Vertex> vertices = this.graph.getVertices();
        for (Vertex vertex : vertices) {
            if (!visited.containsKey(vertex.id())) {
                count++;
                compute(vertex);
            }
        }
    }

    // 计算联通分量，遍历
    void compute(Vertex vertex) {
        int sourceId = vertex.id();
        visited.put(sourceId, count);
        List<Edge> edges = this.graph.adjacentEdges(sourceId);

        for (Edge edge : edges) {
            Vertex target = edge.getTo();
            int targetId = target.id();
            if (!visited.containsKey(targetId)) {
                this.visited.put(targetId, count);
                this.compute(target);
            }
        }
    }

    public boolean hasPath(String source, String target) {
        if (source == null || target == null) {
            throw new IllegalArgumentException("Vertex name cannot be null");
        }

        if (graph.isEmpty()) {
            return false;
        }

        Vertex fromV = this.graph.getVertexIndex().getVertex(source);
        Vertex toV = this.graph.getVertexIndex().getVertex(target);

        if (fromV == null || toV == null) {
            return false;
        }
        return Objects.equals(visited.get(fromV.id()), visited.get(toV.id()));
    }

}
