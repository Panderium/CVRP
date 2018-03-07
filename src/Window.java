import javax.swing.*;
import java.awt.*;

public class Window extends JFrame {

    public Window(String title, String filename) throws HeadlessException {
        super(title);

        Graph graph = new Graph(filename);

        JPanel panel = new JPanel();
        panel.setSize(640, 640);


        graph.getClients().forEach(client -> {
            panel.getGraphics().drawLine(client.getX(), client.getY(), client.getX(), client.getY());
        });

        this.pack();
        this.setVisible(true);
    }
}
