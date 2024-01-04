package src.algorithms.wfca.rulesets;

import java.util.List;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.vertices.interfaces.Vertice;

public interface Ruleset {

    /**
     * 
     * @param graph
     * @param v
     * @param possibilities
     * @return
     */
    Set<Integer> ruleset(Graph graph, Vertice v, List<Set<Integer>> possibilities);

    Set<Integer> numbers();

    String stringRepresentation(int i);
} 
