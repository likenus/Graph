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

    public T peek() {
        return min.value;
    }

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

        if (z.right.equals(z)) {
            min = null;
            root = null;
        } else {
            min = z.right;
            consolidate();
        }
        
        size--;
        return z.value;
    }

    public void decPrio(T x, int prio) {
		Node node = contents.get(x);
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
        if (c.right.equals(c)) {
            p.child = null;
        } else {
            c.left.right = c.right;
            c.right.left = c.left;
        }
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
        if (node.equals(root)) {
            root = node.right;
        }
        node.left.right = node.right;
        node.right.left = node.left;
    }

    private void consolidate() {
        @SuppressWarnings("unchecked")
		Node[] arr = new FibonacciHeap.Node[(int) Math.log(size) * 2];
        List<Node> nodes = rootList();
        for (int i = 0; i < nodes.size(); i++) {
            Node x = nodes.get(i);
            int d = x.degree;
            while (arr[d] != null) {
                Node y = arr[d];
                if (x.prio > y.prio) {
                    Node tmp = x;
                    x = y;
                    y = tmp;
                }
                this.relink(y, x);
                arr[d] = null;
                d++;
            }
            arr[d] = x;
        }
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] != null && arr[i].prio < min.prio) {
                min = arr[i];
            }
        }
    }

    private void relink(Node p, Node c) {
        if (p.hasChild()) {
            Node child = p.child;
            c.left = child;
            c.right = child.right;
        } else {
            p.child = c;
            c.left = c.right = c;
        }
        c.parent = p;
    }

    private List<Node> rootList() {
        Node c = this.root;
        List<Node> nodes = new ArrayList<>();
        while (!c.right.equals(root)) {
            nodes.add(c);
            c = c.right;
        }
        return nodes;
    }

	public int size() {
		return size;
	}

	public boolean isEmpty() {
		return size == 0;
	}

	public Set<T> contents() {
		return Collections.unmodifiableSet(contents.keySet());
	}

	private class Node {
		private T value;
		private int prio;
		private int degree;
		private boolean mark;
	
		private Node parent;
		private Node child;
		private Node left;
		private Node right;
	
		public Node(T value, int key) {
			this.value = value;
			this.prio = key;
			degree = 0;
		}
	
		public Iterator<Node> children() {
			Node z = this.child;
			return new Iterator<Node>() {
				
				Node start = z;
				Node curr = z;
	
				@Override
				public boolean hasNext() {
					return !curr.right.equals(start);
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
	
		public int getPrio() {
			return prio;
		}
	}
}
