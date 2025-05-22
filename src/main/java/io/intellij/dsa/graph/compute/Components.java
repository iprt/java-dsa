package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;
import io.intellij.dsa.uf.IndexedUnionFind;
import io.intellij.dsa.uf.UnionFind;
import lombok.Getter;

import java.util.List;

/**
 * Components
 * <p>
 * 图的联通分量
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class Components extends GraphCompute {

    public Components(Graph graph) {
        super(graph);
        checkGraph().checkDirected(false);
    }

    // 计算联通分量
    public Result compute() {
        Result result = new Result(this.graph);
        List<Vertex> vertices = this.graph.getVertices();
        for (Vertex vertex : vertices) {
            if (!result.visited.contains(vertex)) {
                result.count = result.count + 1;
                this.compute(vertex, result);
            }
        }
        return result;
    }

    // 递归计算
    void compute(Vertex vertex, Result result) {
        // result.visited.put(vertex.name(), result.count);
        result.visited.add(vertex);
        for (Edge edge : this.graph.adjacentEdges(vertex.name())) {
            Vertex next = edge.getTo();
            if (!result.visited.contains(next)) {
                this.compute(next, result);
            }
            result.visited.union(vertex, next);
        }
    }

    public static class Result {
        private final Graph graph;

        @Getter
        private int count;
        // private final Map<String, Integer> visited;
        private final UnionFind<Vertex> visited;

        private Result(Graph graph) {
            this.graph = graph;
            this.visited = new IndexedUnionFind<>(Vertex::id);
        }

        public boolean hasPath(String source, String target) {
            Vertex sourceV = this.graph.vertexIndex().getVertex(source);
            Vertex targetV = this.graph.vertexIndex().getVertex(target);

            if (sourceV == null || targetV == null) {
                return false;
            }
            return this.visited.isConnected(sourceV, targetV);
        }

    }

}
