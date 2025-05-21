package io.intellij.dsa.graph.algo;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphAlgo;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;

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

    public Components(Graph graph) {
        super(graph);
        checkGraph().checkDirected(false);
    }

    // 计算联通分量
    public Result compute() {
        Result result = new Result(this.graph);
        List<Vertex> vertices = this.graph.getVertices();
        for (Vertex vertex : vertices) {
            if (!result.visited.containsKey(vertex.name())) {
                result.count = result.count + 1;
                this.compute(vertex, result);
            }
        }
        return result;
    }

    // 递归计算
    void compute(Vertex vertex, Result result) {
        result.visited.put(vertex.name(), result.count);

        List<Edge> edges = this.graph.adjacentEdges(vertex.name());
        for (Edge edge : edges) {
            Vertex neighbor = edge.getTo();
            if (!result.visited.containsKey(neighbor.name())) {
                result.visited.put(neighbor.name(), result.count);
                this.compute(neighbor, result);
            }
        }
    }

    public static class Result {
        @Getter
        private final Graph graph;
        @Getter
        private int count;
        private final Map<String, Integer> visited;

        Result(Graph graph) {
            this.graph = graph;
            this.visited = new HashMap<>();
        }

        public boolean hasPath(String source, String target) {
            Integer sourceGroup = this.visited.get(source);
            Integer targetGroup = this.visited.get(target);
            if (sourceGroup == null || targetGroup == null) {
                return false;
            }
            return Objects.equals(sourceGroup, targetGroup);
        }

    }

}
