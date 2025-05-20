package io.intellij.dsa.graph.algo;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphAlgo;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Rings
 *
 * @author tech@intellij.io
 */
public class UndirectedCycles extends GraphAlgo {

    public UndirectedCycles(Graph graph) {
        super(graph);
        checkGraphNotEmpty().checkDirected(false);
    }

    public Result findCycles() {
        Result result = new Result();
        Set<String> visited = new HashSet<>();
        Set<String> stack = new HashSet<>();
        List<Vertex> vertices = this.graph.getVertices();
        for (Vertex vertex : vertices) {
            if (!visited.contains(vertex.name())) {
                // Start DFS from this vertex
                dfs(vertex, visited, stack, new ArrayList<>(), result);
            }
        }
        return result;
    }

    private void dfs(Vertex vertex, Set<String> visited, Set<String> stack, List<Vertex> path, Result result) {
        // Mark the current node as visited
        visited.add(vertex.name());
        // Add the current node to the stack
        stack.add(vertex.name());
        // Add the current node to the path
        path.add(vertex);

        for (Edge edge : this.graph.adjacentEdges(vertex.name())) {
            Vertex neighbor = edge.getTo();
            if (!visited.contains(neighbor.name())) {
                // Continue the DFS search
                dfs(neighbor, visited, stack, new ArrayList<>(path), result);
            } else if (stack.contains(neighbor.name())) {
                // Found a cycle, the neighbor is already in the current path
                List<Vertex> cycle = new ArrayList<>();
                int index = path.indexOf(neighbor);
                for (int i = index; i < path.size(); i++) {
                    cycle.add(path.get(i));
                }
                result.cycles.add(cycle);
            }
        }
        // Backtrack: remove the current node from the path
        stack.remove(vertex.name());
    }

    public static class Result {
        @Getter
        private final List<List<Vertex>> cycles;

        public Result() {
            this.cycles = new ArrayList<>();
        }

        public void printCycles() {
            System.out.println("Cycles found:");
            for (List<Vertex> cycle : cycles) {
                this.printCycle(cycle);
            }
        }

        private void printCycle(List<Vertex> cycle) {
            System.out.println("Printing cycle:");
            /*
              A - B - C
              |       |
              D - E - F
             */
            // 打印环，如果只有两个节点，则打印成 A - B
            // 如果超过两个节点，打印成三行，打印出一个可以理解的环
            if (cycle.size() == 2) {
                System.out.println(cycle.get(0).name() + " - " + cycle.get(1).name());
                System.out.println();
                return;
            }

            // 是否是偶数
            boolean isEven = (cycle.size() % 2 == 0);
            int mid = isEven ? cycle.size() / 2 : cycle.size() / 2 + 1;

            // 打印上半部分
            List<Vertex> upper = cycle.subList(0, mid);
            // A - B - C
            StringBuilder upBuilder = new StringBuilder();
            for (int i = 0; i < upper.size(); i++) {
                if (i == upper.size() - 1) {
                    upBuilder.append(upper.get(i).name());
                } else {
                    upBuilder.append(upper.get(i).name()).append(" - ");
                }
            }
            System.out.println(upBuilder);

            // 打印中间部分
            String midStart = "|";
            String midEnd = isEven ? "|" : "/";
            int sub = isEven ? 2 : 3;
            System.out.println(midStart + " ".repeat(upBuilder.length() - sub) + midEnd);

            // 打印下半部分
            // D - E - F
            List<Vertex> lower = cycle.subList(mid, cycle.size());
            StringBuilder downBuilder = new StringBuilder();
            for (int i = lower.size() - 1; i >= 0; i--) {
                if (i == 0) {
                    downBuilder.append(lower.get(i).name());
                } else {
                    downBuilder.append(lower.get(i).name()).append(" - ");
                }
            }
            System.out.println(downBuilder);
            System.out.println();
        }
    }

}