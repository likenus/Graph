package src.edge.models;

import java.util.Objects;

import src.edge.interfaces.Edge;
import src.util.Tuple;
import src.vertices.interfaces.Vertice;

public class UndirectedEdge implements Edge {
    
    protected Tuple<Vertice> vertices;
    protected int value;

    /**
     * Creates a new non-directional edge. In undirected edges the connected 
     * vertices are not required to be in order.
     * @param v The first vertice
     * @param w The second vertice
     */
    public UndirectedEdge(Vertice v, Vertice w) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(w);
        
        vertices = new Tuple<>(v, w);
        this.value = 1;
    }

    public UndirectedEdge(Vertice v, Vertice w, int i) {
        this(v, w);
        this.value = i;
    }

    @Override
    public Vertice getOther(Vertice v) {
        if (!vertices.contains(v)) {
            return null;
        }

        for (Vertice w : this.vertices) {
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
    public Tuple<Vertice> getVertices() {
        return this.vertices;
    }

    @Override
    public Vertice start() {
        return vertices.a();
    }

    @Override
    public Vertice end() {
        return vertices.b();
    }

    @Override
    public int getWeight() {
        return this.value;
    }

    @Override
    public void setWeight(int weight) {
        this.value = weight;
    }
}
