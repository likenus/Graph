package src.graph.models;

import src.vertices.interfaces.Vertice;

public class UndirectedGraph extends UndirectedWeightedGraph {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, 1);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        return addEdge(a, b);
    }
}
