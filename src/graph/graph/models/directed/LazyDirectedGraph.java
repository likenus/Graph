package src.graph.graph.models.directed;

import src.graph.edge.interfaces.Edge;
import src.graph.edge.models.DirectedEdge;
import src.graph.vertices.interfaces.Vertice;
import src.graph.vertices.models.Node;

/**
 * Lazy Graph for performance optimization of BFS. WARNING: This graph does not keep track if its edges, these are only
 * accessible through the vertices.
 */
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

    @Override
    protected boolean addEdge(Vertice v, Vertice w, int value) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w, value);

        v.connectEdge(edge);

        return true;
    }
}
