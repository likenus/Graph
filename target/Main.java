package target;

import java.io.IOException;

import src.graph.interfaces.Graph;
import src.util.Graphs;

public class Main {
    
    public static void main(String[] args) {
        foo();
    }

    private static void foo() {
        GraphLoader graphLoader = new GraphLoader();
        Graph g = null;

        try {
            g = graphLoader.loadFromFile("Graph.dat");

        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Graph mst = Graphs.mst(g);

        mst.vertices().forEach(v -> System.out.println(v.neighbours()));
    }
}
