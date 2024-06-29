package src.graph.graph.models.undirected;

import src.graph.edge.Edge;
import src.graph.edge.models.UndirectedEdge;
import src.graph.graph.abstracts.LinkedGraph;
import src.graph.graph.interfaces.UndirectedGraph;
import src.graph.vertices.Vertex;

public class UndirectedWeightedLinkedGraph extends LinkedGraph implements UndirectedGraph {

    public UndirectedWeightedLinkedGraph() {
        super();
    }

    public UndirectedWeightedLinkedGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        Vertex v = parseVertex(a);
        Vertex w = parseVertex(b);

        return addEdge(v, w, value);
    }

    protected boolean addEdge(Vertex v, Vertex w, int value) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new UndirectedEdge(v, w, value);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);
        w.connectEdge(edge);

        return this.edges.add(edge);
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }
}
