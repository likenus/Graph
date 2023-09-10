package src.graph;

import src.edge.UndirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.abstracts.SingleUnweightedGraph;
import src.vertices.Vertice;

public class UndirectedGraph extends SingleUnweightedGraph {

    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w);
    }

    @Override
    public boolean addEdge(Vertice v, Vertice w) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new UndirectedEdge(v, w);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);
        w.connectEdge(edge);

        return this.edges.add(edge);
    }
}
