package src.util;

import java.util.Set;


public enum Tiles {

    DEEP_WATER(1, "\u001B[34m", Set.of(1, 2)),
    
    WATER(2, "\u001B[36m", Set.of(1, 2, 3)),
    
    BEACH(3, "\u001B[33m", Set.of(2, 3, 4)),
    
    FOREST(4, "\u001B[32m", Set.of(4, 3, 5)),
    
    MOUNTAIN(5, "\u001B[30m", Set.of(4, 5)), 
    
    ERROR(-1, "\u001B[32m", Set.of());

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

    private int identifier;
    private String ansiColor;
    private Set<Integer> possibleNeighbours;
    
    private Tiles(int identifier, String ansiColor, Set<Integer> possibleNeighbours) {
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

    @Override
    public String toString() {
        return this.ansiColor + TILE_SYMBOL;
    }
}
