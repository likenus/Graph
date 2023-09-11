package src.util;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

public record Tuple<T>(T a, T b) implements Iterable<T> {

    @Override
    public Iterator<T> iterator() {
        List<T> list = new LinkedList<>();
        list.add(a);
        list.add(b);

        return list.iterator();
    }

    public boolean contains(Object o) {
        return a.equals(o) || b.equals(o);
    } 

    public Tuple<T> flip() {
        return new Tuple<>(b, a);
    }
}
