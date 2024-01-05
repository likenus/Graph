package src.algorithms.wfca.rulesets;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import src.graph.interfaces.Graph;
import src.graph.models.undirected.Mesh2D;
import src.vertices.interfaces.Vertice;

/**
 * This ruleset uses these ASCII characters {'╩', '╚', '╠' etc...} to form a structure
 * that connects these characters like one would connect them logically. (Duh!)
 */
public class PipesRuleset implements Ruleset {

    private static final Set<Integer> NUMBERS = new HashSet<>(Arrays.asList(Tile.values()).stream().map(Tile::getIdentifier).toList());

    @Override
    @SuppressWarnings("all") // If it works dont touch it
    public Set<Integer> ruleset(Graph graph, Vertice v, List<Set<Integer>> possibilities) {
        Mesh2D mesh = (Mesh2D) graph;

        List<Set<Integer>> allPossibleInts = new ArrayList<>();

        List<Vertice> neighbours = graph.neighbours(v.getKey());

        for (Vertice neighbour : neighbours) {
            Set<Integer> ints = new HashSet<>();
            allPossibleInts.add(ints);

            Set<Integer> neighbourInts = possibilities.get(neighbour.getKey());
            
            // Left
            if (neighbour.getKey() == v.getKey() - 1 || neighbour.getKey() == v.getKey() + mesh.getWidth() - 1) {
                List<Tile> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tile.parseTile(i));
                }
                for (Tile tile : tiles) {
                    if (tile.right() == 0) {
                        for (Tile t : Tile.values()) {
                            if (t.left() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tile t : Tile.values()) {
                            if (t.left() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Right
            if (neighbour.getKey() == v.getKey() + 1 || neighbour.getKey() == v.getKey() - mesh.getWidth() + 1) {
                List<Tile> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tile.parseTile(i));
                }
                for (Tile tile : tiles) {
                    if (tile.left() == 0) {
                        for (Tile t : Tile.values()) {
                            if (t.right() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tile t : Tile.values()) {
                            if (t.right() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Top
            if (neighbour.getKey() == v.getKey() - mesh.getWidth()) {
                List<Tile> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tile.parseTile(i));
                }
                for (Tile tile : tiles) {
                    if (tile.bot() == 0) {
                        for (Tile t : Tile.values()) {
                            if (t.top() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tile t : Tile.values()) {
                            if (t.top() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }

            // Bot
            if (neighbour.getKey() == v.getKey() + mesh.getWidth()) {
                List<Tile> tiles = new ArrayList<>();
                for (int i : neighbourInts) {
                    tiles.add(Tile.parseTile(i));
                }
                for (Tile tile : tiles) {
                    if (tile.top() == 0) {
                        for (Tile t : Tile.values()) {
                            if (t.bot() == 0) {
                                ints.add(t.getIdentifier());
                            }
                        }
                    } else {
                       for (Tile t : Tile.values()) {
                            if (t.bot() == 1) {
                                ints.add(t.getIdentifier());
                            }
                        } 
                    }
                }
            }
        }

        Set<Integer> possibleInts = new HashSet<>();

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
        return tile == null ? " " : tile.toString();
    }

    private enum Tile {
    
        TOP(1, new int[][] {
            {0, 0, 0}, 
            {1, 1, 1}, 
            {0, 1, 0}
            }),
    
        BOTTOM(2, new int[][] {
            {0, 1, 0}, 
            {1, 1, 1}, 
            {0, 0, 0}
            }),
    
        LEFT(3, new int[][] {
            {0, 1, 0}, 
            {0, 1, 1}, 
            {0, 1, 0}
            }),
    
        RIGHT(4, new int[][] {
            {0, 1, 0}, 
            {1, 1, 0}, 
            {0, 1, 0}
            }),
    
        NONE(5, new int[][] {
            {0, 1, 0}, 
            {1, 1, 1}, 
            {0, 1, 0}
            }), 
        
        ALL(6, new int[][] {
            {0, 0, 0},
            {0, 1, 0},
            {0, 0, 0}
            }), 
            
        L_BOT_LEFT(7, new int[][] {
            {0, 1, 0},
            {0, 1, 1},
            {0, 0, 0}
            }), 
        
        L_TOP_LEFT(8, new int[][] {
            {0, 0, 0},
            {0, 1, 1},
            {0, 1, 0}
            }), 
        
        L_TOP_RIGHT(9, new int[][] {
            {0, 0, 0},
            {1, 1, 0},
            {0, 1, 0}
            }), 
        
        L_BOT_RIGHT(10, new int[][] {
            {0, 1, 0},
            {1, 1, 0},
            {0, 0, 0}
            });
    
        private int identifier;
        private int[][] shape;
    
        private Tile(int identifier, int[][] shape) {
            this.identifier = identifier;
            this.shape = shape;
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
            return switch(identifier) {
                case 1 -> "╦";
                case 2 -> "╩";
                case 3 -> "╠";
                case 4 -> "╣";
                case 5 -> "╬";
                case 6 -> "¤";
                case 7 -> "╚";
                case 8 -> "╔";
                case 9 -> "╗";
                case 10 -> "╝";
                default -> " ";
            };
        }
    }

}
