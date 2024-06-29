package src.graph.graph.models.undirected;

import java.util.ArrayList;
import java.util.List;

import src.graph.edge.Edge;
import src.graph.edge.models.UndirectedEdge;
import src.graph.graph.abstracts.ArrayGraph;
import src.graph.graph.interfaces.UndirectedGraph;

public class UndirectedWeightedArrayGraph extends ArrayGraph implements UndirectedGraph {

    public UndirectedWeightedArrayGraph() {
        super();
    }

    public UndirectedWeightedArrayGraph(int i) {
        super(i);
    }

    @Override
    public boolean addEdge(int a, int b) {
        return addEdge(a, b, 1);
    }

    @Override
    public void removeEdge(int a, int b) {
        if (a >= n || b >= n) { return; }
        adjacencyMatrix[a][b] = adjacencyMatrix[b][a] = null;
    }

    @Override
    public boolean addEdge(int a, int b, int value) {
        if (a >= n || b >= n) { return false; }
        if (adjacencyMatrix[a][b] != null) { return false; }
        Edge edge = new UndirectedEdge(vertices.get(a), vertices.get(b), value);
        adjacencyMatrix[a][b] = adjacencyMatrix[b][a] = edge;
        vertices.get(a).connectEdge(edge);
        vertices.get(b).connectEdge(edge);
        return true;
    }

    @Override
    public List<Edge> edges() {
        List<Edge> edges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            for (int j = i; j < n; j++) {
                if (adjacencyMatrix[i][j] != null) {
                    edges.add(adjacencyMatrix[i][j]);
                }
            }
        }
        return edges;
    }
    
}
