import java.io.IOException;
import java.util.Random;
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
	
	//Méthode pour afficher le plateau
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
	
	public void  mixBoard() {
		Random rand = new Random(); //genere des nombres aleatoires
		for (int i = 0; i < boardSize; i++) { //parcours toutes les lignes et colonnes du plateau
		    for (int j = 0; j < boardSize; j++) {
			int newRow = rand.nextInt(boardSize);
			int newCol = rand.nextInt(boardSize);

			Box temp = grid[i][j]; //stock tmp la case actuelle dans une variable 
			if(temp.getValue() >= 0) {
				grid[i][j] = grid[newRow][newCol]; 
				grid[newRow][newCol] = temp;
			}
		    }
	    	}
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
	
	public void swap(int row, int row2, int col, int col2) {
		Box box1 = grid[row][col];
		Box box2 = grid[row2][col2];

		if (box1 instanceof Empty && box2 instanceof Box) {
		    if (isAdjacent(row, col, row2, col2)) {
			grid[row][col] = box2;
			grid[row2][col2] = box1;
		    } else {
			System.out.println("Échange invalide : les cases ne sont pas adjacentes.");
		    }
		} else {
		    System.out.println("Échange invalide : les cases ne satisfont pas les règles.");
		}
    	}

    private boolean isAdjacent(int row1, int col1, int row2, int col2) {
        return Math.abs(row1 - row2) + Math.abs(col1 - col2) == 1;
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

    public boolean gameSolved() {
        try {
        	//creer un bufferedReader pour lire le fichier 
            BufferedReader reader = new BufferedReader(new FileReader("levels/lvl1.csv"));
            String line;
            int i = 0;
            //lire le fichier ligne par ligne 
            while ((line = reader.readLine()) != null) {
                String[] values = line.split(","); // Séparer la ligne en valeurs en utilisant la virgule comme délimiteur.
                for (int j = 0; j < values.length; j++) {
                    // Si la valeur est -1, nous l'ignorons.
                    if(grid[i][j].getValue() == -1) {
                        continue;
                    }
                    // Convertir le string en int et comparer avec la valeur dans le tableau
                    if (grid[i][j].getValue() != Integer.parseInt(values[j])) {
                        reader.close();
                        return false;
                    }
                }
                i++; //augmenter l'index apres verification
            }
            reader.close();// on ferme 
            //si probleme une exception est levée (si le fichiner n'existe pas ou si il est vide)
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

}
