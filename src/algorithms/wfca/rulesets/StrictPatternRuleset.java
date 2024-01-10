package src.algorithms.wfca.rulesets;

import java.awt.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Map.Entry;
import java.util.Set;

import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.Vertex;
import src.util.Ansi;

/**
 * WIP
 * NOT working yet
 * Update It's still not working but starting to take shape
 */
public class StrictPatternRuleset implements Ruleset {

    public static final String TILE_SYMBOL = "■";

    private final Set<Integer> numbers = new HashSet<>();
    private final Map<Integer, Tile> pattern = new HashMap<>();

    private int idCounter = 0;
    
    public StrictPatternRuleset(int[][] input) {
        int height = input.length;
        int width = input[0].length;

        Map<Tile, Integer> map = new HashMap<>();
        int[][] bitmap = new int[height][width];

        // Remap input map to bitmap
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                DirectionalTupel<Integer> tupel = new DirectionalTupel<>();
                tupel = neighboursOf(i, j, input, tupel);
                Tile tile = new Tile(0, input[i][j], tupel);
                if (!map.keySet().contains(tile)) {
                    map.put(tile, idCounter);
                    numbers.add(idCounter);
                    idCounter++;
                }
                bitmap[i][j] = map.get(tile);
            }
        }

        for (int n : numbers) {
            pattern.put(n, new Tile(n, -1, new DirectionalTupel<>()));
        }

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int group = input[i][j];
                int identifier = bitmap[i][j];
                Tile tile = pattern.get(identifier);
                neighboursOf(i, j, bitmap, tile.getNeighbours());
                tile.setGroup(group);
            }
        }

        // Error Tile
        DirectionalTupel<Integer> tupel = new DirectionalTupel<>();
        for (Direction direction : Direction.values()) {
            tupel.addAll(direction, numbers);
        }
        pattern.put(-1, new Tile(idCounter + 1, -1, tupel));

    }

    private DirectionalTupel<Integer> neighboursOf(int x, int y, int[][] input, DirectionalTupel<Integer> neighbours) {
        int height = input.length;
        int width = input[0].length;

        if (y < width) {
            neighbours.add(Direction.RIGHT, input[x][(y + 1) % width]);
        }
        if (y > 0) {
            neighbours.add(Direction.LEFT, input[x][y - 1]);
        } else {
            neighbours.add(Direction.LEFT, input[x][width - 1]);
        }
        if (x < height) {
            neighbours.add(Direction.DOWN, input[(x + 1) % height][y]);
        }
        if (x > 0) {
            neighbours.add(Direction.UP, input[x - 1][y]);
        } else {
            neighbours.add(Direction.UP, input[height - 1][y]);
        }

        return neighbours;
    }

    @Override
    public Set<Integer> ruleset(Graph graph, Vertex v, List<Set<Integer>> possibilities) {
        Mesh2D mesh = (Mesh2D) graph;

        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        Map<Direction, Vertex> neighbours = initNeighbours(v, graph.neighbours(v.getKey()), mesh);

        for (Entry<Direction, Vertex> entry : neighbours.entrySet()) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Vertex neighbour = entry.getValue();
            Direction direction = entry.getKey();

            Set<Integer> neighbourInts = possibilities.get(neighbour.getKey());

            for (int i : neighbourInts) {
                ints.addAll(pattern.get(i).getNeighbours().get(direction.opposite()));
            }
        }

        return Ruleset.intersect(allPossibleInts, numbers);
    }

    private static Map<Direction, Vertex> initNeighbours(Vertex v, List<Vertex> neighbours, Mesh2D mesh) {
        Map<Direction, Vertex> map = new EnumMap<>(Direction.class);

        for (Vertex neighbour : neighbours) {
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
    public String stringRepresentation(int i) {
        // return String.valueOf(i);
        int group = pattern.get(i).getGroup();
        return switch(group) {
            case 3 -> Ansi.White + TILE_SYMBOL;
            case 1 -> Ansi.Blue + TILE_SYMBOL;
            case 2 -> Ansi.Red + TILE_SYMBOL;
            default -> " ";
        };
    }

    @Override
    public Set<Integer> initialPossibilities(Vertex v) {
        return numbers;
    }

    @Override
    public int maxBFSDepth() {
        return Integer.MAX_VALUE - 1;
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

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }
            if (o == this) {
                return true;
            }
            if (!(o instanceof DirectionalTupel)) {
                return false;
            }
            DirectionalTupel<?> other = (DirectionalTupel<?>) o;
            return other.up.equals(this.up) && other.down.equals(this.down) 
                && other.left.equals(this.left) && other.right.equals(this.right);
        }

        @Override
        public int hashCode() {
            return Objects.hash(up, down, left, right);
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

    private class Tile {

        private int group;
        private final int identifier;
        private final DirectionalTupel<Integer> neighbours;

        public Tile(int identifier, int group, DirectionalTupel<Integer> neighbours) {
            this.identifier = identifier;
            this.group = group;
            this.neighbours = neighbours;
        }

        public int getGroup() {
            return this.group;
        }

        // public int getIdentifier() {
        //     return this.identifier;
        // }

        public void setGroup(int i) {
            this.group = i;
        }

        public DirectionalTupel<Integer> getNeighbours() {
            return this.neighbours;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) return false;
            if (o == this) return true;
            if (!(o instanceof Tile)) return false;
            Tile other = (Tile) o;

            return other.identifier == this.identifier && other.group == this.group 
                && other.neighbours.equals(this.neighbours);
        }

        @Override
        public int hashCode() {
            return Objects.hash(group, neighbours, identifier);
        }
    }

    @Override
    public Color getTileColor(int tileValue) {
        return new Color(0,0,0,255);
    }
}
