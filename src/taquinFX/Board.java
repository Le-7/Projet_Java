package taquinFX;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.io.BufferedReader;
import java.io.FileReader;

public class Board {
	private Box[] grid;
	private int boardSize;
	private String Csv_path = "";
	
	// Constructeur
	public Board(String Csv_path) {
		this.Csv_path = Csv_path;
		initializeBoard();	
	}
	
	public Board(Board other) {
	    this.boardSize = other.getBoardSize();
	    this.Csv_path = other.getCsvPath();
	    this.grid = new Box[boardSize * boardSize];

	    for (int i = 0; i < boardSize * boardSize; i++) {
	        Box box = other.getGrid()[i];
	        if (box instanceof Empty) {
	            grid[i] = new Empty();
	        } else if (box instanceof Block) {
	            grid[i] = new Block();
	        } else if (box instanceof Box) {
	            Box numberedBox = (Box) box;
	            grid[i] = new Box(numberedBox.getValue(), numberedBox.isMovable(), numberedBox.getDisplay());
	        }
	    }
	}

	public Box[] getGrid() {
		return grid;
	}
	
	public String getCsvPath() {
		return Csv_path;
	}
	
	public int getBoardSize() {
		return boardSize;
	}

	// Nous allons initialiser le plateau
	private void initializeBoard() {
	    try (BufferedReader reader = new BufferedReader(new FileReader(Csv_path))) {
	        String line;
	        int row = 0;
	        int number_elements = 0;
	        while ((line = reader.readLine()) != null) {
	            String[] values = line.split(",");
	            
	            if (row == 0) {
	                number_elements = values.length;
	                boardSize = number_elements;
	                grid = new Box[number_elements * number_elements];
	            }
	            
	            for (int col = 0; col < values.length; col++) {
	                int num = Integer.parseInt(values[col].trim());
	                int index = row * number_elements + col;
	                
	                if (num > 0) {
	                    grid[index] = new Box(num, true, "" + num + "");
	                } else if (num == 0) {
	                    grid[index] = new Empty();
	                } else {
	                    grid[index] = new Block();
	                }
	            }
	            
	            row++;
	        }
	        
	        reader.close();
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}

	
	// Méthode pour afficher le plateau
	public void displayBoard() {
        int maxLength = getMaxValueLength();
        
	    for (int i = 0; i < boardSize; i++) {
	        for (int j = 0; j < boardSize; j++) {
	        	int index = i * boardSize + j;
	        	String formattedValue = String.format("%-" + maxLength + "s", grid[index].getDisplay());
                System.out.print(formattedValue + " ");
	        }
	        
	        System.out.println();
	    }
	    
	    System.out.println();
	}


	
	public void mixBoardAuto() {
		Random rand = new Random();
		
		for (int i = 0; i < boardSize * boardSize; i++) {
		    int newIndex = rand.nextInt(boardSize * boardSize);

		    Box temp = grid[i];
		    
		    if (temp.isMovable() && grid[newIndex].isMovable()) {
		    	grid[i] = grid[newIndex];
		    	grid[newIndex] = temp;
		    }
		}
	}
	
	public void mixBoard(int numMoves) {
	    Random rand = new Random();

	    List<int[]> emptyBoxCoordinates = findEmptyBoxes();

	   while(numMoves != 0) {
	        // Sélectionner une case vide aléatoire parmi les coordonnées disponibles
	        int randomIndex = rand.nextInt(emptyBoxCoordinates.size());
	        int[] emptyBox = emptyBoxCoordinates.get(randomIndex);
	        int emptyRow = emptyBox[0];
	        int emptyCol = emptyBox[1];
	        int emptyIndex = emptyBox[0] * boardSize + emptyBox[1];

	        // Générer un nombre aléatoire pour choisir le mouvement
	        int move = rand.nextInt(4); // 0: haut, 1: bas, 2: gauche, 3: droite

	        // Vérifier si le mouvement est valide
	        boolean validMove = false;
	        int newRow = emptyRow;
	        int newCol = emptyCol;

	        if (move == 0 && emptyRow > 0) { // Mouvement vers le haut
	            newRow = emptyRow - 1;
	            validMove = true;
	        } else if (move == 1 && emptyRow < boardSize - 1) { // Mouvement vers le bas
	            newRow = emptyRow + 1;
	            validMove = true;
	        } else if (move == 2 && emptyCol > 0) { // Mouvement vers la gauche
	            newCol = emptyCol - 1;
	            validMove = true;
	        } else if (move == 3 && emptyCol < boardSize - 1) { // Mouvement vers la droite
	            newCol = emptyCol + 1;
	            validMove = true;
	        }

	        // Effectuer le mouvement
	        if (validMove) {
	            if(swap2(emptyIndex, newRow * boardSize + newCol)) {
	            	numMoves--;
	            	emptyBox[0] = newRow;
	                emptyBox[1] = newCol;
	            }
	            
	        }
	    }
	}



	private int getMaxValueLength() {
		int maxLength = 0;

		for (int i = 0; i < boardSize * boardSize; i++) {
			int value = grid[i].getValue();
			int valueLength = String.valueOf(value).length();
			maxLength = Math.max(maxLength, valueLength);
		}
		
		return maxLength;
	}
	
	
	
	public List<int[]> findEmptyBoxes() {
		List<int[]> emptyBoxes = new ArrayList<>();
	    
	    for (int i = 0; i < boardSize * boardSize; i++) {
	        if (grid[i].getValue() == 0) {
	            emptyBoxes.add(new int[] {i / boardSize, i % boardSize});
	        }
	    }
	    
	    return emptyBoxes;
	}

	public boolean swap(int row, int col, int row2, int col2) {
		int index1 = row * boardSize + col;
		int index2 = row2 * boardSize + col2;
		
		Box box1 = grid[index1];
		Box box2 = grid[index2];

		if ((box1 instanceof Empty || box2 instanceof Empty) && box2.isMovable() && box1.isMovable()) {
		    if (isAdjacent(index1, index2)) {
				grid[index1] = box2;
				grid[index2] = box1;
				return true;
		    } else {
		    	System.out.println("Échange invalide : les cases ne sont pas adjacentes.");
		    } 
		} else {
			System.out.println("Échange invalide : les cases ne satisfont pas les règles.");
		}
		
		return false;
    }
	
	public boolean swap2(int index1, int index2) {
		
		Box box1 = grid[index1];
		Box box2 = grid[index2];

		if ((box1 instanceof Empty || box2 instanceof Empty) && box2.isMovable() && box1.isMovable()) {
		    if (isAdjacent(index1, index2)) {
				grid[index1] = box2;
				grid[index2] = box1;
				return true;
		    } 
		}
		return false;
    }
	
	protected boolean isAdjacent(int index1, int index2) {
	    int row1 = index1 / boardSize;
	    int col1 = index1 % boardSize;
	    int row2 = index2 / boardSize;
	    int col2 = index2 % boardSize;

	    int rowDiff = Math.abs(row1 - row2);
	    int colDiff = Math.abs(col1 - col2);

	    // Vérifier si les cases sont adjacentes horizontalement ou verticalement
	    if ((rowDiff == 1 && colDiff == 0) || (rowDiff == 0 && colDiff == 1)) {
	        return true;
	    }

	    return false;
	}

	
	public boolean InitialPosition() {
        try {
            List<Short> gridValues = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(this.getCsvPath()));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    gridValues.add(Short.parseShort(value));
                }
            }

            reader.close();

            for (int i = 0; i < gridValues.size(); i++) {
            	if (gridValues.get(i) != -1 && this.grid[i].getValue() == gridValues.get(i)) {
					return true;
				}
            }

        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
		return false;
    }
	
	public List<String> solve() {
        IDASolver solver = new IDASolver(this,this.getCsvPath());
        return solver.solve();
    }
	public static List<short[]> findEmptyBoxes(short[] grid) {
        List<short[]> emptyBoxes = new ArrayList<>();
        int boardSize = (int) Math.sqrt(grid.length);
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0) {
                int row = i / boardSize;
                int col = i % boardSize;
                emptyBoxes.add(new short[]{(short) row, (short) col});
            }
        }
        return emptyBoxes;
    }
}
