package taquinFX;

public class Main {
    public static void main(String[] args) {
        Board board = new Board("levels/lvl1.csv"); // créer un nouveau tableau de taille n
        int maxCompteur = 5;//on definit le nombre max de tentative 
        int compteur = 0; //initialisation du compteur
        
        board.displayBoard(); // Afficher le plateau avant le mélange
        System.out.println( );
        
        do {
            board.mixBoard(10); // Mélanger le plateau
            compteur++;
            System.out.println("Tentative mélange n°" + compteur); // Afficher le compteur de tentatives
            if (compteur == maxCompteur) {
            	System.out.println("impossible de mélanger le plateau sans revenir a sa position initial");
            	return ;
            }
        } while(board.InitialPosition()); //Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau.

        board.displayBoard(); // Afficher le plateau après le mélange
    }
}
