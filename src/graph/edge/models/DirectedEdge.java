package src.graph.edge.models;

import java.util.Objects;

import src.graph.edge.abstracts.AbstractEdge;
import src.graph.edge.interfaces.Edge;
import src.graph.vertices.interfaces.Vertex;
import src.util.Tuple;

public class DirectedEdge extends AbstractEdge {
    
    protected Vertex start;
    protected Vertex end;

    /**
     * Creates a new directed edge. Both vertices are required to be non-null.
     * A directed edge has an explicit start and end, thus the order of params is important.
     * @param start The starting vertex of this edge/The vertex this edge is pointing from
     * @param end The ending vertex of this edge/The vertex this edge is pointing towards
     */
    public DirectedEdge(Vertex start, Vertex end) {
        Objects.requireNonNull(start);
        Objects.requireNonNull(end);

        this.start = start;
        this.end = end;
        this.value = 1;
    }

    public DirectedEdge(Vertex start, Vertex end, int i) {
        this(start, end);
        this.value = i;
    }

    @Override
    public Vertex getOther(Vertex v) {
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
    public Tuple<Vertex> getVertices() {
        return new Tuple<>(start, end);
    }

    @Override
    public Vertex start() {
        return this.start;
    }

    @Override
    public Vertex end() {
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
}
