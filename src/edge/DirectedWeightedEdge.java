package src.edge;

import java.util.Objects;

import src.edge.interfaces.WeightedEdge;
import src.vertices.Vertice;

public class DirectedWeightedEdge extends DirectedEdge implements WeightedEdge {

    private int value;

    public DirectedWeightedEdge(Vertice v, Vertice w, int value) {
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
            WeightedEdge other = (DirectedWeightedEdge) o;

            return other.start().equals(this.start) && other.end().equals(this.end)
                && other.getWeight() == this.value;
        }

        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(start, end, value);
    }
}
