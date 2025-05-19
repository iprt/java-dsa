package io.intellij.dsa.graph;

import lombok.Getter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * VertexIndex
 *
 * @author tech@intellij.io
 * @since 2025-05-19
 */
public class VertexIndex {
    @Getter
    private final List<Vertex> vertices;
    private final Map<String, Vertex> nameIndex;

    public VertexIndex() {
        this.vertices = new ArrayList<>();
        this.nameIndex = new HashMap<>();
    }

    public int size() {
        return nameIndex.size();
    }

    int getIndex() {
        return vertices.size();
    }

    public Vertex getVertex(String name) {
        return nameIndex.get(name);
    }

    public Vertex getVertex(int index) {
        if (index >= vertices.size()) {
            return null;
        }
        return vertices.get(index);
    }

    public Vertex createVertex(String name) {
        if (nameIndex.containsKey(name)) {
            return nameIndex.get(name);
        }
        Vertex vertex = new Vertex(vertices.size(), name);
        vertices.add(vertex);
        nameIndex.put(name, vertex);
        return vertex;
    }

}
