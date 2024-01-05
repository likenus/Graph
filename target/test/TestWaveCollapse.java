package target.test;

import org.junit.Assert;
import org.junit.Test;

import src.algorithms.wfca.WaveFunctionCollapse;
import src.algorithms.wfca.rulesets.LandscapeRuleset;
import src.algorithms.wfca.rulesets.Ruleset;
import src.graph.graph.interfaces.Graph;
import src.graph.graph.models.undirected.Mesh2D;
import target.GraphLoader;

public class TestWaveCollapse {

    private static final long SEED = 314159265358979L;
    private static final String EXPECTED = """
        2 2 1 1 2 2 2 2 1 1
        2 2 1 1 1 1 1 1 2 2
        2 1 1 1 1 2 1 1 1 1
        1 2 2 1 1 2 2 1 1 1
        2 2 1 2 2 3 2 1 1 2
        1 1 1 1 2 3 3 2 2 1
        1 1 1 2 2 3 2 1 2 1
        1 2 2 2 1 2 2 2 2 2
        2 2 2 1 2 2 2 2 1 2
        3 2 1 1 1 2 1 1 2 3
        """;
    
    @Test
    public void testAlgorithm() {
        Ruleset ruleset = new LandscapeRuleset();
        GraphLoader graphLoader = new GraphLoader();
        Graph graph = graphLoader.zylinder(10);

        WaveFunctionCollapse wca = new WaveFunctionCollapse(graph, ruleset, SEED);

        wca.run();

        Assert.assertEquals(EXPECTED, printGraph(wca));
    }

    public static String printGraph(WaveFunctionCollapse wca) {

        Mesh2D mesh = (Mesh2D) wca.getGraph();

        int width = mesh.getWidth();
        int height = mesh.getHeight();

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                int x = mesh.getValue(width * i + j);

                if (j < width - 1) {
                    sb.append(x + " ");
                } else {
                    sb.append(x);
                }
            }
            sb.append(System.lineSeparator());
            
        }

        return sb.toString();
    }
}
