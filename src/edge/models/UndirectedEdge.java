package src.edge.models;

import java.util.Objects;

import src.edge.interfaces.Edge;
import src.util.Tuple;
import src.vertices.interfaces.Vertice;

public class UndirectedEdge<T> implements Edge<T> {
    
    protected Tuple<Vertice<T>> vertices;
    protected int value;

    /**
     * Creates a new non-directional edge. In undirected edges the connected 
     * vertices are not required to be in order.
     * @param v The first vertice
     * @param w The second vertice
     */
    public UndirectedEdge(Vertice<T> v, Vertice<T> w) {
        Objects.requireNonNull(v);
        Objects.requireNonNull(w);
        
        vertices = new Tuple<>(v, w);
        this.value = 1;
    }

    public UndirectedEdge(Vertice<T> v, Vertice<T> w, int i) {
        this(v, w);
        this.value = i;
    }

    @Override
    public Vertice<T> getOther(Vertice<T> v) {
        if (!vertices.contains(v)) {
            return null;
        }

        for (Vertice<T> w : this.vertices) {
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

        if (o.getClass() != this.getClass()) {
            return false;    
        }

        Edge<?> other = (Edge<?>) o;

        return (other.getVertices().equals(this.vertices) 
            || other.getVertices().flip().equals(this.vertices))
            && other.getWeight() == this.value;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices, value);
    }

    @Override
    public Tuple<Vertice<T>> getVertices() {
        return this.vertices;
    }

    @Override
    public Vertice<T> start() {
        return vertices.a();
    }

    @Override
    public Vertice<T> end() {
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
