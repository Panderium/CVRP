package graphics;

import algorithm.Graph;

import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    private final static int WINDOW_WIDTH = 640, WINDOW_HEIGHT = 640, PADDING = 5;

    private Graph graph;

    private GraphPanel panel;

    private int minSize, maxSize;

    public Window(String title, String filename) throws HeadlessException {
        super(title);

        graph = new Graph(filename);

        panel = new GraphPanel();

        this.getRootPane().getContentPane().add(panel);

        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        // Get the minimal and maximal value for flexible points drawing
        minSize = graph.getClients().stream().min();
        maxSize = graph.getClients().stream().max();

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);
    }


    public void drawClients() {
        panel.drawClients(graph);
    }

}
