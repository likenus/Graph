package target;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.PipesRuleset;
import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.interfaces.Vertice;

public class Runner {

    private static final boolean ANIMATED_OUTPUT = true;
    private static final boolean PRINT_RESULT = false;
    private static final int SLEEP_TIMER = 1000;

    private int[] numbers = {100};


    public void run() {

        GraphLoader graphLoader = new GraphLoader();
        Ruleset ruleset = new PipesRuleset();

        List<Thread> threads = new ArrayList<>();
        List<WaveFunctionCollapse> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        long t1 = System.currentTimeMillis();

        for (int n : numbers) {
            Mesh2D graph = graphLoader.zylinder(n);
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
            if (elapsedTime > 180) {
                eTime = "%.0fmin".formatted(elapsedTime / 60);
            } else {
                eTime = "%.2fs".formatted(elapsedTime);
            }
            if (remainingTime > 180) {
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
                // debugPrint(algorithms.get(0));
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
                String c = null;

                c = ruleset.stringRepresentation(x);
                
                sb.append(c + " ");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + "\u001B[0m");
        System.out.println();
    }

    private void debugPrint(WaveFunctionCollapse wfc) {

        Graph graph = wfc.getGraph();
        List<Boolean> isCollapsed = wfc.getIsCollapsed();
        List<Set<Integer>> possibilities = wfc.getPossibilities();

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
           
        printGraph(wfc);

        for (Vertice vertice : graph.vertices()) {
            if (vertice.getValue() == -1) {
                vertice.setValue(0);
            }
        }
    }
}
