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
import java.util.List;
import java.util.PriorityQueue;

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

    public Result lazyPrim() {
        Result result = new Result();
        boolean[] visited = new boolean[this.graph.vertexIndex().size()];

        Vertex start = this.graph.vertexIndex().getVertex(0);
        PriorityQueue<Edge> minHeap = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        this.lazyPrim(start, visited, minHeap, result);
        return result;
    }

    // prim: 深度遍历 + 切分
    private void lazyPrim(Vertex vertex, boolean[] visited, PriorityQueue<Edge> minHeap, Result result) {
        visited[vertex.id()] = true;
        minHeap.addAll(this.graph.adjacentEdges(vertex.id()));

        while (!minHeap.isEmpty()) {
            Edge min = minHeap.poll();
            if (min == null) {
                return;
            }
            Vertex toV = min.getTo();

            if (!visited[toV.id()]) {
                visited[toV.id()] = true;
                result.edges.add(min);
                result.totalWeight += min.getWeight();
                // 形成切分
                this.lazyPrim(toV, visited, minHeap, result);
            }
        }

    }

    // kruskal: 最小堆 + 并查集 + 切分
    public Result kruskal() {
        Result result = new Result();
        UnionFind<Vertex> vertexUF = new IndexedUnionFind<>(Vertex::id);

        List<Edge> edges = this.graph.getEdges();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        queue.addAll(edges);

        while (!queue.isEmpty()) {
            Edge minE = queue.poll();
            Vertex fromV = minE.getFrom(), toV = minE.getTo();

            vertexUF.add(fromV);
            vertexUF.add(toV);

            if (!vertexUF.isConnected(fromV, toV)) {
                // 说明不在同一个集合中
                result.edges.add(minE);
                result.totalWeight += minE.getWeight();
                vertexUF.union(fromV, toV);
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
