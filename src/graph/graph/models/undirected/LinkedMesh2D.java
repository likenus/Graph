package src.graph.graph.models.undirected;

public class LinkedMesh2D extends UndirectedLinkedGraph implements Mesh2D {
    
    private final int width;
    private final int height;

    private MeshType meshType;

    public LinkedMesh2D(int width, int height) {
        super(width * height);
        this.width = width;
        this.height = height;

        // Horizontal lines
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width - 1; j++) {
                this.addEdge(i * width + j, i * width + j + 1);
            }
        }

        // Vertical lines
        for (int i = 0; i < height - 1; i++) {
            for (int j = 0; j < width; j++) {
                this.addEdge(width * i + j, width * i + j + width);
            }
        }

        this.meshType = MeshType.SQUARE;
    }

    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    public MeshType getMeshType() {
        return meshType;
    }

    public void setMeshType(MeshType meshType) {
        this.meshType = meshType;
    }
}
