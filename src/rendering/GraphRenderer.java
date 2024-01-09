package src.rendering;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.models.undirected.Mesh2D;
import src.graph.vertices.Vertex;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphRenderer {
    private BufferedImage image;
    private WaveFunctionCollapse wfc;

    public GraphRenderer(WaveFunctionCollapse wfc) {
        Mesh2D graph = (Mesh2D) wfc.getGraph();
        image = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        this.wfc = wfc;
    }

    public void renderDiff() {
        Mesh2D graph = (Mesh2D) wfc.getGraph();
        Ruleset ruleset = wfc.getRuleset();

        BufferedImage newTiles = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = newTiles.createGraphics();
        Vertex[] updatedTiles = wfc.getUpdatedSinceRender();
        for (Vertex v: updatedTiles) {
            int vertexX = v.getKey() % graph.getWidth();
            int vertexY = v.getKey() / graph.getWidth();

            g.setColor(ruleset.getTileColor(v.getValue()));
            g.fillRect(vertexX, vertexY, 1, 1);
        }
        g.dispose();

        Graphics2D merger = image.createGraphics();
        merger.drawImage(newTiles, 0, 0, null);
        merger.dispose();
    }

    public void renderFull() {
        Mesh2D graph = (Mesh2D) wfc.getGraph();
        Ruleset ruleset = wfc.getRuleset();

        image = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                g.setColor(ruleset.getTileColor(graph.getValue(y * graph.getWidth() + x)));
                g.fillRect(x, y, 1, 1);
            }
        }

        g.dispose();
    }

    public Image getImage() {
        return image;
    }
}
