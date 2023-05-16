package tanquinFX
public class Main {
    public static void main(String[] args) {
        Board board = new Board("levels/lvl1.csv"); // créer un nouveau tableau de taille n
        int maxCompteur = 5;//on definit le nombre max de tentative 
        int compteur = 0; //initialisation du compteur
        
        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println( );
        
        do {
            board.mixBoardauto(); // Mélanger le plateau
            compteur++;
            System.out.println("Tentative mélange n°" + compteur); // Afficher le compteur de tentatives
            if (compteur == maxCompteur) {
            	System.out.println("impossible de mélanger le plateau sans revenir a sa position initial");
            	return ;
            }
        } while(board.InitialPosition()); //Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau.

        board.displayBoard(); // Afficher le plateau après le mélange
        // Vérifier si le jeu est résolu
        if (board.gameSolved()) {
            System.out.println("Félicitations, le jeu est résolu!");
        } else {
            System.out.println("Le jeu n'est pas encore résolu. Continuez à essayer!");
        }
    }
}
