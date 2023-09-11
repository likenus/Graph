package target;

import src.edge.interfaces.WeightedEdge;
import src.graph.DirectedTree;
import src.graph.DirectedWeightedGraph;
import src.graph.Graphs;
import src.graph.interfaces.Tree;
import src.graph.interfaces.WeightedGraph;

public class Main {
    
    public static void main(String[] args) {
        
        WeightedGraph g = new DirectedWeightedGraph(5);

        g.addEdge(0, 1, 10);
        g.addEdge(0, 2, 5);
        g.addEdge(2, 1, 1);
        g.addEdge(0, 3, 1);
        g.addEdge(3, 2, 1);

        System.out.println(Graphs.dijkstra(g, 0, 1));
    }
}
