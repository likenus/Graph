package target;

import java.io.IOException;
import java.util.List;

import src.graph.interfaces.Graph;
import src.graph.models.directed.DirectedGraph;
import src.graph.models.directed.DirectedWeightedGraph;
import src.graph.models.directed.DirectedWeightedTree;
import src.graph.models.undirected.Mesh2D;
import src.graph.models.undirected.UndirectedGraph;
import src.graph.models.undirected.UndirectedTree;
import src.graph.models.undirected.UndirectedWeightedGraph;
import src.graph.models.undirected.Mesh2D.MeshType;

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
    public Mesh2D mesh2D(int width, int height) {
        return new Mesh2D(width, height);
    }

    public Mesh2D mesh2D(int size) {
        return mesh2D(size, size);
    }

    public Mesh2D zylinder(int width, int height) {
        Mesh2D graph = mesh2D(width, height);

        graph.setMeshType(MeshType.ZYLINDER);

        for (int i = 0; i < height; i++) {
            graph.addEdge(i * width, i * width + width - 1);
        }

        return graph;
    }

    public Mesh2D zylinder(int size) {
        return zylinder(size, size);
    }

    public Mesh2D donut(int width, int height) {
        Mesh2D graph = zylinder(width, height);
        graph.setMeshType(MeshType.DONUT);

        for (int i = 0; i < width; i++) {
            graph.addEdge(i, height * width - width + i);
        }

        return graph;
    }

    public UndirectedGraph donut(int size) {
        return donut(size, size);
    }

    public UndirectedGraph rnd(int size) {
        UndirectedGraph graph = new UndirectedGraph(size * size);

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size - 2; j++) {
                graph.addEdge(i * size + j, i * size + j + 2);
            }
        }

        for (int i = 0; i < size - 2; i++) {
            for (int j = 0; j < size; j++) {
                graph.addEdge(size * i + j, size * i + j + 2 * size);
            }
        }

        for (int i = 0; i < size - 1; i++) {
            for (int j = 1; j < size - 1; j++) {
                graph.addEdge(size * i + j, size * (i + 1) + j - 1);
                graph.addEdge(size * i + j, size * (i + 1) + j + 1);
            }
        }

        return graph;
    }
}
