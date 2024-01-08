package src.graph.edge.interfaces;

import src.graph.vertices.interfaces.Vertex;
import src.util.Tuple;

/**
 * Used to connect two vertices in a graph. An edge can be directed or non-directional.
 * This interface does not distuinguish from directed or undirected edges thus implimentations must 
 * take care of that themselves. Every edge must always be connected to two vetices. There cannot be
 * edges which connect to nothing. 
 * @version 1.0
 */
public interface Edge { 
    
    /**
     * Returns the other vertex connected to the edge. 
     * Returns {@code null} if the vertex is not connected to this edge and returns v, if this edge is a loop.
     * @param v The first vertex connected to the edge
     * @return The other vertex connected to the edge
     */
    Vertex getOther(Vertex v);

    /**
     * Returns a tuple containing both connected vertices. In directed edges, the tuple is ordered. 
     * Undirected edges do not require an order, thus the returned order is the order of insertion.
     * @return The two connected vertices
     */
    Tuple<Vertex> getVertices();

    /**
     * Returns one of the connected vertices. On directed edges always returns the starting vertex. 
     * On undirected edges this does not imply any direction. Use {@link Edge#getVertices} for undirected edges.
     * @return The starting vertex
     */
    Vertex start();

    /**
     * Returns one of the connected vertices. On directed edges always returns the ending vertex. 
     * On undirected edges this does not imply any direction. Use {@link Edge#getVertices} for undirected edges.
     * @return The ending vertex
     */
    Vertex end();

    /**
     * Returns the weight value of this edge. Can be negative.
     * @return An integer
     */
    public int getWeight();

    /**
     * Sets the weight value for this edge. Value may be negative.
     * @param weight The value to set the weight to
     */
    public void setWeight(int weight);
}
