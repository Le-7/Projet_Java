import java.io.IOException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {
	private Box[][] grid;
	private int boardSize;
	private static final String CSV_PATH = "levels/lvl2.csv";
	
	//constructeur 
	public Board(int boardSize) {
		this.boardSize = boardSize;
		grid = new Box [boardSize][boardSize];
		initializeBoard();	
	}
	// Méthode pour initialiser le plateau de jeu
	private void initializeBoard() {
	    try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH))) { // On tente d'ouvrir le fichier CSV pour lecture
	        String line; // Variable pour stocker chaque ligne du fichier
	        int row = 0; // Variable pour suivre la ligne courante dans la grille
	        while ((line = reader.readLine()) != null) {  // Boucle qui lit chaque ligne du fichier jusqu'à ce qu'il n'y en ait plus
	            String[] values = line.split(",");// On sépare la ligne en plusieurs valeurs en utilisant la virgule comme séparateur
	            for (int col = 0; col < values.length; col++) { // Boucle sur chaque valeur de la ligne
	                int num = Integer.parseInt(values[col].trim()); // On convertit la valeur en nombre entier
	                grid[row][col] = new Box(num, num == 0, num == 0 ? "" : Integer.toString(num));  // On crée une nouvelle case (Box) avec cette valeur et on l'ajoute à la grille
	            }
	            row++;  // On incrémente le compteur de ligne pour passer à la ligne suivante
	        }
	    // Si une exception d'entrée/sortie se produit (par exemple si le fichier n'est pas trouvé), on l'affiche
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}   
	
//méthode pour melanger les cases
	public void  mixBoard() {
		Random rand = new Random(); //genere des nombres aleatoires
		 for (int i = 0; i < boardSize; i++) { //parcours toutes les lignes et colonnes du plateau
	            for (int j = 0; j < boardSize; j++) {
	                int newRow = rand.nextInt(boardSize);
	                int newCol = rand.nextInt(boardSize);

	                Box temp = grid[i][j]; //stock tmp la case actuelle dans une variable 
	                grid[i][j] = grid[newRow][newCol]; 
	                grid[newRow][newCol] = temp;
	            }
	        }
	    }
	// Méthode pour afficher le plateau de jeu
	public void displayBoard() {
	    for (int i = 0; i < boardSize; i++) {  // On boucle sur chaque ligne de la grille
	        for (int j = 0; j < boardSize; j++) { // On boucle sur chaque colonne de la ligne
	            System.out.print(grid[i][j].getDisplay() + " ");  // On affiche la représentation de la case
	        }
	        System.out.println(); // On passe à la ligne suivante dans la console
	    }
	}
}
