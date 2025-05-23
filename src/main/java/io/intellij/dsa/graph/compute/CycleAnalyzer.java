package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;
import lombok.Getter;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * CycleAnalyzer
 *
 * @author tech@intellij.io
 * @since 2025-05-22
 */
public class CycleAnalyzer extends GraphCompute {

    public CycleAnalyzer(Graph graph) {
        super(graph);
        checkGraph();
    }

    public Result findCycles() {
        Result record = new Result(this.graph.isDirected());
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

    // 保证每个节点都被深度遍历一次
    // 某个节点在遍历的时候
    // 1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
    // 2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
    // 3. 递归结束后，当前节点出栈
    private void dfs(Vertex current, Set<String> visited, List<Vertex> path, Set<String> marked, Result record) {
        visited.add(current.name());
        path.add(current);
        marked.add(current.name());

        //  1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
        //  2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
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
                    // record.cycles.add(cycle);
                    record.addCycle(cycle);
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
        private final boolean directed;

        @Getter
        private final List<List<Vertex>> cycles;

        private Result(boolean directed) {
            this.directed = directed;
            this.cycles = new ArrayList<>();
        }

        final Set<String> cycleZip = new HashSet<>();

        void addCycle(List<Vertex> cycle) {
            if (!directed) {
                String zip = cycle.stream().map(Vertex::name)
                        .sorted()
                        .collect(Collectors.joining(" "));
                if (cycleZip.contains(zip)) {
                    return;
                }
                cycleZip.add(zip);
                this.cycles.add(cycle);
            } else {
                this.cycles.add(cycle);
            }
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
            String midStart = lineStart + (directed ? "↑" : "|");
            String midEnd = isEven ? (directed ? "↓" : "|") : (directed ? "↙" : "/");
            int sub = isEven ? 2 : (directed ? 4 : 3);
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
