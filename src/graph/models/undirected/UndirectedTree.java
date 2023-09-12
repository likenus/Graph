package src.graph.models.undirected;

import src.vertices.interfaces.Vertice;

public class UndirectedTree extends UndirectedWeightedTree {

    public UndirectedTree() {
        super();
    }

    public UndirectedTree(int i) {
        super(i);
    }
    
    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w, 1);
    }
}
