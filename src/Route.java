import java.util.List;

public class Route {

    private List<Client> route;
    private int capacityLeft;

    public Route(List<Client> route) {
        this.route = route;
        this.capacityLeft = 100;
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
        if (capacityLeft < 0)
            capacityLeft = 0;
    }
}
