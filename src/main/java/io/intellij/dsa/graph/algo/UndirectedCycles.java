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
 * Rings
 *
 * @author tech@intellij.io
 */
public class UndirectedCycles extends GraphAlgo {

    public UndirectedCycles(Graph graph) {
        super(graph);
        checkGraph().checkDirected(false);
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

    // 保证每个节点都被深度遍历一次
    // 某个节点在遍历的时候
    // 1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
    // 2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
    // 3. 递归结束后，当前节点出栈
    private void dfs(Vertex vertex, Set<String> visited, Set<String> vertexStack, List<Vertex> path, Result record) {
        // 标记当前节点为已访问
        visited.add(vertex.name());
        // 将当前节点添加到递归栈中
        vertexStack.add(vertex.name());
        // 将当前节点添加到路径中
        path.add(vertex);

        //  1. 如果邻居未被访问，则递归调用 DFS 继续搜索（注意传递了路径的副本)
        //  2. 如果邻居已被访问且在当前递归栈中，说明找到了一个环。记录从邻居到当前顶点的路径作为环
        for (Edge edge : this.graph.adjacentEdges(vertex.name())) {
            Vertex neighbor = edge.getTo();
            if (!visited.contains(neighbor.name())) {
                // Continue the DFS search
                this.dfs(neighbor, visited, vertexStack, new ArrayList<>(path), record);
            } else if (vertexStack.contains(neighbor.name())) {
                // Found a cycle, the neighbor is already in the current path
                List<Vertex> cycle = new ArrayList<>();
                int index = path.indexOf(neighbor);
                for (int i = index; i < path.size(); i++) {
                    cycle.add(path.get(i));
                }
                record.cycles.add(cycle);
            }
        }
        // Backtrack: remove the current node from the path
        vertexStack.remove(vertex.name());
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
            System.out.println("Printing Cycle|size = " + cycle.size() + "|vertexes = " + cycle.stream().map(Vertex::name).collect(Collectors.joining(" ")));
            String lineStart = " ".repeat(2);

            /*
              A - B - C
              |       |
              D - E - F
             */
            // 打印环，如果只有两个节点，则打印成 A - B
            // 如果超过两个节点，打印成三行，打印出一个可以理解的环
            if (cycle.size() == 2) {
                System.out.println(lineStart + cycle.get(0).name() + " - " + cycle.get(1).name());
                System.out.println();
                return;
            }

            // 是否是偶数
            boolean isEven = (cycle.size() % 2 == 0);
            int mid = isEven ? cycle.size() / 2 : cycle.size() / 2 + 1;


            // 打印上半部分
            List<Vertex> upper = cycle.subList(0, mid);
            // A - B - C
            StringBuilder upBuilder = new StringBuilder(lineStart);
            for (int i = 0; i < upper.size(); i++) {
                if (i == upper.size() - 1) {
                    upBuilder.append(upper.get(i).name());
                } else {
                    upBuilder.append(upper.get(i).name()).append(" - ");
                }
            }
            System.out.println(upBuilder);

            // 打印中间部分
            String midStart = lineStart + "|";
            String midEnd = isEven ? "|" : "/";
            int sub = isEven ? 2 : 3;
            System.out.println(midStart + " ".repeat(upBuilder.length() - lineStart.length() - sub) + midEnd);

            // 打印下半部分
            // D - E - F
            List<Vertex> lower = cycle.subList(mid, cycle.size());
            StringBuilder downBuilder = new StringBuilder(lineStart);
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
