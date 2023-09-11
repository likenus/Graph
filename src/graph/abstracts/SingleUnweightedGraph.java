package src.graph.abstracts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.graph.interfaces.UnweightedGraph;
import src.vertices.Vertice;

public abstract class SingleUnweightedGraph extends SingleGraph implements UnweightedGraph {

    protected final List<Edge> edges;

    protected SingleUnweightedGraph() {
        this.edges = new LinkedList<>();
    }

    protected SingleUnweightedGraph(int i) {
        super(i);
        this.edges = new LinkedList<>();
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public Edge parseEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        for (Edge edge : edges) {
            if (edge.getVertices().contains(v) && edge.getVertices().contains(w)) {
                return edge;
            }
        }
        return null;
    }
}
