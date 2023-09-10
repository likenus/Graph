package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.Vertice;

public interface Graph {
    
    List<Vertice> neighbours(int key);

    List<Vertice> vertices();

    boolean add(int key);

    boolean isEmpty();

    Vertice parseVertice(int key);

    Edge parseEdge(int a, int b);

    void removeEdge(int a, int b);

    void remove(int key);
}