package src.graph.models.directed;

import src.edge.interfaces.Edge;
import src.edge.models.DirectedEdge;
import src.graph.abstracts.SingleGraph;
import src.vertices.interfaces.Vertice;

public class DirectedWeightedGraph extends SingleGraph {

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

        Edge edge = new DirectedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);

        return this.edges.add(edge);
    }

    protected boolean addEdge(Vertice v, Vertice w) {
        return addEdge(v, w, 1);
    }

    protected void swapEdge(Vertice a, Vertice b, int value) {
        for (Edge e : a.edges()) {
            if (e.getOther(a).equals(b)) {
                removeEdge(e);
                break;
            }
        }
        this.addEdge(b, a, value);
    }
}
