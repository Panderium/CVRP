package algorithm;

import java.util.Objects;

public class Client {

    private int id;
    private int x;
    private int y;
    private int quantity;

    public Client(int id, int x, int y, int quantity) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.quantity = quantity;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int getQuantity() {
        return quantity;
    }

    @Override
    public String toString() {
        return "id=" + id +
                ",x=" + x +
                ",y=" + y +
                ",quantity=" + quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return id == client.id &&
                x == client.x &&
                y == client.y &&
                quantity == client.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, x, y, quantity);
    }
}
