package src.graph;

import src.edge.UndirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.abstracts.SingleGraph;
import src.vertices.interfaces.Vertice;

public class UndirectedWeightedGraph extends SingleGraph {

    public UndirectedWeightedGraph() {
        super();
    }

    public UndirectedWeightedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, value);
    }

    protected boolean addEdge(Vertice v, Vertice w, int value) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new UndirectedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);
        w.connectEdge(edge);

        return this.edges.add(edge);
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }
}
