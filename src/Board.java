import java.io.IOException;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {
	private Box[][] grid;
	private int boardSize;
	private static final String CSV_PATH = "levels/test_cas.csv";
	
	//constructeur 
	public Board(int boardSize) {
		this.boardSize = boardSize;
		grid = new Box [boardSize][boardSize];
		initializeBoard();	
	}
	// Méthode pour initialiser le plateau de jeu
	private void initializeBoard() {
		 try (BufferedReader reader = new BufferedReader(new FileReader(CSV_PATH))) {
	            String line;
	            int row = 0;
	            while ((line = reader.readLine()) != null) {
	                String[] values = line.split(",");
	                for (int col = 0; col < values.length; col++) {
	                    int num = Integer.parseInt(values[col].trim());
	                    if(num > 0) {
	                    	grid[row][col] = new Box(num, true, ""+num+"");
	                    }else if(num == 0) {
	                    	grid[row][col] = new Empty(num, true, "");
	                    }else {
	                    	grid[row][col] = new Block(num, false, "");
	                    }
	                }
	                row++;
	            }
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
// on va verifier si le plateau est dans sa position initiale 
	boolean InitialPosition() {
	    for (int i=0; i< boardSize; i++) {
	        for (int j=0; j< boardSize; j++) {
	            if(grid[i][j].getValue() != i*boardSize + j + 1) {
	                return false;
	            }
	        }
	    }
	    return true;
	}

	// Méthode pour afficher le plateau de jeu
	public void displayBoard() {
		int maxLength = getMaxValueLength();
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				String formattedValue = String.format("%-" + maxLength + "s", grid[i][j].getDisplay());
				System.out.print(formattedValue + " ");
			}
			System.out.println();
		}
		System.out.println();
	}
	
	private int getMaxValueLength() {
		int maxLength = 0;
		for (int i = 0; i < boardSize; i++) {
			for (int j = 0; j < boardSize; j++) {
				int value = grid[i][j].getValue();
				int valueLength = String.valueOf(value).length();
				maxLength = Math.max(maxLength, valueLength);
			}
		}
		return maxLength;
	   }
	
}
