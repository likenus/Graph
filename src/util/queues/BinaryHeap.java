package src.util.queues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class BinaryHeap<T> implements PriorityQueue<T>, Iterable<T> {

    private final List<HeapNode> content = new ArrayList<>();
    private final Map<T, Integer> index = new HashMap<>(); // This cant be changed due to decPrio

    @Override
    public boolean push(T x, int priority) {
        content.add(new HeapNode(x, priority));
        index.put(x, size() - 1);
        bubbleUp(size() - 1);
		return true;
    }

    @Override
    public T popMin() {
        T tmp = content.get(0).getValue();

        swap(0, size() - 1);
        content.remove(size() - 1);
        index.remove(tmp);
        sinkDown(0);

        return tmp;
    }

    @Override
    public boolean isEmpty() {
        return content.isEmpty();
    }

    @Override
    public T peek() {
        return content.get(0).getValue();
    }

    @Override
    public Set<T> contents() {
        return Collections.unmodifiableSet(index.keySet());
    }

    @Override
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

    @Override
    public void decPrio(T v, int prio) {
        Integer i = index.get(v);
        if (i == null) {
            return;
        }
        HeapNode n = content.get(i);

        long oldPrio = n.getPrio();
        n.setPrio(prio);

        if (prio < oldPrio) {
            bubbleUp(i);
        } else {
            sinkDown(i);
        }
    }

    private class HeapNode {

        private final T value;
        private long priority;

        public HeapNode(T value, int priority) {
            this.value = value;
            this.priority = priority;
        }

        public T getValue() {
            return value;
        }

        public long getPrio() {
            return priority;
        }

        public void setPrio(int p) {
            this.priority = p;
        }
    }

    @Override
    public Iterator<T> iterator() {
        return contents().iterator();
    }

    public boolean contains(Object o) {
        return contents().contains(o);
    }

    public void clear() {
        this.content.clear();
        this.index.clear();
    }
}
