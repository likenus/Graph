package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertex;
import src.util.Ansi;

public class TopoRuleset implements Ruleset {

    private static final String TILE_SYMBOL = "■";

    private static final Set<Integer> NUMBERS = new HashSet<>(Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());

    private enum Tile {

        ROCK_BOTTOM(1, Ansi.Black, Set.of(1, 2)),

        BOTTOM(2, Ansi.Blue, Set.of(1, 2, 3)),
        
        VERY_LOW(3, Ansi.Cyan, Set.of(2, 3, 4)),
        
        LOW(4, Ansi.Yellow, Set.of(3, 4, 5)),
        
        LEVEL(5, Ansi.Green, Set.of(4, 5, 6)),
        
        HIGH(6, Ansi.Magenta, Set.of(5, 6, 7)), 

        VERY_HIGH(7, Ansi.Red, Set.of(6, 7, 8)),

        TOP(8, Ansi.White, Set.of(7, 8));

        private int identifier;
        private Ansi ansiConfig;
        private Set<Integer> possibleNeighbours;
        
        private Tile(int identifier, Ansi ansiConfig, Set<Integer> possibleNeighbours) {
            this.identifier = identifier;
            this.ansiConfig = ansiConfig;
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
            return this.ansiConfig + TILE_SYMBOL;
        }
    }

    @Override
    public Set<Integer> ruleset(Graph graph, Vertex v, List<Set<Integer>> possibilities) {
        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertex> neighbours = graph.neighbours(v.getKey());

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

        return Ruleset.intersect(allPossibleInts, NUMBERS);
    }

    @Override
    public String stringRepresentation(int i) {
        if (i == 0) {
            return " ";
        }
        Tile tile = Tile.parseTile(i);
        return tile == null ? Ansi.Red + TILE_SYMBOL : tile.toString();
    }

    @Override
    public Set<Integer> initialPossibilities(Vertex v) {
        return NUMBERS;
    }

    @Override
    public int maxBFSDepth() {
        return 8;
    }
}
