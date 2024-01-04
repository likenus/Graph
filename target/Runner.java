package target;

import java.util.ArrayList;
import java.util.List;

import src.graph.models.undirected.Mesh2D;
import src.util.WaveCollapseAlgorithm;

public class Runner {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String TILE_SYMBOL = "■";

    private static final boolean animateOutput = false;

    public void run() {
        GraphLoader graphLoader = new GraphLoader();

        int[] numbers = {99, 100, 101};

        List<Thread> threads = new ArrayList<>();
        List<WaveCollapseAlgorithm> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        long t1 = System.currentTimeMillis();

        for (int n : numbers) {
            Mesh2D graph = graphLoader.zylinder(n);
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

        double oldRemainingTime = 1e10;

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
            double deltaTime = Math.abs(oldRemainingTime - remainingTime);
            System.out.println(" %.3fs elapsed | Estimated time remaining: %.3fs.".formatted((t2 - t1) / 1000f, remainingTime));
            oldRemainingTime = remainingTime / deltaTime;
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if (animateOutput) {
                printGraph((Mesh2D) algorithms.get(0).getGraph());
            }
        }

        t2 = System.currentTimeMillis();
        System.out.println("%.3fs elapsed.".formatted((t2 - t1) / 1000f));

        for (WaveCollapseAlgorithm wca : algorithms) {
            printGraph((Mesh2D) wca.getGraph());
        }
    }

    public static void printGraph(Mesh2D mesh) {

        int width = mesh.getWidth();
        int height = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = mesh.getValue(width * i + j);
                String c;
                switch(x) {
                    case 1:  c = ANSI_GREEN + TILE_SYMBOL;
                        break;
                    case 2:  c = ANSI_YELLOW + TILE_SYMBOL;
                        break;
                    case 3:  c = ANSI_CYAN + TILE_SYMBOL;
                        break;
                    case 4:  c = ANSI_BLACK + TILE_SYMBOL;
                        break;
                    case 5:  c = ANSI_BLUE + TILE_SYMBOL;
                        break;
                    case -1: c = ANSI_RED + TILE_SYMBOL;
                        break;
                    default: c = " ";
                }
                sb.append(c + " ");
            }
            if (i < height - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + ANSI_RESET);
        System.out.println();
    }
}
