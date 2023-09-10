package src.edge.interfaces;

import src.Tuple;
import src.vertices.Vertice;

public interface Edge { 
    
    Vertice getOther(Vertice v);

    Tuple<Vertice> getVertices();

    /**
     * Returns one of the connected vertices. On directed edges always returns the starting vertice. 
     * On undirected edges this does not imply any direction. Use {@link getVertices} for undirected edges.
     * @return The starting vertice
     */
    Vertice start();

    /**
     * Returns one of the connected vertices. On directed edges always returns the ending vertice. 
     * On undirected edges this does not imply any direction. Use {@link getVertices} for undirected edges.
     * @return The ending vertice
     */
    Vertice end();
}
