package taquinFX;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Scanner;
import java.util.Arrays;

//La classe LevelSelector est utilisée pour sélectionner le niveau d'un jeu à partir d'un dossier de niveaux
public class LevelSelector {
    public static String select(Scanner scanner, String saveFile) { //Cette méthode est utilisée pour sélectionner un niveau à partir d'un dossier
        String folder = "levels"; // Le dossier contenant les niveaux du jeu

        // On obtiens tous les fichiers CSV dans le dossier spécifié
        File folderFiles = new File(folder);
        File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));
	Arrays.sort(files, (file1, file2) -> file1.getName().compareToIgnoreCase(file2.getName())); //Tri ordre alphabétique

	// Vérifie si des fichiers sont disponibles dans le dossier
        if (files != null && files.length > 0) {
            System.out.println("Veuillez sélectionner votre niveau :");// Affichez la liste des fichiers disponibles
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName().replace(".csv", "");  // Retire l'extension .csv du nom du fichier
                boolean accessible = isLevelAccessible(saveFile, fileName);  // Vérifie si le niveau est accessible
                String accessibilityStatus = accessible ? "Oui" : "Non"; // Initialise le statut d'accessibilité du niveau
                System.out.println((i + 1) + ". " + fileName + " - Accessible : " + accessibilityStatus); // Affiche le numéro du niveau, le nom du niveau et le statut d'accessibilité       
            }

            // Boucle pour demander à l'utilisateur de choisir un niveau valide et accessible
            while (true) {
                // Demande à l'utilisateur de choisir un numero de niveau
                System.out.print("Choisissez un niveau (entrez le numéro correspondant) : ");
                int choice = 0; // Vérifie si l'entrée de l'utilisateur est un entier
                if (scanner.hasNextInt()) {
                choice = scanner.nextInt();
                }else {
                	// Informe l'utilisateur que l'entrée doit être un entier
                	  System.out.println("Erreur : vous devez entrer un entier.");
                	  scanner.next();
				}

             // Si le choix de l'utilisateur est valide, traitez le fichier sélectionné
                if (choice >= 1 && choice <= files.length) {
                    // Traitez le fichier sélectionné
                    File selectedFile = files[choice - 1]; // Sélectionne le fichier correspondant au choix de l'utilisateur
                    String selectedFileName = selectedFile.getName().replace(".csv", "");  // Retire l'extension .csv du nom du fichier sélectionné
                    System.out.println("\n\nVous avez choisi le niveau : " + selectedFileName + "\n"); // Informe l'utilisateur du niveau qu'il a sélectionné
                    if (isLevelAccessible(saveFile, selectedFileName)) {   // Vérifie si le niveau sélectionné est accessible
                    	 return selectedFile.getName();
					}else {
						  // Informe l'utilisateur que le niveau sélectionné n'est pas accessible
						System.out.println("Choix invalide. Veuillez sélectionner un niveau accesible\n.");
					}
                   
                } else {
                	// Informe l'utilisateur que le numéro de niveau qu'il a sélectionné est invalide
                    System.out.println("Choix invalide. Veuillez sélectionner un niveau valide.");
                }
            }
        } else {
        	// Informe l'utilisateur qu'aucun niveau n'a été trouvé dans le dossier spécifié
            System.out.println("Aucun niveau trouvé dans le dossier spécifié.");
        }
     // Renvoie null si aucun fichier n'est trouvé ou si un fichier valide n'est pas sélectionné
        return null;
    }

    private static boolean isLevelAccessible(String saveFile, String levelFile) {
        // Vérifier la sauvegarde pour voir si le niveau est accessible
        // Vérifiez le fichier CSV de la sauvegarde et le fichier CSV du niveau pour déterminer l'accessibilité

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
