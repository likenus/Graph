package src.util;

import java.util.ArrayList;
import java.util.Collection;
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
import src.vertices.interfaces.Vertice;
import target.Runner;

/**
 * The Wave Function Collapse Algorithm, short WaveCollapseAlgorithm,
 * is a algorithm for procedual generation.
 * </p>
 * This implementation uses graphs as a basis and a fixed ruleset to
 * generate randomized patterns. 
 */
public class WaveCollapseAlgorithm implements Runnable {

    private static final List<Integer> NUMBERS = List.of(1, 2, 3, 4, 5);

    private final Graph graph;
    private final List<Boolean> isCollapsed = new ArrayList<>();
    private final BinaryHeap<Vertice> notCollapsed = new BinaryHeap<>();
    private final List<Set<Integer>> possibilities = new ArrayList<>();
    private final Random rnd = new Random();
    private final int n;
    private final Vertice startVertice;

    private boolean finished = false;
    private int finishedCounter = 0;

    public WaveCollapseAlgorithm(Graph graph) {
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
            System.err.println(Runner.ANSI_RED + "Trying to reevaluate" + Runner.ANSI_RESET);
            possibleInts = ruleset(v);
            if (possibleInts.isEmpty()) {
                System.err.println(Runner.ANSI_RED + "A critical error has occurred: Evaluation not possible" + Runner.ANSI_RESET);
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
            possibleInts = ruleset(v);
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
        while (isCollapsed.get(notCollapsed.peek().getKey()).booleanValue()) {
            notCollapsed.pop();
        }

        return notCollapsed.pop();
    }

    private void update(Vertice v, DirectedGraph searchTree) {
        boolean changed = false;

        Set<Integer> possibleInts = ruleset2(v);
        
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

        if (possibleInts.size() == 1) {
            instantCollapse(graph.parseVertice(v.getKey()));
        }
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
            
            /*
             * 1 -> Terrain
             * 2 -> Beach
             * 3 -> Water
             * 4 -> Mountain
             * 5 -> Deep Water
             */
            if (neighbourInts.contains(1)) {
                ints.add(1);
                ints.add(2);
                ints.add(4);
            }
            if (neighbourInts.contains(2)) {
                ints.add(1);
                ints.add(2);
                ints.add(3);
            }
            if (neighbourInts.contains(3)) {
                ints.add(2);
                ints.add(3);
                ints.add(5);
            }
            if (neighbourInts.contains(4)) {
                ints.add(1);
                ints.add(4);
            }
            if (neighbourInts.contains(5)) {
                ints.add(3);
                ints.add(5);
            }
        }

        Set<Integer> possibleInts = new HashSet<>();

        if (allPossibleInts.stream().filter(set -> set.size() == 2).count() >= 2) {
            if (allPossibleInts.stream().allMatch(set -> set.contains(4))) {
                possibleInts.add(4);
                return possibleInts;
            }
            if (allPossibleInts.stream().allMatch(set -> set.contains(5))) {
                possibleInts.add(5);
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
