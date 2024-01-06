package src.algorithms.wfca.rulesets;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertice;

/**
 * Rulesets use numbers and a set of rules to give the numbers meaning. 
 * Rulesets are used by the {@link src.algorithms.wfca.WaveFunctionCollapse}.
 */
public interface Ruleset {

    /**
     * Takes the current state of the graph and calculates possible states for the given vertice
     * according to the specified ruleset.
     * @param graph A refrence to the underlying graph
     * @param v The vertice that needs to be evaluated
     * @param possibilities The state of possibilities of all vertices
     * @return Some numbers
     */
    Set<Integer> ruleset(Graph graph, Vertice v, List<Set<Integer>> possibilities);

    /**
     * Returns the set of numbers, used by this ruleset.
     * @return A set of numbers, cannot be empty
     */
    Set<Integer> numbers();

    /**
     * Interpretes the numbers of the working set into an easier to understand string-representation.
     * @param i An integer
     * @return A Strings
     */
    String stringRepresentation(int i);

    /* Package Private */ static Set<Integer> intersect(List<Set<Integer>> sets, Set<Integer> numbers) {
        Set<Integer> possibleInts = new HashSet<>();

        for (int i : numbers) {
            Integer num = i;
            if (sets.stream().allMatch(set -> set.contains(num) || set.contains(-1))) {
                possibleInts.add(num);
            }
        }

        return possibleInts;
    }
} 
