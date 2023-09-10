package src.graph;

import java.util.LinkedList;
import java.util.List;

import src.edge.DirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.interfaces.Tree;
import src.vertices.Node;
import src.vertices.Vertice;

public class DirectedTree extends DirectedGraph implements Tree {
    
    private Vertice root;

    /**
     * Creates a new empty Directed Tree.
     */
    public DirectedTree() {

    }
     
    /**
     * Creates a new Directed Tree with i nodes. Nodes are labeled {@code 0} to {@code i - 1}, 
     * with the 0th node being set as the root.
     * @param i An integer. Must be greater than 0.
     */
    public DirectedTree(int i) {
        if (i < 0) {
            throw new IllegalArgumentException();
        }

        for (int j = 0; j < i; j++) {
            Vertice v = new Node(j);
            this.vertices.add(v);
        }

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
        // TODO implement
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

        boolean b = false;
        for (Edge e : edges) {
            if (e.second().equals(w)) {
                return false;
            }
            if (e.second().equals(v)) {
                b = true;
            }
        }

        if (!b) {
            return false;
        }

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

    @Override
    public List<Vertice> pathToRoot(Vertice vertice) {
        List<Vertice> path = new LinkedList<>();
        path.add(vertice);
        Vertice v = vertice.getParent();

        while (!v.getParent().equals(this.root)) {
            path.add(v);
            v = v.getParent();
        }

        return path;
    }
}
