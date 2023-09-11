package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.WeightedEdge;

public interface WeightedGraph extends Graph {
    
    boolean addEdge(int a, int b, int value);

    WeightedEdge parseEdge(int a, int b);

    int value(int a, int b);

    int distance(int key);

    void setDistance(int key, int i);

    List<WeightedEdge> edges();
}
