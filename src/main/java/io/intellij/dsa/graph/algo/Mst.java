package io.intellij.dsa.graph.algo;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphAlgo;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * Mst 最小生成树
 *
 * @author tech@intellij.io
 */
public class Mst extends GraphAlgo {

    public Mst(Graph graph) {
        super(graph);
        check();
    }

    private void check() {
        checkGraphNotEmpty().checkDirected(false).checkWeighted(true);
        Components components = new Components(this.graph);
        if (components.count() > 1) {
            throw new IllegalArgumentException("Graph is not connected");
        }
    }

    public Result prim() {
        return this.prim(this.graph.getVertexIndex().getVertex(0), new HashSet<>());
    }

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

    // kruskal 算法，切分和并查集
    public Result kruskal() {
        Result result = new Result();
        Map<String, Integer> visitedUf = new HashMap<>();
        List<Edge> edges = this.graph.getEdges();
        PriorityQueue<Edge> queue = new PriorityQueue<>(Comparator.comparingDouble(Edge::getWeight));
        queue.addAll(edges);

        int index = 0;
        while (!queue.isEmpty()) {
            Edge min = queue.poll();
            String from = min.getFrom().name();
            String to = min.getTo().name();

            Integer fromUF = visitedUf.get(from);
            Integer toUF = visitedUf.get(to);

            if (fromUF == null && toUF == null) {
                index++;
                // 这条边第一次被访问
                visitedUf.put(from, index);
                visitedUf.put(to, index);
                result.edges.add(min);
                result.totalWeight += min.getWeight();
            } else {
                if (fromUF != null && toUF == null) {
                    // 其中有一个顶点被访问过,说明在同一个集合中
                    visitedUf.put(to, fromUF);
                    result.edges.add(min);
                    result.totalWeight += min.getWeight();
                } else if (fromUF == null) {
                    // 其中有一个顶点被访问过,说明在同一个集合中
                    visitedUf.put(from, toUF);
                    result.edges.add(min);
                    result.totalWeight += min.getWeight();
                } else {
                    // 判断两个顶点是不是切分的两边
                    if (!fromUF.equals(toUF)) {
                        // 说明不在同一个集合中
                        result.edges.add(min);
                        result.totalWeight += min.getWeight();
                        for (Map.Entry<String, Integer> entry : visitedUf.entrySet()) {
                            if (Objects.equals(entry.getValue(), toUF)) {
                                // 更新集合
                                visitedUf.put(entry.getKey(), fromUF);
                            }
                        }
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

}
