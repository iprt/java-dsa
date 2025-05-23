package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    private final boolean weighted;

    public Dijkstra(Graph graph) {
        super(graph);
        this.weighted = graph.isWeighted();
    }

    public Result compute(String source) {
        return this.compute(source, null);
    }

    public Result compute(String source, Set<String> breakFilter) {
        Vertex sourceV = checkGraph().checkVertex(source, true);
        if (breakFilter != null) {
            breakFilter = breakFilter.stream()
                    .map(name -> checkVertex(name, false))
                    .filter(Objects::nonNull)
                    .map(Vertex::name)
                    .filter(name -> !name.equals(sourceV.name()))
                    .collect(Collectors.toSet());

            if (breakFilter.isEmpty()) {
                breakFilter = null;
            }
        }
        return this.compute(sourceV, breakFilter);
    }

    private Result compute(Vertex source, Set<String> breakFilter) {
        if (source == null) {
            throw new IllegalArgumentException("Source vertex cannot be null");
        }
        Result result = new Result(source, this.graph);
        PriorityQueue<EdgeWrapper> minHeap = new PriorityQueue<>(
                this.graph.adjacentEdges(source.id()).stream()
                        .map(edge -> new EdgeWrapper(edge, this.weighted)).toList()
        );
        /*
                       B
                    /    \
                  1       2
                /          \
               A --- 4 ---  C
         */
        while (!minHeap.isEmpty()) {
            EdgeWrapper shortest = minHeap.poll();

            Vertex to = shortest.getTo();
            if (result.calculateCompleted[to.id()]) {
                continue;
            }

            Vertex from = shortest.edge.getFrom();
            double totalWeight = shortest.getTotalWeight();

            if (!result.distanceToSource.containsKey(to.name())) {
                result.pathFrom.put(to.name(), from.name());
                result.distanceToSource.put(to.name(), totalWeight);
            } else {
                // 计算间的初始化
                // 存在 pathFrom ,但是 totalWeight 小于记录的 distanceToSource
                if (totalWeight < result.distanceToSource.get(to.name())) {
                    result.pathFrom.put(to.name(), from.name());
                    result.distanceToSource.put(to.name(), totalWeight);
                }
            }
            double toDistance = result.distanceToSource.get(to.name());

            // 获取to的所有边，用来更新权重
            List<EdgeWrapper> toEdges = this.graph.adjacentEdges(to.id()).stream().map(e -> new EdgeWrapper(e, this.weighted)).toList();

            for (EdgeWrapper toEdgeWrapper : toEdges) {
                double toEdgeWeight = toEdgeWrapper.getEdgeWeight();
                Vertex toto = toEdgeWrapper.getTo();

                if (result.calculateCompleted[toto.id()]) {
                    // toto 已经计算过了
                    continue;
                }

                double updatedWeight = toDistance + toEdgeWeight;

                if (!result.pathFrom.containsKey(toto.name())) {
                    // toto 还没有记录过 pathFrom
                    result.pathFrom.put(toto.name(), to.name());
                    result.distanceToSource.put(toto.name(), updatedWeight);
                    // 更新 to 的 edge 的 totalWeight
                    toEdgeWrapper.setTotalWeight(updatedWeight);
                    minHeap.add(toEdgeWrapper);
                } else {
                    // toto 记录过pathFrom
                    double oldDistance = result.distanceToSource.get(toto.name());
                    if (updatedWeight < oldDistance) {
                        result.pathFrom.put(toto.name(), to.name());
                        result.distanceToSource.put(toto.name(), updatedWeight);
                        // 更新 to 的 edge 的 totalWeight
                        toEdgeWrapper.setTotalWeight(updatedWeight);
                        minHeap.add(toEdgeWrapper);
                    }
                }
            }
            // 标记pivotal已经完全计算
            result.calculateCompleted[to.id()] = true;

            // 如果指定了目标节点，且已经计算过了，则退出
            if (canBreak(breakFilter, to.name())) {
                break;
            }

        }
        return result;
    }

    private boolean canBreak(Set<String> breakFilter, String complete) {
        if (breakFilter == null) {
            return false;
        }
        breakFilter.remove(complete);
        return breakFilter.isEmpty();
    }

    @Getter
    private static class EdgeWrapper implements Comparable<EdgeWrapper> {
        private final Vertex to;
        private final double edgeWeight;
        @Setter
        private double totalWeight;

        private final Edge edge;

        public EdgeWrapper(Edge edge, boolean weighted) {
            this.edge = edge;
            this.to = edge.getTo();
            if (weighted) {
                this.edgeWeight = edge.getWeight();
            } else {
                this.edgeWeight = Graph.DEFAULT_UNWEIGHTED_VALUE;
            }
            this.totalWeight = this.edgeWeight;
        }

        @Override
        public int compareTo(EdgeWrapper o) {
            return Double.compare(this.totalWeight, o.totalWeight);
        }
    }

    public static class Result {
        private final Vertex source;
        private final Graph graph;

        // 计算完成的节点
        private final boolean[] calculateCompleted;
        @Getter
        private final Map<String, Double> distanceToSource;
        @Getter
        private final Map<String, String> pathFrom;


        private Result(Vertex source, Graph graph) {
            this.source = source;
            this.graph = graph;

            this.calculateCompleted = new boolean[graph.verticesNum()];
            this.distanceToSource = new HashMap<>();
            this.pathFrom = new HashMap<>();

            this.init();
        }

        private void init() {
            this.calculateCompleted[source.id()] = true;
            this.distanceToSource.put(source.name(), 0.0);
        }

        public List<Edge> getRoutes(String dest) {
            if (!pathFrom.containsKey(dest) || Objects.equals(dest, source.name())) {
                return List.of();
            }
            List<Edge> reversedRoutes = new ArrayList<>();
            while (!Objects.equals(dest, source.name())) {
                String from = pathFrom.get(dest);
                Edge edge = graph.getEdge(from, dest);
                reversedRoutes.add(edge);
                dest = from;
            }
            // 反转
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
            System.out.println("\nShortest path from " + source.name() + " to " + lastEdge.getTo().name());
            System.out.println("Distance: " + distanceToSource.get(lastEdge.getTo().name()));
            System.out.print("Route:");
            String notEndFmt = " %s --%.1f-> ";
            String endFmt = "%s --%.1f-> %s";
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
