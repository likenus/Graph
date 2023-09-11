package src.graph.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.graph.interfaces.Graph;
import src.vertices.interfaces.Vertice;
import src.vertices.models.Node;

public abstract class SingleGraph<T> implements Graph<T> {

    protected final List<Vertice<T>> vertices = new ArrayList<>();
    protected final List<Edge<T>> edges = new LinkedList<>();
    protected int id;

    protected SingleGraph() {

    }

    protected SingleGraph(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        this.id  = i;

        for (int j = 0; j < i; j++) {
            Vertice<T> node = new Node<>(j);
            this.vertices.add(j, node);
        }
    }

    protected List<Vertice<T>> neighbours(Vertice<T> v) {
        return v.neighbours();
    }

    protected boolean add(Vertice<T> v) {
        if (this.vertices.contains(v)) {
            return false;
        }
        return this.vertices.add(v);
    }

    @Override
    public void remove(int key) {
        Vertice<T> v = parseVertice(key);
        for (Edge<T> edge : v.edges()) {
            removeEdge(edge);
        }

        this.vertices.remove(v);
    }

    @Override
    public void removeEdge(int a, int b) {
        Edge<T> edge = parseEdge(a, b);

        if (edge == null) {
            return;
        }

        removeEdge(edge);
    }

    protected void removeEdge(Edge<T> e) {
        this.edges.remove(e);
    }

    @Override
    public boolean isEmpty() {
        return this.vertices.isEmpty();
    }

    @Override
    public List<Vertice<T>> vertices() {
        return Collections.unmodifiableList(this.vertices);
    }

    @Override
    public List<Vertice<T>> neighbours(int key) {
        Vertice<T> v = parseVertice(key);

        if (v != null) {
            return this.neighbours(v);
        }

        return Collections.emptyList();
    }

    @Override
    public boolean add() {
        Vertice<T> v = new Node<>(this.id);
        this.id++;
        return add(v);
    }
    
    @Override
    public Vertice<T> parseVertice(int key) {
        for (Vertice<T> vertice : vertices) {
            if (vertice.getKey() == key) {
                return vertice;
            }
        }

        return null;
    }

    @Override
    public List<Edge<T>> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public Edge<T> parseEdge(int a, int b) {
        Vertice<T> v = parseVertice(a);
        Vertice<T> w = parseVertice(b);

        for (Edge<T> edge : edges) {
            if (edge.getVertices().contains(v) && edge.getVertices().contains(w)) {
                return edge;
            }
        }
        return null;
    }

    @Override
    public int weight(int a, int b) {
        Edge<T> edge = parseEdge(a, b);

        if (edge == null) {
            throw new IllegalArgumentException();
        }

        return edge.getWeight();
    }
}
