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

    public Route(List<Client> route) {
        this.capacityLeft = 100;
        this.route = route;
    }

    public void addClient(Client client) {
        this.route.add(client);
        decreaseCapacityLeft(client.getQuantity());
    }

    public List<Client> getRoute() {
        return route;
    }

    public void setRoute(List<Client> route) {
        this.route = route;
    }

    public int getCapacityLeft() {
        return capacityLeft;
    }

    public void decreaseCapacityLeft(int quantity) {
        this.capacityLeft -= quantity;
    }
}
