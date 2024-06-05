package src.util.queues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
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
        Node z = min;

        if (z == null) {
            return null;
        }

        if (z.hasChild()) {
            Iterator<Node> children = z.children();
            while (children.hasNext()) {
                Node c = children.next();
                c.parent = null;
                mergeToRoot(c);
            }
        }

        removeFromRoot(z);

        if (z.right == z) {
            min = null;
            root = null;
        } else {
            min = z.right;
            consolidate();
        }
        
        size--;
        contents.remove(z.value);
        return z.value;
    }

    @Override
    public void decPrio(T x, int prio) {
        Node node = contents.get(x);
        if (node == null) { return; }
        if (prio >= node.prio) {
            return;
        }
        node.prio = prio;
        Node p = node.parent;
        if (p != null && node.prio < p.prio) {
            cut(node, p);
            cascadingCut(p);
        }
        if (node.prio < min.prio) {
            min = node;
        }
    }

    private void cut(Node x, Node y) {
        unlinkChild(x, y);
        y.degree--;
        mergeToRoot(x);
        x.parent = null;
        x.mark = false;
    }

    private void unlinkChild(Node c, Node p) {
        if (c.right == c) {
            p.child = null;
        } else if (c.key == p.child.key) {
            p.child = c.right;
        }
        c.left.right = c.right;
        c.right.left = c.left;

    }

    private void cascadingCut(Node node) {
        Node p = node.parent;
        if (p != null) {
            if (!node.mark) {
                node.mark = true;
            } else {
                cut(node, p);
                cascadingCut(p);
            }
        }
    }

    private void mergeToRoot(Node node) {
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
        if (node == root) {
            root = node.right;
        }
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void consolidate() {
        List<Node> arr = new ArrayList<>();
        for (int i = 0; i < (int) Math.log(size) * 3; i++) {
            arr.add(null);
        }
        List<Node> nodes = rootList();
        for (int i = 0; i < nodes.size(); i++) {
            Node x = nodes.get(i);
            int d = x.degree;
            while (arr.get(d) != null) {
                Node y = arr.get(d);
                if (x.prio > y.prio) {
                    Node tmp = x;
                    x = y;
                    y = tmp;
                }
                this.relink(y, x);
                arr.set(d, null);
                d++;
            }
            arr.set(d, x);
        }
        for (int i = 0; i < arr.size(); i++) {
            if (arr.get(i) != null && arr.get(i).prio < min.prio) {
                min = arr.get(i);
            }
        }
    }

    private void relink(Node p, Node c) {
        removeFromRoot(p);
        p.left = p.right = p;
        mergeWithChildList(c, p);
        c.degree++;
        p.parent = c;
        p.mark = false;
    }

    private void mergeWithChildList(Node p, Node c) {
        if (p.child == null) {
            p.child = c;
        } else {
            c.right = p.child.right;
            c.left = p.child;
            p.child.right.left = c;
            p.child.right = c;
        }
    }

    private List<Node> rootList() {
        Node c = root.right;
        List<Node> nodes = new ArrayList<>();
        nodes.add(root);
        while (c.key != root.key) {
            nodes.add(c);
            c = c.right;
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
    
        public Iterator<Node> children() {
            Node z = this.child;
            return new Iterator<Node>() {
                
                Node start = z;
                Node curr = z;
    
                @Override
                public boolean hasNext() {
                    return curr.right != start;
                }
    
                @Override
                public Node next() {
                    if (!hasNext()) {
                        throw new NoSuchElementException();
                    }
                    Node tmp = curr;
                    curr = curr.right;
                    return tmp;
                }
            };
        }
    
        public boolean hasChild() {
            return child != null;
        }
    }
}
