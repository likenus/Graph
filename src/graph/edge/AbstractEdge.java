package src.graph.edge;

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
