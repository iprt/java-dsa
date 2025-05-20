package io.intellij.dsa.graph;

import io.intellij.dsa.graph.algo.Components;
import io.intellij.dsa.graph.algo.Dijkstra;
import io.intellij.dsa.graph.algo.Mst;
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
    public void testTraverse() {
        String graphText = """
                A B 1
                A C 1
                B D 1
                C E 1
                """;
        GraphUtils utils = new GraphUtils(false, true);
        utils.connect(graphText, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);
        Traverse traverse = new Traverse(utils.getGraph());

        traverse.dfs("A");
        traverse.bfs("A");
    }

    @Test
    public void testMst() {
        String graphText = """
                A B 1
                A C 2
                B C 2
                B D 3
                C D 4
                """;
        GraphUtils utils = new GraphUtils(false, true);
        utils.connect(graphText, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);

        Mst algo = new Mst(utils.getGraph());
        Mst.Result result = algo.prim();

        result.getEdges().stream().map(Edge.UNDIRECTED_TO_STRING).forEach(System.out::println);
        Assertions.assertEquals(6, result.getTotalWeight());
    }


    @Test
    public void testComponents() {
        String graphText = """
                A B 1
                A C 1
                D E 1
                """;
        GraphUtils utils = new GraphUtils(false, false);
        utils.connect(graphText, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);

        Components cps = new Components(utils.getGraph());

        Assertions.assertEquals(2, cps.count());
        Assertions.assertTrue(cps.hasPath("B", "C"));
        Assertions.assertFalse(cps.hasPath("A", "D"));
    }

    @Test
    public void testDijkstra() {
        // A -> C -> E -> F
        String graphText = """
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
        utils.connect(graphText, GraphUtils.LINE_TO_EDGE_SPILT_SPACE);

        Dijkstra dijkstra = new Dijkstra(utils.getGraph());

        Dijkstra.Result result = dijkstra.compute("A");
        Stream.of("B", "C", "D", "E", "F")
                .map(result::getRoutes)
                .forEach(result::printRoutes);
    }

}
