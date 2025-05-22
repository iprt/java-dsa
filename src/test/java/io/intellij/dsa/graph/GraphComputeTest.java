package io.intellij.dsa.graph;

import io.intellij.dsa.graph.compute.Components;
import io.intellij.dsa.graph.compute.CycleAnalyzer;
import io.intellij.dsa.graph.compute.Dijkstra;
import io.intellij.dsa.graph.compute.Mst;
import io.intellij.dsa.graph.compute.Traverse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.stream.Stream;

import static io.intellij.dsa.graph.GraphUtils.buildGraph;

/**
 * GraphAlgoTest
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class GraphComputeTest {

    @Test
    public void testTraverse() {
        Traverse traverse = new Traverse(buildGraph("""
                A B 1
                A C 1
                B D 1
                C E 1
                """, false, false)
        );
        traverse.dfs("A");
        traverse.bfs("A");
    }

    @Test
    public void testMstPrim() {
        Mst mst = new Mst(buildGraph("""
                A B 1
                A C 2
                B C 2
                B D 3
                C D 4
                """, false, true));
        Mst.Result result = mst.prim();

        result.getEdges().stream().map(Edge.UNDIRECTED_TO_STRING).forEach(System.out::println);
        Assertions.assertEquals(6, result.getTotalWeight());
    }

    @Test
    public void testMstKruskal() {
        Mst mst = new Mst(buildGraph("""
                0 1 4
                0 5 8
                1 5 11
                1 2 8
                5 6 7
                2 6 2
                5 4 8
                4 6 4
                2 3 3
                4 3 3
                """, false, true));
        Mst.Result result = mst.kruskal();
        result.getEdges().stream().map(Edge.UNDIRECTED_TO_STRING).forEach(System.out::println);
        // 2 3 3 4 7 8
        Assertions.assertEquals(2 + 3 + 3 + 4 + 7 + 8, result.getTotalWeight());
    }


    @Test
    public void testComponents() {
        Components cps = new Components(buildGraph("""
                A B 1
                A C 1
                D E 1
                """, false, false)
        );
        Components.Result result = cps.compute();
        Assertions.assertEquals(2, result.getCount());
        Assertions.assertTrue(result.hasPath("B", "C"));
        Assertions.assertFalse(result.hasPath("A", "D"));
    }

    @Test
    public void testDijkstra() {
        Dijkstra dijkstra = new Dijkstra(buildGraph("""
                A B 3
                A C 1
                B D 3
                C B 1
                C D 5
                C E 2
                D F 2
                E F 1
                B F 8
                """, false, true)
        );
        // A -> C -> E -> F
        Dijkstra.Result result = dijkstra.compute("A");
        Stream.of("B", "C", "D", "E", "F")
                .map(result::getRoutes)
                .forEach(result::printRoutes);
    }

    @Test
    public void testUndirectedCycles() {
        /*
            A - B - C
             \  |  /
                D
         */
        CycleAnalyzer analyzer = new CycleAnalyzer(buildGraph("""
                A B 1
                B C 1
                C D 1
                D A 1
                B D 1
                """, false, false)
        );
        CycleAnalyzer.Result result = analyzer.findCycles();
        result.printCycles();
        Assertions.assertEquals(3, result.getCycles().stream()
                .filter(cycle -> cycle.size() > 2).count());

        System.out.println(" --------------------- ");

        /*
            A  -  B
             \   /
               C
              / \
            D  -  E
         */
        analyzer.setGraph(buildGraph("""
                A B 1
                C A 1
                C B 1
                C D 1
                C E 1
                D E 1
                """, false, false));

        result = analyzer.findCycles();
        result.printCycles();
        Assertions.assertEquals(2, result.getCycles().stream()
                .filter(cycle -> cycle.size() > 2).count());

    }

    @Test
    public void testDirectedCycles() {
        CycleAnalyzer analyzer = new CycleAnalyzer(buildGraph("""
                A B 1
                B C 1
                C D 1
                D E 1
                E F 1
                F A 1
                """, true, false)
        );
        CycleAnalyzer.Result result = analyzer.findCycles();
        result.printCycles();

        System.out.println(" --------------------- ");
        Assertions.assertEquals(1, result.getCycles().size());

        /*
                 B
              ↗  ↓   ↘
          A      ↓     C
            ↖    ↓   ↙
                 D
         */
        analyzer.setGraph(buildGraph("""
                A B 1
                B C 1
                C D 1
                D A 1
                B D 1
                """, true, false));
        result = analyzer.findCycles();
        result.printCycles();
        Assertions.assertEquals(2, result.getCycles().size());
    }

}
