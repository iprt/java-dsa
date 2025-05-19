package io.intellij.dsa.graph;

import io.intellij.dsa.graph.algo.Components;
import io.intellij.dsa.graph.algo.Dijkstra;
import io.intellij.dsa.graph.algo.Traverse;
import io.intellij.dsa.graph.impl.DenseGraph;
import io.intellij.dsa.graph.impl.SparseGraph;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

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
        // A -> B (权重: 3)
        // A -> C (权重: 1)
        // B -> D (权重: 3)
        // C -> B (权重: 1)
        // C -> D (权重: 5)
        // C -> E (权重: 2)
        // D -> F (权重: 2)
        // E -> F (权重: 1)
        // B -> F (权重: 8)

        // A -> C -> E -> F
        graph.connect("A", "B", 3);
        graph.connect("A", "C", 1);
        graph.connect("B", "D", 3);
        graph.connect("C", "B", 1);
        graph.connect("C", "D", 5);
        graph.connect("C", "E", 2);
        graph.connect("D", "F", 2);
        graph.connect("E", "F", 1);
        graph.connect("B", "F", 8);

        Dijkstra dijkstra = new Dijkstra(graph);

        Dijkstra.ComputeResult result = dijkstra.compute("A");
        Stream.of("B", "C", "D", "E", "F")
                .map(result::getRoutes)
                .forEach(result::printRoutes);
    }

    @Test
    public void testTraverse() {
        Graph graph = new DenseGraph(false, false);

        graph.connect("A", "B");
        graph.connect("B", "D");
        graph.connect("A", "C");
        graph.connect("C", "E");

        Traverse traverse = new Traverse(graph);

        traverse.dfs("A");
        traverse.bfs("A");

    }

}
