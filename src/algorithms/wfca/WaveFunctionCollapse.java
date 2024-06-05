package src.algorithms.wfca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Random;
import java.util.Set;

import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.vertices.Vertex;
import src.graph.graph.interfaces.Graph;
import src.rendering.GraphRenderer;
import src.util.queues.BinaryHeap;
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
            Vertex v = notCollapsed.popMin();
            collapse(v);
        }
        if (renderer != null && Runner.GUI_OUTPUT) {
            renderer.renderFull();
        }
    }

    private void collapse(Vertex vertex) {
        Set<Integer> possibleInts = possibilities.get(vertex.getKey());

        if (possibleInts.isEmpty()) {
            possibleInts = Set.of(-1); // This can crash the algorithm, depending on the ruleset
            errorCounter++;
        }

        int value = possibleInts.stream().toList().get(rnd.nextInt(possibleInts.size()));
        vertex.setValue(value);
        int key = vertex.getKey();

        possibilities.set(key, Set.of(value));
        isCollapsed.set(key, true);

        Vertex start = graph.parseVertex(key);
        int maxBFSDepth = ruleset.maxBFSDepth();

        Iterator<Vertex> neighbourIterator = start.neighboursIterator();
        while (neighbourIterator.hasNext()) {
            Vertex v = neighbourIterator.next();
            if (!isCollapsed.get(v.getKey()).booleanValue() && update(v))
                propagate(start, v, maxBFSDepth);
        }

        // Propagate
        // boolean[] exploredNodes = new boolean[graph.sizeVertices()];
        // int[] distances = new int[graph.sizeVertices()];

        // Queue<Vertex> queue = new ConcurrentLinkedQueue<>();
        // queue.add(start);
        // exploredNodes[key] = true;
        // distances[key] = 0;

        // while (!queue.isEmpty()) {
        //     Vertex u = queue.poll();
        //     if (distances[u.getKey()] > maxBFSDepth) {
        //         continue;
        //     }
        //     Iterator<Vertex> neighbourIterator = u.neighboursIterator();
        //     while (neighbourIterator.hasNext()) {
        //         Vertex v = neighbourIterator.next();
        //         if (isCollapsed.get(v.getKey()).booleanValue()) {
        //             exploredNodes[v.getKey()] = true;
        //             continue;
        //         }
        //         if (!exploredNodes[v.getKey()] && update(v)) {
        //             queue.add(v);
        //             distances[v.getKey()] = distances[u.getKey()] + 1;
        //             exploredNodes[v.getKey()] = true;
        //         }
        //     }
        // }

        updateGui(vertex);
    }

    private void propagate(Vertex vertex, Vertex parent, int depth) {
        if (depth <= 0) {
            return;
        }
        Iterator<Vertex> neighbourIterator = vertex.neighboursIterator();
        while (neighbourIterator.hasNext()) {
            Vertex v = neighbourIterator.next();
            if (v.equals(parent)) { continue; }
            if (!isCollapsed.get(v.getKey()).booleanValue() && update(v)) {
                propagate(vertex, v, depth - 1);
            }
        }
    }

    private void updateGui(Vertex vertex) {
        if (renderer != null && Runner.GUI_OUTPUT && Runner.ANIMATED_OUTPUT) {
            updatedSinceRender[renderCounter++] = vertex;
            if (renderCounter >= UPDATES_PER_RENDER) {
                renderer.renderDiff();
                renderCounter = 0;
            }
        }
    }

    private boolean update(Vertex v) {

        boolean changed = false;
    
        Set<Integer> possibleInts = ruleset.ruleset(this.graph, v, possibilities); // Magic happens in here

        if (!possibilities.get(v.getKey()).equals(possibleInts)) {
            possibilities.set(v.getKey(), possibleInts);
            notCollapsed.decPrio(v, possibleInts.size());
            changed = true;
        }

        return changed;
    }

    public int getErrorCount() {
        return errorCounter;
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

    public void setRenderer(GraphRenderer graphRenderer) {
        this.renderer = graphRenderer;
    }
}
