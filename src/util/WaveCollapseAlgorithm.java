package src.util;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.graph.models.directed.DirectedTree;
import src.vertices.interfaces.Vertice;

/**
 * WaveCollapseAlgorithm
 */
public class WaveCollapseAlgorithm {

    private static final List<Integer> NUMBERS = List.of(0, 1, 2, 3 , 4, 5, 6, 7, 8, 9);

    private final Graph graph;
    private final List<Boolean> isCollapsed = new ArrayList<>();
    private final List<Set<Integer>> possibilities = new ArrayList<>();
    private final Random rnd = new Random();
    private final boolean[] checked;
    
    private boolean finished = false;

    public WaveCollapseAlgorithm(Graph graph) {
        for (int i = 0; i < graph.sizeVertices(); i++) {
            possibilities.add(new HashSet<>());
        }
        this.graph = graph;
        for (Vertice v : graph.vertices()) {
            isCollapsed.add(false);
            possibilities.set(v.getKey(), new HashSet<>(NUMBERS));
        }

        this.checked = new boolean[graph.sizeVertices()];
    }

    public void evaluate() {
        collapse(graph.parseVertice(0));
        while (!finished) {
            Vertice v = findLowestEntropy();
            collapse(v);
        }
    }

    private void collapse(Vertice v) {
        Set<Integer> possibleInts = possibilities.get(v.getKey());

        if (possibleInts.isEmpty()) {
            throw new IllegalStateException();
        }

        int key = possibleInts.stream().toList().get(Math.abs(rnd.nextInt() % possibleInts.size()));
        v.setValue(key);

        possibilities.set(v.getKey(), Set.of(key));
        isCollapsed.set(v.getKey(), true);
        
        DirectedTree searchTree = Graphs.bfsTree(graph, v.getKey());

        for (Vertice w : searchTree.neighbours(v.getKey())) {
            if (isCollapsed.get(w.getKey()).booleanValue()) {
                continue;
            }
            update(w, searchTree);
            for (int i = 0; i < graph.sizeVertices(); i++) {
                checked[i] = false;
            }
        }
        if (isCollapsed.stream().allMatch(e -> e)) {
            finished = true;
        }
    }

    private Vertice findLowestEntropy() {
        Optional<Vertice> vertice = graph.vertices().stream()
            .filter(v -> !isCollapsed.get(v.getKey()).booleanValue())
            .min(Comparator.comparing(v -> possibilities.get(v.getKey()).size()));

        if (!vertice.isPresent()) {
            throw new IllegalStateException();
        }

        return vertice.get();
    }

    private void update(Vertice v, DirectedTree searchTree) {
        boolean changed = false;

        Set<Integer> possibleInts = ruleset(v);
        

        if (!possibilities.get(v.getKey()).equals(possibleInts)) {
            possibilities.set(v.getKey(), possibleInts);
            changed = true;
            checked[v.getKey()] = true;
        }

        if (changed) {
            for (Vertice w : searchTree.neighbours(v.getKey())) {
                if (isCollapsed.get(w.getKey()).booleanValue() || checked[w.getKey()]) {
                    continue;
                }
                update(w, searchTree);
            }
        }
    }

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
}