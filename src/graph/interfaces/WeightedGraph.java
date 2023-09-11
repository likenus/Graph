package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.WeightedEdge;
import src.vertices.Vertice;

public interface WeightedGraph extends Graph {
    
    boolean addEdge(Vertice v, Vertice w, int value);

    boolean addEdge(int a, int b, int value);

    WeightedEdge parseEdge(int a, int b);

    int value(WeightedEdge edge);

    int distance(int key);

    void setDistance(int key, int i);

    List<WeightedEdge> edges();
}
