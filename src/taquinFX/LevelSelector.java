package taquinFX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;

public class LevelSelector {
    public static String select(Scanner scanner, String saveFile) {
        String folder = "../levels";

        // Obtenez tous les fichiers CSV dans le dossier spécifié
        File folderFiles = new File(folder);
        File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            // Affichez la liste des fichiers disponibles
            System.out.println("Veuillez sélectionner votre niveau :");
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName().replace(".csv", "");
                boolean accessible = isLevelAccessible(saveFile, fileName);
                String accessibilityStatus = accessible ? "Oui" : "Non";
                System.out.println((i + 1) + ". " + fileName + " - Accessible : " + accessibilityStatus);
                
            }

            // Boucle pour demander à l'utilisateur de choisir un niveau valide et accessible
            while (true) {
                // Demandez à l'utilisateur de choisir un fichier
                System.out.print("Choisissez un niveau (entrez le numéro correspondant) : ");
                int choice = 0;
                if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                }else {
                	  System.out.println("Erreur : vous devez entrer un entier.");
                	  scanner.next();
				}

                if (choice >= 1 && choice <= files.length) {
                    // Traitez le fichier sélectionné
                    File selectedFile = files[choice - 1];
                    String selectedFileName = selectedFile.getName().replace(".csv", "");
                    System.out.println("\n\nVous avez choisi le niveau : " + selectedFileName + "\n");
                    if (isLevelAccessible(saveFile, selectedFileName)) {
                    	 return selectedFile.getName();
					}else {
						System.out.println("Choix invalide. Veuillez sélectionner un niveau accesible\n.");
					}
                   
                } else {
                    System.out.println("Choix invalide. Veuillez sélectionner un niveau valide.");
                }
            }
        } else {
            System.out.println("Aucun niveau trouvé dans le dossier spécifié.");
        }
        return null;
    }

    private static boolean isLevelAccessible(String saveFile, String levelFile) {
        // Vérifier la sauvegarde pour voir si le niveau est accessible
        try {
            // Lire le contenu du fichier de sauvegarde
            List<String> saveLines = Files.readAllLines(Path.of(saveFile));

            // Rechercher le niveau correspondant dans le fichier de sauvegarde
            for (String saveLine : saveLines) {
                String[] saveData = saveLine.split(",");
                String saveLevel = saveData[0];
                String saveAccessible = saveData[1];

                // Vérifier si le niveau correspond au niveau recherché
                if (saveLevel.equals(levelFile)) {
                    return Boolean.parseBoolean(saveAccessible);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier de sauvegarde : " + saveFile);
            e.printStackTrace();
        }

        // Par défaut, considérer le niveau comme inaccessible
        return false;
    }
}
