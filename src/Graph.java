import javax.swing.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.util.stream.Collectors.joining;

public class Graph {
    private final List<Route> routes;
    private final List<Client> clients;
    private Client warehouse;

    public Graph(String filename) {
        this.routes = new ArrayList<>();
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

                // TODO lambdaa <3<3<3
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
    }

    @Override
    public String toString() {
        return clients.stream()
                .map(Client::toString)
                .collect(joining("\n"));

    }

    public List<Client> getClients() {
        return clients;
    }

    private void generateFirstSolution() {
        // TODO generer une premiere solution qu'on ameliorera par la suite
    }
}
