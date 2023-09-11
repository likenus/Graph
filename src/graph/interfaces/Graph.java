package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.interfaces.Vertice;

public interface Graph {
    
    List<Vertice> neighbours(int key);

    List<Vertice> vertices();

    boolean add();

    boolean isEmpty();

    boolean addEdge(int a, int b);

    Vertice parseVertice(int key);

    void removeEdge(int a, int b);

    void remove(int key);

    boolean addEdge(int a, int b, int value);

    int value(int a, int b);

    List<Edge> edges();

    Edge parseEdge(int a, int b);
}