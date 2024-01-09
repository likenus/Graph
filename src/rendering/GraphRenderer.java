package src.rendering;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.models.undirected.Mesh2D;

import java.awt.*;
import java.awt.image.BufferedImage;

public class GraphRenderer {
    private GraphRenderer() {
        throw new UnsupportedOperationException();
    }

    public static Image render(WaveFunctionCollapse wfc) {
        Mesh2D graph = (Mesh2D) wfc.getGraph();
        Ruleset ruleset = wfc.getRuleset();

        BufferedImage image = new BufferedImage(graph.getWidth(), graph.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = image.createGraphics();

        for (int y = 0; y < graph.getHeight(); y++) {
            for (int x = 0; x < graph.getWidth(); x++) {
                g.setColor(ruleset.getTileColor(graph.getValue(y * graph.getWidth() + x)));
                g.fillRect(x, y, 1, 1);
            }
        }

        g.dispose();
        return image;
    }
}
