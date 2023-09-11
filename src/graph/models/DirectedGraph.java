package src.graph.models;

import src.vertices.interfaces.Vertice;

public class DirectedGraph<T> extends DirectedWeightedGraph<T> {

    public DirectedGraph() {
        super();
    }

    public DirectedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice<T> v = parseVertice(a);
        Vertice<T> w = parseVertice(b);

        return addEdge(v, w, 1);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        return addEdge(a, b);
    }
}
