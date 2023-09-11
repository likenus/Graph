package src.graph.models;

import src.edge.interfaces.Edge;
import src.edge.models.UndirectedEdge;
import src.graph.abstracts.SingleGraph;
import src.vertices.interfaces.Vertice;

public class UndirectedWeightedGraph<T> extends SingleGraph<T> {

    public UndirectedWeightedGraph() {
        super();
    }

    public UndirectedWeightedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice<T> v = parseVertice(a);
        Vertice<T> w = parseVertice(b);

        return addEdge(v, w, value);
    }

    protected boolean addEdge(Vertice<T> v, Vertice<T> w, int value) {
        if (v == null || w == null) {
            return false;
        }

        Edge<T> edge = new UndirectedEdge<>(v, w, value);

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
