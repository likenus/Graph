package src.graph;


public class UndirectedGraph extends UndirectedWeightedGraph {

    public UndirectedGraph() {
        super();
    }

    public UndirectedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        return addEdge(a, b);
    }
}
