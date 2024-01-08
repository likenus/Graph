package src.graph.edge.models;

import java.util.Objects;

import src.graph.edge.abstracts.AbstractEdge;
import src.graph.edge.interfaces.Edge;
import src.graph.vertices.interfaces.Vertex;
import src.util.Tuple;

public class UndirectedEdge extends AbstractEdge {
    
    protected Tuple<Vertex> vertices;

    /**
     * Creates a new non-directional edge. In undirected edges the connected 
     * vertices are not required to be in order.
     * @param v The first vertex
     * @param w The second vertex
     */
    public UndirectedEdge(Vertex v, Vertex w) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(w);
        
        vertices = new Tuple<>(v, w);
        this.value = 1;
    }

    public UndirectedEdge(Vertex v, Vertex w, int i) {
        this(v, w);
        this.value = i;
    }

    @Override
    public Vertex getOther(Vertex v) {
        if (!vertices.contains(v)) {
            return null;
        }

        for (Vertex w : this.vertices) {
            if (!v.equals(w)) {
                return w;
            }
        }

        return v;
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

            return (other.getVertices().equals(this.vertices) 
                || other.getVertices().flip().equals(this.vertices))
                && other.getWeight() == this.value;
                
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, value);
    }

    @Override
    public String toString() {
        return "[%s %s, %s]".formatted(vertices.a(), vertices.b(), value);
    }

    @Override
    public Tuple<Vertex> getVertices() {
        return this.vertices;
    }

    @Override
    public Vertex start() {
        return vertices.a();
    }

    @Override
    public Vertex end() {
        return vertices.b();
    }
}
