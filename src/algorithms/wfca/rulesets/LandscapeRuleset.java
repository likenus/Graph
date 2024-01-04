package src.algo.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.vertices.interfaces.Vertice;

public class LandscapeRuleset implements Ruleset {

    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String TILE_SYMBOL = "■";

    private static final Set<Integer> NUMBERS = new HashSet<>(Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());

    @Override
    public Set<Integer> ruleset(Graph graph, Vertice v, List<Set<Integer>> possibilities) {
        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertice> neighbours = graph.neighbours(v.getKey());

        // Check all neighbours
        for (int i = 0; i < neighbours.size(); i++) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Set<Integer> neighbourInts = possibilities.get(neighbours.get(i).getKey());
            
            for (Tile tile : Tile.values()) {
                if (neighbourInts.contains(tile.getIdentifier())) {
                    ints.addAll(tile.getNeighbours());
                }
            }
        }

        Set<Integer> possibleInts = new HashSet<>();

        if (allPossibleInts.stream().filter(set -> set.size() == 2).count() >= 2) {
            if (allPossibleInts.stream().allMatch(set -> set.contains(Tile.DEEP_WATER.getIdentifier()))) {
                possibleInts.add(Tile.DEEP_WATER.getIdentifier());
                return possibleInts;
            }
            if (allPossibleInts.stream().allMatch(set -> set.contains(Tile.FOREST.getIdentifier()))) {
                possibleInts.add(Tile.FOREST.getIdentifier());
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

    @Override
    public Set<Integer> numbers() {
        return NUMBERS;
    }

    @Override
    public String stringRepresentation(int i) {
        Tile tile = Tile.parseTile(i);
        return tile == null ? ANSI_RED + TILE_SYMBOL : tile.toString();
    }
    
    private enum Tile {

        DEEP_WATER(1, ANSI_BLUE, Set.of(1, 2)),
        
        WATER(2, ANSI_CYAN, Set.of(1, 2, 3)),
        
        BEACH(3, ANSI_YELLOW, Set.of(2, 3, 4)),
        
        FOREST(4, ANSI_GREEN, Set.of(4, 3, 5)),
        
        MOUNTAIN(5, ANSI_BLACK, Set.of(4, 5));  
        
    
        private int identifier;
        private String ansiColor;
        private Set<Integer> possibleNeighbours;
        
        private Tile(int identifier, String ansiColor, Set<Integer> possibleNeighbours) {
            this.identifier = identifier;
            this.ansiColor = ansiColor;
            this.possibleNeighbours = possibleNeighbours;
        }
    
        public int getIdentifier() {
            return identifier;
        }
    
        public Set<Integer> getNeighbours() {
            return possibleNeighbours;
        }

        public static Tile parseTile(int i) {
            for (Tile tile : values()) {
                if (tile.getIdentifier() == i) {
                    return tile;
                }
            }

            return null;
        }
    
        @Override
        public String toString() {
            return this.ansiColor + TILE_SYMBOL;
        }
    }

    
}
