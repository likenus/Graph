package src.vertices.models;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

import src.edge.interfaces.Edge;
import src.vertices.interfaces.Vertice;

public class Node implements Vertice {
    
    protected final List<Edge> edges;

    protected int key;
    protected int value;

    public Node(int key) {
        this.edges = new LinkedList<>();
        this.key = key;
    }

    @Override
    public List<Vertice> neighbours() {
        List<Vertice> neighbours = new LinkedList<>();
        for (Edge edge : this.edges) {
            neighbours.add(edge.getOther(this));
        }
        return Collections.unmodifiableList(neighbours);
    }

    @Override
    public int getKey() {
        return this.key;
    }

    public void setKey(int key) {
        this.key = key;
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
    public int degree() {
        return edges.size();
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public int getValue() {
        return this.value;
    }

    @Override
    public void setValue(int i) {
        this.value = i;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass() != this.getClass()) {
            return false;
        }

        Node other = (Node) o;

        return other.key == this.key;
    } 

    @Override
    public int hashCode() {
        return Objects.hash(key);
    }
}
