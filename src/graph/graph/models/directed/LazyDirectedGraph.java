package src.graph.graph.models.directed;

import src.graph.vertices.interfaces.Vertice;
import src.graph.vertices.models.Node;

public class LazyDirectedGraph extends DirectedGraph {
    public LazyDirectedGraph(int startNode) {
        add(new Node(startNode));
    }
    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        if (v == null) {
            v = new Node(a);
            add(v);
        }
        Vertice w = parseVertice(b);
        if (w == null) {
            w = new Node(b);
            add(w);
        }

        return addEdge(v, w, 1);
    }
}
