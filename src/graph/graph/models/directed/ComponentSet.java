package src.graph.graph.models.directed;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import src.graph.graph.interfaces.UnionFind;
import src.graph.vertices.models.Node;

public class ComponentSet implements UnionFind {

    private final List<UnionFindNode> nodes;
    private int id = 0;

    /**
     * Creates a new empty Component Set.
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
        if (v.getParent().equals(v)) {
            return v;
        }

        UnionFindNode p = find(v.getParent());
        v.setParent(p);
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
        private UnionFindNode parent;

        public UnionFindNode(int key) {
            super(key);
            this.rank = 1;
            this.parent = this;
        }

        public UnionFindNode getParent() {
            return parent;
        }

        public void setParent(UnionFindNode node) {
            parent = node;
        }

        public int getRank() {
            return this.rank;
        }

        public void setRank(int i) {
            this.rank = i;
        }

        @Override
        public boolean equals(Object o) {
            if (o == null) {
                return false;
            }

            if (o == this) {
                return true;
            }

            if (o.getClass() != this.getClass()) {
                return false;
            }

            UnionFindNode other = (UnionFindNode) o;

            return other.key == this.key && other.value == this.value && other.rank == this.rank;
        }

        @Override
        public int hashCode() {
            return Objects.hash(key, value, rank);
        }
    }
}
