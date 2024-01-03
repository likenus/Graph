package target;

import java.util.ArrayList;
import java.util.List;

import src.graph.interfaces.Graph;
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

    public void run() {
        GraphLoader graphLoader = new GraphLoader();

        int[] numbers = {100};

        List<Thread> threads = new ArrayList<>();
        List<WaveCollapseAlgorithm> algorithms = new ArrayList<>();

        for (int n : numbers) {
            WaveCollapseAlgorithm wca = new WaveCollapseAlgorithm(graphLoader.mesh2D(n));
            algorithms.add(wca);
            threads.add(new Thread(wca));
        }

        long t1 = System.currentTimeMillis();

        for (Thread thread : threads) {
            thread.start();
        }

        while (threads.stream().anyMatch(Thread::isAlive)) {
            long t2 = System.currentTimeMillis();
            System.out.print("Calculating... ");
            for (WaveCollapseAlgorithm wca : algorithms) {
                System.out.print("| %.2f%% |".formatted(wca.getProgress() * 100));
            }
            System.out.println(" %.3fs elapsed.".formatted((t2 - t1) / 1000f));
            try {
                Thread.sleep(1000);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
        }

        long t2 = System.currentTimeMillis();
        System.out.println("%.3fs elapsed.".formatted((t2 - t1) / 1000f));

        for (WaveCollapseAlgorithm wca : algorithms) {
            printGraph(wca.getGraph());
        }
    }

    public static void printGraph(Graph g) {

        int n = (int) Math.sqrt(g.sizeVertices());

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                int x = g.get(n * i + j);
                String c;
                switch(x) {
                    case 1:  c = ANSI_GREEN + "X" + ANSI_RESET;
                        break;
                    case 2:  c = ANSI_YELLOW + "X" + ANSI_RESET;
                        break;
                    case 3:  c = ANSI_CYAN + "X" + ANSI_RESET;
                        break;
                    case 4:  c = ANSI_BLACK + "X" + ANSI_RESET;
                        break;
                    case 5:  c = ANSI_BLUE + "X" + ANSI_RESET;
                        break;
                    case -1: c = ANSI_RED + "X" + ANSI_RESET;
                        break;
                    default: c = " ";
                }
                sb.append(c + " ");
            }
            if (i < n - 1) {
                sb.append(System.lineSeparator());
            }
        }

        System.out.println(sb.toString());
        System.out.println();
    }
}
