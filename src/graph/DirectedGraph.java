package src.graph;

import src.edge.DirectedEdge;
import src.edge.interfaces.Edge;
import src.graph.abstracts.SingleGraph;
import src.vertices.Vertice;

public class DirectedGraph extends SingleGraph {

    public DirectedGraph() {
        super();
    }

    public DirectedGraph(int i) {
        super(i);
    }
    
    
    protected boolean addEdge(Vertice v, Vertice w) {
        if (v == null || w == null) {
            return false;
        }

        Edge edge = new DirectedEdge(v, w);

        if (this.edges.contains(edge)) {
            return false;
        }

        v.connectEdge(edge);

        return this.edges.add(edge);
    }

    @Override
    public boolean addEdge(int a, int b) {
        Vertice v = parseVertice(a);
        Vertice w = parseVertice(b);

        return addEdge(v, w);
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addEdge'");
    }
}
