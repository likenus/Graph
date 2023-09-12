package src.graph.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;
import src.edge.models.DirectedEdge;
import src.vertices.interfaces.Vertice;

public class DirectedTree extends DirectedWeightedTree {    

    /**
     * Creates a new empty Directed Tree.
     */
    public DirectedTree() {
        super();
    }
     
    /**
     * Creates a new Directed Tree with i nodes. Nodes are labeled {@code 0} to {@code i - 1}, 
     * with the 0th node being set as the root.
     * @param i An integer. Must be greater than 0.
     */
    public DirectedTree(int i) {
        super(i);

        if (i > 0) {
            this.root = this.vertices.get(0);
        }
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, 1);
    }
}
