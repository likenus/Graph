package src.graph.graph.models.undirected;

import src.graph.vertices.interfaces.Vertex;

public class UndirectedTree extends UndirectedWeightedTree {

    public UndirectedTree() {
        super();
    }

    public UndirectedTree(int i) {
        super(i);
    }
    
    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertex v = parseVertex(a);
        Vertex w = parseVertex(b);

        return addEdge(v, w, 1);
    }
}
