package algorithm;

import java.util.LinkedList;
import java.util.List;
import java.util.Observer;

public class Taboo extends Algorithm implements Runnable {

    private Graph graph;

    private Route currentRoute;

    private Client[] switchedClient;

    public Taboo(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;
        currentRoute = null;
        switchedClient = new Client[2];
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

    @Override
    protected void step() {
        // échanger 2 points d'un chemin ssi ces 2 points ne sont pas dans la


        /*switchedClient[0] = ;
        switchedClient[1] = ;*/
    }

    @Override
    public void run() {
        initRoutes();
        while(true) {
            setChanged();
            notifyObservers();
            //step();
        }
    }
}
