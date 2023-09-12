package src.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class BinaryHeap<T> {

    private final List<HeapNode> content = new ArrayList<>();
    private final Map<T, Integer> index = new HashMap<>();

    /**
     * Adds an element to the heap.
     * </p>
     * This runs in log(n) time.
     * @param x The element to be added
     * @param priority The priority of the element
     */
    public void push(T x, int priority) {
        content.add(new HeapNode(x, priority));
        index.put(x, size() - 1);
        bubbleUp(size() - 1);
    }

    /**
     * Removes and retrieves the head of the heap.
     * </p>
     * This runs in log(n) time.
     * @return The elements with the smalles priority from this queue.
     */
    public T pop() {
        T tmp = content.get(0).getValue();

        swap(0, size() - 1);
        content.remove(size() - 1);
        index.remove(tmp);
        sinkDown(0);

        return tmp;
    }

    /**
     * Returns whether the heap is empty.
     * @return True if heap is empty
     */
    public boolean isEmpty() {
        return content.isEmpty();
    }

    /**
     * Returns the head of the queue without removing it. This will not affect
     * the internal order of the heap.
     * @return The element with the lowest priority
     */
    public T peek() {
        return content.get(0).getValue();
    }

    /**
     * Returns the current amount of elements inside the heap.
     * @return the size of the heap
     */
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
        index.replace(content.get(v).getValue(), w);
        index.replace(content.get(w).getValue(), v);
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

    /**
     * Decreases the priority of the given element in the queue.
     * </p>
     * This runs in expected log(n) time.
     * @param v The element to reduce the priority of.
     * @param prio The priority
     */
    public void decPrio(T v, int prio) {
        Integer i = index.get(v);
        if (i == null) {
            return;
        }
        HeapNode n = content.get(i);

        int oldPrio = n.getPrio();
        n.setPrio(prio);

        if (prio < oldPrio) {
            bubbleUp(i);
        } else {
            sinkDown(i);
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
