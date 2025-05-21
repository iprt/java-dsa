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
import java.util.stream.Collectors;

/**
 * DirectedCycles 有向图计算所有环
 *
 * @author tech@intellij.io
 */
public class DirectedCycles extends GraphAlgo {

    public DirectedCycles(Graph graph) {
        super(graph);
        checkGraph().checkDirected(true);
    }

    public Result findCycles() {
        Result record = new Result();
        List<Vertex> vertices = this.graph.getVertices();
        Set<String> visited = new HashSet<>();
        // 全遍历
        for (Vertex vertex : vertices) {
            boolean hasVisited = visited.contains(vertex.name());
            if (!hasVisited) {
                this.dfs(vertex, visited, new ArrayList<>(), new HashSet<>(), record);
            }
        }
        return record;
    }

    private void dfs(Vertex current, Set<String> visited, List<Vertex> path, Set<String> marked, Result record) {
        visited.add(current.name());
        path.add(current);
        marked.add(current.name());
        for (Edge edge : this.graph.adjacentEdges(current.id())) {
            Vertex next = edge.getTo();
            if (!visited.contains(next.name())) {
                this.dfs(next, visited, new ArrayList<>(path), marked, record);
            } else {
                // 核心理解 作用域是 current Vertex
                if (marked.contains(next.name())) {
                    // 说明有环
                    List<Vertex> cycle = new ArrayList<>();
                    int start = path.indexOf(next);
                    for (int i = start; i < path.size(); i++) {
                        cycle.add(path.get(i));
                    }
                    record.cycles.add(cycle);
                } else {
                    // important
                    this.dfs(next, visited, new ArrayList<>(path), marked, record);
                }
            }
        }
        // 出栈
        marked.remove(current.name());
    }

    public static class Result {
        @Getter
        private final List<List<Vertex>> cycles;

        public Result() {
            this.cycles = new ArrayList<>();
        }

        public void printCycles() {
            System.out.println("Cycles Found|Cycle's Number = " + this.cycles.size());
            cycles.forEach(this::printCycle);
        }

        private void printCycle(List<Vertex> cycle) {
            System.out.println("Printing Cycle|Vertex's Number = " + cycle.size() + "|Vertexes = " + cycle.stream().map(Vertex::name).collect(Collectors.joining(" ")));
            String lineStart = " ".repeat(2);

            // 打印环，如果只有两个节点，则打印成 A - B
            // 如果超过两个节点，打印成三行，打印出一个可以理解的环
            if (cycle.size() == 2) {
                System.out.println(lineStart + cycle.get(0).name() + " <=> " + cycle.get(1).name());
                System.out.println();
                return;
            }

             /*
              A -> B -> C
              |        |
              D - E - F
             */

            // 是否是偶数
            boolean isEven = (cycle.size() % 2 == 0);
            int mid = isEven ? cycle.size() / 2 : cycle.size() / 2 + 1;

            // 打印上半部分
            List<Vertex> upper = cycle.subList(0, mid);
            // A -> B -> C
            StringBuilder upBuilder = new StringBuilder(lineStart);
            for (int i = 0; i < upper.size(); i++) {
                if (i == upper.size() - 1) {
                    upBuilder.append(upper.get(i).name());
                } else {
                    upBuilder.append(upper.get(i).name()).append(" -> ");
                }
            }
            System.out.println(upBuilder);

            // 打印中间部分
            String midStart = lineStart + "↑";
            String midEnd = isEven ? "↓" : "↙";
            int sub = isEven ? 2 : 4;
            System.out.println(midStart + " ".repeat(upBuilder.length() - lineStart.length() - sub) + midEnd);

            // 打印下半部分
            // D - E - F
            List<Vertex> lower = cycle.subList(mid, cycle.size());
            StringBuilder downBuilder = new StringBuilder(lineStart);
            for (int i = lower.size() - 1; i >= 0; i--) {
                if (i == 0) {
                    downBuilder.append(lower.get(i).name());
                } else {
                    downBuilder.append(lower.get(i).name()).append(" <- ");
                }
            }
            System.out.println(downBuilder);
            System.out.println();
        }
    }

}
