package src.graph.graph.models.undirected;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.graph.edge.Edge;
import src.graph.edge.models.UndirectedEdge;
import src.graph.vertices.Vertex;
import src.graph.vertices.models.Node;
import src.util.Pair;

public class ArrayMesh2D implements Mesh2D {
    
    private final int width;
    private final int height;
    private final Vertex[][] bitmap;
    private Map<Pair<Integer, Integer>, Edge> edges = new HashMap<>();

    private MeshType meshType;

    public ArrayMesh2D(int width, int height) {
        this.width = width;
        this.height = height;
        bitmap = new Vertex[height][width];
        for (int i = 0; i < height * width; i++) {
            bitmap[i / width][i % width] = new Node(i);
        }

        // Horizontal lines
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                this.addEdge(i * width + j, i * width + j + 1);
            }
        }

        // Vertical lines
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                this.addEdge(width * i + j, width * i + j + width);
            }
        }
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MeshType getMeshType() {
        return meshType;
    }

    public void setMeshType(MeshType meshType) {
        this.meshType = meshType;
    }

    @Override
    public List<Vertex> neighbours(int key) {
        List<Vertex> neighbours = new ArrayList<>();
        int x = key / width;
        int y = key % width;
        if (x > 0) neighbours.add(bitmap[x - 1][y]);
        if (x < width - 1) neighbours.add(bitmap[x + 1][y]);
        if (y > 0) neighbours.add(bitmap[x][y - 1]);
        if (y < height - 1) neighbours.add(bitmap[x][y + 1]);

        return neighbours;
    }

    @Override
    public List<Vertex> vertices() {
        List<Vertex> vertices = new ArrayList<>();
        for (Vertex[] line : bitmap) {
            vertices.addAll(Arrays.asList(line));
        }
        return vertices;
    }

    @Override
    public boolean addVertex() {
        return false;
    }

    @Override
    public boolean isEmpty() {
        return width * height == 0;
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }

    @Override
    public Vertex parseVertex(int key) {
        return bitmap[key / width][key % width];
    }

    @Override
    public void removeEdge(int a, int b) {
        edges.remove(new Pair<>(a, b));
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertex v = parseVertex(a);
        Vertex w = parseVertex(b);
        Edge edge = new UndirectedEdge(v, w, value);
        edges.put(new Pair<>(a, b), edge);
        v.connectEdge(edge);
        w.connectEdge(edge);
        edges.put(new Pair<>(a, b), edge);
        return true;
    }

    @Override
    public int getValue(int key) {
        return bitmap[key / width][key % width].getValue();
    }

    @Override
    public void setValue(int key, int value) {
        bitmap[key / width][key % width].setValue(value);
    }

    @Override
    public int weightOf(int a, int b) {
        return 1;
    }

    @Override
    public List<Edge> edges() {
        return edges.values().stream().toList();
    }

    @Override
    public Edge parseEdge(int a, int b) {
        Edge e = edges.get(new Pair<>(a, b));
        if (e == null) {
            return edges.get(new Pair<>(b, a));
        }
        return e;
    }

    @Override
    public int sizeVertices() {
        return width * height;
    }

    @Override
    public int sizeEdges() {
        return edges.size();
    }
}
