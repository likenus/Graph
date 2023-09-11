package src.graph.interfaces;

public interface UnionFind<T> {

    boolean add();
    
    void union(int a, int b);

    int find(int key);
}
