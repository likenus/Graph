package src.graph.interfaces;

public interface UnionFind {

    boolean add();
    
    void union(int a, int b);

    int find(int key);
}
