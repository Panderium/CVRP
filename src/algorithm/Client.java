package algorithm;

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

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
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

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
