package src.graph.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;
import src.vertices.interfaces.Vertice;

/**
 * TODO javadoc
 */
public interface Graph {
    
    /**
     * Returns the neighbours of a vertice. The neighbours are other vertices,
     * that are available from the current vertice. Not all vertices that are
     * connected to the node are neccessarily neighbours.
     * @param key The key of the vertice to get the neighbours from
     * @return A list of vertices
     */
    List<Vertice> neighbours(int key);

    /**
     * Returns a list view of all vertices in the graph. Changes made to this list
     * are reflected in the graph.
     * @return A unmodifiable List of vertices
     */
    List<Vertice> vertices();

    /**
     * Adds a Vertice to the graph. Vertices are indexed starting from 0.
     * The added vertice will have the key {@code n - 1}, where n is the 
     * total amount of vertices.
     * @return true on succesfull call
     */
    boolean addVertice();

    /**
     * Checks if the graph is empty. 
     * </p>
     * A graph is considered empty when it has no vertices.
     * @return True if empty, else false
     */
    boolean isEmpty();

    /**
     * Adds an edge to the graph. An edge must connect two vertices and cannot
     * point into the void. Some graphs may not support multiple edges between 
     * the same vertices.
     * </p>
     * Note: Some graphs are order-sensitive.
     * @param a The key of the starting vertice
     * @param b The key of the ending vertice
     * @return True if the edge was successfully added
     */
    boolean addEdge(int a, int b);

    /**
     * Parses a key to its corresponding vertice. Will return {@code null}
     * if the vertice does not exist. 
     * </p>
     * This runs in constant time.
     * @param key The key of a vertice
     * @return A vertice
     */
    Vertice parseVertice(int key);

    /**
     * Removes an edge from the graph. This will do nothing if the edge was not found.
     * </p>
     * Note: Some graphs are order-sensistve.
     * @param a The key of the starting vertice
     * @param b The key of the ending vertice
     */
    void removeEdge(int a, int b);

    /**
     * Adds a weighted edge. All rules of {@link #addEdge(int, int)} apply.
     * Additionally unweighted graphs will ignore the weight.
     * @param a The key of the starting vertice
     * @param b The key of the ending vertice
     * @param value The weight of the edge
     * @return True on successful call
     */
    boolean addEdge(int a, int b, int value);

    /**
     * Returns the value of the vertice associated with the key.
     * @param key The key of the corresponding vertice
     * @return The value of the vertice
     * @see Vertice
     */
    int getValue(int key);

    void set(int key, int value);

    int value(int a, int b);

    List<Edge> edges();

    Edge parseEdge(int a, int b);

    int sizeVertices();

    int sizeEdges();
}