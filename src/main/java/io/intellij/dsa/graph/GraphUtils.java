package io.intellij.dsa.graph;

import io.intellij.dsa.graph.impl.DenseGraph;
import io.intellij.dsa.graph.impl.SparseGraph;
import lombok.Getter;

import java.util.Random;
import java.util.function.Function;

/**
 * GraphUtils
 *
 * @author tech@intellij.io
 */

public class GraphUtils {

    public static final Function<String, EdgePO> LINE_TO_EDGE_SPILT_COMMA = line -> {
        String[] parts = line.split(",");
        if (parts.length < 3) {
            return null;
        }
        String from = parts[0].trim();
        String to = parts[1].trim();
        String weightStr = parts[2].trim();
        try {
            double weight = Double.parseDouble(weightStr);
            return new EdgePO(from, to, weight);
        } catch (NumberFormatException e) {
            // Handle the case where the weight is not a valid number
            return null;
        }
    };

    public static final Function<String, EdgePO> LINE_TO_EDGE_SPILT_SPACE = line -> {
        String[] parts = line.split("\\s+");
        if (parts.length < 3) {
            return null;
        }
        String from = parts[0].trim();
        String to = parts[1].trim();
        String weightStr = parts[2].trim();
        try {
            double weight = Double.parseDouble(weightStr);
            return new EdgePO(from, to, weight);
        } catch (NumberFormatException e) {
            // Handle the case where the weight is not a valid number
            return null;
        }
    };

    public enum Type {
        DENSITY,
        SPARSITY,
    }

    @Getter
    private final Graph graph;

    public GraphUtils(boolean directed, boolean weighted) {
        this(null, directed, weighted);
    }

    public GraphUtils(Type type, boolean directed, boolean weighted) {
        // random boolean
        Random random = new Random();
        boolean randomBool = random.nextBoolean();
        if (type == Type.DENSITY) {
            this.graph = new DenseGraph(directed, weighted);
        } else if (type == Type.SPARSITY) {
            this.graph = new SparseGraph(directed, weighted);
        } else {
            this.graph = randomBool ? new DenseGraph(directed, weighted) : new SparseGraph(directed, weighted);
        }
    }

    public void connect(String text, Function<String, EdgePO> lineToEdge) {
        String[] lines = text.split("\n");
        for (String line : lines) {
            this.doConnect(line, lineToEdge);
        }
    }

    private void doConnect(String line, Function<String, EdgePO> lineToEdge) {
        EdgePO edge = lineToEdge.apply(line);
        if (edge != null) {
            graph.connect(edge.from, edge.to, edge.weight);
        }
    }

    public record EdgePO(String from, String to, double weight) {
    }

}
