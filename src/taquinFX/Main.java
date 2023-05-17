package taquinFX;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("../levels/block.csv"); // créer un nouveau tableau de taille n
        int maxCompteur = 5; // on définit le nombre maximum de tentatives
        int compteur = 0; // initialisation du compteur

        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println();

        do {
            board.mixBoard(1000); // Mélanger le plateau
            compteur++;
            System.out.println("Tentative mélange n°" + compteur); // Afficher le compteur de tentatives
            if (compteur == maxCompteur) {
                System.out.println("Impossible de mélanger le plateau sans revenir à sa position initiale");
                return;
            }
        } while (board.InitialPosition()); // Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau.

        Scanner scanner = new Scanner(System.in);

        while (!board.gameSolved()) {
            board.displayBoard(); // Afficher le plateau après le mélange

            // Demander à l'utilisateur de saisir les coordonnées
            System.out.print("Entrez les coordonnées de la case à déplacer (ligne colonne, pour le solveur entrez 0 0): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();
            
            List<int[]> emptyBoxes = board.findEmptyBoxes(); // on récupère les cases vides
            
            
            // Vérifier si les coordonnées sont égales à 0
            if (row == 0 && col == 0) {
            	if(emptyBoxes.size() == 1) {  // POUR L'INSTANT LE SOLVEUR NE FONCTIONNE QU'AVEC UNE SEULE CASE VIDE 
	                // Appeler le solveur pour résoudre le taquin
	                List<String> solution = board.solve();
	                for (String move : solution) {
	                    System.out.println("Déplacement : " + move + "\n\n");
	                    int row1, col1, row2, col2;
	                    int[] emptyBoxSolver = board.findEmptyBox();
	                    int rowEmptySolver = emptyBoxSolver[0];
	                    int colEmptySolver = emptyBoxSolver[1];
	                    // Analyser le mouvement pour obtenir les coordonnées des cases
	                    if (move.equals("UP")) {
	                        row1 = rowEmptySolver - 1;
	                        col1 = colEmptySolver;
	                        row2 = rowEmptySolver;
	                        col2 = colEmptySolver;
	                    } else if (move.equals("DOWN")) {
	                        row1 = rowEmptySolver + 1;
	                        col1 = colEmptySolver;
	                        row2 = rowEmptySolver;
	                        col2 = colEmptySolver;
	                    } else if (move.equals("LEFT")) {
	                        row1 = rowEmptySolver;
	                        col1 = colEmptySolver - 1;
	                        row2 = rowEmptySolver;
	                        col2 = colEmptySolver;
	                    } else if (move.equals("RIGHT")) {
	                        row1 = rowEmptySolver;
	                        col1 = colEmptySolver + 1;
	                        row2 = rowEmptySolver;
	                        col2 = colEmptySolver;
	                    } else {
	                        // Mouvement invalide, ignorer
	                        continue;
	                    }
	
	                    // Effectuer le déplacement sur le plateau de jeu en utilisant la méthode swap()
	                    board.swap2(row2, col2, row1, col1);
	                    board.displayBoard();
	
	                }
	                break; // Sortir de la boucle while
            	}else {
            		System.out.println("Désolé mais le solveur ne fonctionne pas encore pour les plateaux avec plus d'une case vide, vous pouvez réessayez avec la méthode classique.");
            		break;
            	}
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
                board.swap(rowEmpty,colEmpty,row-1, col-1);
            } else if (adjacentEmptyBoxes.size() >1) { // si il y en a plus 
                System.out.println("Il y a plusieurs cases vides adjacentes. Veuillez choisir une des cases vides suivantes :");
                for (int i = 0; i < adjacentEmptyBoxes.size(); i++) {
                    int[] emptyBox=adjacentEmptyBoxes.get(i);
                    int rowEmpty=emptyBox[0];
                    int colEmpty=emptyBox[1];
                    System.out.println((i +1)+ ") Coordonnées : ("+(rowEmpty + 1)+", "+(colEmpty +1)+")");
                }

                System.out.print("Entrez le numéro de la case vide adjacente à utiliser : ");
                int choice = scanner.nextInt();

                // on vérifie si le choix rentré est valide
                if (choice >= 1 && choice <=adjacentEmptyBoxes.size()) {
                    int[] emptyBox = adjacentEmptyBoxes.get(choice - 1);
                    int rowEmpty1 = emptyBox[0];
                    int colEmpty1 = emptyBox[1];

                    if (row >= 1 && row <= board.getBoardSize() && col >= 1 && col <= board.getBoardSize()) {
                        board.swap(rowEmpty1, colEmpty1, row - 1, col - 1);  //on fait l'échange 
                    }else {
                        System.out.println("Coordonnées invalides. Veuillez réessayer.");
                    }
                }else {
                    System.out.println("Choix invalide. Veuillez réessayer.");
                }
            }else{
                System.out.println("Aucune case vide adjacente. Veuillez réessayer.");
            }
        }

        // Fermer le scanner
        scanner.close();
        System.out.println("\n\nBravo vous avez gagné !");
    }
}
