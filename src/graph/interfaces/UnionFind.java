package src.graph.interfaces;

public interface UnionFind {

    /**
     * Adds a new component to the set.
     * @return always returns true
     */
    boolean add();
    
    /**
     * Merge the components, containing the nodes of the two specified keys.
     * </p>
     * This runs in armotised log*(n) time.
     * @param a The key of the first component
     * @param b The key of the second component
     */
    void union(int a, int b);

    /**
     * Returns the value of a representant of the component associated with the inserted keys node.
     * </p>
     * This runs in armortised log*(n) time.
     * @param key Key of the node, whose component-representant needs to be found
     * @return The value of the component representant
     */
    int find(int key);

    /**
     * Returns the total size of the component set.
     * @return The size
     */
    int size();
}
