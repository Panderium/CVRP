package graphics;

import algorithm.Client;
import algorithm.Graph;
import algorithm.Taboo;

import javax.swing.*;
import java.awt.*;
import java.util.Observable;

public class GraphPanel extends JPanel {
    private final int RATIO = 9;

    private Graph graph;
    private Taboo taboo;

    public void drawClients(Graph graph, Observable o) {
        this.graph = graph;
        this.taboo = (Taboo)o;
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        if (graph == null) {
            System.out.println("graph RETURNED");
            return;
        }

        taboo.getRoutes().forEach(route -> {
            g.setColor(Color.GREEN);

            Client lastClient = route.getRoute().get(0);

            for (int i = 1; i < route.getRoute().size(); i++) {
                Client currentClient = route.getRoute().get(i);
                g.drawLine(lastClient.getX() * RATIO, lastClient.getY() * RATIO, currentClient.getX() * RATIO, currentClient.getY() * RATIO);
                lastClient = currentClient;
            }
        });

        g.setColor(Color.BLUE);
        g.drawRect(graph.getWarehouse().getX() * RATIO, graph.getWarehouse().getY() * RATIO, 1, 1);
        g.drawRect(graph.getWarehouse().getX() * RATIO, graph.getWarehouse().getY() * RATIO, 2, 2);
        g.drawRect(graph.getWarehouse().getX() * RATIO, graph.getWarehouse().getY() * RATIO, 3, 3);

        g.setColor(Color.RED);
        graph.getClients().forEach(client -> {
            g.drawRect(client.getX() * RATIO, client.getY() * RATIO, 1, 1);
            g.drawRect(client.getX() * RATIO, client.getY() * RATIO, 2, 2);
            g.drawRect(client.getX() * RATIO, client.getY() * RATIO, 3, 3);
        });

    }
}
