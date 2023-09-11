package src.vertices.interfaces;

import java.util.List;

import src.edge.interfaces.Edge;

public interface Vertice {

    /**
     * Returns all vertices that can be reached from this vertice.
     * @return A list containing all reachable vertices.
     */
    List<Vertice> neighbours();

    /**
     * Returns the key associated with this vertice.
     * @return An integer
     */
    int getKey();

    List<Edge> edges();

    void connectEdge(Edge e);

    void disconnectEdge(Edge e);

    void setParent(Vertice v);

    Vertice getParent();

    int degree();

    int getValue();

    void setValue(int i);
}
