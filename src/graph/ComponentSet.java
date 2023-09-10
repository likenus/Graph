package src.graph;

import java.util.LinkedList;
import java.util.List;

import src.graph.interfaces.Tree;
import src.graph.interfaces.UnionFind;
import src.vertices.Node;

public class ComponentSet implements UnionFind {

    private static final int BASE_CAPACITY = 16;

    private final Tree components;

    private UnionFindNode[] nodes;
    private int id = 0;

    /**
     * Creates a new empty Component Set with a base capacity of 16.
     */
    public ComponentSet() {
        this.components = new DirectedTree();
        this.nodes = new UnionFindNode[BASE_CAPACITY];
    }

    /**
     * Creates a new Set of Components containing i components labeled 0 to i - 1.
     */
    public ComponentSet(int i) {
        this.id  = i;
        this.components = new DirectedTree();
        this.nodes = new UnionFindNode[i];

        for (int j = 0; j < i; j++) {
            UnionFindNode node = new UnionFindNode(j);
            node.setParent(node);
            this.components.add(node);
            this.nodes[j] = node;
        }
    }

    @Override
    public boolean add() {
        if (nodes.length == id) {
            UnionFindNode[] tmp = new UnionFindNode[nodes.length * 2];
            for (int i = 0; i < nodes.length; i++) {
                tmp[i] = nodes[i];
            }
        }
        UnionFindNode node = new UnionFindNode(id);
        node.setParent(node);
        this.nodes[id] = node;
        boolean b = components.add(node);

        id++;
        return b;
    }
    
    @Override
    public int find(int key) {
        UnionFindNode v = nodes[key];
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
        UnionFindNode v = nodes[a];
        UnionFindNode w = nodes[b];

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
