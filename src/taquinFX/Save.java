package taquinFX;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Save {

    public static String select(Scanner scanner) {
        String folder = "Saves";

        // Obtenez tous les fichiers CSV dans le dossier spécifié
        File folderFiles = new File(folder);
        File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));

        if (files != null && files.length > 0) {
            // Affichez la liste des fichiers disponibles
            System.out.println("Veuillez sélectionner votre sauvegarde :");
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName().replace(".csv", "");
                System.out.println((i + 1) + ". " + fileName);
            }

            // Boucle pour demander à l'utilisateur de choisir un fichier valide
            while (true) {
                // Demandez à l'utilisateur de choisir un fichier
                System.out.print("Choisissez une sauvegarde (entrez le numéro correspondant) : ");
                int choice = scanner.nextInt();

                if (choice >= 1 && choice <= files.length) {
                    // Traitez le fichier sélectionné
                    File selectedFile = files[choice - 1];
                    String selectedFileName = selectedFile.getName().replace(".csv", "");
                    System.out.println("\n\nVous avez choisi la sauvegarde : " + selectedFileName + "\n");
                    return selectedFile.getName();
                } else {
                    System.out.println("Choix invalide. Veuillez sélectionner une sauvegarde valide.");
                }
            }
        } else {
            System.out.println("Aucune sauvegarde trouvée dans le dossier spécifié.");
        }
        return null;
    }

    public static void updateScoreAndAccessibility(String SaveName,String Levelname,int newScore) {
        File file = new File("Saves/" + SaveName);

        // Lire les informations actuelles du fichier CSV
        List<String[]> levelInfo = readCSV(file);
        if (levelInfo != null) {
            for (int i = 0; i < levelInfo.size(); i++) {
                String[] info = levelInfo.get(i);
                String bestScore = info[2];        // Meilleur score du niveau (en tant que chaîne de caractères)

                // Mettre à jour le meilleur score si nécessaire
                if (info[0].equals(Levelname) && Integer.parseInt(bestScore) < newScore) {
                    info[2] = String.valueOf(newScore);  // Convertir le nouveau score en chaîne de caractères et le mettre à jour
                }

                // Modifier l'accessibilité du niveau suivant
                if (info[0].equals(Levelname) && i < levelInfo.size() - 1) { //on verifie si on est pas a la derniere ligne
                    String[] nextLevelInfo = levelInfo.get(i + 1);  // Obtenir les informations du niveau suivant
                    nextLevelInfo[1] = String.valueOf(true);  // Mettre à jour l'accessibilité du niveau suivant
                }
            }

            // Écrire les nouvelles informations dans le fichier CSV
            writeCSV(file, levelInfo);
        }
    }



    private static List<String[]> readCSV(File file) {
        List<String[]> levelInfo = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    levelInfo.add(data);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la lecture du fichier CSV : " + file.getName());
            e.printStackTrace();
        }

        return levelInfo;
    }

    private static void writeCSV(File file, List<String[]> levelInfo) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String[] info : levelInfo) {
                // Écrire chaque ligne du fichier CSV
                writer.println(String.join(",", info));
            }
        } catch (FileNotFoundException e) {
            System.out.println("Erreur lors de l'écriture du fichier CSV : " + file.getName());
            e.printStackTrace();
        }
    }
    
    public static int getBestScore(String SaveName, String Levelname) {
        File file = new File("Saves/" + SaveName);

        // Lire les informations actuelles du fichier CSV
        List<String[]> levelInfo = readCSV(file);
        if (levelInfo != null) {
            for (String[] info : levelInfo) {
                String level = info[0];
                String bestScore = info[2];

                if (level.equals(Levelname)) {
                    return Integer.parseInt(bestScore);
                }
            }
        }

        // Retourner -1 si le niveau n'a pas été trouvé ou s'il n'y a pas de meilleur score enregistré
        return -1;
    }
}
