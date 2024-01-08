package src.graph.util;

import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.graph.edge.interfaces.Edge;
import src.graph.edge.models.UndirectedEdge;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.interfaces.Tree;
import src.graph.graph.interfaces.UnionFind;
import src.graph.graph.models.directed.ComponentSet;
import src.graph.graph.models.directed.DirectedGraph;
import src.graph.graph.models.directed.DirectedWeightedGraph;
import src.graph.graph.models.undirected.UndirectedGraph;
import src.graph.graph.models.undirected.UndirectedWeightedTree;
import src.graph.vertices.interfaces.Vertice;
import src.util.BinaryHeap;

/**
 * This class consists of static utility methods operating on graphs.
 */
public final class Graphs {
    
    private Graphs() {
        throw new UnsupportedOperationException();
    }

    private static Vertice parent(Vertice[] parents, Vertice v) {
        return parents[v.getKey()];
    }

    private static void setParent(Vertice[] parents, Vertice v, Vertice w) {
        parents[v.getKey()] = w;
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
     * @see Graphs#dijkstra()
     */
    public static List<Vertice> bfs(Graph g, int s, int t) {
        Objects.requireNonNull(g);

        List<Vertice> path = new LinkedList<>();
        Vertice[] parents = new Vertice[g.sizeVertices()];
        boolean[] exploredNodes = new boolean[g.sizeVertices()];

        Vertice start = g.parseVertice(s);
        Vertice target = g.parseVertice(t);

        if (start == null || target == null) {
            throw new IllegalArgumentException("Start or target vertice dont exist.");
        }

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
                    setParent(parents, v, u);
                }
                if (v.equals(target)) {
                    queue.clear();
                }
            }
        }

        return reconstructPath(parents, start, target);
    }

    /**
     * Clones a graph such that {@code (g == clone(g)) == false} and 
     * {@code g.equals(clone(g)) == true}.
     * @param graph The graph to be cloned
     * @return The cloned graph
     */
    public static Graph clone(Graph graph) {
        Graph clone = new UndirectedGraph(graph.sizeVertices());
        for (Vertice vertice : graph.vertices()) {
            clone.setValue(vertice.getKey(), vertice.getValue());
        }
        for (Edge edge : graph.edges()) {
            clone.addEdge(edge.start().getKey(), edge.end().getKey());
        }

        return clone;
    }

    /**
     * Will calculate the search tree of a breadth first search
     * @param g The graph to calculate the search tree from
     * @param s The starting vertice, the root of the search tree
     * @return A bfs search tree
     */
    public static DirectedGraph bfsTree(Graph g, int s) {
        Objects.requireNonNull(g);

        boolean[] exploredNodes = new boolean[g.sizeVertices()];
        
        DirectedGraph tree = new DirectedGraph(g.sizeVertices());
        Vertice start = g.parseVertice(s);

        if (start == null) {
            throw new IllegalArgumentException("Start does not exist");
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
                    tree.addEdge(u.getKey(), v.getKey());
                    exploredNodes[v.getKey()] = true;
                }
            }
        }

        return tree;
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
     * </p>
     * Returns an empty list if {@code s == t} is true.
     * Returns null if no path was found.
     */
    public static List<Vertice> dijkstra(Graph g, int s, int t) {
        Objects.requireNonNull(g);
        
        List<Vertice> path = new LinkedList<>();
        Vertice[] parents = new Vertice[g.sizeVertices()];
        boolean[] exploredNodes = new boolean[g.sizeVertices()];

        Vertice start = g.parseVertice(s);
        Vertice target = g.parseVertice(t);

        if (start == null || target == null) {
            throw new IllegalArgumentException();
        }

        if (start.equals(target)) {
            return path;
        }

        double[] distances = infinityArray(g.sizeVertices());
        distances[s] = 0;

        BinaryHeap<Vertice> heap = new BinaryHeap<>();
        for (Vertice v : g.vertices()) {
            heap.push(v, (int) distances[v.getKey()]);
        }

        while(!heap.isEmpty()) {
            Vertice u = heap.pop();
            int j = u.getKey();
            exploredNodes[j] = true;
            List<Edge> uEdges = u.edges();
            for (Vertice v : u.neighbours()) {
                int i = v.getKey();
                Edge e = uEdges.stream().filter((Edge ed) -> (ed.start() == v || ed.end() == v)).findFirst().orElse(null);
                if (distances[i] > distances[j] + e.getWeight()) {
                    distances[i] = distances[j] + e.getWeight();
                    if (!exploredNodes[i]) {
                        setParent(parents, v, u);
                    }
                    heap.decPrio(v, (int) distances[i]);
                }
            }
        }
    
        return reconstructPath(parents, start, target);
    }

    private static List<Vertice> reconstructPath(Vertice[] parents, Vertice start, Vertice target) {
        List<Vertice> path = new LinkedList<>();

        if (parent(parents, target) == null) {
            throw new IllegalArgumentException();
        }

        Vertice parent = target;

        while (parent(parents, parent) != null) {
            path.add(parent);
            parent = parent(parents, parent);
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
     * @param g The graph to calculate the mst from, may not be null and must be non diretional
     * @return A copy of the original graph as a MST
     * @throws ClassCastException When input graph is a directed graph
     */
    public static Tree mst(Graph g) {
        Objects.requireNonNull(g);

        if (g instanceof DirectedWeightedGraph) {
            throw new ClassCastException();
        }

        Tree copy = new UndirectedWeightedTree(g.sizeVertices());

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

    private static double[] infinityArray(int size) {
        double[] array = new double[size];
        for (int i = 0; i < array.length; i++) {
            array[i] = Double.POSITIVE_INFINITY;
        }

        return array;
    }

    /**
     * Checks whether a given graph forms a single component.
     * </p>
     * A graph is considered coherent if all vertices can be reached from any vertice.
     * @param g The graph to be checked
     * @return True if the graph is coherent
     */
    public static boolean isCoherent(UndirectedGraph g) {
        boolean[] exploredNodes = new boolean[g.sizeVertices()];

        Vertice start = g.parseVertice(0);
        Queue<Vertice> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        exploredNodes[0] = true;

        // BFS
        while (!queue.isEmpty()) {
            Vertice u = queue.poll();
            for (Vertice v : u.neighbours()) {
                if (!exploredNodes[v.getKey()]) {
                    queue.add(v);
                    exploredNodes[v.getKey()] = true;
                }
            }
        }

        for (boolean explored : exploredNodes) {
            if(!explored) {
                return false;
            }
        }

        return true;
    }
}
