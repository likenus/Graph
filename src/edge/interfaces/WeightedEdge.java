package src.edge.interfaces;

public interface WeightedEdge extends Edge {
    
    /**
     * Returns the weight value of this edge. Can be negative.
     * @return An integer
     */
    public int getWeight();

    /**
     * Sets the weight value for this edge. Value may be negative.
     * @param weight The value to set the weight to
     */
    public void setWeight(int weight);
}
