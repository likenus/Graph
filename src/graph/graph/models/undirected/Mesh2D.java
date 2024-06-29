package src.graph.graph.models.undirected;

import src.graph.graph.interfaces.UndirectedGraph;

public interface Mesh2D extends UndirectedGraph {
    
    int getHeight();

    int getWidth();

    MeshType getMeshType();

    void setMeshType(MeshType meshType);

    public enum MeshType {

        SQUARE,

        ZYLINDER,

        DONUT;

        @Override
        public String toString() {
            return name().substring(0, 1) + name().substring(1, name().length()).toLowerCase();
        }
    }
}
