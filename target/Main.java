package target;

import src.graph.DirectedTree;
import src.graph.interfaces.Tree;

public class Main {
    
    public static void main(String[] args) {
        
        Tree tree = new DirectedTree(6);

        tree.addEdge(0, 1);
        tree.addEdge(0, 2);
        tree.addEdge(1, 3);
        tree.addEdge(3, 5);
        tree.addEdge(0, 5);

        System.out.println(tree.pathToRoot(5));
        tree.setRoot(3);
        System.out.println(tree.pathToRoot(0));
        System.out.println(tree.pathToRoot(5));


    }
}
