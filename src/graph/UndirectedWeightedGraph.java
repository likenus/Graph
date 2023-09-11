package src.graph;

import src.edge.UndirectedWeightedEdge;
import src.edge.interfaces.WeightedEdge;
import src.graph.abstracts.SingleWeightedGraph;
import src.vertices.Vertice;

public class UndirectedWeightedGraph extends SingleWeightedGraph {

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

        WeightedEdge edge = new UndirectedWeightedEdge(v, w, value);

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
