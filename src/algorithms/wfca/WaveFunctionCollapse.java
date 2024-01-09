package src.algorithms.wfca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.models.directed.DirectedGraph;
import src.graph.graph.models.directed.LazyDirectedGraph;
import src.graph.vertices.Vertex;
import src.graph.graph.interfaces.Graph;
import src.rendering.GraphRenderer;
import src.util.BinaryHeap;
import target.Runner;

/**
 * The Wave Function Collapse Algorithm, is an algorithm for procedual
 * generation.
 * </p>
 * The Algorithm starts with a blank field of tiles. Every tile is in a
 * superposition meaning it has different possible states it can collapse
 * to. Collapsing a tile will force it to choose one state.
 * Then according to a fixed set of rules, surrounding tiles will lose possible
 * states, thus reducing their entropy. Then the algorithm iteratively
 * collapses the tile with the lowest entropy until all tiles are collapsed.
 * </p>
 * This implementation uses graphs as a basis and a fixed ruleset to
 * generate randomized patterns.
 * </p>
 * To use this algorithm generate a graph to feed to the algorithm. The
 * algorithm will modify the graph and does NOT create an internal copy.
 * The graph can either be kept after the
 * evaluation or retrieved via {@link #getGraph()}. To start the algorithm
 * simply use {@link #run()} implemented via the {@code Runnable} interface.
 * </p>
 * This algorithm runs in quasi-linear time.
 * 
 * @version 2.2
 * 
 * @author likenus
 * 
 * @see src.util.Graphs#dijkstra()
 */
public class WaveFunctionCollapse implements Runnable {
    private static final int UPDATES_PER_RENDER = 2000;

    private final List<Boolean> isCollapsed = new ArrayList<>();
    private final BinaryHeap<Vertex> notCollapsed = new BinaryHeap<>();
    private final List<Set<Integer>> possibilities = new ArrayList<>();

    GraphRenderer renderer;
    private Vertex[] updatedSinceRender = new Vertex[UPDATES_PER_RENDER];
    private int renderCounter = 0;

    private final Ruleset ruleset;
    private final Vertex startVertex;
    private final Graph graph;
    private final Random rnd;
    private final int n;

    private int errorCounter = 0;

    private WaveFunctionCollapse(Graph graph, Ruleset ruleset, Random rnd) {

        for (int i = 0; i < graph.sizeVertices(); i++) {
            possibilities.add(new HashSet<>());
        }

        this.rnd = rnd;
        this.graph = graph;
        this.n = graph.sizeVertices();
        this.ruleset = ruleset;

        this.startVertex = this.graph.parseVertex(rnd.nextInt(n));

        for (Vertex v : this.graph.vertices()) {
            Set<Integer> initialPossibilities = ruleset.initialPossibilities(v);
            if (!v.equals(startVertex)) {
                notCollapsed.push(v, initialPossibilities.size());
            }
            isCollapsed.add(false);
            possibilities.set(v.getKey(), initialPossibilities);
        }

        this.renderer = new GraphRenderer(this);
    }

    /**
     * Initializes a new Wave Function Collapse Algorithm with a random seed.
     * 
     * @param graph The graph to be collapsed
     */
    public WaveFunctionCollapse(Graph graph, Ruleset ruleset) {
        this(graph, ruleset, new Random());
    }

    /**
     * Initializes a new Wave Function Collapse Algorithm with a set seed.
     * 
     * @param graph The graph to be collapsed
     * @param seed  The seed
     */
    public WaveFunctionCollapse(Graph graph, Ruleset ruleset, long seed) {
        this(graph, ruleset, new Random(seed));
    }

    @Override
    public void run() {
        evaluate();
    }

    private void evaluate() {
        collapse(startVertex);
        while (!notCollapsed.isEmpty()) {
            Vertex v = notCollapsed.pop();
            collapse(v);
        }
        if (Runner.GUI_OUTPUT) {
            renderer.renderFull();
        }
    }

