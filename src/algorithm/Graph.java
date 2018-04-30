package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import static java.util.stream.Collectors.joining;

public class Graph {
    private final List<Client> clients;
    private Client warehouse;

    private Taboo taboo;

    public Graph(String filename, Observer observer) {
        this.clients = new ArrayList<>();

        try {
            BufferedReader reader = new BufferedReader(new FileReader(filename));

            warehouse = null;

            // Skip header on first line
            reader.readLine();

            String line;
            String[] lineSplit;

            while ((line = reader.readLine()) != null) {
                lineSplit = line.split(";");

                // TODO lambdaaa <3<3<3
                Client client = new Client(Integer.parseInt(lineSplit[0]),
                        Integer.parseInt(lineSplit[1]),
                        Integer.parseInt(lineSplit[2]),
                        Integer.parseInt(lineSplit[3]));

                if (lineSplit.length == 4) {
                    // First client is the warehouse
                    if (warehouse == null)
                        warehouse = client;
                    else
                        clients.add(client);
                } else
                    System.err.println("FILE ERROR : line length = " + lineSplit.length);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
        this.taboo = null;

        (new Thread(new Taboo(this, observer))).start();
    }

    @Override
    public String toString() {
        return clients.stream()
                .map(Client::toString)
                .collect(joining("\n"));
    }

    public Taboo getTaboo() {
        return taboo;
    }


    public Client getWarehouse() {
        return warehouse;
    }

    public List<Client> getClients() {
        return clients;
    }
}
