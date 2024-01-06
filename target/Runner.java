package target;

import java.util.ArrayList;
import java.util.List;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.LandscapeRuleset;
import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.models.undirected.Mesh2D;

public class Runner {

    private static final boolean ANIMATED_OUTPUT = false;
    private static final boolean PRINT_RESULT = true;
    private static final int SLEEP_TIMER = 1000;

    private int[] numbers = {50};

    @SuppressWarnings("all")
    public void run() {

        GraphLoader graphLoader = new GraphLoader();
        Ruleset ruleset = new LandscapeRuleset();

        List<Thread> threads = new ArrayList<>();
        List<WaveFunctionCollapse> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        long t1 = System.currentTimeMillis();

        for (int n : numbers) {
            Mesh2D graph = graphLoader.zylinder(400, 400); // <-- Meshes are generated here
            System.out.println("%s: Width: %d Height: %d | %d total Nodes".formatted(graph.getMeshType(), graph.getWidth(), graph.getHeight(), graph.getWidth() * graph.getHeight()));
            WaveFunctionCollapse wfc = new WaveFunctionCollapse(graph, ruleset, 1);
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

                sb.append(ruleset.stringRepresentation(x) + " ");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + "\u001B[0m");
        System.out.println();
    }
}
