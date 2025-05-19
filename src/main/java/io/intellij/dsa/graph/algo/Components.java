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
        this.compute();
    }

    @Override
    public void reset() {
        this.count = 0;
        this.visited.clear();
    }

    public int componentsCount() {
        return count;
    }

    // 计算联通分量
    public void compute() {
        checkGraph().checkDirected(false).reset();
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
        int fromId = vertex.id();
        visited.put(fromId, count);

        List<Edge> edges = this.graph.adjacentEdges(fromId);
        for (Edge edge : edges) {
            Vertex toV = edge.getTo();
            int toId = toV.id();
            if (!visited.containsKey(toId)) {
                this.visited.put(toId, count);
                this.compute(toV);
            }
        }
    }

    public boolean hasPath(String source, String target) {
        Vertex fromV = checkGraph().checkVertex(source, false);
        Vertex toV = checkVertex(target, false);
        if (fromV == null || toV == null) {
            return false;
        }
        return Objects.equals(visited.get(fromV.id()), visited.get(toV.id()));
    }

}
