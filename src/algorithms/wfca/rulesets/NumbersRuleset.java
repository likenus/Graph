package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertex;

/**
 * In this ruleset integers in adjacent tiles may only have a difference of exaclty 1.
 */
public class NumbersRuleset implements Ruleset {

    private static final Set<Integer> NUMBERS = Set.of(0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
    
    @Override
    public Set<Integer> ruleset(Graph graph, Vertex v, List<Set<Integer>> possibilities) {
        List<Set<Integer>> allPossibleInts = new ArrayList<>();
    
        List<Vertex> neighbours = graph.neighbours(v.getKey());

        // Check all neighbours
        for (int i = 0; i < neighbours.size(); i++) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);
            
            for (int n : possibilities.get(neighbours.get(i).getKey())) {
                if (n > NUMBERS.stream().min(Integer::compare).orElseThrow(RuntimeException::new)) {
                    ints.add(n - 1);
                }
    
                ints.remove(n);
    
                if (n < NUMBERS.stream().max(Integer::compare).orElseThrow(RuntimeException::new)) {
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

    @Override
    public String stringRepresentation(int i) {
        return String.valueOf(i);
    }

    @Override
    public Set<Integer> initialPossibilities(Vertex v) {
        return NUMBERS;
    }
}
