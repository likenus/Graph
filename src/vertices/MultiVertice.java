package src.vertices;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import src.edge.interfaces.Edge;

public class MultiVertice implements Vertice {

    protected final List<Edge> edges;

    protected int key;

    public MultiVertice(int key) {
        this.key = key;
        this.edges = new LinkedList<>();
    }

    @Override
    public List<Vertice> neighbours() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'neighbours'");
    }

    @Override
    public int getKey() {
        return this.key;
    }

    @Override
    public void connectEdge(Edge e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'connectEdge'");
    }

    @Override
    public void mark() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'mark'");
    }

    @Override
    public boolean isMarked() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'isMarked'");
    }

    @Override
    public void setParent(Vertice v) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setParent'");
    }

    @Override
    public Vertice getParent() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getParent'");
    }

    @Override
    public int degree() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'degree'");
    }

    @Override
    public void reset() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'reset'");
    }

    @Override
    public List<Edge> edges() {
        return Collections.unmodifiableList(this.edges);
    }

    @Override
    public void disconnectEdge(Edge e) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'disconnectEdge'");
    }

    @Override
    public int getValue() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getValue'");
    }

    @Override
    public void setValue(int i) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setValue'");
    }
    
}
