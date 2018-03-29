package algorithm;

import java.util.List;
import java.util.Observer;

public class Taboo extends Algorithm implements Runnable {

    private final int SLEEPING_TIME = 500;

    private Graph graph;

    private Route currentRoute;

    public Taboo(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;
        currentRoute = null;
    }

    private void initRoutes() {
        List<Client> clients = graph.getClients();

        closeAndNewRoute();

        for (Client c : clients) {
            if (c != graph.getWarehouse()) {
                if (c.getQuantity() > currentRoute.getCapacityLeft()) {
                    routes.add(currentRoute);
                    closeAndNewRoute();
                }
                currentRoute.addClient(c);
            }
        }
        routes.add(currentRoute);
        closeAndNewRoute();
    }

    private void closeAndNewRoute() {
        if(currentRoute != null)
            currentRoute.addClient(graph.getWarehouse());
        currentRoute = new Route();
        currentRoute.addClient(graph.getWarehouse());
    }

    private void step() {

    }


    @Override
    public void run() {
        initRoutes();
        while(true) {
            // Step taboo


            setChanged();
            notifyObservers();
            try {
                Thread.sleep(SLEEPING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
