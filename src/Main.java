import algorithm.AlgoGen;
import algorithm.Graph;
import graphics.Window;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("- 1 : Taboo");
        System.out.println("- 2 : Algorithme génétique");

        do {
            System.out.print("Choix de l'algorithme : ");
            Graph.algorithmChoice = scanner.nextInt();
        } while (Graph.algorithmChoice < 1 || Graph.algorithmChoice > 2);

        if(Graph.algorithmChoice == 2) { // If AlgoGen
            do {
                System.out.print("Probabilité de croisement (entre 0 et 1) : ");
                AlgoGen.PROB_CROSS = (double)scanner.nextInt() / 100;
            }while(AlgoGen.PROB_CROSS <= 0 || AlgoGen.PROB_CROSS >= 1);
        }

        int fileNumber;

        do {
            System.out.print("Choix du fichier de données (1 - 3) : ");
            fileNumber = scanner.nextInt();
        } while (fileNumber < 1 || fileNumber > 3);

        Window window = new Window("Visualisation du graph", "data0" + fileNumber + ".txt");
    }
}
