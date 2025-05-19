package io.intellij.dsa.graph;

import java.util.List;

/**
 * Graph
 *
 * @author tech@intellij.io
 * @since 2025-05-17
 */
public interface Graph {
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

    // 连接两个点
    void connect(String from, String to, double weight);

    // 连接两个点
    default void connect(String from, String to) {
        connect(from, to, 0.0);
    }

    // 获取顶点的邻边
    List<Edge> adjacentEdges(String vertexName);

    // 打印图
    void showGraph();

}
