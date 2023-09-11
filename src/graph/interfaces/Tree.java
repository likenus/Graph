package src.graph.interfaces;

import java.util.List;

import src.vertices.interfaces.Vertice;

public interface Tree<T> extends Graph<T> {
    
    /**
     * Returns the current root of the tree.
     * @return The root
     */
    Vertice<T> getRoot();

    /**
     * Attempts to set the root to the vertice with the associated key. 
     * This call will change the internal structure of the tree to fit the new root.
     */
    void setRoot(int key);

    List<Vertice<T>> pathToRoot(int key);
}
