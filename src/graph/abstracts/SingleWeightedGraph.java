package src.graph.abstracts;

import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.edge.interfaces.WeightedEdge;
import src.graph.interfaces.WeightedGraph;
import src.vertices.Vertice;

public abstract class SingleWeightedGraph extends SingleGraph implements WeightedGraph {
    
    protected final List<WeightedEdge> edges;
    protected final List<Integer> distances;

    protected SingleWeightedGraph() {
        this.edges = new LinkedList<>();
        this.distances = new ArrayList<>();
    }

    protected SingleWeightedGraph(int i) {
        super(i);
        this.edges = new LinkedList<>();
        this.distances = new ArrayList<>();    
    }

    @Override
    public List<WeightedEdge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public WeightedEdge parseEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);
        
        for (WeightedEdge edge : edges) {
            if (edge.getVertices().contains(v) && edge.getVertices().contains(w)) {
                return edge;
            }
        }

        return null;
    }

    @Override
    protected boolean add(Vertice v) {
        boolean success = super.add(v);
        distances.add(v.getKey(), 0);

        return success;
    }

    @Override
    public int value(int a, int b) {
        WeightedEdge edge = parseEdge(a, b);

        if (edge == null) {
            throw new IllegalArgumentException();
        }

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

        if (edge == null) {
            return;
        }

        removeEdge(edge);
    }

    protected void removeEdge(Edge e) {
        this.edges.remove(e);
    }

    @Override
    public int distance(int key) {
        return distances.get(key);
    }

    @Override
    public void setDistance(int key, int i) {
        distances.set(key, i);
    }
}
