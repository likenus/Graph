package target.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import src.graph.Graphs;
import src.graph.interfaces.Graph;
import src.graph.models.UndirectedGraph;
import src.graph.models.UndirectedWeightedGraph;
import src.vertices.interfaces.Vertice;

public class TestGraphs {

    private static <T> Graph<T> exampleGraph() {
        Graph<T> graph = new UndirectedGraph<>(10);

        for (int i = 0; i < 8; i++) {
            graph.addEdge(i, i + 1);
        }

        graph.addEdge(3, 5);
        graph.addEdge(1, 6);
        graph.addEdge(6, 8);
        graph.addEdge(6, 9);
        graph.addEdge(7, 9);
        graph.addEdge(9, 9);

        return graph;
    }
    
    private static <T> Graph<T> weightedExampleGraph() {
        Graph<T> graph = new UndirectedWeightedGraph<>(10);

        graph.addEdge(0, 1, 7);
        graph.addEdge(1, 2, 1);
        graph.addEdge(2, 3, 1);
        graph.addEdge(1, 4, 2);
        graph.addEdge(0, 5, 6);
        graph.addEdge(0, 6, 2);
        graph.addEdge(2, 4, 3);
        graph.addEdge(5, 6, 1);
        graph.addEdge(6, 7, 1);
        graph.addEdge(8, 9, 2);
        graph.addEdge(5, 8, 3);
        graph.addEdge(6, 8, 5);
        graph.addEdge(7, 9, 3);

        return graph;
    }

    @Test
    public void testDijkstra() {
        Graph<Object> graph = weightedExampleGraph();

        List<Vertice<Object>> path = new LinkedList<>();
        path.add(graph.parseVertice(0));
        path.add(graph.parseVertice(6));
        path.add(graph.parseVertice(7));
        path.add(graph.parseVertice(9));

        Assert.assertEquals(path, Graphs.dijkstra(graph, 0, 9));
    }

    @Test
    public void testBFS() {
        Graph<Object> graph = exampleGraph();

        List<Vertice<Object>> path = new LinkedList<>();
        path.add(graph.parseVertice(2));
        path.add(graph.parseVertice(1));
        path.add(graph.parseVertice(6));
        path.add(graph.parseVertice(9));

        Assert.assertEquals(path, Graphs.bfs(graph, 2, 9));
    }
}
