package src.edge;

import java.util.Objects;

import src.edge.interfaces.WeightedEdge;
import src.vertices.Vertice;

public class UndirectedWeightedEdge extends UndirectedEdge implements WeightedEdge {

    private int value;

    public UndirectedWeightedEdge(Vertice v, Vertice w, int value) {
        super(v, w);
        this.value = value;
    }

    @Override
    public int getWeight() {
        return this.value;
    }

    @Override
    public void setWeight(int weight) {
        this.value = weight;
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
            WeightedEdge other = (UndirectedWeightedEdge) o;

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
}
