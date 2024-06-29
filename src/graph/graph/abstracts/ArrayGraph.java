package src.graph.graph.abstracts;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import src.graph.edge.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.vertices.Vertex;
import src.graph.vertices.models.Node;

public abstract class ArrayGraph implements Graph {

    protected Map<Integer, Vertex> vertices = new HashMap<>();
    protected Edge[][] adjacencyMatrix;
    protected int n;
    protected int m;
    protected int capacity = 16;

    protected ArrayGraph() {
        adjacencyMatrix = new Edge[capacity][capacity];
        n = 0;
    }

    protected ArrayGraph(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        capacity = (int) Math.pow(2, Math.ceil(log2(i)));
        adjacencyMatrix = new Edge[capacity][capacity];
        for (int j = 0; j < i; j++) {
            addVertex();
        }
    }

    private static double log2(int x) {
        return Math.log(x) / Math.log(2);
    }

    @Override
    public List<Vertex> neighbours(int key) {
        List<Vertex> neighbours = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            if (adjacencyMatrix[key][i] != null) {
                neighbours.add(vertices.get(i));
            }
        }
        return neighbours;
    }

    @Override
    public List<Vertex> vertices() {
        return vertices.values().stream().toList();
    }

    @Override
    public boolean addVertex() {
        if (n == capacity) {
            increaseCapacity();
        }
        vertices.put(n, new Node(n));
        n++;
        return true;
    }

    private void increaseCapacity() {
        capacity *= 2;

        Edge[][] newMatrix = new Edge[capacity][];
        for (int i = 0; i < n; i++) {
            newMatrix[i] = Arrays.copyOf(adjacencyMatrix[i], capacity);
        }
        
        adjacencyMatrix = newMatrix;
    }

    @Override
    public boolean isEmpty() {
        return n == 0;
    }

    @Override
    public Vertex parseVertex(int key) {
        return vertices.get(key);
    }

    @Override
    public int getValue(int key) {
        return vertices.get(key).getValue();
    }

    @Override
    public void setValue(int key, int value) {
        vertices.get(key).setValue(value);
    }

    @Override
    public int weightOf(int a, int b) {
        return adjacencyMatrix[a][b].getWeight();
    }

    @Override
    public Edge parseEdge(int a, int b) {
        return adjacencyMatrix[a][b];
    }

    @Override
    public int sizeVertices() {
        return n;
    }

    @Override
    public int sizeEdges() {
        return m;
    }
    
}
