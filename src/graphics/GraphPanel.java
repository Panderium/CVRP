package graphics;

import algorithm.Graph;

import javax.swing.*;
import java.awt.*;

public class GraphPanel extends JPanel {

    private Graph graph;

    public void drawClients(Graph graph) {
        this.graph = graph;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if(graph == null)
            return;

        graph.getClients().forEach(client -> {
            g.setColor(Color.RED);
            g.drawLine(client.getX(), client.getY(), client.getX(), client.getY());
        });
    }
}
