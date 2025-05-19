package io.intellij.dsa.graph;

import io.intellij.dsa.graph.impl.DenseGraph;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * GraphTest
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class GraphTest {

    @Test
    public void testShowDenseGraph() {
        Graph graph = new DenseGraph(false, false);
        graph.connect("A", "B");
        graph.connect("A", "C");
        graph.connect("B", "C");
        graph.showGraph();

        List<Edge> edges = graph.adjacentEdges("A");
        for (Edge edge : edges) {
            System.out.println(edge);
        }
    }

}
