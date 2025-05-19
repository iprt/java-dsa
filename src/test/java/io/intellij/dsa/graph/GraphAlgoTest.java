package io.intellij.dsa.graph;

import io.intellij.dsa.graph.algo.Components;
import io.intellij.dsa.graph.algo.Dijkstra;
import io.intellij.dsa.graph.impl.SparseGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * GraphAlgoTest
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class GraphAlgoTest {

    @Test
    public void testComponents() {
        Graph graph = new SparseGraph(false, false);

        graph.connect("A", "B");
        graph.connect("A", "C");
        graph.connect("D", "E");

        Components algo = new Components(graph);

        Assertions.assertEquals(2, algo.componentsCount());
        Assertions.assertTrue(algo.hasPath("B", "C"));
        Assertions.assertFalse(algo.hasPath("A", "D"));
    }

    @Test
    public void testDijkstra() {
        Graph graph = new SparseGraph(false, true);

        graph.connect("A", "B", 4);
        graph.connect("A", "D", 2);
        graph.connect("B", "D", 1);
        graph.connect("B", "C", 4);
        graph.connect("C", "D", 1);
        graph.connect("C", "E", 3);
        graph.connect("D", "E", 7);

        Dijkstra dijkstra = new Dijkstra(graph);

        Dijkstra.ComputeResult result = dijkstra.compute("A");

        List.of("B", "C", "D", "E").forEach(vertex -> {
            result.printRoutes(
                    result.getRoutes(vertex)
            );
        });

    }

}
