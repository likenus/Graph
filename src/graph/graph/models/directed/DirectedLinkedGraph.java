package src.graph.graph.models.directed;

import src.graph.vertices.Vertex;

public class DirectedLinkedGraph extends DirectedWeightedLinkedGraph {

    public DirectedLinkedGraph() {
        super();
    }

    public DirectedLinkedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertex v = parseVertex(a);
        Vertex w = parseVertex(b);

        return addEdge(v, w, 1);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        return addEdge(a, b);
    }
}
