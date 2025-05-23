package io.intellij.dsa.graph.impl;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.Vertex;
import io.intellij.dsa.graph.VertexIndex;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * SparseGraph 稀疏图
 * <p>
 * 稀疏图是指边数远小于顶点数的平方的图，通常用邻接表表示。
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
@Slf4j
public class SparseGraph implements Graph {
    private final VertexIndex vertexIndex = new VertexIndex();

    private final boolean directed;
    private final boolean weighted;

    // 邻接表
    private final List<Map<Integer, Double>> adjacencyList;

    private int edgesCount;

    public SparseGraph(boolean directed, boolean weighted) {
        this.directed = directed;
        this.weighted = weighted;
        this.adjacencyList = new ArrayList<>();
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
    public int getVerticesNum() {
        return vertexIndex.size();
    }

    @Override
    public int getEdgesNum() {
        return this.edgesCount;
    }

    @Override
    public List<Vertex> getVertices() {
        return vertexIndex.getVertices();
    }

    @Override
    public Edge getEdge(String from, String to) {
        if (StringUtils.isBlank(from) || StringUtils.isBlank(to)) {
            return null;
        }
        if (from.equals(to)) {
            return null;
        }
        Vertex fromV = vertexIndex.getVertex(from);
        Vertex toV = vertexIndex.getVertex(to);
        if (fromV == null || toV == null) {
            return null;
        }
        Map<Integer, Double> toMap = this.adjacencyList.get(fromV.id());
        return toMap.containsKey(toV.id()) ?
                new Edge(fromV, toV, toMap.get(toV.id())) :
                null;
    }

    @Override
    public void connect(String from, String to, double weight) {
        if (from == null || to == null) {
            throw new IllegalArgumentException("Vertex names cannot be null");
        }

        Vertex fromV = vertexIndex.createVertex(from);
        Vertex toV = vertexIndex.createVertex(to);
        this.doConnect(fromV, toV, weight, directed);
    }

    private void doConnect(Vertex from, Vertex to, double weight, boolean directed) {
        // 决定是add还是get(index)
        int fromId = from.id();
        int toId = to.id();

        if (fromId + 1 > adjacencyList.size()) {
            adjacencyList.add(new TreeMap<>());
        }
        Map<Integer, Double> fromMap = adjacencyList.get(fromId);
        if (weighted) {
            if (fromMap.containsKey(toId)) {
                log.info("reset edge's weight: {} -> {} = {}", from, to, weight);
            }
            fromMap.put(toId, weight);
        } else {
            fromMap.put(toId, DEFAULT_UNWEIGHTED_VALUE);
        }
        this.edgesCount++;
        if (!directed) {
            // 如果是无向图，则添加反向边
            this.doConnect(to, from, weight, true);
        }
    }

    @Override
    public List<Edge> adjacentEdges(String name) {
        Vertex vertex = vertexIndex.getVertex(name);
        return vertex == null ? null : adjacentEdges(vertex.id());
    }

    @Override
    public List<Edge> adjacentEdges(int index) {
        if (index < 0 || index >= vertexIndex.size()) {
            return null;
        }
        Map<Integer, Double> toWeightMap = adjacencyList.get(index);
        if (null == toWeightMap || toWeightMap.isEmpty()) {
            return List.of();
        }
        List<Edge> edges = new ArrayList<>();
        Vertex source = vertexIndex.getVertex(index);
        for (Map.Entry<Integer, Double> entry : toWeightMap.entrySet()) {
            Vertex target = vertexIndex.getVertex(entry.getKey());
            if (target != null) {
                edges.add(new Edge(source, target, entry.getValue()));
            }
        }
        return edges;
    }

    @Override
    public void showGraph() {
        System.out.println("Graph: " + (directed ? "Directed" : "Undirected") + ", " + (weighted ? "Weighted" : "Unweighted"));
        System.out.println("Vertices: " + vertexIndex.size());
        System.out.println("Edges: " + edgesCount);

        // 打印邻接表
        System.out.println("Adjacency List:");
        String startFmt = "%s(%d) : ";
        String toFmt = "%s(%d) -- %.2f -> %s(%d)   ";
        for (int fromId = 0; fromId < adjacencyList.size(); fromId++) {
            Vertex fromV = vertexIndex.getVertex(fromId);
            System.out.printf(startFmt, fromV.name(), fromId);
            Map<Integer, Double> map = adjacencyList.get(fromId);
            for (Map.Entry<Integer, Double> to : map.entrySet()) {
                Integer toId = to.getKey();
                Vertex toV = vertexIndex.getVertex(toId);
                System.out.printf(toFmt,
                        fromV.name(), fromId,
                        to.getValue(),
                        toV.name(), toId);
            }
            System.out.println();
        }
    }

    @Override
    public VertexIndex vertexIndex() {
        return this.vertexIndex;
    }

    @Override
    public List<Map<Integer, Double>> getAdjacencyList() {
        return this.adjacencyList;
    }

}
