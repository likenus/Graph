package src.graph;

import java.util.LinkedList;
import java.util.List;

import src.edge.DirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.interfaces.Tree;
import src.vertices.Vertice;

public class DirectedTree extends DirectedGraph implements Tree {
    
    private Vertice root;

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

    private void swapEdge(Vertice a, Vertice b) {
        for (Edge e : a.edges()) {
            if (e.getOther(a).equals(b)) {
                removeEdge(e);
                break;
            }
        }

        addEdgeRaw(b, a);
    }

    @Override
    public boolean addEdge(Vertice v, Vertice w) {
        if (v == null || w == null || w.equals(this.root)) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w);

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
        w.setParent(v);

        return this.edges.add(edge);
    }

    private boolean addEdgeRaw(Vertice v, Vertice w) {
        Edge edge = new DirectedEdge(v, w);

        v.connectEdge(edge);
        w.setParent(v);

        return this.edges.add(edge);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w);
    }

    private List<Vertice> pathToRoot(Vertice vertice) {
        List<Vertice> path = new LinkedList<>();

        if (vertice.equals(root)) {
            return path;
        }

        path.add(vertice);
        Vertice v = vertice;

        while (v.getParent() != null) {
            v = v.getParent();
            path.add(v);
        }

        return path;
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
        if (e.end().getParent().equals(e.start())) {
            e.end().setParent(null);
        }
    }
}
