package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.WeightedEdge;
import src.vertices.Vertice;

public interface WeightedGraph extends Graph {
    
    boolean addEdge(Vertice v, Vertice w, int value);

    boolean addEdge(int a, int b, int value);

    int value(WeightedEdge edge);

    int distance(Vertice v);

    void setDistance(Vertice v, int i);

    List<WeightedEdge> edges();
}
