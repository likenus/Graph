package src.edge.interfaces;

import src.Tuple;
import src.vertices.Vertice;

public interface Edge { 
    
    /**
     * Returns the other vertice connected to the edge.
     * @param v
     * @return
     */
    Vertice getOther(Vertice v);

    /**
     * Returns a tuple containing both connected vertices. In directed edges, the tuple is ordered. 
     * Undirected edges do not require an order, thus the returned order is the order of insertion.
     * @return The two connected vertices
     */
    Tuple<Vertice> getVertices();

    /**
     * Returns one of the connected vertices. On directed edges always returns the starting vertice. 
     * On undirected edges this does not imply any direction. Use {@link Edge#getVertices} for undirected edges.
     * @return The starting vertice
     */
    Vertice start();

    /**
     * Returns one of the connected vertices. On directed edges always returns the ending vertice. 
     * On undirected edges this does not imply any direction. Use {@link Edge#getVertices} for undirected edges.
     * @return The ending vertice
     */
    Vertice end();
}
