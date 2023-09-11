package src.graph.models;

import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.edge.models.DirectedEdge;
import src.graph.interfaces.Tree;
import src.vertices.interfaces.Vertice;

public class DirectedTree<T> extends DirectedGraph<T> implements Tree<T> {
    
    private Vertice<T> root;

    /**
     * Creates a new empty Directed Tree.
     */
    public DirectedTree() {
        super();
    }
     
    /**
     * Creates a new Directed Tree with i nodes. Nodes are labeled {@code 0} to {@code i - 1}, 
     * with the 0th node being set as the root.
     * @param i An integer. Must be greater than 0.
     */
    public DirectedTree(int i) {
        super(i);

        if (i > 0) {
            this.root = this.vertices.get(0);
        }
    }

    @Override
    public Vertice<T> getRoot() {
        return this.root;
    }

    @Override
    public void setRoot(int key) {
        Vertice<T> oldRoot = this.root;
        Vertice<T> newRoot = parseVertice(key);

        List<Vertice<T>> path = pathToRoot(newRoot);

        if (path.isEmpty()) {
            return;
        }

        if (path.size() == 1) {
            swapEdge(oldRoot, newRoot);
            this.root = newRoot;
            return;
        }
        
        for (int i = 0; i < path.size() - 1; i++) {
            Vertice<T> v = path.get(i);
            Vertice<T> n = path.get(i + 1);

            swapEdge(n, v);
        }

        this.root = newRoot;
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice<T> v = parseVertice(a);
        Vertice<T> w = parseVertice(b);

        return addEdge(v, w);
    }

    @Override
    protected boolean addEdge(Vertice<T> v, Vertice<T> w) {
        if (v == null || w == null || w.equals(this.root)) {
            return false;
        }

        Edge<T> edge = new DirectedEdge<>(v, w);

        if (this.edges.contains(edge)) {
            return false;
        }

        boolean valid = false;

        if (this.edges.isEmpty()) {
            valid = true;
        }

        for (Edge<T> e : edges) {
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
        w.setParent(v);

        return this.edges.add(edge);
    }

    private List<Vertice<T>> pathToRoot(Vertice<T> vertice) {
        List<Vertice<T>> path = new LinkedList<>();

        if (vertice.equals(root)) {
            return path;
        }

        path.add(vertice);
        Vertice<T> v = vertice;

        while (v.getParent() != null) {
            v = v.getParent();
            path.add(v);
        }

        return path;
    }

    @Override
    public List<Vertice<T>> pathToRoot(int key) {
        return pathToRoot(parseVertice(key));
    }

    @Override
    protected void removeEdge(Edge<T> e) {
        this.edges.remove(e);
        for (Vertice<T> v : e.getVertices()) {
            v.disconnectEdge(e);
        }
        if (e.end().getParent().equals(e.start())) {
            e.end().setParent(null);
        }
    }
}
