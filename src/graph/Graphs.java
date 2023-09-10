package src.graph;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

import src.graph.interfaces.Graph;
import src.vertices.Vertice;

public final class Graphs {
    
    private Graphs() {
        throw new UnsupportedOperationException();
    }

    /**
     * Uses Breadth First Search to calculate the shortest Path. 
     * Note: The shortest Path is considered the path with the least amount of edges and
     * does not account for weighted Graphs.
     * @param g The Graph to execute the BFS on.
     * @param s The key of the start node.
     * @param t The key of the target node.
     * @return The Path from s to t including s and t. Returns an empty list if {@code s.equals(t)} is true.
     * Returns null if no path was found.
     */
    public static List<Vertice> breadthFirstSearch(Graph g, int s, int t) {
        List<Vertice> path = new LinkedList<>();

        Vertice start = g.parseVertice(s);
        Vertice target = g.parseVertice(t);

        if (start.equals(target)) {
            return path;
        }
        
        Queue<Vertice> queue = new ConcurrentLinkedQueue<>();
        queue.add(start);
        start.mark();

        while (!queue.isEmpty()) {
            Vertice u = queue.poll();
            for (Vertice v : u.neighbours()) {
                if (!v.isMarked()) {
                    queue.add(v);
                    v.mark();
                    v.setParent(u);
                }
            }
        }

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
}
