package io.intellij.dsa.graph;

import org.junit.jupiter.api.Test;

import static io.intellij.dsa.graph.GraphUtils.buildGraph;

/**
 * GraphTest
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class GraphTest {
    final String graphText = """
            A B 1
            A C 1
            B C 1
            """;

    @Test
    public void testShowDenseGraph() {
        buildGraph(GraphUtils.Type.DENSITY, graphText, false, false)
                .showGraph();
    }

    @Test
    public void testShowSparseGraph() {
        buildGraph(GraphUtils.Type.SPARSITY, graphText, true, false)
                .showGraph();
    }

}
