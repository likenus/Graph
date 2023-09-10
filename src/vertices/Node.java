package src.vertices;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;

public class Node implements Vertice {
    
    private final List<Edge> edges;

    private boolean marked = false;
    private Vertice parent;
    private int key;

    public Node(int key) {
        this.edges = new LinkedList<>();
        this.key = key;
    }

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

    public void mark() {
        this.marked = true;
    }

    @Override
    public boolean isMarked() {
        return marked;
    }

    @Override
    public void reset() {
        this.marked = false;
    }

    @Override
    public void setParent(Vertice v) {
        this.parent = v;
    }

    @Override
    public Vertice getParent() {
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
}
