package io.intellij.dsa.graph;

import io.intellij.dsa.graph.impl.DenseGraph;
import io.intellij.dsa.graph.impl.SparseGraph;
import lombok.Getter;
import org.apache.commons.collections4.CollectionUtils;

import java.util.List;
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

    public void connect(String graphText, Function<String, EdgePO> lineToEdge) {
        this.connect(graphText, text -> {
            String[] lines = text.split("\\r?\\n");
            return List.of(lines);
        }, lineToEdge);
    }

    public void connect(String graphText, Function<String, List<String>> textToLines, Function<String, EdgePO> lineToEdge) {
        List<String> lines = textToLines.apply(graphText);
        this.connect(lines, lineToEdge);
    }

    public void connect(List<String> lines, Function<String, EdgePO> lineToEdge) {
        if (CollectionUtils.isNotEmpty(lines)) {
            lines.forEach(line -> this.doConnect(line, lineToEdge));
        }
    }

    private void doConnect(String line, Function<String, EdgePO> lineToEdge) {
        EdgePO edge = lineToEdge.apply(line);
        if (edge != null) {
            graph.connect(edge.from, edge.to, edge.weight);
        }
    }

    public static Graph buildGraph(String graphText, boolean directed, boolean weighted) {
        GraphUtils graphUtils = new GraphUtils(null, directed, weighted);
        graphUtils.connect(graphText, LINE_TO_EDGE_SPILT_SPACE);
        return graphUtils.getGraph();
    }

    public static Graph buildGraph(Type type, String graphText, boolean directed, boolean weighted) {
        GraphUtils graphUtils = new GraphUtils(type, directed, weighted);
        graphUtils.connect(graphText, LINE_TO_EDGE_SPILT_SPACE);
        return graphUtils.getGraph();
    }

    public record EdgePO(String from, String to, double weight) {
    }

}
