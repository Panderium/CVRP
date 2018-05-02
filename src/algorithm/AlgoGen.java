package algorithm;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class AlgoGen extends Algorithm implements Runnable {

    private final int SLEEPING_TIME = 5;
    public static double PROB_CROSS = 0.1;

    private int TOTAL_LOOP_BY_NB_ROUTE;
    private int TOTAL_LOOP;
    private int NB_LOOP = 0;

    private Graph graph;
    private Route currentRoute;

    private Random random;

    public AlgoGen(Graph graph, Observer observer) {
        super(observer);
        this.graph = graph;

        TOTAL_LOOP_BY_NB_ROUTE = graph.getClients().size() * 10;
        TOTAL_LOOP = TOTAL_LOOP_BY_NB_ROUTE * 4;

        random = new Random();
    }

    private void sort() {
        List<Client> clients = graph.getClients();
        Client warehouse = graph.getWarehouse();
        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                return (o2.getX() + o2.getY()) - (o1.getX() + o1.getY());
            }
        });
        System.out.println(warehouse);
        System.out.println(clients);
        graph.setClients(clients);
    }

    private void reverseSort() {
        List<Client> clients = graph.getClients();
        Client warehouse = graph.getWarehouse();
        Collections.sort(clients, new Comparator<Client>() {
            @Override
            public int compare(Client o1, Client o2) {
                return (o2.getId() - o1.getId());
            }
        });
        System.out.println(warehouse);
        System.out.println(clients);
        graph.setClients(clients);
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
        if (NB_LOOP != 0 && NB_LOOP % TOTAL_LOOP_BY_NB_ROUTE == 0) {
            System.out.println("adding new route...");
            Route newRoute = new Route();
            newRoute.addClient(graph.getWarehouse());
            newRoute.addClient(graph.getWarehouse());
            routes.add(newRoute);
        }
        Route best = reproductionWheel();
        for (Route r : routes) {
            if (!r.equals(best)) {
                if (random.nextDouble() <= PROB_CROSS) {
                    crossover(best, r);
                } else {
                    mutation(best, r);
                }
            }
        }
        System.out.println("distance=" + calculateDistance());
        NB_LOOP++;
        setChanged();
        notifyObservers();
    }

    private void mutation(Route best, Route r) {
        int size = r.getRoute().size() < best.getRoute().size() ? r.getRoute().size() : best.getRoute().size();
        float distanceBefore = best.distanceRoute() + r.distanceRoute();
        for (int j = 0; j < 2; j++) {
            for (int i = 0; i < size - 1; i++) {
                //do mutation
                doSwap(r, best, i);
                //check (and reverse)
                float newDistance = best.distanceRoute() + r.distanceRoute();
                if (distanceBefore < newDistance) {
                    //swap back
                    doSwap(r, best, i);
                } else
                    distanceBefore = newDistance;

            }
        }
    }

    private void crossover(Route best, Route r) {
        int size = r.getRoute().size() < best.getRoute().size() ? r.getRoute().size() : best.getRoute().size();
        int size_max = r.getRoute().size() > best.getRoute().size() ? r.getRoute().size() : best.getRoute().size();
        float distanceBefore = best.distanceRoute() + r.distanceRoute();
        int starter = random.nextInt(size - 1) + 1;

        //saved
        Route best_copy = new Route();
        Route r_copy = new Route();
        best_copy.setCapacityLeft(best.getCapacityLeft());
        best_copy.setRoute(best.getRoute());
        r_copy.setRoute(r.getRoute());
        r_copy.setCapacityLeft(r.getCapacityLeft());
        for (int i = starter; i < size - 1; i++)
            doSwap(r, best, i);

        completeRoute(r, best, size - 1, size_max);
        float newDistance = best.distanceRoute() + r.distanceRoute();

        //swap back
        if (distanceBefore < newDistance) {
            best.setCapacityLeft(best_copy.getCapacityLeft());
            best.setRoute(best_copy.getRoute());
            r.setRoute(r_copy.getRoute());
            r.setCapacityLeft(r_copy.getCapacityLeft());
        }
    }

    private Route reproductionWheel() {
        return routes.stream().min(Comparator.comparing(Route::distanceRoute)).get();
    }

    private void doSwap(Route r1, Route r2, int i) {
        if (r1.getRoute().get(i) != this.graph.getWarehouse()
                && r2.getRoute().get(i) != this.graph.getWarehouse()
                && r2.getCapacityLeft() - r1.getRoute().get(i).getQuantity() + r2.getRoute().get(i).getQuantity() >= 0
                && r1.getCapacityLeft() - r2.getRoute().get(i).getQuantity() + r1.getRoute().get(i).getQuantity() >= 0) {

            Client tmp = r1.getRoute().get(i);
            r1.getRoute().set(i, r2.getRoute().get(i));
            r1.decreaseCapacityLeft(r2.getRoute().get(i).getQuantity() - tmp.getQuantity());
            r2.getRoute().set(i, tmp);
            r2.decreaseCapacityLeft(tmp.getQuantity() - r1.getRoute().get(i).getQuantity());
        }
    }

    public void completeRoute(Route r, Route best, int size, int size_max) {
        int idx = size;
        if (r.getRoute().size() < best.getRoute().size()) {
            r.removeLastWarehouse();
            for (int i = size; i < size_max - 1; i++) {
                if (r.getCapacityLeft() - best.getRoute().get(idx).getQuantity() >= 0) {
                    r.addClient(best.getRoute().get(idx));
                    r.decreaseCapacityLeft(best.getRoute().get(idx).getQuantity());
                    best.decreaseCapacityLeft(-best.getRoute().get(idx).getQuantity());
                    best.removeClient(idx);
                } else
                    idx++;
            }
        } else {
            best.removeLastWarehouse();
            for (int i = size; i < size_max - 1; i++) {
                if (best.getCapacityLeft() - r.getRoute().get(idx).getQuantity() >= 0) {
                    best.addClient(r.getRoute().get(idx));
                    best.decreaseCapacityLeft(r.getRoute().get(idx).getQuantity());
                    r.decreaseCapacityLeft(-r.getRoute().get(idx).getQuantity());
                    r.removeClient(idx);
                } else
                    idx++;
            }
        }
        if (!r.getRoute().get(r.getRoute().size() - 1).equals(graph.getWarehouse()))
            r.addClient(graph.getWarehouse());
        if (!best.getRoute().get(best.getRoute().size() - 1).equals(graph.getWarehouse()))
            best.addClient(graph.getWarehouse());
    }

    @Override
    public void run() {
        HashMap<Integer, List<Route>> solutions = new HashMap<>();
        //sort();
        reverseSort();
        initRoutes();
        System.out.println("distance=" + calculateDistance());

        //for (int i = 0; i < graph.getClients().size(); i++)
        while (NB_LOOP != TOTAL_LOOP) {
            try {
                Thread.sleep(SLEEPING_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            step();
            if (solutions.containsKey(routes.size())) {
                float actualBestDistance = 0;
                for (Route route : solutions.get(routes.size()))
                    actualBestDistance += route.distanceRoute();
                if (calculateDistance() < actualBestDistance) {
                    List<Route> solution = new ArrayList<>(routes);
                    solutions.put(routes.size(), solution);
                }
            }else {
                List<Route> solution = new ArrayList<>(routes);
                solutions.put(routes.size(), solution);
            }
        }
        //Display solutions
        solutions.forEach((nbRoute, routes1) -> {
            AtomicInteger distance = new AtomicInteger(0);
            routes1.forEach(route -> {
                distance.addAndGet((int) route.distanceRoute());
            });
            System.out.println("Noombre de routes : " + nbRoute + ", distance min : " + distance);
        });

    }
}
