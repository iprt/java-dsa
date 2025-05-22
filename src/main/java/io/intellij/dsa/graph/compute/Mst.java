package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;
import io.intellij.dsa.uf.IndexedUnionFind;
import io.intellij.dsa.uf.UnionFind;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Mst 最小生成树
 *
 * @author tech@intellij.io
 */
public class Mst extends GraphCompute {

    public Mst(Graph graph) {
        super(graph);
        check();
    }

    public Result prim() {
        Vertex start = this.graph.vertexIndex().getVertex(0);
        return this.prim(start, new HashSet<>());
    }

    // prim: 广度遍历 + 切分
    private Result prim(Vertex vertex, Set<String> visited) {
        Result result = new Result();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        queue.addAll(this.graph.adjacentEdges(vertex.name()));

        while (!queue.isEmpty()) {
            Edge min = queue.poll();
            String fromName = min.getFrom().name();
            String toName = min.getTo().name();
            if (visited.contains(fromName) && visited.contains(min.getTo().name())) {
                continue;
            }
            result.edges.add(this.graph.getEdge(fromName, toName));
            result.totalWeight += min.getWeight();
            visited.add(fromName);
            visited.add(toName);
            for (Edge edge : this.graph.adjacentEdges(toName)) {
                if (!visited.contains(edge.getTo().name())) {
                    queue.add(edge);
                }
            }
        }
        return result;
    }

    // kruskal: 堆 + 并查集 + 切分
    public Result kruskal() {
        Result result = new Result();
        UnionFind<Vertex> vertexUF = new IndexedUnionFind<>(Vertex::id);

        List<Edge> edges = this.graph.getEdges();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        queue.addAll(edges);

        while (!queue.isEmpty()) {
            Edge minE = queue.poll();
            Vertex fromV = minE.getFrom(), toV = minE.getTo();

            boolean containsFrom = vertexUF.contains(fromV);
            boolean containsTo = vertexUF.contains(toV);

            if (!containsFrom && !containsTo) {
                // 这条边第一次被访问
                vertexUF.union(fromV, toV);
                result.edges.add(minE);
                result.totalWeight += minE.getWeight();
            } else {
                if (containsFrom && !containsTo) {
                    // 其中有一个顶点被访问过,说明在同一个集合中
                    vertexUF.union(fromV, toV);
                    result.edges.add(minE);
                    result.totalWeight += minE.getWeight();
                } else if (!containsFrom) {
                    // 其中有一个顶点被访问过,说明在同一个集合中
                    vertexUF.union(toV, fromV);
                    result.edges.add(minE);
                    result.totalWeight += minE.getWeight();
                } else {
                    // 判断两个顶点是不是切分的两边
                    if (!vertexUF.isConnected(fromV, toV)) {
                        // 说明不在同一个集合中
                        result.edges.add(minE);
                        result.totalWeight += minE.getWeight();

                        vertexUF.union(fromV, toV);
                    }
                }
            }

        }
        return result;
    }

    public static class Result {
        @Getter
        private final List<Edge> edges;

        @Getter
        private double totalWeight;

        public Result() {
            this.edges = new ArrayList<>();
            this.totalWeight = 0;
        }
    }

    private void check() {
        checkGraph().checkDirected(false).checkWeighted(true);
        if (new Components(this.graph).compute().getCount() > 1) {
            throw new IllegalArgumentException("Graph is not connected");
        }
    }


}
