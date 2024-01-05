package src.graph.graph.models.undirected;

import java.util.List;

import src.graph.edge.interfaces.Edge;
import src.graph.edge.models.UndirectedEdge;
import src.graph.graph.interfaces.Tree;
import src.graph.graph.interfaces.UnionFind;
import src.graph.graph.models.directed.ComponentSet;
import src.graph.util.Graphs;
import src.graph.vertices.interfaces.Vertice;

public class UndirectedWeightedTree extends UndirectedWeightedGraph implements Tree {

    private Vertice root;
    private UnionFind uf = new ComponentSet();

    public UndirectedWeightedTree() {
        super();
    }

    public UndirectedWeightedTree(int i) {
        super(i);

        for (int j = 0; j < i; j++) {
            uf.add();
        }

        if (i > 0) {
            this.root = this.vertices.get(0);
        }
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, value);
    }

    @Override
    protected boolean addEdge(Vertice v, Vertice w, int value) {
        if (v == null || w == null) {
            return false;
        }

        if (edges.size() < uf.size()) {
            this.uf = new ComponentSet(vertices.size());

            for (Edge edge : edges) {
                uf.union(edge.start().getKey(), edge.end().getKey());
            }
        }

        if (uf.find(v.getKey()) == uf.find(w.getKey())) {
            return false;
        }

        Edge edge = new UndirectedEdge(v, w, value);
        v.connectEdge(edge);
        w.connectEdge(edge);
        uf.union(v.getKey(), w.getKey());

        return this.edges.add(edge);
    }

    @Override
    public Vertice getRoot() {
        return this.root;
    }

    @Override
    public void setRoot(int key) {
        if (uf.find(key) == uf.find(root.getKey())) {
            this.root = this.vertices.get(key);
        }
    }

    @Override
    public List<Vertice> pathToRoot(int key) {
        return Graphs.bfs(this, this.root.getKey(), key);
    }
}
