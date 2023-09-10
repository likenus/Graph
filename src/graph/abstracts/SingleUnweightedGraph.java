package src.graph.abstracts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.graph.interfaces.UnweightedGraph;

public abstract class SingleUnweightedGraph extends SingleGraph implements UnweightedGraph {

    protected final List<Edge> edges;

    protected SingleUnweightedGraph() {
        this.edges = new LinkedList<>();
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public Edge parseEdge(int a, int b) {
        for (Edge edge : edges) {
            if (edge.getVertices().contains(a) && edge.getVertices().contains(b)) {
                return edge;
            }
        }
        return null;
    }
}
