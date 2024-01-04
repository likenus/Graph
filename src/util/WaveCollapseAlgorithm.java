package src.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.Random;
import java.util.Set;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.graph.interfaces.Graph;
import src.graph.models.directed.DirectedGraph;
import src.graph.models.undirected.Mesh2D;
import src.vertices.interfaces.Vertice;

/**
 * The Wave Function Collapse Algorithm, short WaveCollapseAlgorithm,
 * is a algorithm for procedual generation.
 * </p>
 * This implementation uses graphs as a basis and a fixed ruleset to
 * generate randomized patterns. 
 */
public class WaveCollapseAlgorithm implements Runnable {

    private static final List<Integer> NUMBERS = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

    private final Graph graph;
    private final List<Boolean> isCollapsed = new ArrayList<>();
    private final BinaryHeap<Vertice> notCollapsed = new BinaryHeap<>();
    private final List<Set<Integer>> possibilities = new ArrayList<>();
    private final Random rnd;
    private final int n;
    private final Vertice startVertice;

    private boolean finished = false;
    private int finishedCounter = 0;

    private WaveCollapseAlgorithm(Graph graph, Random rnd) {
        
        this.rnd = rnd;
        
        for (int i = 0; i < graph.sizeVertices(); i++) {
            possibilities.add(new HashSet<>());
        }

        this.graph = graph;
        this.n = graph.sizeVertices();

        this.startVertice = this.graph.parseVertice(rnd.nextInt(n));

        for (Vertice v : this.graph.vertices()) {
            if (!v.equals(startVertice)) {
                notCollapsed.push(v, NUMBERS.size());
            }
            isCollapsed.add(false);
            possibilities.set(v.getKey(), new HashSet<>(NUMBERS));
        }
    }

    public WaveCollapseAlgorithm(Graph graph) {
        this(graph, new Random());
    }

    public WaveCollapseAlgorithm(Graph graph, long seed) {
        this(graph, new Random(seed));
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

        // Experimental
        if (possibleInts.isEmpty()) {
            System.err.println(Tiles.ANSI_RED + "Trying to reevaluate" + Tiles.ANSI_RESET);
            possibleInts = ruleset3(v);
            if (possibleInts.isEmpty()) {
                System.err.println(Tiles.ANSI_RED + "A critical error has occurred: Evaluation not possible" + Tiles.ANSI_RESET);
                possibleInts = Set.of(-1);
            }
        }

        int value = possibleInts.stream().toList().get(rnd.nextInt(possibleInts.size()));
        v.setValue(value);

        possibilities.set(v.getKey(), Set.of(value));
        finishedCounter++;
        isCollapsed.set(v.getKey(), true);
        
        DirectedGraph searchTree = bfsTree(graph, v.getKey());

        for (Vertice w : searchTree.neighbours(v.getKey())) {
            if (isCollapsed.get(w.getKey()).booleanValue()) {
                continue;
            }
            update(w, searchTree);
        }
        if (finishedCounter == n) {
            finished = true;
        }
    }

    private void instantCollapse(Vertice v) {
        Set<Integer> possibleInts = possibilities.get(v.getKey());

        if (possibleInts.isEmpty()) {
            System.err.println("Trying to reevaluate");
            possibleInts = ruleset3(v);
            if (possibleInts.isEmpty()) {
                System.err.println("A critical error has occurred: Evaluation not possible");
                possibleInts = Set.of(-1);
            }
        }

        int value = possibleInts.stream().toList().get(0);
        v.setValue(value);

        possibilities.set(v.getKey(), Set.of(value));
        finishedCounter++;
        isCollapsed.set(v.getKey(), true);

        if (finishedCounter == n) {
            finished = true;
        }
    }

    private Vertice findLowestEntropy() {
        // Experimental
        while (isCollapsed.get(notCollapsed.peek().getKey()).booleanValue()) {
            notCollapsed.pop();
        }

        return notCollapsed.pop();
    }

    private void update(Vertice v, DirectedGraph searchTree) {
        boolean changed = false;

        Set<Integer> possibleInts = ruleset3(v);
        
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

        // Experimental
        // if (possibleInts.size() == 1) {
        //     instantCollapse(graph.parseVertice(v.getKey()));
        // }
    }

    /**
     * Custom ruleset trying to generate a landscape
     * @param v A vertice
     * @return Some numbers
     */
    private Set<Integer> ruleset2(Vertice v) {
        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertice> neighbours = graph.neighbours(v.getKey());
        // Check all neighbours
        for (int i = 0; i < neighbours.size(); i++) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Set<Integer> neighbourInts = possibilities.get(neighbours.get(i).getKey());
            
            for (Tiles tile : Tiles.values()) {
                if (neighbourInts.contains(tile.getIdentifier())) {
                    ints.addAll(tile.getNeighbours());
                }
            }
        }

        Set<Integer> possibleInts = new HashSet<>();

