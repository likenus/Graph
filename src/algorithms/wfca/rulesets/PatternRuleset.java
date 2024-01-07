package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.interfaces.Vertice;
import src.util.Ansi;

public class PatternRuleset implements Ruleset {

    public static final String TILE_SYMBOL = "■";

    private final Set<Integer> numbers = new HashSet<>();
    private final Map<Integer, DirectionalTupel<Integer>> pattern = new HashMap<>();
    
    public PatternRuleset(int[][] input) {
        for (int[] line : input) {
            for (int i : line) {
                numbers.add(i);
            }
        }

        for (int i : numbers) {
            pattern.put(i, new DirectionalTupel<>());
        }

        int height = input.length;
        int width = input[0].length;
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                inintNeighboursOf(i, j, input, pattern.get(input[i][j]));
            }
        }
        
        DirectionalTupel<Integer> tupel = new DirectionalTupel<>();
        for (Direction direction : Direction.values()) {
            tupel.addAll(direction, numbers);
        }
        pattern.put(-1, tupel);
    }

    private void inintNeighboursOf(int x, int y, int[][] input, DirectionalTupel<Integer> neighbours) {

        if (y < input[0].length - 1) {
            neighbours.add(Direction.RIGHT, input[x][y + 1]);
        }
        if (y > 0) {
            neighbours.add(Direction.LEFT, input[x][y - 1]);
        }
        if (x < input.length - 1) {
            neighbours.add(Direction.DOWN, input[x + 1][y]);
        }
        if (x > 0) {
            neighbours.add(Direction.UP, input[x - 1][y]);
        }
    }

    @Override
    public Set<Integer> ruleset(Graph graph, Vertice v, List<Set<Integer>> possibilities) {
        Mesh2D mesh = (Mesh2D) graph;

        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        Map<Direction, Vertice> neighbours = initNeighbours(v, graph.neighbours(v.getKey()), mesh);

        for (Entry<Direction, Vertice> entry : neighbours.entrySet()) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Vertice neighbour = entry.getValue();
            Direction direction = entry.getKey();

            Set<Integer> neighbourInts = possibilities.get(neighbour.getKey());

            for (int i : neighbourInts) {
                ints.addAll(pattern.get(i).get(direction.opposite()));
            }
        }

        return Ruleset.intersect(allPossibleInts, numbers);
    }

    private static Map<Direction, Vertice> initNeighbours(Vertice v, List<Vertice> neighbours, Mesh2D mesh) {
        Map<Direction, Vertice> map = new EnumMap<>(Direction.class);

        for (Vertice neighbour : neighbours) {
            if (neighbour.getKey() == v.getKey() - 1 || neighbour.getKey() == v.getKey() + mesh.getWidth() - 1) {
                map.put(Direction.LEFT, neighbour);
            }
            if (neighbour.getKey() == v.getKey() + 1 || neighbour.getKey() == v.getKey() - mesh.getWidth() + 1) {
                map.put(Direction.RIGHT, neighbour);
            }
            if (neighbour.getKey() == v.getKey() - mesh.getWidth()) {
                map.put(Direction.UP, neighbour);
            }
            if (neighbour.getKey() == v.getKey() + mesh.getWidth()) {
                map.put(Direction.DOWN, neighbour);
            }
        }

        return map;
    }

    @Override
    public Set<Integer> numbers() {
        return numbers;
    }

    @Override
    public String stringRepresentation(int i) {
        return switch(i) {
            case 1 -> Ansi.Blue + TILE_SYMBOL;
            case 0 -> Ansi.Red + TILE_SYMBOL;
            default -> " ";
        };
    }

    private class DirectionalTupel<T> {

        private final Set<T> up = new HashSet<>();        
        private final Set<T> down = new HashSet<>();
        private final Set<T> left = new HashSet<>();
        private final Set<T> right = new HashSet<>();

        public void add(Direction direction, T e) {
            get(direction).add(e);
        }

        public void addAll(Direction direction, Collection<? extends T> c) {
            get(direction).addAll(c);
        }

        public Set<T> get(Direction direction) {
            return switch(direction) {
                case UP -> up;
                case DOWN -> down;
                case LEFT -> left;
                case RIGHT -> right;
            };
        }
    }

    private enum Direction {
        UP,

        DOWN,

        LEFT,

        RIGHT;

        public Direction opposite() {
            return switch (this) {
                case UP -> DOWN;
                case DOWN -> UP;
                case LEFT -> RIGHT;
                case RIGHT -> LEFT;
            };
        }
    }
}
