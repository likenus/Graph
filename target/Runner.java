package target;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.*;
import src.graph.edge.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.Vertex;
import src.rendering.GraphRenderer;
import src.rendering.RenderResultFrame;
import src.util.Graphs;

public class Runner {

    public static final long SEED = 69420;

    public static final boolean ANIMATED_OUTPUT = true;
    public static final boolean GUI_OUTPUT = true;
    private static final boolean PRINT_RESULT = false;
    private static final int SLEEP_TIMER = 100; // lower than 100 reintroduces too many race conditions
    
    private final Random random = new Random();
    
    private RenderResultFrame outputFrame;
    private GraphRenderer graphRenderer;

    private int threadCount = 1;
    private int width = 1000;
    private int height = 800;
    private long startTime;
    @SuppressWarnings("all")
    public void run() {

        GraphLoader graphLoader = new GraphLoader();
        
        List<Thread> threads = new ArrayList<>();
        List<WaveFunctionCollapse> algorithms = new ArrayList<>();
        
        System.out.println("Initializing...");
        
        this.startTime = System.currentTimeMillis();
        
        for (int i = 0; i < threadCount; i++) {
            Mesh2D graph = graphLoader.mesh2D(width, height); // <-- Meshes are generated here (Width, Height)
            Ruleset ruleset = new LandscapeRuleset();
            System.out.println("%s: Width: %d Height: %d | %d total Nodes".formatted(graph.getMeshType(), graph.getWidth(), graph.getHeight(), graph.getWidth() * graph.getHeight()));
            WaveFunctionCollapse wfc = new WaveFunctionCollapse(graph, ruleset, SEED);
            graphRenderer = new GraphRenderer(wfc);
            algorithms.add(wfc);
            wfc.setRenderer(graphRenderer);
            threads.add(new Thread(wfc));
        }

        System.out.println("Took %.3fs.".formatted(deltaTime() / 1000f));
        System.out.println("Starting Threads...");


        for (Thread thread : threads) {
            thread.start();
        }

        while (threads.stream().anyMatch(Thread::isAlive)) {
            printInformation(algorithms);

            try {
                Thread.sleep(SLEEP_TIMER);
            } catch (InterruptedException exception) {
                exception.printStackTrace();
            }
            if (ANIMATED_OUTPUT) {
                drawGraph();
            }
        }

        System.out.println("%.3fs elapsed.".formatted(deltaTime() / 1000f));

        if (GUI_OUTPUT) {
            drawGraph();
        }

        if (true) {
            printRandomPath(algorithms.get(0));
        }

        if (PRINT_RESULT) {
            for (WaveFunctionCollapse wfc : algorithms) {
                printGraph(wfc);
            }
        }

    }

    private void printInformation(List<WaveFunctionCollapse> algorithms) {
        double progressAvg = 1e-20;
        StringBuilder waveInformation = new StringBuilder();
        for (WaveFunctionCollapse wfc : algorithms) {
            double progress = wfc.getProgress();
            waveInformation.append("| %.2f%% |".formatted(progress * 100));
            progressAvg += progress;
        }
        progressAvg /= algorithms.size();
        double elapsedTime = deltaTime() / 1000f;
        double remainingTime = (deltaTime() / 1000f) / progressAvg - deltaTime() / 1000f;

        String eTime = timeFormatted(elapsedTime);
        String rTime = timeFormatted(remainingTime);
        String timeInformation = " %s elapsed | Estimated time remaining: %s.".formatted(eTime, rTime);
        String informationString = "Calculating... " + waveInformation.toString() + timeInformation;

        if (this.outputFrame != null) this.outputFrame.setTitle(informationString);
        System.out.println(informationString);
    }

    private static String timeFormatted(double elapsedTime) {
        String formattedTime;
        if (elapsedTime > 120) formattedTime = "%.0fmin".formatted(elapsedTime / 60);
        else formattedTime = "%.2fs".formatted(elapsedTime);
        return formattedTime;
    }

    private long deltaTime(){
        return System.currentTimeMillis() - this.startTime;
    }

    private void drawGraph() {
        if (GUI_OUTPUT) {
            if (outputFrame == null) {
                outputFrame = new RenderResultFrame(graphRenderer.getImage());
            } else {
                outputFrame.updateImage(graphRenderer.getImage());
            }
        }
    }

    private void printGraph(WaveFunctionCollapse wfc) {
        Mesh2D mesh = (Mesh2D) wfc.getGraph();
        Ruleset ruleset = wfc.getRuleset();

        int widthMesh = mesh.getWidth();
        int heightMesh = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < heightMesh; i++) {
            for (int j = 0; j < widthMesh; j++) {
                int x = mesh.getValue(widthMesh * i + j);

                sb.append(ruleset.stringRepresentation(x)).append(" ");
            }
            if (i < heightMesh - 1) {
                sb.append(System.lineSeparator());
            }
        }
        System.out.println(sb.toString() + "\u001B[0m");
        System.out.println(wfc.getErrorCount() + " Errors");
    }

    private void printRandomPath(WaveFunctionCollapse wfc) {

        Graph g = wfc.getGraph();

        for (Edge edge : g.edges()) {
            edge.setWeight((edge.start().getValue() + edge.end().getValue()) / 2);
        }

        Vertex start = g.vertices().get(random.nextInt(g.sizeVertices()));
        Vertex end = g.vertices().get(random.nextInt(g.sizeVertices()));

        System.out.println("Starting dijkstra");
        List<Vertex> path = Graphs.dijkstra(g, start.getKey(), end.getKey());
        System.out.println("Finished Dijkstra");
        for (Vertex vertex : path) {
            vertex.setValue(-1);
        }

        graphRenderer.renderFull();
        drawGraph();
    }

    private int[][] loadPattern() {
        PNGLoader pngLoader = null;
        FileLoader fileLoader = null;
        List<String> lines = new ArrayList<>();
        int[][] png = null;
        try {
            pngLoader = new PNGLoader("files/WaveFunction_Patterns");
            fileLoader = new FileLoader("files/WaveFunction_Patterns");
            lines = fileLoader.loadFile("Pattern.pat");
            png = pngLoader.loadPng("Flowers.png");
        } catch (IOException exception) {
            exception.printStackTrace();
        }

        String[] parts = lines.get(0).split("x");
        lines.remove(0);
        int h = Integer.parseInt(parts[0]);
        int w = Integer.parseInt(parts[1]);
        int[][] arr = new int[h][w];
        for (int i = 0; i < h; i++) {
            String line = lines.get(i);
            String[] chars = line.split(" ");
            for (int j = 0; j < w; j++) {
                arr[i][j] = Integer.parseInt(chars[j]);
            }
        }

        return arr;
    }
}
