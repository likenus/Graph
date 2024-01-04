package src.util;

public enum Tiles2 {
    
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

    private Tiles2(int identifier, int[][] shape) {
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

    public static Tiles2 parseTile(int identifier) {
        for (Tiles2 tile : values()) {
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
