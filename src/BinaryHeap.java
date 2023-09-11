package src;

import java.util.ArrayList;
import java.util.List;


public class BinaryHeap<T> {

    private final List<HeapNode> content = new ArrayList<>();

    public void push(T x, int priority) {
        content.add(new HeapNode(x, priority));
        bubbleUp(size() - 1);
    }

    public T pop() {
        T tmp = content.get(0).getValue();

        swap(0, size() - 1);
        content.remove(size() - 1);
        sinkDown(0);

        return tmp;
    }

    public boolean isEmpty() {
        return content.isEmpty();
    }

    public T peek() {
        return content.get(0).getValue();
    }

    public int size() {
        return content.size();
    }

    private int leftChild(int v) {
        return 2 * v + 1;
    }

    private int rightChild(int v) {
        return 2 * v + 2;
    }

    private int parent(int v) {
        return (v - 1) / 2;
    }

    private void swap(int v, int w) {
        HeapNode tmp = content.get(v);
        content.set(v, content.get(w));
        content.set(w, tmp);
    }

    private void bubbleUp(int v) {
        if (v != 0 && content.get(v).getPrio() < content.get(parent(v)).getPrio()) {
            swap(v, parent(v));
            bubbleUp(parent(v));
        }
    }

    private void sinkDown(int v) {
        int uL = leftChild(v);
        int uR = rightChild(v);
        int u = v;

        if (uL < size() && content.get(uL).getPrio() < content.get(u).getPrio()) {
            u = uL;
        }
        if (uR < size() && content.get(uR).getPrio() < content.get(u).getPrio()) {
            u = uR;
        }

        if (u != v) {
            swap(u, v);
            sinkDown(u);
        }
    }

    public void decPrio(T v, int prio) {
        HeapNode n = null;
        int index = -1;
        for (HeapNode node : content) {
            if (node.getValue().equals(v)) {
                n = node;
                index = content.indexOf(node);
            }
        }

        if (n == null) {
            return;
        }

        int oldPrio = n.getPrio();
        n.setPrio(prio);

        if (prio < oldPrio) {
            bubbleUp(index);
        } else {
            sinkDown(index);
        }
    }

    private class HeapNode {

        private final T value;
        private int priority;

        public HeapNode(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        public T getValue() {
            return value;
        }

        public int getPrio() {
            return priority;
        }

        public void setPrio(int p) {
            this.priority = p;
        }
    }
}
