package src.edge.interfaces;

import src.Tuple;
import src.vertices.Vertice;

public interface Edge { 
    
    Vertice getOther(Vertice v);

    Tuple<Vertice> getVertices();

    Vertice first();

    Vertice second();
}
