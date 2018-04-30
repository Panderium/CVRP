package algorithm;

import java.util.Comparator;
import java.util.List;
import java.util.Observer;
import java.util.Random;

public class AlgoGen extends Algorithm implements Runnable {

    private int SLEEPING_TIME = 500;
    private double PROB_CROSS = 0.5;

    private Graph graph;
    private Route currentRoute;

    private Random random;

    public AlgoGen(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;

        random = new Random();
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

    @Override
    protected void step() {
        Route best = reproductionWheel();
        for (Route r : routes) {
            if (!r.equals(best)) {
                if (random.nextDouble() <= PROB_CROSS) {
                    crossover(best, r);
                }
                else {
                    mutation(best, r);
                }
            }
        }
        System.out.println("distance=" + calculateDistance());
        setChanged();
        notifyObservers();
    }

    private void mutation(Route best, Route r) {
        System.out.println("mutation");
        int size = r.getRoute().size() < best.getRoute().size() ? r.getRoute().size(): best.getRoute().size();
        int max = r.getRoute().size() < best.getRoute().size() ? best.getRoute().size(): r.getRoute().size();
        float distanceBefore = best.distanceRoute() + r.distanceRoute();

        for (int i = 0; i < size - 1 ; i++) {
            //do mutation
            doSwap(r, best, i);
            //check (and reverse)
            float newDistance = best.distanceRoute() + r.distanceRoute();
            if (distanceBefore < newDistance) {
                //swap back
                doSwap(r, best, i);
            }
            else
                distanceBefore = newDistance;

        }
        for (int i = size; i < max; i++) {

        }
    }

    private void crossover(Route best, Route r) {
        System.out.println("croisement");
        int size = r.getRoute().size() < best.getRoute().size() ? r.getRoute().size(): best.getRoute().size();
        float distanceBefore = best.distanceRoute() + r.distanceRoute();
        int starter = random.nextInt(size);

        for (int i = starter; i < size - 1; i++)
            doSwap(r, best, i);

        float newDistance = best.distanceRoute() + r.distanceRoute();

        //swap back
        if (distanceBefore < newDistance) {
            for (int i = starter; i < size - 1; i++)
                doSwap(r, best, i);
        }

    }

    private Route reproductionWheel() {
         return routes.stream().min(Comparator.comparing(Route::distanceRoute)).get();
    }

    private void doSwap(Route r1, Route r2, int i) {
        if (r1.getRoute().get(i) != this.graph.getWarehouse()
                && r2.getRoute().get(i) != this.graph.getWarehouse()
                && r1.getRoute().get(i).getQuantity() - r2.getRoute().get(i).getQuantity() <= r2.getCapacityLeft()
                && r2.getRoute().get(i).getQuantity() - r1.getRoute().get(i).getQuantity() <= r1.getCapacityLeft()) {

            Client tmp = r1.getRoute().get(i);
            r1.getRoute().set(i, r2.getRoute().get(i));
            r1.decreaseCapacityLeft(r2.getRoute().get(i).getQuantity() - tmp.getQuantity());
            r2.getRoute().set(i, tmp);
            r2.decreaseCapacityLeft(tmp.getQuantity() - r1.getRoute().get(i).getQuantity());
        }
    }

    @Override
    public void run() {
        initRoutes();
        System.out.println("distance=" + calculateDistance());

        //for (int i = 0; i < graph.getClients().size(); i++)
        while (true){
            try {
                Thread.sleep(SLEEPING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            step();
        }

    }
}
