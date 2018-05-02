package algorithm;

import java.util.ArrayList;
import java.util.Collections;
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

    public List<Client> getRoute() {
        return route;
    }

    public int getCapacityLeft() {
        return capacityLeft;
    }

    public void decreaseCapacityLeft(int quantity) {
        this.capacityLeft -= quantity;
    }

    public void swapClient(int i, int j) {
        Collections.swap(route, i, j);
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
