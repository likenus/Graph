package src.graph.abstracts;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.WeightedEdge;
import src.graph.interfaces.WeightedGraph;

public abstract class SingleWeightedGraph extends SingleGraph implements WeightedGraph {
    
    protected final List<WeightedEdge> edges;

    protected SingleWeightedGraph() {
        this.edges = new LinkedList<>();
    }

    @Override
    public List<WeightedEdge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public WeightedEdge parseEdge(int a, int b) {
        for (WeightedEdge edge : edges) {
            if (edge.getVertices().contains(a) && edge.getVertices().contains(b)) {
                return edge;
            }
        }

        return null;
    }

    @Override
    public int value(WeightedEdge edge) {
        return edge.getWeight();
    }
}
