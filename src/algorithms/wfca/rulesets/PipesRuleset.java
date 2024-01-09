package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.Vertex;

/**
 * This ruleset uses these ASCII characters {'╩', '╚', '╠' etc...} to form a structure
 * that connects these characters like one would connect them logically. (Duh!)
 */
public class PipesRuleset implements Ruleset {

    private static final Set<Integer> NUMBERS = new HashSet<>(Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());

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

            List<Tile> tiles = initTiles(neighbourInts);

            for (Tile tile : tiles) {
                int footprint = tile.getFootprint(direction.opposite());
                for (Tile t : Tile.values()) {
                    if (footprint == t.getFootprint(direction)) {
                        ints.add(t.getIdentifier());
                    }
                }
                if (ints.size() == NUMBERS.size()) {
                    break;
                }
            }
        }

        return Ruleset.intersect(allPossibleInts, NUMBERS);
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

    private static List<Tile> initTiles(Set<Integer> neighbourInts) {
        List<Tile> tiles = new ArrayList<>();
        for (int i : neighbourInts) {
            tiles.add(Tile.parseTile(i));
        }

        return tiles;
    }

    @Override
    public String stringRepresentation(int i) {
        Tile tile = Tile.parseTile(i);
        return tile == null ? " " : tile.toString();
    }

    @Override
    public Set<Integer> initialPossibilities(Vertex v) {
        return NUMBERS;
    }

    @Override
    public int maxBFSDepth() {
        return 8;
    }

    private enum Tile {
    
        TOP(1, "╦", new int[][] {
            {0, 0, 0}, 
            {1, 1, 1}, 
            {0, 1, 0}
            }),
    
        BOTTOM(2, "╩", new int[][] {
            {0, 1, 0}, 
            {1, 1, 1}, 
            {0, 0, 0}
            }),
    
        LEFT(3, "╠", new int[][] {
            {0, 1, 0}, 
            {0, 1, 1}, 
            {0, 1, 0}
            }),
    
        RIGHT(4, "╣", new int[][] {
            {0, 1, 0}, 
            {1, 1, 0}, 
            {0, 1, 0}
            }),
    
        NONE(5, "╬", new int[][] {
            {0, 1, 0}, 
            {1, 1, 1}, 
            {0, 1, 0}
            }), 
        
        ALL(6, " ", new int[][] {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
            }), 
            
        L_BOT_LEFT(7, "╚", new int[][] {
            {0, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
            }), 
        
        L_TOP_LEFT(8, "╔", new int[][] {
            {0, 0, 0},
            {0, 1, 1},
            {0, 1, 0}
            }), 
        
        L_TOP_RIGHT(9, "╗", new int[][] {
            {0, 0, 0},
            {1, 1, 0},
            {0, 1, 0}
            }), 
        
        L_BOT_RIGHT(10, "╝", new int[][] {
            {0, 1, 0},
            {1, 1, 0},
            {0, 0, 0}
            }), 
        
        HORIZONTAL(11, "═", new int[][] {
            {0, 0, 0},
            {1, 1, 1},
            {0, 0, 0}
            }), 
            
        VERTICAL(12, "║", new int[][] {
            {0, 1, 0},
            {0, 1, 0},
            {0, 1, 0}
            });
    
        private int identifier;
        private int[][] shape;
        private String symbol;
    
        private Tile(int identifier, String symbol, int[][] shape) {
            this.identifier = identifier;
            this.shape = shape;
            this.symbol = symbol;
        }
    
        public int bot() {
            return shape[2][1];
        }
    
        public int top() {
            return shape[0][1];
        }
    
        public int left() {
            return shape[1][0];
        }
    
        public int right() {
            return shape[1][2];
        }
    
        public int getIdentifier() {
            return identifier;
        }
    
        public static Tile parseTile(int identifier) {
            for (Tile tile : values()) {
                if (tile.identifier == identifier) {
                    return tile;
                }
            }
    
            return null;
        }
    
        @Override
        public String toString() {
            return symbol;
        }

        public int getFootprint(Direction direction) {
            return switch(direction) {
                case UP -> top();
                case DOWN -> bot();
                case LEFT -> left();
                case RIGHT -> right();
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
