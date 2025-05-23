package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Dijkstra
 * <p>
 * 单源最短路径算法，无负权重边
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class Dijkstra extends GraphCompute {

    public Dijkstra(Graph graph) {
        super(graph);
        checkGraph();
    }

    public Result compute(String source) {
        return this.compute(source, null);
    }

    public Result compute(String source, Set<String> brokenFilter) {
        Vertex sourceV = checkGraph().checkVertex(source, true);
        if (brokenFilter != null) {
            brokenFilter = brokenFilter.stream()
                    .map(name -> checkVertex(name, false))
                    .filter(Objects::nonNull)
                    .map(Vertex::name)
                    .filter(name -> !name.equals(sourceV.name()))
                    .collect(Collectors.toSet());
            if (brokenFilter.isEmpty()) {
                brokenFilter = null;
            }
        }

        Result record = new Result(sourceV, this.graph);
        // 局部最优 更新到 全局最优
        PriorityQueue<TotalWeight> minHeap = this.graph.adjacentEdges(sourceV.id()).stream()
                .map(edge -> new TotalWeight(edge, this.graph.isWeighted() ? edge.getWeight() : Graph.DEFAULT_UNWEIGHTED_VALUE))
                .peek(tw -> {
                    int otherVertexId = tw.edge.getTo().id();
                    record.dynamicDistanceToSource[otherVertexId] = tw.totalWeight;
                    record.dynamicPathFrom[otherVertexId] = tw.edge;
                })
                .collect(Collectors.toCollection(
                        () -> new PriorityQueue<>(Comparator.comparingDouble(TotalWeight::totalWeight)))
                );

        record.calculateCompleted[sourceV.id()] = true;
        record.dynamicDistanceToSource[sourceV.id()] = 0.0;
        record.dynamicPathFrom[sourceV.id()] = null;
        while (!minHeap.isEmpty()) {
            TotalWeight min = minHeap.poll();
            if (!this.compute(min, minHeap, record, brokenFilter)) {
                break;
            }
        }
        return record;
    }

    private boolean compute(TotalWeight min, PriorityQueue<TotalWeight> minHeap, Result record, Set<String> brokenFilter) {
        boolean[] completed = record.calculateCompleted;
        Double[] dts = record.dynamicDistanceToSource;
        Edge[] pathFrom = record.dynamicPathFrom;

        Vertex toV = min.edge().getTo();
        if (completed[toV.id()]) {
            return true;
        }

        Double toW = dts[toV.id()];
        List<Edge> updatedEdges = this.graph.adjacentEdges(toV.id());
        for (Edge updatedEdge : updatedEdges) {
            Vertex toto = updatedEdge.getTo();
            if (completed[toto.id()]) {
                continue;
            }
            double updatedWeight = updatedEdge.getWeight() + toW;

            Double totoDis = dts[toto.id()];
            if (totoDis == null) {
                // 没有到达过
                dts[toto.id()] = updatedWeight;
                pathFrom[toto.id()] = updatedEdge;
                minHeap.add(new TotalWeight(updatedEdge, updatedWeight));
            } else if (updatedWeight < totoDis) {
                // 更新最短路径
                dts[toto.id()] = updatedWeight;
                pathFrom[toto.id()] = updatedEdge;
                minHeap.add(new TotalWeight(updatedEdge, updatedWeight));
            }

        }
        completed[toV.id()] = true;
        return !canBeBroken(brokenFilter, toV.name());
    }

    private boolean canBeBroken(Set<String> breakFilter, String complete) {
        if (breakFilter == null) {
            return false;
        }
        breakFilter.remove(complete);
        return breakFilter.isEmpty();
    }

    private record TotalWeight(Edge edge, double totalWeight) {
    }

    public static class Result {
        private final Vertex source;
        private final Graph graph;

        private final boolean[] calculateCompleted;
        // 优化策略，索引最小堆
        private final Double[] dynamicDistanceToSource;
        private final Edge[] dynamicPathFrom;

        private Result(Vertex source, Graph graph) {
            this.source = source;
            this.graph = graph;

            this.calculateCompleted = new boolean[graph.getVerticesNum()];
            this.dynamicDistanceToSource = new Double[graph.getVerticesNum()];
            this.dynamicPathFrom = new Edge[graph.getVerticesNum()];
        }

        public List<Edge> getRoutes(String destName) {
            Vertex destV = this.graph.vertexIndex().getVertex(destName);
            if (destV == null || destV.id() >= this.dynamicPathFrom.length) {
                return List.of();
            }

            List<Edge> reversedRoutes = new ArrayList<>();
            while (!this.source.name().equals(destV.name())) {
                Edge edge = this.dynamicPathFrom[destV.id()];
                reversedRoutes.add(edge);
                destV = edge.getFrom();
            }

            List<Edge> routes = new ArrayList<>();
            for (int i = reversedRoutes.size() - 1; i >= 0; i--) {
                routes.add(reversedRoutes.get(i));
            }
            return routes;
        }

        public void printRoutes(List<Edge> edges) {
            if (edges == null || edges.isEmpty()) {
                System.out.println("No route found");
                return;
            }
            Edge lastEdge = edges.get(edges.size() - 1);

            String titleFmt = """
                    Shortest Path:
                      source: [%s] target: [%s]
                    """;

            System.out.printf(titleFmt, this.source.name(), lastEdge.getTo().name());
            System.out.println("Distance: " + dynamicDistanceToSource[lastEdge.getTo().id()] +
                    " = " +
                    edges.stream().map(Edge::getWeight).map(String::valueOf).collect(Collectors.joining(" + ")));
            System.out.print("Route:");
            String notEndFmt = " [%s] --%.1f-> ";
            String endFmt = "[%s] --%.1f-> [%s]";
            for (int i = 0; i < edges.size(); i++) {
                // A --1.0-> B --2.0-> C
                Edge edge = edges.get(i);
                String from = edge.getFrom().name();
                if (i == edges.size() - 1) {
                    String to = edge.getTo().name();
                    System.out.printf(endFmt, from, edge.getWeight(), to);
                    System.out.println();
                } else {
                    System.out.printf(notEndFmt, from, edge.getWeight());
                }
            }
            System.out.println();
        }

    }

}
