package target.test;

import java.util.LinkedList;
import java.util.List;

import org.junit.Assert;
import org.junit.Test;

import src.graph.edge.Edge;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.interfaces.Tree;
import src.graph.graph.models.undirected.UndirectedGraph;
import src.graph.graph.models.undirected.UndirectedTree;
import src.graph.graph.models.undirected.UndirectedWeightedGraph;
import src.graph.vertices.Vertex;
import src.util.Graphs;

public class TestGraphs {

    private static Graph exampleGraph() {
        Graph graph = new UndirectedGraph(10);

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
    
    public static Graph weightedExampleGraph() {
        Graph graph = new UndirectedWeightedGraph(10);

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
        Graph graph = weightedExampleGraph();

        List<Vertex> path = new LinkedList<>();
        path.add(graph.parseVertex(0));
        path.add(graph.parseVertex(6));
        path.add(graph.parseVertex(7));
        path.add(graph.parseVertex(9));

        Assert.assertEquals(path, Graphs.dijkstra(graph, 0, 9));
    }

    @Test
    public void testBFS() {
        Graph graph = exampleGraph();

        List<Vertex> path = new LinkedList<>();
        path.add(graph.parseVertex(2));
        path.add(graph.parseVertex(1));
        path.add(graph.parseVertex(6));
        path.add(graph.parseVertex(9));

        Assert.assertEquals(path, Graphs.bfs(graph, 2, 9));
    }

    @Test
    public void testTree() {
        Tree tree = new UndirectedTree(10);
        tree.addEdge(0, 1);
        tree.addEdge(1, 2);
        tree.addEdge(3, 4);
        tree.addEdge(0, 5);
        tree.addEdge(5, 6);
        tree.addEdge(5, 7);

        List<Edge> edges = new LinkedList<>(tree.edges());
        tree.addEdge(6, 7);

        Assert.assertEquals(edges, tree.edges());
        
        System.out.println(tree.edges());

        tree.removeEdge(5, 7);
        tree.removeEdge(0, 5);
        tree.addEdge(5, 7);
        tree.addEdge(0, 5);

        System.out.println(tree.edges());
    }
}
