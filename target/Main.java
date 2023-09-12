package target;

import src.graph.interfaces.*;
import src.graph.models.*;

public class Main {
    
    public static void main(String[] args) {
        UnionFind<String> u = new ComponentSet<>(10);

        u.union(0, 1);
        u.union(1, 2);
        u.union(2, 3);
        u.union(4, 5);
        u.union(5, 6);
        u.union(7, 9);
        u.union(8, 9);

        System.out.println(u.find(0) + " " + u.find(5) + " " + u.find(9));
    }
}
