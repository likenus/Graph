package src.graph.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import src.graph.interfaces.Graph;
import src.vertices.Node;
import src.vertices.Vertice;

public abstract class SingleGraph implements Graph {

    protected final List<Vertice> vertices;
    protected int id;

    protected SingleGraph() {
        this.vertices = new ArrayList<>();
    }

    protected SingleGraph(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }
        
        this.vertices = new ArrayList<>();
        this.id  = i;

        for (int j = 0; j < i; j++) {
            Vertice node = new Node(j);
            node.setParent(node);
            this.vertices.add(j, node);
        }
    }

    protected List<Vertice> neighbours(Vertice v) {
        return v.neighbours();
    }

    protected boolean add(Vertice v) {
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
    public boolean add() {
        Vertice v = new Node(this.id);
        this.id++;
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
