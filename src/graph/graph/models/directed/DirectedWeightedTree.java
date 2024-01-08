package src.graph.graph.models.directed;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import src.graph.edge.interfaces.Edge;
import src.graph.edge.models.DirectedEdge;
import src.graph.graph.interfaces.Tree;
import src.graph.vertices.interfaces.Vertex;

public class DirectedWeightedTree extends DirectedWeightedGraph implements Tree {
    
    protected final List<Vertex> parents = new ArrayList<>();
    
    protected Vertex root;

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
    public Vertex getRoot() {
        return this.root;
    }

    @Override
    public void setRoot(int key) {
        Vertex oldRoot = this.root;
        Vertex newRoot = parseVertex(key);

        List<Vertex> path = pathToRoot(newRoot);

        if (path.isEmpty() && !edges.isEmpty()) {
            return;
        }

        if (path.size() == 1) {
            Edge e = parseEdge(oldRoot.getKey(), newRoot.getKey());
            swapEdge(oldRoot, newRoot, e.getWeight());
            this.root = newRoot;
            return;
        }
        
        for (int i = 0; i < path.size() - 1; i++) {
            Vertex v = path.get(i);
            Vertex n = path.get(i + 1);

            Edge e = parseEdge(n.getKey(), v.getKey());
            swapEdge(n, v, e.getWeight());
        }

        this.root = newRoot;
    }

    @Override
    protected boolean add(Vertex v) {
        if (super.add(v)) {
            parents.add(null);
            return true;
        }
        return false;
    }

    @Override
    protected boolean addEdge(Vertex v, Vertex w, int value) {
        if (v == null || w == null || w.equals(this.root)) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        boolean valid = false;

        if (v.equals(this.root)) {
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

    protected List<Vertex> pathToRoot(Vertex vertex) {
        List<Vertex> path = new LinkedList<>();

        if (vertex.equals(root)) {
            return path;
        }

        path.add(vertex);
        Vertex v = vertex;

        while (parent(v) != null) {
            v = parent(v);
            path.add(v);
        }

        if (!path.get(path.size() - 1).equals(root)) {
            return new LinkedList<>();
        }

        return path;
    }

    protected Vertex parent(Vertex v) {
        return parents.get(v.getKey());
    }

    protected void setParent(Vertex v, Vertex w) {
        parents.set(v.getKey(), w);
    }

    @Override
    public List<Vertex> pathToRoot(int key) {
        Vertex v = parseVertex(key);
        if (v == null) {
            throw new IllegalArgumentException("Vertex does not exist");
        }

        return pathToRoot(v);
    }

    @Override
    protected void removeEdge(Edge e) {
        this.edges.remove(e);
        for (Vertex v : e.getVertices()) {
            v.separateEdge(e);
        }
        if (parent(e.end()).equals(e.start())) {
            setParent(e.end(), null);
        }
    }
}
