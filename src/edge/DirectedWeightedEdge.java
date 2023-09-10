package src.edge;

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
}
