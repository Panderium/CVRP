package algorithm;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observer;

import static java.util.stream.Collectors.joining;

public class Graph {
    public static int algorithmChoice;

    private List<Client> clients;
    private Client warehouse;

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

        if (algorithmChoice == 1) // Taboo choice
            (new Thread(new Taboo(this, observer))).start();
        else // Genetic choice
            (new Thread(new AlgoGen(this, observer))).start();
    }

    @Override
    public String toString() {
        return clients.stream()
                .map(Client::toString)
                .collect(joining("\n"));
    }

    public Client getWarehouse() {
        return warehouse;
    }

    public List<Client> getClients() {
        return clients;
    }

    public void setClients(List<Client> clients) {
        this.clients = clients;
    }
}
