import graphics.Window;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int algorithmChoice;

        System.out.println("- 1 : Taboo");
        System.out.println("- 2 : Algorithme génétique");

        do {
            System.out.print("Choix de l'algorithme : ");
            algorithmChoice = scanner.nextInt();
        }while(algorithmChoice < 1 || algorithmChoice > 2);

        int fileNumber;

        do {
            System.out.print("Choix du fichier de données (1 - 3) : ");
            fileNumber = scanner.nextInt();
        }while(fileNumber < 1 || fileNumber > 3);

        Window window = new Window("Visualisation du graph", "data0" + fileNumber + ".txt");
    }
}
