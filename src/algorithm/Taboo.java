package algorithm;

import java.util.List;
import java.util.Observer;
import java.util.Random;

public class Taboo extends Algorithm implements Runnable {

    private final int SLEEPING_TIME = 5;

    private Graph graph;

    private Route currentRoute;

    private Random randomGenerator;

    public Taboo(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;
        currentRoute = null;

        randomGenerator = new Random();
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
        setChanged();
        notifyObservers();
    }

    private void closeAndNewRoute() {
        if (currentRoute != null)
            currentRoute.addClient(graph.getWarehouse());
        currentRoute = new Route();
        currentRoute.addClient(graph.getWarehouse());
    }

    public float calculateDistance() {
        float distance = 0;

        for (Route r : routes)
            distance += r.distanceRoute();

        return distance;
    }

    private void swapClient(Route[] route, Client[] client) {
        float lastDistance = calculateDistance();

        route[0].setClient(route[0].getRoute().indexOf(client[0]), client[1]);
        route[1].setClient(route[1].getRoute().indexOf(client[1]), client[0]);

        // Test if routes created are correct
        if (route[0].getCapacityLeft() < 0 || route[1].getCapacityLeft() < 0 || calculateDistance() > lastDistance) {
            // SWAP BACK BECAUSE IMPOSSIBLE MOVE !!
            route[0].setClient(route[0].getRoute().indexOf(client[1]), client[0]);
            route[1].setClient(route[1].getRoute().indexOf(client[0]), client[1]);
            //System.err.println("Run back because impossible route or not worth it");
        }
        else{
            setChanged();
            notifyObservers();
            System.out.println("distance=" + calculateDistance());
        }
    }

    @Override
    protected void step() {
        // Pick 2 random routes
        Route randomRoute[] = new Route[2];

        randomRoute[0] = routes.get(randomGenerator.nextInt(routes.size() - 1));
        randomRoute[1] = routes.get(randomGenerator.nextInt(routes.size() - 1));


        // Pick random client from each of these routes
        // First and last index are the warehouse !! Don't swap with these !!
        Client randomClient[] = new Client[2];
        randomClient[0] = randomRoute[0].getRoute().get(randomGenerator.nextInt(randomRoute[0].getRoute().size() - 2) + 1);
        do {
            randomClient[1] = randomRoute[1].getRoute().get(randomGenerator.nextInt(randomRoute[1].getRoute().size() - 2) + 1);
        } while (randomClient[0] == randomClient[1]);

        //TODO ajouter la possibilité de simplement transférer un client dans un autre chemin (à une place random)
        swapClient(randomRoute, randomClient);
    }

    @Override
    public void run() {
        initRoutes();

        while (true) {
            try {
                Thread.sleep(SLEEPING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            // Step taboo
            step();
        }
    }
}
