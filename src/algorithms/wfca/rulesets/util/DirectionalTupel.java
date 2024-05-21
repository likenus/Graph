package src.algorithms.wfca.rulesets.util;

import java.util.Collection;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class DirectionalTupel<T> {

    private final Set<T> up = new HashSet<>();
    private final Set<T> down = new HashSet<>();
    private final Set<T> left = new HashSet<>();
    private final Set<T> right = new HashSet<>();

    public void add(Direction direction, T e) {
        get(direction).add(e);
    }

    public void addAll(Direction direction, Collection<? extends T> c) {
        get(direction).addAll(c);
    }

    public Set<T> get(Direction direction) {
        return switch (direction) {
            case UP -> up;
            case DOWN -> down;
            case LEFT -> left;
            case RIGHT -> right;
        };
    }

    @Override
    public boolean equals(Object o) {
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof DirectionalTupel)) {
            return false;
        }
        DirectionalTupel<?> other = (DirectionalTupel<?>) o;
        return other.up.equals(this.up) && other.down.equals(this.down)
                && other.left.equals(this.left) && other.right.equals(this.right);
    }

    @Override
    public int hashCode() {
        return Objects.hash(up, down, left, right);
    }
}