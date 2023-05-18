package taquinFX;

import java.io.File;
import java.util.Scanner;

public class LevelSelector {
    public static String select(Scanner scanner) {
        String folder = "../levels";

        // Obtenez tous les fichiers CSV dans le dossier spécifié
        File folderFiles = new File(folder);
        File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            // Affichez la liste des fichiers disponibles
            System.out.println("Veuillez sélectionner votre niveau :");
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName().replace(".csv", "");
                System.out.println((i + 1) + ". " + fileName);
            }

            // Boucle pour demander à l'utilisateur de choisir un fichier valide
            while (true) {
                // Demandez à l'utilisateur de choisir un fichier
                System.out.print("Choisissez un niveau (entrez le numéro correspondant) : ");
                int choice = scanner.nextInt();

                if (choice >= 1 && choice <= files.length) {
                    // Traitez le fichier sélectionné
                    File selectedFile = files[choice - 1];
                    String selectedFileName = selectedFile.getName().replace(".csv", "");
                    System.out.println("\n\nVous avez choisi le niveau : " + selectedFileName+"\n");
                    return selectedFile.getName();
                } else {
                    System.out.println("Choix invalide. Veuillez sélectionner un niveau valide.");
                }
            }
        } else {
            System.out.println("Aucun niveau trouvé dans le dossier spécifié.");
        }
        return null;
    }
}
