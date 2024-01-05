package src.edge.models;

import java.util.Objects;

import src.edge.abstracts.AbstractEdge;
import src.edge.interfaces.Edge;
import src.util.Tuple;
import src.vertices.interfaces.Vertice;

public class DirectedEdge extends AbstractEdge {
    
    protected Vertice start;
    protected Vertice end;
    protected int value;

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
        this.value = 1;
    }

    public DirectedEdge(Vertice start, Vertice end, int i) {
        this(start, end);
        this.value = i;
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
            Edge other = (Edge) o;

            return other.start().equals(this.start) && other.end().equals(this.end)
                && other.getWeight() == this.value;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, value);
    }

    @Override
    public String toString() {
        return "[%s %s, %s]".formatted(start, end, value);
    }

    @Override
    public void setWeight(int weight) {
        this.value = weight;
    }
}
