package io.intellij.dsa.graph.impl;

import io.intellij.dsa.DSAUtils;
import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.Vertex;
import io.intellij.dsa.graph.VertexIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * DenseGraph 稠密图
 * <p>
 * 稠密图是指边数接近于顶点数的平方的图，通常用邻接矩阵表示。
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
@Slf4j
public class DenseGraph implements Graph {
    private final VertexIndex vertexIndex = new VertexIndex();
    private final boolean directed;
    private final boolean weighted;

    // 邻接矩阵
    private Double[][] adjacencyMatrix;

    private int edgesCount;

    public DenseGraph(boolean directed, boolean weighted) {
        this.directed = directed;
        this.weighted = weighted;
        // 初始化邻接矩阵，后续会根据顶点数动态调整大小，一张图理论上最少2个点
        this.adjacencyMatrix = new Double[2][2];
        this.edgesCount = 0;
    }

    @Override
    public boolean isDirected() {
        return this.directed;
    }

    @Override
    public boolean isWeighted() {
        return this.weighted;
    }

    @Override
    public int verticesNum() {
        return vertexIndex.size();
    }

    @Override
    public int edgesNum() {
        return this.edgesCount;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertexIndex.getVertices();
    }


    @Override
    public void connect(String from, String to, double weight) {
        if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            throw new IllegalArgumentException("vertex name is null or empty");
        }
        if (from.equals(to)) {
            // 自环边
            throw new IllegalArgumentException("vertex name is same");
        }
        Vertex fromV = vertexIndex.createVertex(from);
        Vertex toV = vertexIndex.createVertex(to);
        this.doConnect(fromV, toV, weight, directed);
    }

    private void doConnect(Vertex from, Vertex to, double weight, boolean directed) {
        int size = vertexIndex.size();
        if (size > adjacencyMatrix.length) {
            expand(size);
        }
        if (weighted) {
            if (this.adjacencyMatrix[from.getId()][to.getId()] != null) {
                log.info("reset edge's weight: {} -> {} = {}", from, to, weight);
            }
            this.adjacencyMatrix[from.getId()][to.getId()] = weight;
        } else {
            this.adjacencyMatrix[from.getId()][to.getId()] = DEFAULT_UNWEIGHTED_VALUE;
        }
        this.edgesCount++;
        if (!directed) {
            // 无向图需要对称
            this.doConnect(to, from, weight, true);
        }
    }

    // 扩展邻接矩阵
    private void expand(int newSize) {
        Double[][] newMatrix = new Double[newSize][newSize];
        for (int i = 0; i < adjacencyMatrix.length; i++) {
            System.arraycopy(adjacencyMatrix[i], 0, newMatrix[i], 0, adjacencyMatrix[i].length);
        }
        this.adjacencyMatrix = newMatrix;
    }

    @Override
    public List<Edge> adjacentEdges(String vertexName) {
        Vertex vertex = vertexIndex.getVertex(vertexName);
        if (vertex == null) {
            return null;
        }
        int id = vertex.getId();
        List<Edge> edges = new ArrayList<>();
        Double[] links = adjacencyMatrix[id];
        for (int i = 0; i < links.length; i++) {
            if (links[i] != null) {
                edges.add(new Edge(vertex, vertexIndex.getVertex(i), links[i]));
            }
        }
        return edges;
    }

    @Override
    public void showGraph() {
        System.out.println("Graph: " + (directed ? "Directed" : "Undirected") + ", " + (weighted ? "Weighted" : "Unweighted"));
        System.out.println("Vertices: " + vertexIndex.size());
        System.out.println("Edges: " + edgesCount);

        // 先打印顶点信息
        System.out.println("Vertex Information:");
        for (Vertex vertex : vertexIndex.getVertices()) {
            System.out.println("ID: " + vertex.getId() + ", Name: " + vertex.getName());
        }

        // 打印 matrix 并在第一行和第一列显示顶点信息
        System.out.println("\nAdjacency Matrix:");
        // 打印顶点ID作为列标题
        System.out.print("      "); // 留出行标题的空间
        for (int i = 0; i < vertexIndex.size(); i++) {
            Vertex v = vertexIndex.getVertex(i);
            System.out.print(DSAUtils.beautify(v.getId() + ":" + v.getName(), 5));
        }
        System.out.println();

        // 打印矩阵内容
        for (int i = 0; i < vertexIndex.size(); i++) {
            Vertex v = vertexIndex.getVertex(i);
            System.out.print(DSAUtils.beautify(v.getId() + ":" + v.getName(), 5));

            for (int j = 0; j < vertexIndex.size(); j++) {
                Double element = (i < adjacencyMatrix.length && j < adjacencyMatrix[i].length) ? adjacencyMatrix[i][j] : null;
                if (element != null) {
                    System.out.print(DSAUtils.beautify("" + element, 5));
                } else {
                    System.out.print(DSAUtils.beautify("nil", 5));
                }
            }
            System.out.println();
        }
    }

    @Override
    public Double[][] getAdjacencyMatrix() {
        return this.adjacencyMatrix;
    }

}
