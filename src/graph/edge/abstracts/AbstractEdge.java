package src.graph.edge.abstracts;

import src.graph.edge.interfaces.Edge;

public abstract class AbstractEdge implements Edge {
    
    protected int value;

    @Override
    public int getWeight() {
        return this.value;
    }
    
    @Override
    public void setWeight(int weight) {
        this.value = weight;
    }
}
