package target;

import src.graph.interfaces.Graph;

public class Main {
    
    public static void main(String[] args) {

        Runner runner = new Runner();

        runner.run();
    }

    private static void foo() {
        GraphLoader graphLoader = new GraphLoader();
        Graph g = graphLoader.mesh2D(3);

        // try {
        //     g = graphLoader.loadFromFile("Graph.dat");

        // } catch (IOException e) {
        //     e.printStackTrace();
        // }
        

        g.vertices().forEach(v -> System.out.println("[%d]".formatted(v.getKey()) + v.neighbours()));
    }
}
