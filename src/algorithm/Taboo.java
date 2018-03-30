package algorithm;

import java.util.List;
import java.util.Observer;
import java.util.Random;

public class Taboo extends Algorithm implements Runnable {

    private final int SLEEPING_TIME = 5;

    private Graph graph;

    private Route currentRoute;

    private int[] swappedClient;
    private Route swappedRoute;

    private Random randomGenerator;

    public Taboo(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;
        currentRoute = null;
        swappedClient = new int[2];

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
    }

    private void closeAndNewRoute() {
        if (currentRoute != null)
            currentRoute.addClient(graph.getWarehouse());
        currentRoute = new Route();
        currentRoute.addClient(graph.getWarehouse());
    }

    private float calculateDistance() {
        float distance = 0;

        for (Route r : routes)
            distance += r.distanceRoute();

        return distance;
    }

    @Override
    protected void step() {
        float lastDistance = calculateDistance();

        // Pick random route
        swappedRoute = routes.get(randomGenerator.nextInt(routes.size()));

        // Pick 2 random clients from route except the couple already switched
        // First and last index are the warehouse !! Don't swap with these !!
        swappedClient[0] = randomGenerator.nextInt(swappedRoute.getRoute().size() - 2) + 1;

        int random;
        do {
            random = randomGenerator.nextInt(swappedRoute.getRoute().size() - 2) + 1;
        }while(random == swappedClient[0]);
        swappedClient[1] = random;

        swappedRoute.swapClient(swappedClient[0], swappedClient[1]);

        // Swap back if not worth
        if (calculateDistance() > lastDistance)
            swappedRoute.swapClient(swappedClient[0], swappedClient[1]);
        //else
        //    System.out.println("Stepped : swap " + swappedClient[0] + " and " + swappedClient[1] + " from " + swappedRoute + ", distance=" + lastDistance);
        System.out.println("distance=" + calculateDistance());
    }

    @Override
    public void run() {
        initRoutes();
        while (true) {
            setChanged();
            notifyObservers();
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
