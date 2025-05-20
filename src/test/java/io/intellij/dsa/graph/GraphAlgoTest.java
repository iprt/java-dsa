package io.intellij.dsa.graph;

import io.intellij.dsa.graph.algo.Components;
import io.intellij.dsa.graph.algo.Dijkstra;
import io.intellij.dsa.graph.algo.Traverse;
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
        String text = """
                A B 1
                A C 1
                D E 1
                """;
        GraphUtils utils = new GraphUtils(false, false);
        utils.connect(text, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);

        Components algo = new Components(utils.getGraph());

        Assertions.assertEquals(2, algo.componentsCount());
        Assertions.assertTrue(algo.hasPath("B", "C"));
        Assertions.assertFalse(algo.hasPath("A", "D"));
    }

    @Test
    public void testDijkstra() {
        // A -> C -> E -> F
        String text = """
                A B 3
                A C 1
                B D 3
                C B 1
                C D 5
                C E 2
                D F 2
                E F 1
                B F 8
                """;
        GraphUtils utils = new GraphUtils(false, true);
        utils.connect(text, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);

        Dijkstra dijkstra = new Dijkstra(utils.getGraph());

        Dijkstra.ComputeResult result = dijkstra.compute("A");
        Stream.of("B", "C", "D", "E", "F")
                .map(result::getRoutes)
                .forEach(result::printRoutes);
    }

    @Test
    public void testTraverse() {
        String text = """
                A B 1
                A C 1
                B D 1
                C E 1
                """;
        GraphUtils utils = new GraphUtils(false, true);
        utils.connect(text, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);
        Traverse traverse = new Traverse(utils.getGraph());

        traverse.dfs("A");
        traverse.bfs("A");

    }

}
