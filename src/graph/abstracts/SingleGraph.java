package src.graph.abstracts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.graph.interfaces.Graph;
import src.vertices.Node;
import src.vertices.Vertice;

public abstract class SingleGraph implements Graph {

    protected final List<Vertice> vertices;

    protected SingleGraph() {
        this.vertices = new LinkedList<>();
    }
    
    @Override
    public List<Vertice> neighbours(Vertice v) {
        return v.neighbours();
    }

    @Override
    public boolean add(Vertice v) {
        if (this.vertices.contains(v)) {
            return false;
        }
        return this.vertices.add(v);
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

        return Collections.emptyList();
    }

    @Override
    public boolean add(int key) {
        Vertice v = new Node(key);
        return add(v);
    }
    
    @Override
    public Vertice parseVertice(int key) {
        for (Vertice vertice : vertices) {
            if (vertice.getKey() == key) {
                return vertice;
            }
        }

        return null;
    }
}
