package target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.*;
import src.graph.edge.interfaces.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.util.Graphs;
import src.graph.vertices.interfaces.Vertice;

public class Runner {

    private static final boolean ANIMATED_OUTPUT = false;
    private static final boolean PRINT_RESULT = true;
    private static final int SLEEP_TIMER = 1000;
    private static final long SEED = 1;

    private int[] numbers = {10};

    @SuppressWarnings("all")
    public void run() {

        GraphLoader graphLoader = new GraphLoader();
        Ruleset ruleset = new PatternRuleset(loadPattern());

        List<Thread> threads = new ArrayList<>();
        List<WaveFunctionCollapse> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        long t1 = System.currentTimeMillis();

        for (int n : numbers) {
            Mesh2D graph = graphLoader.mesh2D(n); // <-- Meshes are generated here
            System.out.println("%s: Width: %d Height: %d | %d total Nodes".formatted(graph.getMeshType(), graph.getWidth(), graph.getHeight(), graph.getWidth() * graph.getHeight()));
            WaveFunctionCollapse wfc = new WaveFunctionCollapse(graph, ruleset);
            algorithms.add(wfc);
            threads.add(new Thread(wfc));
        }

        long t2 = System.currentTimeMillis();

        System.out.println("Took %.3fs.".formatted((t2 - t1) / 1000f));

        System.out.println("Starting Threads...");

        t1 = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        while (threads.stream().anyMatch(Thread::isAlive)) {
            t2 = System.currentTimeMillis();
            System.out.print("Calculating... ");
            double progressAvg = 1e-20;
            for (WaveFunctionCollapse wfc : algorithms) {
                double progress = wfc.getProgress();
                System.out.print("| %.2f%% |".formatted(progress * 100));
                progressAvg += progress;
            }
            progressAvg /= algorithms.size();
            double elapsedTime = (t2 - t1) / 1000f;
            double remainingTime = ((t2 - t1) / 1000f) / progressAvg - (t2 - t1) / 1000f;
            String eTime;
            String rTime;
            if (elapsedTime > 120) {
                eTime = "%.0fmin".formatted(elapsedTime / 60);
            } else {
                eTime = "%.2fs".formatted(elapsedTime);
            }
            if (remainingTime > 120) {
                rTime = "%.0fmin".formatted(remainingTime / 60);
            } else {
                rTime = "%.2fs".formatted(remainingTime);
            }
                System.out.println(" %s elapsed | Estimated time remaining: %s.".formatted(eTime, rTime));
            
            try {
                Thread.sleep(SLEEP_TIMER);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if (ANIMATED_OUTPUT) {
                printGraph(algorithms.get(0));
            }
        }

        t2 = System.currentTimeMillis();
        System.out.println("%.3fs elapsed.".formatted((t2 - t1) / 1000f));

        if (!PRINT_RESULT) {
            return;
        }

        for (WaveFunctionCollapse wfc : algorithms) {
            printGraph(wfc);
        }

    }

    public static void printGraph(WaveFunctionCollapse wfc) {

        Mesh2D mesh = (Mesh2D) wfc.getGraph();
        Ruleset ruleset = wfc.getRuleset();

        int width = mesh.getWidth();
        int height = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = mesh.getValue(width * i + j);

                sb.append(ruleset.stringRepresentation(x) + " ");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + "\u001B[0m");
        System.out.println();
    }

    public void printRandomPath(WaveFunctionCollapse wfc) {

        Graph g = wfc.getGraph();
        Random random = new Random();

        for (Edge edge : g.edges()) {
            edge.setWeight((edge.start().getValue() + edge.end().getValue()) / 2);
        }

        Vertice start = g.vertices().get(random.nextInt(g.sizeVertices()));
        Vertice end = g.vertices().get(random.nextInt(g.sizeVertices()));

        System.out.println("Starting dijkstra");
        List<Vertice> path = Graphs.dijkstra(g, start.getKey(), end.getKey());

        for (Vertice vertice : path) {
            vertice.setValue(-1);
        }

        printGraph(wfc);
    }

    private int[][] loadPattern() {
        FileLoader fileLoader = null;
        List<String> lines = new ArrayList<>();
        try {
            fileLoader = new FileLoader("files/WaveFunction_Patterns");
            lines = fileLoader.loadSimulationFile("NumbersPattern.pat");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        int[][] arr = new int[lines.size()][lines.size()];
        for (int i = 0; i < lines.size(); i++) {
            String line = lines.get(i);
            String[] chars = line.split(" ");
            for (int j = 0; j < chars.length; j++) {
                arr[i][j] = Integer.parseInt(chars[j]);
            }
        }

        return arr;
    }
}
