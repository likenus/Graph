package src.graph.graph.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.edge.interfaces.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertice;
import src.graph.vertices.models.Node;

public abstract class SingleGraph implements Graph {

    protected final List<Vertice> vertices = new ArrayList<>();
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
            Vertice node = new Node(j);
            this.vertices.add(j, node);
        }
    }

    protected List<Vertice> neighbours(Vertice v) {
        return v.neighbours();
    }

    protected boolean add(Vertice v) {
        // Node is always unique
        return vertices.add(v);
    }

    protected void remove(int key) {
        // To be implemented
        throw new UnsupportedOperationException();
    }

    @Override
    public int getValue(int key) {
        return parseVertice(key).getValue();
    }

    @Override
    public void setValue(int key, int value) {
        parseVertice(key).setValue(value);
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
    public List<Vertice> vertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    @Override
    public List<Vertice> neighbours(int key) {
        Vertice v = parseVertice(key);

        if (v != null) {
            return this.neighbours(v);
        }

        return new ArrayList<>();
    }

    @Override
    public boolean addVertice() {
        Vertice v = new Node(this.id);
        this.id++;
        return add(v);
    }
    
    @Override
    public Vertice parseVertice(int key) {
        if (key >= vertices.size()) {
            return null;
        }
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
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        for (Edge edge : edges) {
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
