package src.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.edge.interfaces.Edge;
import src.edge.models.UndirectedEdge;
import src.graph.interfaces.Graph;
import src.graph.interfaces.UnionFind;
import src.graph.models.ComponentSet;
import src.graph.models.DirectedWeightedGraph;
import src.graph.models.UndirectedWeightedGraph;
import src.vertices.interfaces.Vertice;

/**
 * This class consists of static utility methods operating on graphs.
 */
public final class Graphs {
    
    private Graphs() {
        throw new UnsupportedOperationException();
    }

    /**
     * Uses Breadth First Search to calculate the shortest Path. 
     * </p>
     * Note: The shortest Path is considered the path with the least amount of edges and
     * does not account for weighted Graphs. 
     * </p>
     * This runs in linear time. 
     * @param g The Graph to execute the BFS on.
     * @param s The key of the start node.
     * @param t The key of the target node.
     * @return The Path from s to t including s and t. Returns an empty list if {@code s.equals(t)} is true.
     * Returns null if no path was found.
     */
    public static List<Vertice> bfs(Graph g, int s, int t) {
        List<Vertice> path = new LinkedList<>();
        boolean[] exploredNodes = new boolean[g.vertices().size()];

        Vertice start = g.parseVertice(s);
        Vertice target = g.parseVertice(t);

        if (start.equals(target)) {
            return path;
        }
        
        Queue<Vertice> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        exploredNodes[s] = true;

        // BFS
        while (!queue.isEmpty()) {
            Vertice u = queue.poll();
            for (Vertice v : u.neighbours()) {
                if (!exploredNodes[v.getKey()]) {
                    queue.add(v);
                    exploredNodes[v.getKey()] = true;
                    v.setParent(u);
                }
            }
        }

        //Reconstrucing path
        if (target.getParent() == null) {
            return null;
        }

        Vertice parent = target;

        while (parent.getParent() != null) {
            path.add(parent);
            parent = parent.getParent();
        }

        path.add(start);
        
        Collections.reverse(path);

        return path;
    }

    /**
     * Uses Dijkstras Algorithm to calculate the shortest path from s to t taking weighted edges into account.
     * </p>
     * Note: The shortest path is therefore the path with the lowest sum of weights.
     * </p>
     * This runs in quasi-linear time.
     * @param g The Graph to execute the algorithm on.
     * @param s The key of the start node.
     * @param t The key of the target node.
     * @return The shortes Path from s to t with respect to weighted edges including s and t. 
     * Returns an empty list if {@code s.equals(t)} is true.
     * Returns null if no path was found.
     */
    public static List<Vertice> dijkstra(Graph g, int s, int t) {
        List<Vertice> path = new LinkedList<>();
        boolean[] exploredNodes = new boolean[g.vertices().size()];

        Vertice start = g.parseVertice(s);
        Vertice target = g.parseVertice(t);

        if (start.equals(target)) {
            return path;
        }


        int[] distances = new int[g.vertices().size()];
        for (int i = 0; i < distances.length; i++) {
            distances[i] = Integer.MAX_VALUE;
        }
        distances[s] = 0;
        BinaryHeap<Vertice> heap = new BinaryHeap<>();
        for (Vertice v : g.vertices()) {
            heap.push(v, distances[v.getKey()]);
        }

        while(!heap.isEmpty()) {
            Vertice u = heap.pop();
            exploredNodes[u.getKey()] = true;
            for (Vertice v : u.neighbours()) {
                if (distances[v.getKey()] > distances[u.getKey()] + g.parseEdge(u.getKey(), v.getKey()).getWeight()) {
                    distances[v.getKey()] = distances[u.getKey()] + g.parseEdge(u.getKey(), v.getKey()).getWeight();
                    if (!exploredNodes[v.getKey()]) {
                        v.setParent(u);
                    }
                    heap.decPrio(v, distances[v.getKey()]);
                }
            }
        }
    
        //Reconstrucing path
        if (target.getParent() == null) {
            return null;
        }

        Vertice parent = target;

        while (parent.getParent() != null) {
            path.add(parent);
            parent = parent.getParent();
        }

        path.add(start);
        
        Collections.reverse(path);

        return path;
    }

    /**
     * Calculates a minimal spanning tree using Kruskal's Algorithm. 
     * A MST is considered a graph that connects all vertices with the minimal amount of edges (i.e n - 1)
     * and the lowest total sum of the weight from all edges.
     * </p>
     * Note: If the input graph does not form a single component, 
     * then all components will be turned into MSTs independently.     
     * </p>
     * This runs in quasi-linear time.
     * @param g The graph to calculate the mst from, may not be null
     * @return A copy of the original graph as a MST
     * @throws ClassCastException When input graph is a directed graph
     */
    public static Graph mst(Graph g) {
        Objects.requireNonNull(g);

        if (g instanceof DirectedWeightedGraph) {
            throw new ClassCastException();
        }

        Graph copy = new UndirectedWeightedGraph(g.vertices().size());

        List<Edge> edges = new LinkedList<>(g.edges());
        for (Edge e : g.edges()) {
            edges.add(new UndirectedEdge(copy.parseVertice(e.start().getKey())
                , copy.parseVertice(e.end().getKey()), e.getWeight()));
        }

        edges.sort(Comparator.comparing(Edge::getWeight));
        UnionFind uf = new ComponentSet(edges.size());

        Edge edge = edges.get(0);
        copy.addEdge(edge.start().getKey(), edge.end().getKey(), edge.getWeight());

        for (int i = 1; i < edges.size() - 1; i++) {
                Edge e = edges.get(i);
                if (uf.find(e.start().getKey()) != uf.find(e.end().getKey())) {
                    uf.union(e.start().getKey(), e.end().getKey());
                    copy.addEdge(e.start().getKey(), e.end().getKey(), e.getWeight());
                }
                
        }

        return copy;
    }
}
