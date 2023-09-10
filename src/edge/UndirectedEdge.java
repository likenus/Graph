package src.edge;

import java.util.Objects;

import src.Tuple;
import src.edge.interfaces.Edge;
import src.vertices.Vertice;

public class UndirectedEdge implements Edge {
    
    Tuple<Vertice> vertices;

    public UndirectedEdge(Vertice v, Vertice w) {
        vertices = new Tuple<>(v, w);
    }

    @Override
    public Vertice getOther(Vertice v) {
        for (Vertice w : this.vertices) {
            if (!v.equals(w)) {
                return w;
            }
        }

        return null;
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
            Edge other = (UndirectedEdge) o;

            return other.getVertices().equals(this.vertices) 
                || other.getVertices().flip().equals(this.vertices);
                
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(vertices);
    }

    @Override
    public Tuple<Vertice> getVertices() {
        return this.vertices;
    }

    @Override
    public Vertice first() {
        return vertices.a();
    }

    @Override
    public Vertice second() {
        return vertices.b();
    }
}
