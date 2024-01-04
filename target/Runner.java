package target;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.graph.models.undirected.Mesh2D;
import src.util.Tiles;
import src.util.Tiles2;
import src.util.WaveCollapseAlgorithm;
import src.vertices.interfaces.Vertice;

public class Runner {

    private static final boolean ANIMATED_OUTPUT = true;

    public void run() {

        long seed = 274551879641337L;
        GraphLoader graphLoader = new GraphLoader();

        int[] numbers = {50};

        List<Thread> threads = new ArrayList<>();
        List<WaveCollapseAlgorithm> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        long t1 = System.currentTimeMillis();

        for (int n : numbers) {
            Mesh2D graph = graphLoader.zylinder(500, 100);
            System.out.println("%s: width: %d height: %d".formatted(graph.getMeshType(), graph.getWidth(), graph.getHeight()));
            WaveCollapseAlgorithm wca = new WaveCollapseAlgorithm(graph);
            algorithms.add(wca);
            threads.add(new Thread(wca));
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
            for (WaveCollapseAlgorithm wca : algorithms) {
                double progress = wca.getProgress();
                System.out.print("| %.2f%% |".formatted(progress * 100));
                progressAvg += progress;
            }
            progressAvg /= algorithms.size();
            double remainingTime = ((t2 - t1) / 1000f) / progressAvg - (t2 - t1) / 1000f;
            System.out.println(" %.3fs elapsed | Estimated time remaining: %.3fs.".formatted((t2 - t1) / 1000f, remainingTime));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if (ANIMATED_OUTPUT) {
                printGraph2((Mesh2D) algorithms.get(0).getGraph());
            }
        }

        t2 = System.currentTimeMillis();
        System.out.println("%.3fs elapsed.".formatted((t2 - t1) / 1000f));

        for (WaveCollapseAlgorithm wca : algorithms) {
            printGraph2((Mesh2D) wca.getGraph());
        }
    }

    public static void printGraph(Mesh2D mesh) {

        int width = mesh.getWidth();
        int height = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = mesh.getValue(width * i + j);
                String c = null;

                for (Tiles tile : Tiles.values()) {
                    if (x == tile.getIdentifier()) {
                        c = tile.toString();
                    }
                }

                if (c == null) {
                    c = " ";
                }
                
                sb.append(c + " ");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + Tiles.ANSI_RESET);
        System.out.println();
    }

    public static void printGraph2(Mesh2D mesh) {

        int width = mesh.getWidth();
        int height = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = mesh.getValue(width * i + j);
                String c = null;

                for (Tiles2 tile : Tiles2.values()) {
                    if (x == tile.getIdentifier()) {
                        c = tile.toString();
                    }
                }

                if (c == null) {
                    c = " ";
                }
                
                sb.append(c + "");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + Tiles.ANSI_RESET);
        System.out.println();
    }

    private void foo(WaveCollapseAlgorithm wca) {

        Graph graph = wca.getGraph();
        List<Boolean> isCollapsed = wca.getIsCollapsed();
        List<Set<Integer>> possibilities = wca.getPossibilities();

        List<Vertice> notEvaluated = graph.vertices().stream()
            .filter(v -> !isCollapsed.get(v.getKey()))
            .toList();
        
        Optional<Integer> min = notEvaluated.stream()
            .map(v -> possibilities.get(v.getKey()).size())
            .min(Integer::compare);

        if (!min.isPresent()) {
            return;
        }

        notEvaluated.stream()
            .filter(v -> possibilities.get(v.getKey()).size() == min.get())
            .forEach(v -> v.setValue(-1));
           
        printGraph((Mesh2D) graph);

        for (Vertice vertice : graph.vertices()) {
            if (vertice.getValue() == -1) {
                vertice.setValue(0);
            }
        }
    }
}
