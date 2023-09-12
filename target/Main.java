package target;

import java.io.IOException;

import src.graph.interfaces.Graph;
import src.util.Graphs;


public class Main {
    
    public static void main(String[] args) {

        GraphLoader graphLoader = new GraphLoader();
        Graph g = null;

        try {
            g = graphLoader.loadFromFile("Graph2.dat");

        } catch (IOException e) {
            e.printStackTrace();
        }

        Graph mst = Graphs.mst(g);

        double t1 = System.currentTimeMillis();
        for (int i = 0; i < 500; i++) {
            System.out.println(Graphs.bfs(g, 0, i));
        }

        System.out.println((System.currentTimeMillis() - t1) / 1000);
    }
}
