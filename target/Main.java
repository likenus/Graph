package target;

import java.io.IOException;

import src.graph.interfaces.Graph;
import src.graph.models.undirected.UndirectedGraph;
import src.util.Graphs;
import src.util.WaveCollapseAlgorithm;

public class Main {
    
    public static void main(String[] args) {
        bar();
    }

    private static void foo() {
        GraphLoader graphLoader = new GraphLoader();
        Graph g = null;

        try {
            g = graphLoader.loadFromFile("Graph.dat");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Graph mst = Graphs.bfsTree(g, 49);
        System.out.println("Path " + Graphs.bfs(g, 49, 0));

        mst.vertices().forEach(v -> System.out.println("[%d]".formatted(v.getKey()) + v.neighbours()));
    }

    private static void bar() {
        // UndirectedGraph g = new UndirectedGraph(9);
        // g.addEdge(0, 1);
        // g.addEdge(1, 2);
        // g.addEdge(3, 4);
        // g.addEdge(4, 5);
        // g.addEdge(6, 7);
        // g.addEdge(7, 8);
        // g.addEdge(0, 3);
        // g.addEdge(1, 4);
        // g.addEdge(2, 5);
        // g.addEdge(3, 6);
        // g.addEdge(4, 7);
        // g.addEdge(5, 8);
        // g.addEdge(0, 5);

        Graph g = null;
        GraphLoader graphLoader = new GraphLoader();
        try {
            g = graphLoader.loadFromFile("Graph.dat");

        } catch (IOException e) {
            e.printStackTrace();
        }

        g = Graphs.mst(g);
        WaveCollapseAlgorithm wca = new WaveCollapseAlgorithm(g);

        wca.evaluate();

        String s = "";
        int n = (int) Math.sqrt(g.sizeVertices());
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = g.get(n * i + j);
                s += x + " ";
            }
            if (i < n - 1) {
                s += System.lineSeparator();
            }
        }

        System.out.println(s);
    }
}
