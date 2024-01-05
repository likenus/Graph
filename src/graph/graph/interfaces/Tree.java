package src.graph.graph.interfaces;

import java.util.List;

import src.graph.vertices.interfaces.Vertice;

/**
 * A tree is specified as a graph where all vertices have exactly one path to a specified root.
 * This implementation does not fully satsify all criteria of a tree, as this allows for disconnected 
 * tree-components. A more precise definition of the graph would be a forest.
 * This tree does not permit any circles and directed versions always have exactly one defined root.
 * @see Graph
 * @version 1.2
 */
public interface Tree extends Graph {
    
    /**
     * Returns the current root of the tree.
     * @return The root
     */
    Vertice getRoot();

    /**
     * Attempts to set the root to the vertice with the associated key. 
     * This operation will (on directed trees) change the internal structure of the tree to fit the 
     * new root. On non-directional trees the root can be any vertice, that is connected the root.
     */
    void setRoot(int key);

    /**
     * Calculates the path from the given Node to the root, if possible. 
     * @param key Key of the vertice to calculate the path from
     * @return The path to the root. Path is empty if vertice is already root,
     * {@code null} if no such path exists or if the specified vertice does not exist
     */
    List<Vertice> pathToRoot(int key);
}
