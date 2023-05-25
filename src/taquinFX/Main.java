package taquinFX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Random;
import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.layout.GridPane;
import javafx.scene.paint.Color;
import javafx.stage.Stage;



public class Main extends Application{
    private GridPane boardPane;
	private Board board;
    private static final int TILE_SIZE = 50; // Taille d'une tuile

    public void start(Stage primaryStage) {
        Scanner scanner = new Scanner(System.in);
        String saveSelectionString = Save.select(scanner);
        String levelSelection = LevelSelector.select(scanner, "Saves/" + saveSelectionString);
        board = new Board("levels/" + levelSelection); // créer un nouveau tableau de taille n
        int maxCount = 1000000; // on définit le nombre maximum de tentatives
        int count = 0; // initialisation du compteur
        Button solverButton = new Button("Solveur du taquin");
        solverButton.setOnAction(event -> solver(primaryStage));
        primaryStage.setTitle("Taquin");
        // Créer une grille pour afficher le plateau
        boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(10);
        boardPane.setVgap(10);
        //boardPane.getChildren().add(solverButton);
        
        
        System.out.println("Votre meilleur score pour ce niveau est de : " + Save.getBestScore(saveSelectionString, levelSelection.replace(".csv", "")) + "\n");
        System.out.println();
        while(board.InitialPosition() || !board.solveWithinTime(50)) { // Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau. {
        	board = new Board("levels/" + levelSelection);
        	if (board.isEZ()) {
        		Random random = new Random();
        		int randomNumber = random.nextInt(2);
        		if(randomNumber == 0) {
        			System.out.println("Mélange automatique");
	        		board.mixBoardAuto();
					while (!board.isSolvable()) {					//si le plateau est "facile" alors on peut utiliser l'algo
						board.mixBoardAuto();
					}
        		}else {
        			System.out.println("Mélange manuel");
        			board.mixBoard(40);
        		}
        		
			}else {
				 board.mixBoard(40); // Sinon on mélange le plateau "manuellement"
			}
           
            count++;
            System.out.println("Tentative mélange n°" + count + "\n"); // Afficher le compteur de tentatives
            if (count == maxCount) {
                System.out.println("Impossible de mélanger le plateau sans revenir à sa position initiale");
                return;
            }
        } 
        displayBoard(board, primaryStage);
        // Créer une scène fixe
        Scene scene = new Scene(boardPane, 968, 544);
        primaryStage.setScene(scene);
        //primaryStage.setResizable(false); // Empêcher le redimensionnement de la fenêtre
        primaryStage.show();

    }

