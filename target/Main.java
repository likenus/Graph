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

        System.out.println(Graphs.bfs(g, 0, 1));
    }
}
