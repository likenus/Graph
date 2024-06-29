package src.graph.graph.models.undirected;

public class UndirectedArrayGraph extends UndirectedWeightedArrayGraph {
    
    public UndirectedArrayGraph() {
        super();
    }

    public UndirectedArrayGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        return super.addEdge(a, b, 1);
    }
}
