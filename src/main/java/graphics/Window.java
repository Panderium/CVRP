package graphics;

import algorithm.Graph;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;
import java.util.Observer;

public class Window extends JFrame implements Observer {

    private final static int WINDOW_WIDTH = 900, WINDOW_HEIGHT = 900;

    private Graph graph;

    private GraphPanel panel;

    public Window(String title, String filename) throws HeadlessException {
        super(title);

        panel = new GraphPanel();

        this.getRootPane().getContentPane().add(panel);

        this.setSize(WINDOW_WIDTH, WINDOW_HEIGHT);

        this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        this.setVisible(true);

        graph = new Graph(filename, this);

    }


    public void drawGraph(Observable o) {
        panel.drawClients(graph, o);
    }

    @Override
    public void update(Observable o, Object arg) {
        drawGraph(o);
    }
}
