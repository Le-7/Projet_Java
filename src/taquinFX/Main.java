package taquinFX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String saveSelectionString = Save.select(scanner);
        String levelSelection = LevelSelector.select(scanner, "../Saves/" + saveSelectionString);
        Board board = new Board("../levels/" + levelSelection); // créer un nouveau tableau de taille n
        int maxCount = 1000000; // on définit le nombre maximum de tentatives
        int count = 0; // initialisation du compteur
        int shot = 0; // Initialisation du compteur de coups
        boolean useSolver = false;
        long start = 0;
        long end = 0;

        System.out.println("Votre meilleur score pour ce niveau est de : " + Save.getBestScore(saveSelectionString, levelSelection.replace(".csv", "")) + "\n");
        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println();
        while(board.InitialPosition() || !board.solveWithinTime(50)) { // Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau. {
        	board = new Board("../levels/" + levelSelection);
        	if (board.isEZ()) {
        		Random random = new Random();
        		int randomNumber = random.nextInt(2);
        		if(randomNumber == 0) {
        			System.out.println("Mélange automatique");
	        		board.mixBoardAuto();
					while (!board.isSolvable()) {					//si le plateau est "facile" alors on peut utiliser l'algo
						board.mixBoardAuto();
					}
        		}else {
        			System.out.println("Mélange manuel");
        			board.mixBoard(40);
        		}
        		
			}else {
				 board.mixBoard(40); // Sinon on mélange le plateau "manuellement"
			}
           
            count++;
            System.out.println("Tentative mélange n°" + count + "\n"); // Afficher le compteur de tentatives
            if (count == maxCount) {
                System.out.println("Impossible de mélanger le plateau sans revenir à sa position initiale");
                return;
            }
        } 

        start = System.currentTimeMillis(); //Debut du chrono dès que le niveau est lancé pour pas tricher
        while (!gameSolved(IDASolver.convertBoxArrayToShortArray(board.getGrid()),"../levels/" + levelSelection)) {
            board.displayBoard(); // Afficher le plateau après le mélange

            // Demander à l'utilisateur de saisir les coordonnées
            System.out.print("Entrez les coordonnées (deux entiers) de la case à déplacer (ligne colonne, pour le solveur entrez 0 0): ");
            int row = 0;
            int col = 0;
            boolean validInt = false;
            while (!validInt) {
                if (scanner.hasNextInt()) {
                    row = scanner.nextInt();
                    if (scanner.hasNextInt()) {
                        col = scanner.nextInt();
                        validInt = true;
                    } else {
                        System.out.println("Erreur : deux entiers doivent être saisis.");
                        scanner.next();
                    }
                } else {
                    System.out.println("Erreur : deux entiers doivent être saisis.");
                    scanner.next();
                }
            }
            List<int[]> emptyBoxes = board.findEmptyBoxes(); // on récupère les cases vides

            // Vérifier si les coordonnées sont égales à 0
            if (row == 0 && col == 0) {
                System.out.println("\nVeuillez patienter, le solveur est en marche...\n");
                useSolver = true;
                long startTime = System.currentTimeMillis();
                // Appeler le solveur pour résoudre le taquin
                List<String> solution = board.solve();
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                for (String move : solution) {
                    String[] parts = move.split(" ");

                    // Extraire le mouvement (première partie) pour l affichage si quelqun a la force de le faire
                    //String direction = parts[0];

                    // Extraire les coordonnées de la case vide d'origine (deuxième partie)
                    int index1 =  Integer.parseInt(parts[1]);

                    // Extraire les coordonnées de la case vide cible (troisième partie)
                    int index2 =  Integer.parseInt(parts[2]);

                    
                    // Effectuer le déplacement sur le plateau de jeu en utilisant la méthode swap()
                    if (board.swap2(index1,index2)) {
                    	shot++;
                        board.displayBoard();
					}
                    
                }
                System.out.println("Temps d'exécution du solveur : " + executionTime + " millisecondes");
                break; // Sortir de la boucle while
            }

            List<int[]> adjacentEmptyBoxes = new ArrayList<>();

            for (int[] emptyBox : emptyBoxes) {
                int rowEmpty = emptyBox[0];
                int colEmpty = emptyBox[1];
                int index1 = (row-1) * board.getBoardSize() + (col-1);
        		int index2 = rowEmpty * board.getBoardSize() + colEmpty;
                if (board.isAdjacent(index1,index2)) {
                    adjacentEmptyBoxes.add(emptyBox); // si la case rentrée est adjacente à une case vide on l'ajoute
                }
            }

            if (adjacentEmptyBoxes.size() == 1) { // le cas classique où il n'y a qu'une seule case vide adjacente
                int rowEmpty = adjacentEmptyBoxes.get(0)[0];
                int colEmpty = adjacentEmptyBoxes.get(0)[1];
                if (row >= 1 && row <= board.getBoardSize() && col >= 1 && col <= board.getBoardSize()) {
                    if (board.swap(rowEmpty, colEmpty, row - 1, col - 1)) {
                        shot++;
                    }
                } else {
                    System.out.println("Coordonnées invalides. Veuillez réessayer.");
                }
            } else if (adjacentEmptyBoxes.size() > 1) { // si il y en a plusieurs
                System.out.println("Il y a plusieurs cases vides adjacentes. Veuillez choisir une des cases vides suivantes :");
                for (int i = 0; i < adjacentEmptyBoxes.size(); i++) {
                    int[] emptyBox = adjacentEmptyBoxes.get(i);
                    int rowEmpty = emptyBox[0];
                    int colEmpty = emptyBox[1];
                    System.out.println((i + 1) + ") Coordonnées : (" + (rowEmpty + 1) + ", " + (colEmpty + 1) + ")");
                }

                System.out.print("Entrez le numéro du choix de la case vide adjacente à utiliser : ");
                int choice = scanner.nextInt();

                // on vérifie si le choix rentré est valide
                if (choice >= 1 && choice <= adjacentEmptyBoxes.size()) {
                    int[] emptyBox = adjacentEmptyBoxes.get(choice - 1);
                    int rowEmpty1 = emptyBox[0];
                    int colEmpty1 = emptyBox[1];

                    if (row >= 1 && row <= board.getBoardSize() && col >= 1 && col <= board.getBoardSize()) {
                        if(board.swap(rowEmpty1, colEmpty1, row - 1, col - 1)) { // on fait l'échange
                            shot++; // Incrémentation seulement si le coup est valide
                        }
                    } else {
                        System.out.println("Coordonnées invalides. Veuillez réessayer.");
                    }
                } else {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            } else {
                System.out.println("Aucune case vide adjacente. Veuillez réessayer.");
            }
            System.out.println("\nNombre de coups : " + shot + "\n"); // On affiche à chaque coup, valide ou non
        }
        end = System.currentTimeMillis();
        long execution = end - start;
        // Fermer le scanner
        scanner.close();


        if (useSolver == false) { // Message de fin selon le type de résolution (utilisateur ou solver)
            Save.updateScoreAndAccessibility(saveSelectionString, levelSelection.replace(".csv", ""), shot);
            System.out.println("\n\nBravo, vous avez gagné en " + shot + " coups !");
            System.out.println("Vous avez résolu le taquin en : " + (execution / 1000.0) + " secondes"); // Affichage provisoire en secondes

        } else {
            System.out.println("\n\nIl a fallu " + shot + " coups pour résoudre le taquin.");

        }

    }

	 public static boolean gameSolved(short[] grid, String Csv_path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Csv_path));
            String line;
            int i = 0;
            int boardSize = (int) Math.sqrt(grid.length);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                for (int j = 0; j < values.length; j++) {
                    if (grid[i * boardSize + j] != Integer.parseInt(values[j])) {
                        reader.close();
                        return false;
                    }
                }

                i++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
}
