package graphics;

import algorithm.Algorithm;
import algorithm.Client;
import algorithm.Graph;
import algorithm.Taboo;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class GraphPanel extends JPanel {
    private final int RATIO = 8;

    private Graph graph;
    private Algorithm algorithm;

    private Color[] colors = {
            Color.BLUE,
            Color.RED,
            Color.BLACK,
            Color.PINK,
            Color.ORANGE,
            Color.YELLOW,
            Color.GRAY,
            Color.DARK_GRAY,
            Color.CYAN
    };
    private int colorCount;

    public void drawClients(Graph graph, Observable o) {
        this.graph = graph;
        this.algorithm = (Algorithm) o;

        repaint();
        revalidate();
    }

    private Color getRandomColor() {
        return colors[colorCount++ % colors.length];
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null) {
            //System.out.println("graph RETURNED");
            return;
        }
        colorCount = 0;

        try {
            algorithm.getRoutes().forEach(route -> {
                g.setColor(getRandomColor());

                Client lastClient = route.getRoute().get(0);

                for (int i = 1; i < route.getRoute().size(); i++) {
                    Client currentClient = route.getRoute().get(i);
                    g.drawLine(lastClient.getX() * RATIO, lastClient.getY() * RATIO, currentClient.getX() * RATIO, currentClient.getY() * RATIO);
                    lastClient = currentClient;
                }
            });
        }
        catch(Exception e) {

        }

        g.setColor(Color.BLUE);
        g.fillOval(graph.getWarehouse().getX() * RATIO - RATIO / 2, graph.getWarehouse().getY() * RATIO - RATIO / 2, RATIO, RATIO);

        g.setColor(Color.RED);
        graph.getClients().forEach(client -> {
            g.fillOval(client.getX() * RATIO - RATIO / 2, client.getY() * RATIO - RATIO / 2, RATIO, RATIO);
        });

    }
}
