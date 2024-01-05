package src.graph.graph.models.directed;

import src.graph.vertices.interfaces.Vertice;

public class DirectedGraph extends DirectedWeightedGraph {

    public DirectedGraph() {
        super();
    }

    public DirectedGraph(int i) {
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
