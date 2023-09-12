package target;

import src.graph.interfaces.Tree;
import src.graph.models.directed.DirectedTree;

public class Main {
    
    public static void main(String[] args) {
        Tree tree = new DirectedTree(5);

        tree.addEdge(0, 1, 0);
        tree.addEdge(1, 2);
    }
}
