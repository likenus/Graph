package src.graph.vertices;

import java.util.Iterator;
import java.util.List;

import src.graph.edge.Edge;

/**
 * A vertex in a graph is mainly an abstact object used to connect edges.
 * Vertices have a unique key to identify them in a graph and can hold a numerical value
 * which can be interpreted freely.
 * 
 * @author likenus
 */
public interface Vertex {

    /**
     * Returns all vertices that can be reached from this vertex.
     * @return A list containing all reachable vertices.
     */
    List<Vertex> neighbours();

    /**
     * Returns an iterator which iterates over all vertices that can be reached from this vertex.
     * @return An iterator over all reachable vertices.
     */
    Iterator<Vertex> neighboursIterator();

    /**
     * Returns the key associated with this vertex.
     * @return An integer
     */
    int getKey();

    /**
     * Returns a list view of all incident edges. All changes made to the elements in the list
     * will be reflected in the graph.
     * @return A list containing edges
     */
    List<Edge> edges();

    /**
     * This method is used to directly connect a vertex to an edge. 
     * </p>
     * Use with care!
     * @param e The edge to connect the vertex to
     */
    void connectEdge(Edge e);

    /**
     * This method is used to directly separate an edge from a vertex.
     * </p>
     * Use with care!
     * @param e The edge to be separated
     */
    void separateEdge(Edge e);

    /**
     * The degree of a vertex is the total amount of incident edges. This accounts for outgoing
     * as well is incoming edges. 
     * </p>
     * This means that {@code v.neighbours().size() == v.degree()} is not always {@code true}.
     * @return
     */
    int degree();

    /**
     * Returns the value stored by the vertex. This value defaults to {@code 0}.
     * @return An integer
     */
    int getValue();

    /**
     * Sets the value to be held by the .
     * @param i an integer
     */
    void setValue(int i);
}
