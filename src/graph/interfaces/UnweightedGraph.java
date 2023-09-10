package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.Vertice;

public interface UnweightedGraph extends Graph {
    
    List<Edge> edges();

    boolean addEdge(Vertice v, Vertice w);

    boolean addEdge(int a, int b);
}
