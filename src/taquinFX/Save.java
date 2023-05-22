package taquinFX;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
//Méthode pour sélectionner une sauvegarde

public class Save {
    public static String select(Scanner scanner) {
        String folder = "Saves";  // Le dossier contenant les fichiers de sauvegarde

        // Obtenir tous les fichiers CSV dans le dossier spécifié
        File folderFiles = new File(folder);
        File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));

     // Vérifier si le dossier contient des fichiers de sauvegarde
        if (files != null && files.length > 0) {
            // Affichez la liste des fichiers disponibles
            System.out.println("Veuillez sélectionner votre sauvegarde :");
            for (int i = 0; i < files.length; i++) {
                String fileName = files[i].getName().replace(".csv", ""); // Afficher chaque nom de fichier, sans l'extension .csv
                System.out.println((i + 1) + ". " + fileName);
            }

            // Boucle pour demander à l'utilisateur de choisir un fichier valide
            while (true) {
                // Demandez à l'utilisateur de choisir un fichier
                System.out.print("Choisissez une sauvegarde (entrez le numéro correspondant) : ");
                int choice = 0;
                if (scanner.hasNextInt()) {  // Si l'utilisateur a entré un nombre entier, le stocker dans la variable "choice"
                	choice = scanner.nextInt();
                }else {  // Si l'utilisateur n'a pas entré un nombre entier, afficher un message d'erreur
                	System.out.println("Erreur : vous devez entrer un entier.");
                	scanner.next();
				}

                // Si le choix de l'utilisateur est dans la plage valide, traiter la sauvegarde sélectionnée
                if (choice >= 1 && choice <= files.length) {
                    File selectedFile = files[choice - 1]; // Sélectionner le fichier correspondant au choix de l'utilisateur
                    String selectedFileName = selectedFile.getName().replace(".csv", ""); 
                    System.out.println("\n\nVous avez choisi la sauvegarde : " + selectedFileName + "\n"); // Afficher le nom de la sauvegarde sélectionnée, sans l'extension .csv
                    return selectedFile.getName();// Retourner le nom du fichier de la sauvegarde sélectionnée
                } else {
                	// Si le choix de l'utilisateur n'est pas dans la plage valide, afficher un message d'erreur
                    System.out.println("Choix invalide. Veuillez sélectionner une sauvegarde valide.");
                }
            }
        } else {
        	// Si aucun fichier de sauvegarde n'a été trouvé, afficher un message d'erreur
            System.out.println("Aucune sauvegarde trouvée dans le dossier spécifié.");
        }
        return null;  // Si aucune sauvegarde n'a été sélectionnée, retourner null
    }

    public static void updateScoreAndAccessibility(String SaveName,String Levelname,int newScore,int newTime) {
        File file = new File("Saves/" + SaveName);

        // Lire les informations actuelles du fichier CSV
        List<String[]> levelInfo = readCSV(file);
        if (levelInfo != null) {
            for (int i = 0; i < levelInfo.size(); i++) {
                String[] info = levelInfo.get(i);
                String bestScore = info[2];        // Meilleur score du niveau (en tant que chaîne de caractères)
                String bestTime =info[3];
               
                if (info[0].equals(Levelname) && Integer.parseInt(bestScore)==0) { //Initialiser le meilleur score que pour le niveau joué
                	info[2] = String.valueOf(newScore);
                	info[3]= String.valueOf(newTime);
                }
                // Mettre à jour le meilleur score si nécessaire
                if (info[0].equals(Levelname) && (Integer.parseInt(bestScore) > newScore)) {
                    info[2] = String.valueOf(newScore);  // Convertir le nouveau score en chaîne de caractères et le mettre à jour
                }
                // Mettre à jour le meilleur temps si nécessaire
                if (info[0].equals(Levelname) && (Integer.parseInt(bestTime) > newTime)) {
                    info[3] = String.valueOf(newTime);  // Convertir le nouveau score en chaîne de caractères et le mettre à jour
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


 // Méthode pour lire le contenu d'un fichier CSV et retourner les données sous forme de liste de tableaux de String
    private static List<String[]> readCSV(File file) {
        List<String[]> levelInfo = new ArrayList<>();  // Initialiser une liste vide pour stocker les informations sur chaque niveau

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) { 
            String line; // Variable pour stocker chaque ligne du fichier
         // Lire le fichier ligne par ligne
            while ((line = reader.readLine()) != null) {
                String[] data = line.split(",");  // Diviser chaque ligne en utilisant la virgule comme séparateur et stocker le résultat dans un tableau de String
                if (data.length == 4) { // Ajouter le tableau de données à la liste des informations de niveau si le tableau contient exactement 4 éléments
                    levelInfo.add(data);
                }
            }
        } catch (IOException e) {
        	// Afficher un message d'erreur si une exception se produit lors de la lecture du fichier
            System.out.println("Erreur lors de la lecture du fichier CSV : " + file.getName());
            e.printStackTrace();
        }
     // Retourner la liste des informations de niveau
        return levelInfo;
    }

    // Méthode pour écrire les informations de niveau dans un fichier CSV
    private static void writeCSV(File file, List<String[]> levelInfo) {
        try (PrintWriter writer = new PrintWriter(file)) {
            for (String[] info : levelInfo) {  // Parcourir chaque tableau d'informations de niveau
                writer.println(String.join(",", info));  // Parcourir chaque tableau d'informations de niveau
            }
        } catch (FileNotFoundException e) {
        	// Afficher un message d'erreur si une exception se produit lors de l'écriture du fichier
            System.out.println("Erreur lors de l'écriture du fichier CSV : " + file.getName());
            e.printStackTrace();
        }
    }
 // Méthode pour obtenir le meilleur score d'un niveau spécifique
    public static int getBestScore(String SaveName, String Levelname) {
        File file = new File("Saves/" + SaveName);  // Initialiser le fichier CSV à partir duquel lire les informations de niveau

        // Lire les informations actuelles du fichier CSV
        List<String[]> levelInfo = readCSV(file);
        if (levelInfo != null) {
            for (String[] info : levelInfo) { // Parcourir chaque tableau d'informations de niveau
                String level = info[0];
                String bestScore = info[2];
                // Si le nom du niveau correspond au niveau demandé, retourner le meilleur score comme un entier
                if (level.equals(Levelname)) {
                    return Integer.parseInt(bestScore);
                }
            }
        }

        // Retourner -1 si le niveau n'a pas été trouvé ou s'il n'y a pas de meilleur score enregistré
        return -1;
    }
    public static int getBestTime(String SaveName, String Levelname) {
        File file = new File("Saves/" + SaveName);

        // Lire les informations actuelles du fichier CSV
        List<String[]> levelInfo = readCSV(file);
        if (levelInfo != null) {
            for (String[] info : levelInfo) {
                String level = info[0];
                String bestTime = info[3];

                if (level.equals(Levelname)) {
                    return Integer.parseInt(bestTime);
                }
            }
        }

        // Retourner -1 si le niveau n'a pas été trouvé ou s'il n'y a pas de meilleur score enregistré
        return 0;
    }
}
