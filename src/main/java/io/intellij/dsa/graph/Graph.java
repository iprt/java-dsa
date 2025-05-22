package io.intellij.dsa.graph;

import java.util.List;
import java.util.Map;

/**
 * Graph
 *
 * @author tech@intellij.io
 * @since 2025-05-17
 */
public interface Graph {
    double DEFAULT_UNWEIGHTED_VALUE = 1.0;

    default boolean isEmpty() {
        return verticesNum() == 0;
    }

    // 是否有向图
    boolean isDirected();

    // 是否加权图
    boolean isWeighted();

    // 顶点数
    int verticesNum();

    // 边数
    int edgesNum();

    // 所有的顶点
    List<Vertex> getVertices();

    default List<Edge> getEdges() {
        if (isEmpty()) {
            return List.of();
        }
        return vertexIndex().getVertices()
                .stream().map(vertex -> adjacentEdges(vertex.id()))
                .flatMap(List::stream)
                .toList();
    }

    // 获取顶点的边
    Edge getEdge(String from, String to);

    // 连接两个点
    void connect(String from, String to, double weight);

    // 连接两个点
    default void connect(String from, String to) {
        connect(from, to, DEFAULT_UNWEIGHTED_VALUE);
    }

    // 获取顶点的邻边
    List<Edge> adjacentEdges(String name);

    // 获取顶点的邻边
    List<Edge> adjacentEdges(int index);

    // 打印图
    void showGraph();

    // 获取顶点的索引
    VertexIndex vertexIndex();

    // 获取邻接矩阵
    default Double[][] getAdjacencyMatrix() {
        return null;
    }

    // 获取邻接表
    default List<Map<Integer, Double>> getAdjacencyList() {
        return null;
    }

}
