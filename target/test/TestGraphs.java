package target.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import src.graph.DirectedWeightedGraph;
import src.graph.Graphs;
import src.graph.UndirectedGraph;
import src.graph.interfaces.Graph;
import src.graph.interfaces.WeightedGraph;
import src.vertices.Vertice;

public class TestGraphs {
    
    @Test
    public void testDijkstra() {
        WeightedGraph graph = new DirectedWeightedGraph(5);

        graph.addEdge(0, 1, 10);
        graph.addEdge(0, 2, 5);
        graph.addEdge(2, 1, 1);
        graph.addEdge(0, 3, 1);
        graph.addEdge(3, 2, 1);

        List<Vertice> path = new LinkedList<>();
        path.add(graph.parseVertice(0));
        path.add(graph.parseVertice(3));
        path.add(graph.parseVertice(2));
        path.add(graph.parseVertice(1));

        Assert.assertEquals(Graphs.dijkstra(graph, 0, 1), path);
    }

    @Test
    public void testBFS() {
        Graph graph = new UndirectedGraph(5);

        
    }
}
