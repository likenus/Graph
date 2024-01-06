package src.algorithms.wfca;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.models.directed.DirectedGraph;
import src.graph.graph.models.directed.LazyDirectedGraph;
import src.graph.vertices.interfaces.Vertice;
import src.graph.graph.interfaces.Graph;
import src.util.BinaryHeap;

/**
 * The Wave Function Collapse Algorithm, is an algorithm for procedual generation.
 * </p>
 * The Algorithm starts with a blank field of tiles. Every tile is in a superposition meaning it has
 * different possible states it can collapse to. Collapsing a tile will force it to choose one state.
 * Then according to a fixed set of rules, surrounding tiles will lose possible states, thus reducing
 * their entropy. Then the algorithm iteratively collapses the tile with the lowest entropy
 * until all tiles are collapsed.
 * </p>
 * This implementation uses graphs as a basis and a fixed ruleset to
 * generate randomized patterns. 
 * @version 2.0
 * 
 * @author likenus
 * 
 * @see src.graph.util.Graphs#dijkstra()
 */
public class WaveFunctionCollapse implements Runnable {

    private static final int MAX_BFS_DEPTH = 8;

    private final List<Boolean> isCollapsed = new ArrayList<>();
    private final BinaryHeap<Vertice> notCollapsed = new BinaryHeap<>();
    private final List<Set<Integer>> possibilities = new ArrayList<>();

    private final Ruleset ruleset;
    private final Set<Integer> numbers;
    private final Vertice startVertice;
    private final Graph graph;
    private final Random rnd;
    private final int n;

    private boolean finished = false;
    private int finishedCounter = 0;

    private WaveFunctionCollapse(Graph graph, Ruleset ruleset, Random rnd) {
        
        for (int i = 0; i < graph.sizeVertices(); i++) {
            possibilities.add(new HashSet<>());
        }
        
        this.rnd = rnd;
        this.graph = graph;
        this.n = graph.sizeVertices();
        this.ruleset = ruleset;
        this.numbers = ruleset.numbers();

        this.startVertice = this.graph.parseVertice(rnd.nextInt(n));

        for (Vertice v : this.graph.vertices()) {
            if (!v.equals(startVertice)) {
                notCollapsed.push(v, numbers.size());
            }
            isCollapsed.add(false);
            possibilities.set(v.getKey(), new HashSet<>(numbers));
        }
    }

    /**
     * Initializes a new Wave Function Collapse Algorithm with a random seed.
     * @param graph The graph to be collapsed
     */
    public WaveFunctionCollapse(Graph graph, Ruleset ruleset) {
        this(graph, ruleset, new Random());
    }

    /**
     * Initializes a new Wave Function Collapse Algorithm with a set seed.
     * @param graph The graph to be collapsed
     * @param seed The seed
     */
    public WaveFunctionCollapse(Graph graph, Ruleset ruleset, long seed) {
        this(graph, ruleset, new Random(seed));
    }

    @Override
    public void run() {
        evaluate();
    }

    private void evaluate() {
        collapse(startVertice);
        while (!finished) {
            Vertice v = findLowestEntropy();
            collapse(v);
        }
    }

    private void collapse(Vertice v) {
        Set<Integer> possibleInts = possibilities.get(v.getKey());

        if (possibleInts.isEmpty()) {
            System.err.println("A critical error has occurred: Evaluation not possible");
            possibleInts = Set.of(-1); // This can crash the algorithm, depending on the ruleset
        }

        int value = possibleInts.stream().toList().get(rnd.nextInt(possibleInts.size()));
        v.setValue(value);

        possibilities.set(v.getKey(), Set.of(value));
        isCollapsed.set(v.getKey(), true);
        finishedCounter++;

        if (finishedCounter == n) {
            finished = true;
        }

        DirectedGraph searchTree = bfsTree(graph, v.getKey());
        for (Vertice w : searchTree.neighbours(v.getKey())) {
            if (isCollapsed.get(w.getKey()).booleanValue()) {
                continue;
            }
            update(w, searchTree);
        }
    }

    private Vertice findLowestEntropy() {
        return notCollapsed.pop();
    }

    private void update(Vertice v, DirectedGraph searchTree) {
        boolean changed = false;

        Set<Integer> possibleInts = ruleset.ruleset(this.graph, v, possibilities); // Magic happens in here
        
        if (!possibilities.get(v.getKey()).equals(possibleInts)) {
            possibilities.set(v.getKey(), possibleInts);
            changed = true;
            notCollapsed.decPrio(v, possibleInts.size());
        }
        
        if (changed) {
            for (Vertice w : searchTree.neighbours(v.getKey())) {
                if (isCollapsed.get(w.getKey()).booleanValue()) {
                    continue;
                }
                update(w, searchTree);
            }
        }
    }

    /**
     * This bfs tree is modified, for a full version see {@link src.graph.util.Graphs#bfsTree()}
     * @param g a graph
     * @param s the starting vertice
     * @return The search tree
     */
    private DirectedGraph bfsTree(Graph g, int s) {
        Objects.requireNonNull(g);
        
        boolean[] exploredNodes = new boolean[g.sizeVertices()];
        int[] distances = new int[g.sizeVertices()];
        
        DirectedGraph tree = new LazyDirectedGraph(s);
        Vertice start = g.parseVertice(s);
        
        if (start == null) {
            throw new IllegalArgumentException("Start does not exist");
        }
        
        Queue<Vertice> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        exploredNodes[s] = true;
        distances[s] = 0;
        
        // BFS
        while (!queue.isEmpty()) {
            Vertice u = queue.poll();
            if (distances[u.getKey()] > MAX_BFS_DEPTH) {
                continue;
            }
            for (Vertice v : u.neighbours()) {
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
     * @return A double between 0 and 1
     */
    public double getProgress() {
        return (double) finishedCounter / n;
    }
    
    /**
     * Getter for {@link #graph}.
     * @return The underlying graph
     */
    public Graph getGraph() {
        return graph;
    }
    
    /**
     * Getter for {@link #isCollapsed}.
     * @return A view of the collapsed nodes
     */
    public List<Boolean> getIsCollapsed() {
        return Collections.unmodifiableList(isCollapsed);
    }
    
    /**
     * Getter for {@link #possibilities}.
     * @return A view of the possiblities list
     */
    public List<Set<Integer>> getPossibilities() {
        return Collections.unmodifiableList(possibilities);
    }

    /**
     * Getter for {@link #ruleset}.
     * @return The ruleset
     */
    public Ruleset getRuleset() {
        return ruleset;
    }
}
