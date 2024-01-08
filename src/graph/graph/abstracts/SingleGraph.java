package src.graph.graph.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import src.graph.edge.interfaces.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertex;
import src.graph.vertices.models.Node;

public abstract class SingleGraph implements Graph {

    protected final Map<Integer, Vertex> vertices = new HashMap<>();
    protected final Set<Edge> edges = new HashSet<>();
    protected int id;

    protected SingleGraph() {

    }

    protected SingleGraph(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        this.id  = i;

        for (int j = 0; j < i; j++) {
            Vertex node = new Node(j);
            this.vertices.put(j, node);
        }
    }

    protected List<Vertex> neighbours(Vertex v) {
        return v.neighbours();
    }

    protected boolean add(Vertex v) {
        // Node is always unique
        if (vertices.get(v.getKey()) == null) {
            vertices.put(v.getKey(), v);
            return true;
        }
        return false;
    }

    protected void remove(int key) {
        // To be implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public int getValue(int key) {
        return parseVertex(key).getValue();
    }

    @Override
    public void setValue(int key, int value) {
        parseVertex(key).setValue(value);
    }

    @Override
    public void removeEdge(int a, int b) {
        Edge edge = parseEdge(a, b);

        if (edge == null) {
            return;
        }

        removeEdge(edge);
    }

    protected void removeEdge(Edge e) {
        this.edges.remove(e);
    }

    @Override
    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }

    @Override
    public List<Vertex> vertices() {
        return Collections.unmodifiableList(this.vertices.values().stream().toList());
    }

    @Override
    public List<Vertex> neighbours(int key) {
        Vertex v = parseVertex(key);

        if (v != null) {
            return this.neighbours(v);
        }

        return new ArrayList<>();
    }

    @Override
    public boolean addVertex() {
        Vertex v = new Node(this.id);
        this.id++;
        return add(v);
    }
    
    @Override
    public Vertex parseVertex(int key) {
        return vertices.get(key);
    }

    @Override
    public int sizeVertices() {
        return this.vertices.size();
    }

    public int sizeEdges() {
        return this.edges.size();
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(new ArrayList<>(this.edges));
    }

    @Override
    public Edge parseEdge(int a, int b) {
        Vertex v = parseVertex(a);
        Vertex w = parseVertex(b);

        List<Edge> incidentEdges = new ArrayList<>(v.edges());
        incidentEdges.addAll(w.edges());

        for (Edge edge : incidentEdges) {
            if (edge.getVertices().contains(v) && edge.getVertices().contains(w)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public int weightOf(int a, int b) {
        Edge edge = parseEdge(a, b);

        if (edge == null) {
            throw new IllegalArgumentException();
        }

        return edge.getWeight();
    }
}
