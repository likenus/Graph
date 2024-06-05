package src.util.queues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class FibonacciHeap<T> implements PriorityQueue<T> {
    
    private Map<T, Node> contents = new HashMap<>();
    private Node root;
    private Node min;
    private int size;

    public FibonacciHeap() {
        size = 0;
        min = null;
        root = null;
    }

    @Override
    public T peek() {
        return min.value;
    }

    @Override
    public boolean push(T x, int prio) {
        Node node = new Node(x, prio);
        contents.put(x, node);
        node.left = node;
        node.right = node;

        mergeToRoot(node);

        if (min == null || node.prio < min.prio) {
            min = node;
        }

        size++;
        return true;
    }

    @Override
    public T popMin() {
        Node p = min;

        if (p == null) {
            return null;
        }

        if (p.hasChild()) {
            for (Node c : p.children()) {
                c.parent = null;
                cut(c, p);
            }
        }

        removeFromRoot(p);

        if (p.right.key == p.key) {
            min = null;
            root = null;
        } else {
            min = p.right;
            consolidate();
        }
        
        size--;
        contents.remove(p.value);
        return p.value;
    }

    @Override
    public void decPrio(T x, int prio) {
        Node node = contents.get(x);
        if (node == null) { return; }
        if (prio >= node.prio) {
            return;
        }
        node.prio = prio;
        Node parent = node.parent;
        if (parent != null && node.prio < parent.prio) {
            cut(node, parent);
            cascadingCut(parent);
        }
        if (node.prio < min.prio) {
            min = node;
        }
    }

    /**
     * Cuts the child off of the tree into the root list, the child will carry all its children
     * @param child The child to be cut from its parent
     * @param parent The parent
     */
    private void cut(Node child, Node parent) {
        unlinkChild(child, parent);
        parent.degree--;
        mergeToRoot(child);
        child.mark = false;
    }

    private void unlinkChild(Node child, Node parent) {
        if (child.right.key == child.key) { // Last child left
            parent.child = null;
        } else if (child.key == parent.child.key) { // If first child
            parent.child = child.right;
        }
        child.left.right = child.right;
        child.right.left = child.left;
        child.left = child;
        child.right = child;
        child.parent = null;
    }

    private void cascadingCut(Node node) {
        Node parent = node.parent;
        if (parent == null) {
            return;
        }
        if (!node.mark) {
            node.mark = true;
        } else {
            cut(node, parent);
            cascadingCut(parent);
        }
    }

    private void mergeToRoot(Node node) {
        node.left = node.right = node;
        if (root == null) {
            root = node;
        } else {
            node.right = root;
            node.left = root.left;
            root.left.right = node;
            root.left = node;
        }
    }

    private void removeFromRoot(Node node) {
        if (node.key == root.key) {
            root = node.getRight();
        }
        node.getLeft().setRight(node.getRight());
        node.getRight().setLeft(node.getLeft());
    }

    private void consolidate() {
        List<Node> arr = new ArrayList<>();
        for (int i = 0; i < (int) Math.log(size) * 2 + 1; i++) {
            arr.add(null);
        }
        List<Node> nodes = rootList();
        for (int i = 0; i < nodes.size(); i++) {
            Node x = nodes.get(i);
            int d = x.degree;
            while (arr.get(d) != null) {
                Node y = arr.get(d);
                if (x.prio > y.prio) {
                    x = arr.get(d);
                    y = nodes.get(i);
                }
                linkChild(y, x);
                arr.set(d, null);
                d++;
            }
            arr.set(d, x);
        }

        // for (Node v : rootList()) {
        //     if (v.prio < min.prio) {
        //         min = v;
        //     }
        // }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) != null && arr.get(i).prio < min.prio) {
                min = arr.get(i);
            }
        }
    }

    private void linkChild(Node child, Node parent) {
        removeFromRoot(child);
        child.left = child.right = child;
        mergeWithChildList(parent, child);
        parent.degree++;
        child.parent = parent;
        child.mark = false;
    }

    private void mergeWithChildList(Node parent, Node child) {
        if (parent.child == null) {
            parent.child = child;
            child.setLeft(child);
            child.setRight(child);
        } else {
            child.setRight(parent.child);
            child.setLeft(parent.child.getLeft());
            parent.child.getLeft().setRight(child);
            parent.child.setLeft(child);
        }
    }

    private List<Node> rootList() {
        Node v = root.right;
        List<Node> nodes = new ArrayList<>();
        nodes.add(root);
        while (v.key != root.key) {
            nodes.add(v);
            v = v.right;
        }
        return nodes;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size == 0;
    }

    @Override
    public Set<T> contents() {
        return Collections.unmodifiableSet(contents.keySet());
    }

    private class Node {
        private static int nextKey = 0;
        private final int key;
        private final T value;
        private long prio;
        private int degree;
        private boolean mark;
    
        private Node parent;
        private Node child;
        private Node left;
        private Node right;
    
        public Node(T value, int prio) {
            this.value = value;
            this.key = nextKey;
            this.prio = prio;
            degree = 0;
            nextKey++;
        }
    
        public List<Node> children() {
            Node curr = this.child.right;

            List<Node> nodes = new ArrayList<>();

            nodes.add(this.child);
            
            while(curr.key != this.child.key) {
                nodes.add(curr);
                curr = curr.right;
            }

            return nodes;
        }

        public Node getLeft() {
            return left;
        }

        public Node getRight() {
            return right;
        }

        public void setLeft(Node v) {
            left = v;
        }

        public void setRight(Node v) {
            right = v;
        }
    
        public boolean hasChild() {
            return child != null;
        }

        @Override
        public String toString() {
            return "Key=" + key + " Val=" + value.toString();
        }
    }
}
