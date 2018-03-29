package algorithm;

import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.Observer;

public abstract class Algorithm extends Observable {
    protected List<Route> routes;


    public Algorithm(Observer observer) {
        this.routes = new ArrayList<>();
        this.addObserver(observer);
    }

    public List<Route> getRoutes() {
        return routes;
    }

    protected abstract void step();
}
