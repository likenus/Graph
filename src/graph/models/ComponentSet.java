package src.graph.models;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import src.graph.interfaces.UnionFind;
import src.vertices.models.Node;

public class ComponentSet implements UnionFind {

    private final List<UnionFindNode> nodes;
    private int id = 0;

    /**
     * Creates a new empty Component Set with a base capacity of 16.
     */
    public ComponentSet() {
        this.nodes = new ArrayList<>();
    }

    /**
     * Creates a new Set of Components containing i components labeled 0 to i - 1.
     */
    public ComponentSet(int i) {
        this.id  = i;
        this.nodes = new ArrayList<>();

        for (int j = 0; j < i; j++) {
            UnionFindNode node = new UnionFindNode(j);
            node.setParent(node);
            this.nodes.add(j, node);
        }
    }

    @Override
    public boolean add() {
        UnionFindNode node = new UnionFindNode(id);
        node.setParent(node);
        this.nodes.add(id, node);

        id++;
        return true;
    }

    public int size() {
        return nodes.size();
    }
    
    @Override
    public int find(int key) {
        UnionFindNode v = nodes.get(key);
        return find(v).getKey();
    }

    private UnionFindNode find(UnionFindNode v) {
        UnionFindNode p = v;
        List<UnionFindNode> pathToRoot = new LinkedList<>();

        while (!p.getParent().equals(p)) {
            pathToRoot.add(p);
            p = (UnionFindNode) p.getParent();
        }

        // Path compression
        for (UnionFindNode w : pathToRoot) {
            w.setParent(p);
        }

        return p;
    }

    @Override
    public void union(int a, int b) {
        UnionFindNode v = nodes.get(a);
        UnionFindNode w = nodes.get(b);

        union(v, w);
    }

    private void union(UnionFindNode v, UnionFindNode w) {
        UnionFindNode r = find(v);
        UnionFindNode s = find(w);

        if (r.getRank() < s.getRank()) {
            r.setParent(s);
            s.setRank(r.getRank() + 1);
        } else {
            s.setParent(r);
            r.setRank(s.getRank() + 1);
        }
    }

    private class UnionFindNode extends Node {

        private int rank;

        public UnionFindNode(int key) {
            super(key);
            this.rank = 1;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int i) {
            this.rank = i;
        }
    }
}
