package io.intellij.dsa.graph.algo;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphAlgo;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
        checkGraph().checkDirected(false).checkWeighted(true);
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
        PriorityQueue<EdgeWrapper> queue = new PriorityQueue<>(
                this.graph.adjacentEdges(vertex.id()).stream()
                        .map(e -> new EdgeWrapper(e.getFrom().name(), e.getTo().name(), e.getWeight())).toList()
        );
        while (!queue.isEmpty()) {
            EdgeWrapper min = queue.poll();
            if (visited.contains(min.from) && visited.contains(min.to)) {
                continue;
            }
            result.edges.add(this.graph.getEdge(min.from, min.to));
            result.totalWeight += min.weight;
            visited.add(min.from);
            visited.add(min.to);
            for (Edge edge : this.graph.adjacentEdges(min.to)) {
                if (!visited.contains(edge.getTo().name())) {
                    queue.add(new EdgeWrapper(edge.getFrom().name(), edge.getTo().name(), edge.getWeight()));
                }
            }
        }
        return result;
    }

    private record EdgeWrapper(String from, String to, double weight) implements Comparable<EdgeWrapper> {
        @Override
        public int compareTo(@NotNull Mst.EdgeWrapper o) {
            return Double.compare(this.weight, o.weight);
        }
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