    private void collapse(Vertex v) {
        Set<Integer> possibleInts = possibilities.get(v.getKey());

        if (possibleInts.isEmpty()) {
            possibleInts = Set.of(-1); // This can crash the algorithm, depending on the ruleset
            errorCounter++;
        }

        int value = possibleInts.stream().toList().get(rnd.nextInt(possibleInts.size()));
        v.setValue(value);
        int key = v.getKey();

        possibilities.set(key, Set.of(value));
        isCollapsed.set(key, true);

        boolean[] exploredNodes = new boolean[graph.sizeVertices()];
        int[] distances = new int[graph.sizeVertices()];
        Queue<Vertex> queue = new ConcurrentLinkedQueue<>();
        exploredNodes[v.getKey()] = true;
        distances[v.getKey()] = 0;
        for (Vertex w: v.neighbours()) {
            queue.add(w);
            exploredNodes[w.getKey()] = true;
            distances[w.getKey()] = 1;
        }
        update(queue, exploredNodes, distances);

        if (Runner.GUI_OUTPUT && Runner.ANIMATED_OUTPUT) {
            updatedSinceRender[renderCounter++] = v;
            if (renderCounter >= UPDATES_PER_RENDER) {
                renderer.renderDiff();
                renderCounter = 0;
            }
        }
    }

    private void update(Queue<Vertex> queue, boolean[] exploredNodes, int[] distances) {
        while (!queue.isEmpty()) {
            Vertex v = queue.poll();
            if (distances[v.getKey()] > ruleset.maxBFSDepth()) {
                continue;
            }
            if (isCollapsed.get(v.getKey())) {
                exploredNodes[v.getKey()] = true;
                continue;
            }

            boolean changed = false;

            Set<Integer> possibleInts = ruleset.ruleset(this.graph, v, possibilities); // Magic happens in here

            if (!possibilities.get(v.getKey()).equals(possibleInts)) {
                possibilities.set(v.getKey(), possibleInts);
                changed = true;
                notCollapsed.decPrio(v, possibleInts.size());
            }

            if (changed) {
                Iterator<Vertex> neighborIt = v.neighboursIterator();
                while (neighborIt.hasNext()) {
                    Vertex w = neighborIt.next();
                    if (!exploredNodes[w.getKey()]) {
                        queue.add(w);
                        distances[w.getKey()] = distances[v.getKey()] + 1;
                        exploredNodes[v.getKey()] = true;
                    }
                }
            }
        }
    }

    public int getErrorCount() {
        return errorCounter;
    }

    /**
     * This bfs tree is modified, for a full version see
     * {@link src.util.Graphs#bfsTree()}
     *
     * @param g a graph
     * @param s the starting vertex
     * @return The search tree
     */
    private DirectedGraph bfsTree(Graph g, int s) {

        boolean[] exploredNodes = new boolean[g.sizeVertices()];
        int[] distances = new int[g.sizeVertices()];

        DirectedGraph tree = new LazyDirectedGraph(s);
        Vertex start = g.parseVertex(s);
        int maxBFSDepth = ruleset.maxBFSDepth();

        if (start == null) {
            throw new IllegalArgumentException("Start does not exist"); // Should never be thrown here
        }

        Queue<Vertex> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        exploredNodes[s] = true;
        distances[s] = 0;

        // BFS
        while (!queue.isEmpty()) {
            Vertex u = queue.poll();
            if (distances[u.getKey()] > maxBFSDepth) {
                continue;
            }
            Iterator<Vertex> neighborIt = u.neighboursIterator();
            while (neighborIt.hasNext()) {
                Vertex v = neighborIt.next();
                if (isCollapsed.get(v.getKey()).booleanValue()) {
                    exploredNodes[v.getKey()] = true;
                    continue;
                }
                if (!exploredNodes[v.getKey()]) {
                    queue.add(v);
                    distances[v.getKey()] = distances[u.getKey()] + 1;
                    tree.addEdge(u.getKey(), v.getKey());
                    exploredNodes[v.getKey()] = true;
                }
            }
        }

        return tree;
    }

    /**
     * Returns the current estimated progress in respect to the collapsed nodes.
     * 
     * @return A double between 0 and 1
     */
    public double getProgress() {
        return 1 - notCollapsed.size() / (double) n;
    }

    /**
     * Getter for {@link #graph}.
     * 
     * @return The underlying graph
     */
    public Graph getGraph() {
        return graph;
    }

    /**
     * Getter for {@link #isCollapsed}.
     * 
     * @return A view of the collapsed nodes
     */
    public List<Boolean> getIsCollapsed() {
        return Collections.unmodifiableList(isCollapsed);
    }

    /**
     * Getter for {@link #possibilities}.
     * 
     * @return A view of the possiblities list
     */
    public List<Set<Integer>> getPossibilities() {
        return Collections.unmodifiableList(possibilities);
    }

    /**
     * Getter for {@link #ruleset}.
     * 
     * @return The ruleset
     */
    public Ruleset getRuleset() {
        return ruleset;
    }

    public Vertex[] getUpdatedSinceRender() {
        return updatedSinceRender;
    }

    public GraphRenderer getRenderer() {
        return renderer;
    }
}