	 public static boolean gameSolved(short[] grid, String Csv_path) {
        try {
            BufferedReader reader = new BufferedReader(new FileReader(Csv_path));
            String line;
            int i = 0;
            int boardSize = (int) Math.sqrt(grid.length);

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");

                for (int j = 0; j < values.length; j++) {
                    if (grid[i * boardSize + j] != Integer.parseInt(values[j])) {
                        reader.close();
                        return false;
                    }
                }

                i++;
            }

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
	 

	 private void displayBoard(Board board, Stage primaryStage) {
	        boardPane.getChildren().clear();

	        
	            for (int i = 0; i < board.getBoardSize() * board.getBoardSize(); i++) {
	            	final int currentIndex = i;
	                Button button = new Button(board.getGrid()[i].getDisplay());
	                button.setPrefSize(TILE_SIZE, TILE_SIZE);
	                button.setOnAction(event -> handleButtonClick(currentIndex, primaryStage, board));
	                GridPane.setRowIndex(button, i/board.getBoardSize());
	                GridPane.setColumnIndex(button, i%board.getBoardSize());
	                if (board.getGrid()[i] instanceof Empty) {
	                    // Case vide
	                    button.setBackground(new Background(new BackgroundFill(Color.GREEN, CornerRadii.EMPTY, javafx.geometry.Insets.EMPTY)));
	                }
	                boardPane.getChildren().add(button);
	            }
	        }

	 private void handleButtonClick(int index1, Stage primaryStage, Board board) {
		    if (!(board.getGrid()[index1] instanceof Empty)) {
		        // Case non vide
		        List<int[]> emptyBoxes = board.findEmptyBoxes(); // on récupère les cases vides
		        List<int[]> adjacentEmptyBoxes = new ArrayList<>();

		        for (int[] emptyBox : emptyBoxes) {
		            int rowEmpty = emptyBox[0];
		            int colEmpty = emptyBox[1];
		            int index2 = rowEmpty * board.getBoardSize() + colEmpty;
		            if (board.isAdjacent(index1, index2)) {
		                adjacentEmptyBoxes.add(emptyBox); // si la case rentrée est adjacente à une case vide on l'ajoute
		            }
		        }

		        if (adjacentEmptyBoxes.size() > 0) {

		            // Stocker les coordonnées de la case non vide sélectionnée
		            int rowNonEmpty = index1 / board.getBoardSize();
		            int colNonEmpty = index1 % board.getBoardSize();

		            // Attente de la sélection du bouton vide par l'utilisateur
		            for (javafx.scene.Node node : primaryStage.getScene().getRoot().getChildrenUnmodifiable()) {
		                if (node instanceof Button) {
		                    Button button = (Button) node;
		                    button.setOnAction(event -> {
		                        Button clickedButton = (Button) event.getSource();
		                        // Vérifier si le bouton cliqué est un bouton vide adjacent
		                        for (int[] adjacentBox : adjacentEmptyBoxes) {
		                            int rowAdjacent = adjacentBox[0];
		                            int colAdjacent = adjacentBox[1];
		                            Button adjacentButton = getButtonAt(rowAdjacent, colAdjacent, boardPane);

		                            if (clickedButton == adjacentButton) {
		                                // Effectuer l'échange avec le bouton vide adjacent sélectionné
		                                if (board.swap(rowNonEmpty, colNonEmpty, rowAdjacent, colAdjacent)) {
		                                    // Actualiser l'affichage après l'échange
		                                    displayBoard(board, primaryStage);
		                                } else {
		                                    System.out.println("Mouvement invalide. Veuillez choisir une autre case vide adjacente.");
		                                }
		                            }
		                        }
		                    });
		                }
		            }
		        } else {
		            System.out.println("Aucune case vide adjacente. Veuillez choisir une autre case non vide.");
		        }
		    }
		}


	 private void solver(Stage primaryStage) {
		  System.out.println("\nVeuillez patienter, le solveur est en marche...\n");
		  long startTime = System.currentTimeMillis();
		  // Appeler le solveur pour résoudre le taquin
		  List<String> solution = this.board.solve();
		  long endTime = System.currentTimeMillis();
		  long executionTime = endTime - startTime;
		  
		  for (String move : solution) {
		      String[] parts = move.split(" ");

		      // Extraire le mouvement (première partie) pour l affichage si quelqun a la force de le faire
		      //String direction = parts[0];

		      // Extraire les coordonnées de la case vide d'origine (deuxième partie)
		      int index1 =  Integer.parseInt(parts[1]);

		      // Extraire les coordonnées de la case vide cible (troisième partie)
		      int index2 =  Integer.parseInt(parts[2]);
		      // Effectuer le déplacement sur le plateau de jeu en utilisant la méthode swap()
		      if (board.swap2(index1,index2)) {
		          displayBoard(board,primaryStage);
				}
		      
		  }
		  System.out.println("Temps d'exécution du solveur : " + executionTime + " millisecondes");
	}
	 private Button getButtonAt(int row, int col, GridPane gridPane) {
		    for (javafx.scene.Node node : gridPane.getChildren()) {
		        if (GridPane.getRowIndex(node) == row && GridPane.getColumnIndex(node) == col && node instanceof Button) {
		            return (Button) node;
		        }
		    }
		    return null; // Si aucun bouton ne correspond aux coordonnées spécifiées
		}


	 public static void main(String[] args) {
	        launch(args);
	    }
}

