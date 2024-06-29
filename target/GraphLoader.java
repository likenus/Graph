package target;

import java.io.IOException;
import java.util.List;

import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.directed.DirectedLinkedGraph;
import src.graph.graph.models.directed.DirectedWeightedLinkedGraph;
import src.graph.graph.models.directed.DirectedWeightedTree;
import src.graph.graph.models.undirected.LinkedMesh2D;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.graph.models.undirected.ArrayMesh2D;
import src.graph.graph.models.undirected.UndirectedLinkedGraph;
import src.graph.graph.models.undirected.UndirectedTree;
import src.graph.graph.models.undirected.UndirectedWeightedLinkedGraph;
import src.graph.graph.models.undirected.Mesh2D.MeshType;

public class GraphLoader {

    private static final String ARG_SPLITTER = "->|:";
    private static final String FOLDER_PATH = "files";

    private FileLoader fileLoader;

    public GraphLoader() {
        this(FOLDER_PATH);
    }

    public GraphLoader(String path) {
        try {
            fileLoader = new FileLoader(path);
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
                graph = new UndirectedLinkedGraph(size);
            } else {
                graph = new UndirectedWeightedLinkedGraph(size);
            }
        } else if (directed.equals("d")) {
            if (weighted.equals("u")) {
                graph = new DirectedLinkedGraph(size);
            } else {
                graph = new DirectedWeightedLinkedGraph(size);
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
    public ArrayMesh2D arrayMesh2D(int width, int height) {
        return new ArrayMesh2D(width, height);
    }

    public Mesh2D mesh2D(int size) {
        return arrayMesh2D(size, size);
    }

    public Mesh2D linkedMesh2D(int width, int height) {
        return new LinkedMesh2D(width, height);
    }

    public ArrayMesh2D zylinder(int width, int height) {
        ArrayMesh2D graph = arrayMesh2D(width, height);

        graph.setMeshType(MeshType.ZYLINDER);

        for (int i = 0; i < height; i++) {
            graph.addEdge(i * width, i * width + width - 1);
        }

        return graph;
    }

    public ArrayMesh2D zylinder(int size) {
        return zylinder(size, size);
    }

    public ArrayMesh2D donut(int width, int height) {
        ArrayMesh2D graph = zylinder(width, height);
        graph.setMeshType(MeshType.DONUT);

        for (int i = 0; i < width; i++) {
            graph.addEdge(i, height * width - width + i);
        }

        return graph;
    }

    public ArrayMesh2D donut(int size) {
        return donut(size, size);
    }

    public UndirectedLinkedGraph rnd(int size) {
        UndirectedLinkedGraph graph = new UndirectedLinkedGraph(size * size);

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
