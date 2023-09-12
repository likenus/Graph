package target;

import src.graph.interfaces.Graph;
import src.graph.models.Graphs;
import src.graph.models.UndirectedTree;
import src.graph.models.UndirectedWeightedTree;

public class Main {
    
    public static void main(String[] args) {
        Graph graph = new UndirectedTree(10);

        graph.addEdge(0, 1, 7);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(1, 4, 2);
        graph.addEdge(0, 5, 6);
        graph.addEdge(0, 6, 2);
        graph.addEdge(2, 4);
        graph.addEdge(5, 6, 1);
        graph.addEdge(6, 7, 1);
        graph.addEdge(8, 9, 2);
        graph.addEdge(5, 8, 3);
        graph.addEdge(6, 8, 5);
        graph.addEdge(7, 9, 3);

        Graph mst = Graphs.mst(graph);

        graph.vertices().stream()
            .forEach(v -> System.out.println(v + ": " + v.neighbours()));
    }
}
