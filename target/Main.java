package target;

import src.graph.interfaces.Graph;
import src.util.Graphs;

public class Main {
    
    public static void main(String[] args) {

        Runner runner = new Runner();

        for (int i = 0; i < 1; i++)
            runner.run();
    }

    private static void foo() {
        GraphLoader graphLoader = new GraphLoader();
        Graph g = graphLoader.rnd(100);
        
        System.out.println(Graphs.isOneComponent(g));
    }
}
