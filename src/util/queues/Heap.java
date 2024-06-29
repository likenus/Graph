package src.util.queues;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class Heap<T> implements PriorityQueue<T> {
    
    private final Map<T, Node> contents = new HashMap<>();
    private int size;
    private Node min;
    private Node root;

    public Heap() {
        min = root = null;
        size = 0;
    }

    public T peek() {
        return min.value;
    }

    public boolean push(T x, int prio) {
        Node node = new Node(x, prio);
        contents.put(x, node);
        node.left = node;
        node.right = node;

        mergeWithRootList(node);

        if (min == null || node.prio < min.prio) {
            min = node;
        }

        size++;
        return true;
    }

    private void mergeWithRootList(Node node) {
        if (root == null) {
            root = node;
            return;
        }
        node.right = root;
        node.left = root.left;
        root.left.right = node;
        root.left = node;
    }

    public T popMin() {
        Node z = min;

        if (z == null) {
            return null;
        }

        if (z.child != null) {
            List<Node> children = iterate(z.child);
            for (Node c : children) {
                mergeWithRootList(c);
                c.parent = null;
            }
        }

        removeFromRootList(z);

        if (z == z.right) {
            min = null;
            root = null;
        } else {
            min = z.right;
            consolidate();
        }

        contents.remove(z.value);
        size--;
        return z.value;
    }

    private List<Node> iterate(Node node) {
        List<Node> list = new ArrayList<>();
        Node curr = node;
        do {
            list.add(curr);
            curr = curr.right;
        } while (curr != node);

        return list;
    }

    private void consolidate() {
        List<Node> arr = new ArrayList<>();
        for (int i = 0; i < Math.log(size) * 2 + 1; i++) {
            arr.add(null);
        }

        List<Node> nodes = iterate(root);

        for (int i = 0; i < arr.size(); i++) {
            Node x = nodes.get(i);
            int d = x.degree;
            while (arr.get(d) != null) {
                Node y = arr.get(d);
                if (x.prio > y.prio) {
                    y = nodes.get(i);
                    x = arr.get(d);
                }
                link(y, x);
                arr.set(d, null);
                d++;
            }
            arr.set(d, x);
        }

        for (Node v : arr) {
            if (v != null && v.prio < min.prio) {
                min = v;
            }
        }
    }

    private void link(Node y, Node x) {
        removeFromRootList(y);
        y.left = y.right = y;
        mergeWithChildList(x, y);
        x.degree++;
        y.parent = x;
        y.mark = false;
    }

    private void mergeWithChildList(Node parent, Node node) {
        if (parent.child == null) {
            parent.child = node;
            return;
        }
        node.left = parent.child.left;
        node.right = parent.child;
        parent.child.left.right = node;
        parent.child.left = node;
    }

    private void removeFromRootList(Node node) {
        if (node == root) {
            root = node.right;
        }
        node.left.right = node.right;
        node.right.left = node.left;
    }

    public void decPrio(T x, int prio) {
        Node node = contents.get(x);

        if (node == null || prio > node.prio) {
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

    private void cut(Node x, Node y) {
        removeFromChildList(y, x);
        y.degree--;
        mergeWithRootList(x);
        x.parent = null;
        x.mark = false;
    }
    
    private void cascadingCut(Node y) {
        Node z = y.parent;
        if (z == null) {
            return;
        }
        if (!y.mark) {
            y.mark = true;
        } else {
            cut(y, z);
            cascadingCut(z);
        }
    }

    @Override
    public boolean isEmpty() {
        return min == null;
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public Set<T> contents() {
        return contents.keySet();
    }

    private void removeFromChildList(Node parent, Node node) {
        if (parent.child == parent.child.right) {
            parent.child = null;
        } else if (parent.child == node) {
            parent.child = node.right;
        }
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private class Node {

        private T value;
        private long prio;
        private int degree;
        private boolean mark;

        private Node parent;
        private Node child;
        private Node left;
        private Node right;

        public Node(T value, long prio) {
            this.value = value;
            this.prio = prio;
            degree = 0;
            mark = false;
        }
    }

}
