package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.interfaces.Vertex;
import src.util.Ansi;
import target.GraphLoader;

/**
 * A ruleset based on {@link LandscapeRuleset} which reduces noise by using biomes.
 */
public class BiomedLandscapeRuleset implements Ruleset {
    public static final String TILE_SYMBOL = "■";

    private static final Set<Integer> NUMBERS = new HashSet<>(Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());
    private List<Set<Biome>> vertexBiomes;

    public BiomedLandscapeRuleset(Mesh2D targetGraph) {
        if (targetGraph.getWidth() < 100 || targetGraph.getHeight() < 100) {
            throw new IllegalArgumentException("Biomed Landscape Generation needs at least 100x100 tiles");
        }
        vertexBiomes = new ArrayList<>();
        for (int i = 0; i < targetGraph.getHeight() * targetGraph.getWidth(); i++) {
            vertexBiomes.add(new HashSet<>());
        }

        GraphLoader loader = new GraphLoader();
        Mesh2D biomeMapBlank = loader.zylinder(targetGraph.getWidth() / 3, targetGraph.getHeight() / 3);
        WaveFunctionCollapse biomeGenerator = new WaveFunctionCollapse(biomeMapBlank, new LandscapeRuleset());
        biomeGenerator.run();
        Mesh2D biomeMap = (Mesh2D) biomeGenerator.getGraph();

        for (Vertex v: biomeMap.vertices()) {
            Biome biome = selectBiome(v);
            int vertexX = v.getKey() % biomeMap.getWidth();
            int vertexY = v.getKey() / biomeMap.getWidth();

            // the overlap of the biomes is intentional
            paintBiome(biome, targetGraph.getWidth(),
                vertexX * 3, Math.min((vertexX + 1) * 3 + 1, targetGraph.getWidth() - 1),
                vertexY * 3, Math.min((vertexY + 1) * 3 + 1, targetGraph.getHeight() - 1));
        }
    }

    private void paintBiome(Biome biome, int graphWidth, int minX, int maxX, int minY, int maxY) {
        for (int x = minX; x <= maxX; x++) {
            for (int y = minY; y <= maxY; y++) {
                vertexBiomes.get(y * graphWidth + x).add(biome);
            }
        }
    }

    private Biome selectBiome(Vertex v) {
        return switch (v.getValue()) {
            case 1 -> Biome.DEEP_OCEAN;
            case 2 -> Biome.OCEAN;
            case 3, 4 -> Biome.PLAINS;
            case 5 -> Biome.MOUNTAIN;
            default -> throw new IllegalStateException("Unexpected value: " + v.getValue());
        };
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

        Set<Integer> allowedByBiome = allowedByBiome(v);
        allPossibleInts.add(allowedByBiome);

        Set<Integer> possibleInts = new HashSet<>();

        int biomeCorrectionFactor = allowedByBiome.size() == 2 ? 1 : 0;
        if (allPossibleInts.stream().filter(set -> set.size() == 2).count() >= 2 + biomeCorrectionFactor) {
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

    private Set<Integer> allowedByBiome(Vertex v) {
        Set<Integer> allowedByBiome = new HashSet<>();
        for (Biome b: vertexBiomes.get(v.getKey())) {
            allowedByBiome.addAll(b.getAllowedTiles());
        }
        return allowedByBiome;
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
        return allowedByBiome(v);
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

    private enum Biome {
        OCEAN(Set.of(Tile.WATER.getIdentifier())),
        DEEP_OCEAN(Set.of(Tile.WATER.getIdentifier(), Tile.DEEP_WATER.getIdentifier())),
        PLAINS(Set.of(Tile.BEACH.getIdentifier(), Tile.FOREST.getIdentifier())),
        MOUNTAIN(Set.of(Tile.MOUNTAIN.getIdentifier()));
        private Set<Integer> allowedTiles;

        private Biome(Set<Integer> allowedTiles) {
            this.allowedTiles = allowedTiles;
        }

        public Set<Integer> getAllowedTiles() {
            return Collections.unmodifiableSet(allowedTiles);
        }
    }
}
