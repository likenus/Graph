package src.algorithms.wfca.rulesets;

import java.util.List;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.vertices.interfaces.Vertice;

/**
 * Rulesets use numbers and a set of rules to give the numbers meaning. 
 * Rulesets are used by the {@link src.algorithms.wfca.WaveCollapseAlgorithm}.
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
} 
