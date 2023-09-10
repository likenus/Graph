package src.graph;

import src.edge.DirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.abstracts.SingleUnweightedGraph;
import src.vertices.Vertice;

public class DirectedGraph extends SingleUnweightedGraph {
    
    @Override
    public boolean addEdge(Vertice v, Vertice w) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);

        return this.edges.add(edge);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w);
    }

    @Override
    public Edge parseEdge(int a, int b) {
        for (Edge edge : edges) {
            if (edge.first().getKey() == a && edge.second().getKey() == b) {
                return edge;
            }
        }

        return null;
    }
}
