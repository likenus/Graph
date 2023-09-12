package src.graph.models.directed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.edge.models.DirectedEdge;
import src.graph.interfaces.Tree;
import src.vertices.interfaces.Vertice;

public class DirectedWeightedTree extends DirectedWeightedGraph implements Tree {
    
    protected final List<Vertice> parents = new ArrayList<>();
    
    protected Vertice root;

    public DirectedWeightedTree() {

    }

    public DirectedWeightedTree(int i) {
        super(i);

        if (i > 0) {
            this.root = this.vertices.get(0);
        }

        for (int j = 0; j < i; j++) {
            parents.add(null);
        }
    }

    @Override
    public Vertice getRoot() {
        return this.root;
    }

    @Override
    public void setRoot(int key) {
        Vertice oldRoot = this.root;
        Vertice newRoot = parseVertice(key);

        List<Vertice> path = pathToRoot(newRoot);

        if (path.isEmpty()) {
            return;
        }

        if (path.size() == 1) {
            swapEdge(oldRoot, newRoot);
            this.root = newRoot;
            return;
        }
        
        for (int i = 0; i < path.size() - 1; i++) {
            Vertice v = path.get(i);
            Vertice n = path.get(i + 1);

            swapEdge(n, v);
        }

        this.root = newRoot;
    }

    @Override
    protected boolean add(Vertice v) {
        if (super.add(v)) {
            parents.add(null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean addEdge(Vertice v, Vertice w, int value) {
        if (v == null || w == null || w.equals(this.root)) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        boolean valid = false;

        if (this.edges.isEmpty()) {
            valid = true;
        }

        for (Edge e : edges) {
            if (e.end().equals(w)) {
                return false;
            }
            if (e.end().equals(v)) {
                valid = true;
            }
        }

        if (!valid) {
            return false;
        }

        v.connectEdge(edge);
        setParent(w, v);

        return this.edges.add(edge);
    }

    protected List<Vertice> pathToRoot(Vertice vertice) {
        List<Vertice> path = new LinkedList<>();

        if (vertice.equals(root)) {
            return path;
        }

        path.add(vertice);
        Vertice v = vertice;

        while (parent(v) != null) {
            v = parent(v);
            path.add(v);
        }

        return path;
    }

    protected Vertice parent(Vertice v) {
        return parents.get(v.getKey());
    }

    protected void setParent(Vertice v, Vertice w) {
        parents.set(v.getKey(), w);
    }

    @Override
    public List<Vertice> pathToRoot(int key) {
        return pathToRoot(parseVertice(key));
    }

    @Override
    protected void removeEdge(Edge e) {
        this.edges.remove(e);
        for (Vertice v : e.getVertices()) {
            v.disconnectEdge(e);
        }
        if (parent(e.end()).equals(e.start())) {
            setParent(e.end(), null);
        }
    }
}
