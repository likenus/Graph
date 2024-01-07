package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.vertices.interfaces.Vertice;
import src.util.Ansi;

/**
 * This ruleset attempts to paint a landscape like image. Its rules say that
 * water and land
 * may not be adjacent and must be connected via a beach.
 */
public class LandscapeRuleset implements Ruleset {
    public static final String TILE_SYMBOL = "■";

    private static final Set<Integer> NUMBERS = new HashSet<>(
            Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());

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
    public Set<Integer> initialPossibilities(Vertice v) {
        return NUMBERS;
    }

    private enum Tile {

        DEEP_WATER(1, Ansi.Blue, Set.of(1, 2)),

        WATER(2, Ansi.Cyan, Set.of(1, 2, 3)),

        BEACH(3, Ansi.Yellow, Set.of(2, 3, 4)),

        FOREST(4, Ansi.Green, Set.of(4, 3, 5)),

        MOUNTAIN(5, Ansi.Black, Set.of(4, 5));

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
}
