package src.graph.abstracts;

import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import src.edge.interfaces.Edge;
import src.edge.interfaces.WeightedEdge;
import src.graph.interfaces.WeightedGraph;
import src.vertices.Vertice;

public abstract class SingleWeightedGraph extends SingleGraph implements WeightedGraph {
    
    protected final List<WeightedEdge> edges;
    protected final Map<Vertice, Integer> distances;

    protected SingleWeightedGraph() {
        this.edges = new LinkedList<>();
        this.distances = new HashMap<>();
    }

    protected SingleWeightedGraph(int i) {
        super(i);
        this.edges = new LinkedList<>();
        this.distances = new HashMap<>();    
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
    protected boolean add(Vertice v) {
        boolean success = super.add(v);
        distances.put(v, 0);

        return success;
    }

    @Override
    public int value(WeightedEdge edge) {
        return edge.getWeight();
    }

    @Override
    public void remove(int key) {
        Vertice v = parseVertice(key);
        for (Edge edge : v.edges()) {
            removeEdge(edge);
        }

        this.vertices.remove(v);
    }

    @Override
    public void removeEdge(int a, int b) {
        Edge edge = parseEdge(a, b);

        removeEdge(edge);
    }

    protected void removeEdge(Edge e) {
        this.edges.remove(e);
    }

    @Override
    public int distance(Vertice v) {
        return distances.get(v);
    }

    @Override
    public void setDistance(Vertice v, int i) {
        distances.replace(v, i);
    }
}
