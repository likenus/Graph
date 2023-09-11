package src.vertices.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.interfaces.Vertice;

public class Node<T> implements Vertice<T> {
    
    protected final List<Edge> edges;

    protected Vertice<T> parent;
    protected int key;
    protected T value;

    public Node(int key) {
        this.edges = new LinkedList<>();
        this.key = key;
    }

    @Override
    public List<Vertice<T>> neighbours() {
        List<Vertice<T>> neighbours = new LinkedList<>();
        for (Edge edge : this.edges) {
            neighbours.add(edge.getOther(this));
        }
        return Collections.unmodifiableList(neighbours);
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public void connectEdge(Edge e) {
        this.edges.add(e);
    }

    @Override
    public void disconnectEdge(Edge e) {
        this.edges.remove(e);
    }

    @Override
    public String toString() {
        return "" + this.key;
    }

    @Override
    public void setParent(Vertice<T> v) {
        this.parent = v;
    }

    @Override
    public Vertice<T> getParent() {
        return this.parent;
    }

    @Override
    public int degree() {
        return edges.size();
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public T getValue() {
        return this.value;
    }

    @Override
    public void setValue(T val) {
        this.value = val;
    }
}