        if (allPossibleInts.stream().filter(set -> set.size() == 2).count() >= 2) {
            if (allPossibleInts.stream().allMatch(set -> set.contains(Tiles.DEEP_WATER.getIdentifier()))) {
                possibleInts.add(Tiles.DEEP_WATER.getIdentifier());
                return possibleInts;
            }
            if (allPossibleInts.stream().allMatch(set -> set.contains(Tiles.FOREST.getIdentifier()))) {
                possibleInts.add(Tiles.FOREST.getIdentifier());
                return possibleInts;
            }
        }

        for (int i : NUMBERS) {
            Integer num = i;
            if (allPossibleInts.stream().allMatch(set -> set.contains(num) || set.contains(-1))) {
                possibleInts.add(num);
            }
        }

        return possibleInts;
    }

    /**
     * Adjacent integers amy only have a value-difference of exaclty 1
     * @param v A vertice
     * @return A set of possible numbers
     */
    private Set<Integer> ruleset(Vertice v) {
        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertice> neighbours = graph.neighbours(v.getKey());
        // Check all neighbours
        for (int i = 0; i < neighbours.size(); i++) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);
            
            for (int n : possibilities.get(neighbours.get(i).getKey())) {
                if (n > NUMBERS.get(0)) {
                    ints.add(n - 1);
                }

                ints.remove(n);

                if (n < NUMBERS.get(NUMBERS.size() - 1)) {
                    ints.add(n + 1);
                }
            }
        }

        Set<Integer> possibleInts = new HashSet<>();
        for (int i : NUMBERS) {
            Integer n = i;
            if (allPossibleInts.stream().allMatch(set -> set.contains(n))) {
                possibleInts.add(n);
            }
        }

        return possibleInts;
    }

    private Set<Integer> ruleset3(Vertice v) {
        Mesh2D mesh = (Mesh2D) this.graph;

        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertice> neighbours = graph.neighbours(v.getKey());

        for (Vertice neighbour : neighbours) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Set<Integer> neighbourInts = possibilities.get(neighbour.getKey());
            
            // Left
            if (neighbour.getKey() == v.getKey() - 1 || neighbour.getKey() == v.getKey() + mesh.getWidth() - 1) {
                List<Tiles2> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tiles2.parseTile(i));
                }
                for (Tiles2 tile : tiles) {
                    if (tile.right() == 0) {
                        for (Tiles2 t : Tiles2.values()) {
                            if (t.left() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tiles2 t : Tiles2.values()) {
                            if (t.left() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Right
            if (neighbour.getKey() == v.getKey() + 1 || neighbour.getKey() == v.getKey() - mesh.getWidth() + 1) {
                List<Tiles2> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tiles2.parseTile(i));
                }
                for (Tiles2 tile : tiles) {
                    if (tile.left() == 0) {
                        for (Tiles2 t : Tiles2.values()) {
                            if (t.right() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tiles2 t : Tiles2.values()) {
                            if (t.right() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Top
            if (neighbour.getKey() == v.getKey() - mesh.getWidth()) {
                List<Tiles2> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tiles2.parseTile(i));
                }
                for (Tiles2 tile : tiles) {
                    if (tile.bot() == 0) {
                        for (Tiles2 t : Tiles2.values()) {
                            if (t.top() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tiles2 t : Tiles2.values()) {
                            if (t.top() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Bot
            if (neighbour.getKey() == v.getKey() + mesh.getWidth()) {
                List<Tiles2> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tiles2.parseTile(i));
                }
                for (Tiles2 tile : tiles) {
                    if (tile.top() == 0) {
                        for (Tiles2 t : Tiles2.values()) {
                            if (t.bot() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tiles2 t : Tiles2.values()) {
                            if (t.bot() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }
        }

        Set<Integer> possibleInts = new HashSet<>();

        for (int i : NUMBERS) {
            Integer num = i;
            if (allPossibleInts.stream().allMatch(set -> set.contains(num) || set.contains(-1))) {
                possibleInts.add(num);
            }
        }

        return possibleInts;
    }

    public double getProgress() {
        return (double) finishedCounter / n;
    }

    public Graph getGraph() {
        return graph;
    }

    public List<Boolean> getIsCollapsed() {
        return Collections.unmodifiableList(isCollapsed);
    }

    public List<Set<Integer>> getPossibilities() {
        return Collections.unmodifiableList(possibilities);
    }

    private DirectedGraph bfsTree(Graph g, int s) {
        Objects.requireNonNull(g);

        boolean[] exploredNodes = new boolean[g.sizeVertices()];
        
        DirectedGraph tree = new DirectedGraph(g.sizeVertices());
        Vertice start = g.parseVertice(s);

        if (start == null) {
            throw new IllegalArgumentException("Start does not exist");
        }

        Queue<Vertice> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        exploredNodes[s] = true;

        // BFS
        while (!queue.isEmpty()) {
            Vertice u = queue.poll();
            for (Vertice v : u.neighbours()) {
                if (isCollapsed.get(v.getKey()).booleanValue()) {
                    exploredNodes[v.getKey()] = true;
                    continue;
                }
                if (!exploredNodes[v.getKey()]) {
                    queue.add(v);
                    tree.addEdge(u.getKey(), v.getKey());
                    exploredNodes[v.getKey()] = true;
                }
            }
        }

        return tree;
    }
}
