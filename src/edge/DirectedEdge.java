package src.edge;

import src.Tuple;
import src.edge.interfaces.Edge;
import src.vertices.Vertice;

public class DirectedEdge implements Edge {
    
    Vertice start;
    Vertice end;

    public DirectedEdge(Vertice v, Vertice w) {
        this.start = v;
        this.end = w;
    }

    @Override
    public Vertice getOther(Vertice v) {
        return end;
    }

    @Override
    public Tuple<Vertice> getVertices() {
        return new Tuple<>(start, end);
    }

    @Override
    public Vertice first() {
        return this.start;
    }

    @Override
    public Vertice second() {
        return this.end;
    }
}
