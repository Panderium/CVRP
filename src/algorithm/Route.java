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

    public void setClient(int index, Client client) {
        if (index > 1 && index < getRoute().size() - 1) { // No warehouse !
            // New calculation of capacityLeft
            capacityLeft += route.get(index).getQuantity();
            Client oldClient = route.set(index, client);
            System.out.println("Replaced " + oldClient);
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

    public float distance(int x1, int y1, int x2, int y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }
}
