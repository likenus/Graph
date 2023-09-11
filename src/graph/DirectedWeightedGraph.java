package src.graph;

import src.edge.DirectedWeightedEdge;
import src.edge.interfaces.WeightedEdge;
import src.graph.abstracts.SingleWeightedGraph;
import src.vertices.Vertice;

public class DirectedWeightedGraph extends SingleWeightedGraph {

    public DirectedWeightedGraph() {
        super();
    }

    public DirectedWeightedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, value);
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }

    protected boolean addEdge(Vertice v, Vertice w, int value) {
        if (v == null || w == null) {
            return false;
        }

        WeightedEdge edge = new DirectedWeightedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);

        return this.edges.add(edge);
    }
}
