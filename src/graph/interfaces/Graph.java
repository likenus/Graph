package src.graph.interfaces;

import java.util.List;

import src.vertices.Vertice;

public interface Graph {
    
    List<Vertice> neighbours(int key);

    List<Vertice> vertices();

    boolean add();

    boolean isEmpty();

    Vertice parseVertice(int key);

    void removeEdge(int a, int b);

    void remove(int key);
}