package taquinFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
    	Scanner scanner = new Scanner(System.in);
    	String levelSelection = LevelSelector.select(scanner);
        Board board = new Board("../levels/"+ levelSelection); // créer un nouveau tableau de taille n
        int maxCount = 5; // on définit le nombre maximum de tentatives
        int count = 0; // initialisation du compteur
        int shot = 0; // Initialisation du compteur de coups
        boolean useSolver = false; 
       
        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println();

        do {
            board.mixBoard(100); // Mélanger le plateau
            count++;
            System.out.println("Tentative mélange n°" + count+"\n"); // Afficher le compteur de tentatives
            if (count == maxCount) {
                System.out.println("Impossible de mélanger le plateau sans revenir à sa position initiale");
                return;
            }
        } while (board.InitialPosition()); // Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau.


        while (!board.gameSolved()) {
            board.displayBoard(); // Afficher le plateau après le mélange

            // Demander à l'utilisateur de saisir les coordonnées
            System.out.print("Entrez les coordonnées de la case à déplacer (ligne colonne, pour le solveur entrez 0 0): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            
            List<int[]> emptyBoxes = board.findEmptyBoxes(); // on récupère les cases vides
            
            
            // Vérifier si les coordonnées sont égales à 0
            if (row == 0 && col == 0) {
            	System.out.println("\nVeuillez patientez, le solveur est en marche...\n " );
            	useSolver = true;
            	long startTime = System.currentTimeMillis();
                // Appeler le solveur pour résoudre le taquin
                List<String> solution = board.solve();
                long endTime = System.currentTimeMillis();
                long executionTime = endTime - startTime;
                for (String move : solution) {
                    
                	int row1, col1, row2, col2;
                    String[] parts = move.split(" ");

	                // Extraire le mouvement (première partie)
	                String direction = parts[0];

	                // Extraire les coordonnées de la case vide d'origine (deuxième partie)
	                String[] startCoords = parts[1].split(",");
	                row1 = Integer.parseInt(startCoords[0]);
	                col1 = Integer.parseInt(startCoords[1]);

	                // Extraire les coordonnées de la case vide cible (troisième partie)
	                String[] endCoords = parts[2].split(",");
	                row2 = Integer.parseInt(endCoords[0]);
	                col2 = Integer.parseInt(endCoords[1]);
                    // Analyser le mouvement pour obtenir les coordonnées des cases
	                System.out.println("\nDéplacement : " + direction + " ("+ (row1+1) + "," + (col1+1) +") <=> ("+ (row2+1) + "," + (col2+1) +")\n");

                    // Effectuer le déplacement sur le plateau de jeu en utilisant la méthode swap()
                    board.swap2(row2, col2, row1, col1);
                    shot ++;
                    board.displayBoard();
	
	           }
               System.out.println("Temps d'execution du solveur: " + executionTime + " millisecondes");
	           break; // Sortir de la boucle while
            }
            
            
            List<int[]> adjacentEmptyBoxes = new ArrayList<>();

            for (int[] emptyBox : emptyBoxes) {
                int rowEmpty = emptyBox[0];
                int colEmpty = emptyBox[1];
                if (board.isAdjacent(row -1, col-1,rowEmpty,colEmpty)) {
                    adjacentEmptyBoxes.add(emptyBox); //si la case rentrée est adjacente à une case vide on l'ajoute
                }
            }

            if (adjacentEmptyBoxes.size() == 1) { // le cas classique où il n'y a qu'une seul case vide adjacente
                int rowEmpty=adjacentEmptyBoxes.get(0)[0];
                int colEmpty=adjacentEmptyBoxes.get(0)[1];
                if( board.swap(rowEmpty,colEmpty,row-1, col-1)) {
                	shot ++;
                }
               
            } else if (adjacentEmptyBoxes.size() >1) { // si il y en a plus 
                System.out.println("Il y a plusieurs cases vides adjacentes. Veuillez choisir une des cases vides suivantes :");
                for (int i = 0; i < adjacentEmptyBoxes.size(); i++) {
                    int[] emptyBox=adjacentEmptyBoxes.get(i);
                    int rowEmpty=emptyBox[0];
                    int colEmpty=emptyBox[1];
                    System.out.println((i +1)+ ") Coordonnées : ("+(rowEmpty + 1)+", "+(colEmpty +1)+")");
                }

                System.out.print("Entrez le numéro du choix de la case vide adjacente à utiliser : ");
                int choice = scanner.nextInt();

                // on vérifie si le choix rentré est valide
                if (choice >= 1 && choice <=adjacentEmptyBoxes.size()) {
                    int[] emptyBox = adjacentEmptyBoxes.get(choice - 1);
                    int rowEmpty1 = emptyBox[0];
                    int colEmpty1 = emptyBox[1];

                    if (row >= 1 && row <= board.getBoardSize() && col >= 1 && col <= board.getBoardSize()) {
                        if(board.swap(rowEmpty1, colEmpty1, row - 1, col - 1)) {//on fait l'échange 
                        	shot++; //Incrémentation seulement si le coup est valide
                        }
                    }else {
                        System.out.println("Coordonnées invalides. Veuillez réessayer.");
                    }
                }else {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }else{
                System.out.println("Aucune case vide adjacente. Veuillez réessayer.");
            }
            System.out.println("\nNombre de coups : " + shot + "\n"); //On affiche a chaque coup, valide ou non
        }

        // Fermer le scanner
        scanner.close();
        if(useSolver == false) { //Message de fin selon le type de résolution (utilisateur ou solver)
        	System.out.println("\n\nBravo vous avez gagné en "+ shot + " coups !");
        }else {
        	System.out.println("\n\nIl a fallu " + shot + " coups pour résoudre le taquin.");
        }
        
    }
}