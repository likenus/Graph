package target;

import java.util.ArrayList;
import java.util.List;

import src.graph.interfaces.Graph;
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

    public void run() {
        GraphLoader graphLoader = new GraphLoader();

        int[] numbers = {100};

        List<Thread> threads = new ArrayList<>();
        List<WaveCollapseAlgorithm> algorithms = new ArrayList<>();

        System.out.println("Initializing...");

        for (int n : numbers) {
            WaveCollapseAlgorithm wca = new WaveCollapseAlgorithm(graphLoader.zylinder(n));
            algorithms.add(wca);
            threads.add(new Thread(wca));
        }

        System.out.println("Starting Threads...");

        long t1 = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        while (threads.stream().anyMatch(Thread::isAlive)) {
            long t2 = System.currentTimeMillis();
            System.out.print("Calculating... ");
            double progressAvg = 1e-20;
            for (WaveCollapseAlgorithm wca : algorithms) {
                double progress = wca.getProgress();
                System.out.print("| %.2f%% |".formatted(progress * 100));
                progressAvg += progress;
            }
            progressAvg /= algorithms.size();
            System.out.println(" %.3fs elapsed | Estimated time remaining: %.3fs.".formatted((t2 - t1) / 1000f, ((t2 - t1) / 1000f) / progressAvg - (t2 - t1) / 1000f));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        long t2 = System.currentTimeMillis();
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
