package taquinFX;

import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("levels/lvl2.csv"); // créer un nouveau tableau de taille n
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

            // Obtenir les coordonnées de la case vide
            int[] emptyBox = board.findEmptyBox();
            int rowEmpty = emptyBox[0];
            int colEmpty = emptyBox[1];
            // Demander à l'utilisateur de saisir les coordonnées
            System.out.print("Entrez les coordonnées de la case à déplacer (ligne colonne, pour le solveur entrez 0 0): ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            // Vérifier si les coordonnées sont égales à 0
            if (row == 0 && col == 0) {
                // Appeler le solveur pour résoudre le taquin
                TaquinSolver solver = new TaquinSolver(board);
                List<String> solution = solver.solve();
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
            }

            // Effectuer l'échange si les coordonnées sont valides
            if (row >= 1 && row <= board.getBoardSize() && col >= 1 && col <= board.getBoardSize()) {
                board.swap(rowEmpty, colEmpty, row - 1, col - 1);
            } else {
                System.out.println("Coordonnées invalides. Veuillez réessayer.");
            }
        }

        // Fermer le scanner
        scanner.close();
        System.out.println("\n\nBravo vous avez gagné !");
    }
}
