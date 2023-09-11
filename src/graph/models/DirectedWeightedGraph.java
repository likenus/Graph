package src.graph.models;

import src.edge.interfaces.Edge;
import src.edge.models.DirectedEdge;
import src.graph.abstracts.SingleGraph;
import src.vertices.interfaces.Vertice;

public class DirectedWeightedGraph<T> extends SingleGraph<T> {

    public DirectedWeightedGraph() {
        super();
    }

    public DirectedWeightedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice<T> v = parseVertice(a);
        Vertice<T> w = parseVertice(b);

        return addEdge(v, w, value);
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }

    protected boolean addEdge(Vertice<T> v, Vertice<T> w, int value) {
        if (v == null || w == null) {
            return false;
        }

        Edge<T> edge = new DirectedEdge<>(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);

        return this.edges.add(edge);
    }

    protected boolean addEdge(Vertice<T> v, Vertice<T> w) {
        return addEdge(v, w, 1);
    }

    protected void swapEdge(Vertice<T> a, Vertice<T> b) {
        for (Edge<T> e : a.edges()) {
            if (e.getOther(a).equals(b)) {
                removeEdge(e);
                break;
            }
        }
        this.addEdge(b, a);
    }
}
