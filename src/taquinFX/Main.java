package taquinFX;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("levels/lvl1.csv"); // créer un nouveau tableau de taille n
        int maxCompteur = 5; // on définit le nombre maximum de tentatives
        int compteur = 0; // initialisation du compteur

        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println();

        do {
            board.mixBoard(100); // Mélanger le plateau
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
            System.out.print("Entrez les coordonnées de la case à déplacer (ligne colonne) : ");
            int row = scanner.nextInt();
            int col = scanner.nextInt();

            // Effectuer l'échange si les coordonnées sont valides
            if (row >= 0 && row <= board.getBoardSize() && col >= 0 && col <= board.getBoardSize()) {
                board.swap(rowEmpty, colEmpty, row-1, col-1);
            } else {
                System.out.println("Coordonnées invalides. Veuillez réessayer.");
            }
        }

        // Fermer le scanner
        scanner.close();
    }
}
