package src.vertices.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;

public interface Vertice<T> {

    /**
     * Returns all vertices that can be reached from this vertice.
     * @return A list containing all reachable vertices.
     */
    List<Vertice<T>> neighbours();

    /**
     * Returns the key associated with this vertice.
     * @return An integer
     */
    int getKey();

    List<Edge<T>> edges();

    void connectEdge(Edge<T> e);

    void disconnectEdge(Edge<T> e);

    void setParent(Vertice<T> v);

    Vertice<T> getParent();

    int degree();

    T getValue();

    void setValue(T val);
}
