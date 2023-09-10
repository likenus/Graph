package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.WeightedEdge;
import src.vertices.Vertice;

public interface WeightedGraph extends Graph {
    
    boolean addEdge(Vertice v, Vertice w, int value);

    boolean addEdge(int a, int b, int value);

    int value(WeightedEdge edge);

    List<WeightedEdge> edges();
}
