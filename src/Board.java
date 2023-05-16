package taquinFX;

import java.io.IOException;
import java.io.BufferedReader;
import java.io.FileReader;


public class Board {
	private Box[][] grid;
	private int boardSize;
	private static final String CSV_PATH = "levels/lvl1.csv";
	
	//constructeur 
	public Board(int boardSize) {
		this.boardSize = boardSize;
		grid = new Box [boardSize][boardSize];
		initializeBoard();	
	}
	//nous allons initialiser le plateau
	private void initializeBoard() {
		 try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH))) {
	            String line;
	            int row = 0;
	            while ((line = reader.readLine()) != null) {
	                String[] values = line.split(",");
	                for (int col = 0; col < values.length; col++) {
	                    int num = Integer.parseInt(values[col].trim());
	                    grid[row][col] = new Box(num, num == 0, num == 0 ? "" : Integer.toString(num));
	                }
	                row++;
	            }
	        } catch (IOException e) {
	            e.printStackTrace();
	        }
	    }
	
//Méthode pour afficher le plateau
public void displayBoard() {
    for (int i = 0; i < boardSize; i++) {
        for (int j = 0; j < boardSize; j++) {
            System.out.print(grid[i][j].getDisplay() + " ");
        }
        System.out.println();
    }
}

public static void main(String[] args) {
    // Créer un nouveau plateau de taille 3
    Board board = new Board(3);

    // Afficher le plateau
    board.displayBoard();
}

}