package algorithm;

import java.util.ArrayList;
import java.util.List;

public class Route {

    private List<Client> route;
    private int capacityLeft;

    public Route() {
        this.route = new ArrayList<>();
        this.capacityLeft = 100;
    }

    public void addClient(Client client) {
        this.route.add(client);
        decreaseCapacityLeft(client.getQuantity());
    }

    public void addClient(int index, Client client) {
        this.route.add(index, client);
        decreaseCapacityLeft(client.getQuantity());
    }

    public void removeClient(Client client) {
        this.route.remove(client);
        capacityLeft += client.getQuantity();
    }

    public void setClient(int index, Client client) {
        if (index > 0 && index < getRoute().size() - 1) { // No warehouse !
            // New calculation of capacityLeft
            capacityLeft += route.get(index).getQuantity();
            route.set(index, client);
            capacityLeft -= client.getQuantity();
        }
        else
            System.err.println("CAN'T SWAP WITH INDEX " + index);
    }

    public List<Client> getRoute() {
        return route;
    }

    public int getCapacityLeft() {
        return capacityLeft;
    }

    public void decreaseCapacityLeft(int quantity) {
        this.capacityLeft -= quantity;
    }

    public float distanceRoute() {
        float distanceRoute = 0;
        Client firstClient = route.get(0);

        for (int i = 1; i < route.size(); i++) {
            distanceRoute += distance(firstClient.getX(), firstClient.getY(), route.get(i).getX(), route.get(i).getY());
            firstClient = route.get(i);
        }

        return distanceRoute;
    }

    public void removeLastWarehouse() {
        route.remove(route.size() - 1);
    }

    public void setRoute(List<Client> route) {
        this.route = route;
    }

    public void setCapacityLeft(int capacityLeft) {
        this.capacityLeft = capacityLeft;
    }

    public void removeClient(int i) {
        route.remove(i);

    }

    public float distance (int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
