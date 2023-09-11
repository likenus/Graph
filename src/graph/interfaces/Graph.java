package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.interfaces.Vertice;

public interface Graph<T> {
    
    List<Vertice<T>> neighbours(int key);

    List<Vertice<T>> vertices();

    boolean add();

    boolean isEmpty();

    boolean addEdge(int a, int b);

    Vertice<T> parseVertice(int key);

    void removeEdge(int a, int b);

    void remove(int key);

    boolean addEdge(int a, int b, int value);

    int weight(int a, int b);

    List<Edge<T>> edges();

    Edge<T> parseEdge(int a, int b);
}