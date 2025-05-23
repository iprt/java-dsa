package io.intellij.dsa.graph.compute;

import io.intellij.dsa.graph.Edge;
import io.intellij.dsa.graph.Graph;
import io.intellij.dsa.graph.GraphCompute;
import io.intellij.dsa.graph.Vertex;
import lombok.Setter;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Traverse
 *
 * @author tech@intellij.io
 */
public class Traverse extends GraphCompute {
    @Setter
    private Consumer<Vertex> vertexConsumer;

    @Setter
    private Consumer<Edge> edgeConsumer;

    public Traverse(Graph graph) {
        this(graph, null, null);
    }

    public Traverse(Graph graph, Consumer<Vertex> vertexConsumer, Consumer<Edge> edgeConsumer) {
        super(graph);
        checkGraph();
        this.vertexConsumer = vertexConsumer;
        this.edgeConsumer = edgeConsumer;
    }

    private void initConsumer() {
        if (this.vertexConsumer == null) {
            this.vertexConsumer = vertex -> System.out.println("Visit Vertex: " + vertex.name());
        }
        if (this.edgeConsumer == null) {
            this.edgeConsumer = edge -> System.out.printf("Visit Edge: %s --%.1f-> %s\n", edge.getFrom().name(), edge.getWeight(), edge.getTo().name());
        }
    }

    public void dfs(String vertexName) {
        Vertex vertex = checkVertex(vertexName, false);
        if (vertex == null) {
            System.out.println("Vertex not found: " + vertexName);
            return;
        }
        initConsumer();
        System.out.println("\nDFS traversal starting from vertex: " + vertexName);
        this.dfs(vertex, vertexConsumer, edgeConsumer, new boolean[this.graph.getVertices().size()]);
        System.out.println("DFS traversal completed.\n");
    }

    private void dfs(Vertex vertex, Consumer<Vertex> vc, Consumer<Edge> ec, boolean[] visited) {
        visited[vertex.id()] = true;
        vc.accept(vertex);
        this.graph.adjacentEdges(vertex.id()).stream()
                .filter(edge -> !visited[edge.getTo().id()])
                .peek(ec)
                .map(Edge::getTo)
                .forEach(other -> dfs(other, vc, ec, visited));
    }


    public void bfs(String vertexName) {
        Vertex vertex = checkVertex(vertexName, false);
        if (vertex == null) {
            System.out.println("Vertex not found: " + vertexName);
            return;
        }
        initConsumer();
        System.out.println("\nBFS traversal starting from vertex: " + vertexName);
        this.bfs(vertex, this.vertexConsumer, this.edgeConsumer, new boolean[this.graph.getVertices().size()]);
        System.out.println("BFS traversal completed.\n");
    }

    private void bfs(Vertex vertex, Consumer<Vertex> vc, Consumer<Edge> ec, boolean[] visited) {
        Queue<Vertex> queue = new LinkedList<>();
        queue.add(vertex);

        while (!queue.isEmpty()) {
            Vertex poll = queue.poll();
            visited[poll.id()] = true;
            vc.accept(poll);
            this.graph.adjacentEdges(poll.id()).stream()
                    .filter(edge -> !visited[edge.getTo().id()])
                    .peek(ec)
                    .map(Edge::getTo)
                    .forEach(queue::add);
        }

    }

}
