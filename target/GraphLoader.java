package target;

import java.io.IOException;
import java.util.List;

import src.graph.interfaces.Graph;
import src.graph.models.directed.DirectedGraph;
import src.graph.models.directed.DirectedWeightedGraph;
import src.graph.models.directed.DirectedWeightedTree;
import src.graph.models.undirected.UndirectedGraph;
import src.graph.models.undirected.UndirectedTree;
import src.graph.models.undirected.UndirectedWeightedGraph;

public class GraphLoader {

    private static final String ARG_SPLITTER = "->|:";

    private FileLoader fileLoader;

    public GraphLoader() {
        try {
            fileLoader = new FileLoader("files");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public Graph loadFromFile(String fileName) throws IOException {
        List<String> specs = fileLoader.loadGraph(fileName);
        String[] specifications = specs.remove(0).split(";");
        int size = Integer.parseInt(specifications[2]);
        String directed = specifications[0];
        String weighted = specifications[1];

        Graph graph;

        if (directed.equals("u")) {
            if (weighted.equals("u")) {
                graph = new UndirectedGraph(size);
            } else {
                graph = new UndirectedWeightedGraph(size);
            }
        } else if (directed.equals("d")) {
            if (weighted.equals("u")) {
                graph = new DirectedGraph(size);
            } else {
                graph = new DirectedWeightedGraph(size);
            }
        } else {
            if (weighted.equals("u")) {
                graph = new UndirectedTree(size);
            } else {
                graph = new DirectedWeightedTree(size);
            }
        }

        for (String line : specs) {
            String[] parts = line.split(ARG_SPLITTER, -1);
            int a = Integer.parseInt(parts[0]);
            int b = Integer.parseInt(parts[1]);
            int val = parts.length == 3 ? Integer.parseInt(parts[2]) : 1;

            graph.addEdge(a, b, val);
        }

        return graph;
    }

    /**
     * Returns a quadratic mesh where adjacent vertices are connected
     * @param size The length of one side of the quadrat
     * @return An undirected graph
     */
    public UndirectedGraph mesh2D(int size) {
        UndirectedGraph graph = new UndirectedGraph(size * size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 1; j++) {
                graph.addEdge(i * size + j, i * size + j + 1);
            }
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = 0; j < size; j++) {
                graph.addEdge(size * i + j, size * i + j + size);
            }
        }

        return graph;
    }
}
