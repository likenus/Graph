package src.edge;

import java.util.Objects;

import src.Tuple;
import src.edge.interfaces.Edge;
import src.vertices.Vertice;

public class DirectedEdge implements Edge {
    
    protected Vertice start;
    protected Vertice end;

    /**
     * Creates a new directed edge. Both vertices are required to be non-null.
     * A directed edge has an explicit start and end, thus the order of params is important.
     * @param start The starting vertice of this edge/The vertice this edge is pointing from
     * @param end The ending vertice of this edge/The vertice this edge is pointing towards
     */
    public DirectedEdge(Vertice start, Vertice end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);

        this.start = start;
        this.end = end;
    }

    @Override
    public Vertice getOther(Vertice v) {
        Objects.requireNonNull(v);

        if (!(start.equals(v) || end.equals(v))) {
            return null;
        }

        if (v.equals(start)) {
            return end;
        }
        return start;
    }

    @Override
    public Tuple<Vertice> getVertices() {
        return new Tuple<>(start, end);
    }

    @Override
    public Vertice start() {
        return this.start;
    }

    @Override
    public Vertice end() {
        return this.end;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }

        if (o == this) {
            return true;
        }

        if (o.getClass() == this.getClass()) {
            Edge other = (DirectedEdge) o;

            return other.start().equals(this.start) && other.end().equals(this.end);
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end);
    }
}
