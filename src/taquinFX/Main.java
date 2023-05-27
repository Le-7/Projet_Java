package taquinFX;


import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Random;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;



public class Main extends Application{
    private GridPane boardPane;
	private Board board;
    private static final int TILE_SIZE = 50; // Taille d'une tuile
    private Label errorLabel; // Label pour afficher les erreurs
    int score = 0;
    private Label timerLabel;
    private Timeline timeline;
    private int elapsedTimeInSeconds = 0;

    public void start(Stage primaryStage) {
        Scanner scanner = new Scanner(System.in);
        String saveSelectionString = Save.select(scanner);
        String levelSelection = LevelSelector.select(scanner, "Saves/" + saveSelectionString);
        board = new Board("levels/" + levelSelection); // créer un nouveau tableau de taille n
        int maxCount = 1000000; // on définit le nombre maximum de tentatives
        int count = 0; // initialisation du compteur
        errorLabel = new Label();
        errorLabel.setVisible(false);
        Button solverButton = new Button("Solveur du taquin");
        solverButton.setOnAction(event -> solver(primaryStage));
        primaryStage.setTitle("Taquin");
        // Créer une grille pour afficher le plateau
        boardPane = new GridPane();
        boardPane.setAlignment(Pos.CENTER);
        boardPane.setHgap(10);
        boardPane.setVgap(10);
        VBox game = new VBox(); // Utilisation d'un VBox comme conteneur du jeu et du errorLabel
        game.setAlignment(Pos.CENTER);
        game.setSpacing(10);
        game.getChildren().add(errorLabel);
        game.getChildren().add(boardPane); // Ajouter le boardPane (taquin) au VBox
        game.getChildren().add(solverButton); // Ajouter le solverButton en dessous du taquin
        HBox root = new HBox(); //conteneur principal
        // Création des composants
        //ImageView clockImage = new ImageView(new Image("clock.png"));
        Label movesLabel = new Label("Moves: 0");
        //ImageView customImage = new ImageView(new Image("custom.png"));
        VBox textPane = new VBox(10);
        textPane.setPadding(new Insets(10));
        initTimer();
        textPane.getChildren().addAll(timerLabel,movesLabel);
        root.getChildren().addAll(textPane,game);
        
        List<String> solution = new ArrayList<>();
        
        Random random = new Random();
        int randomNumber = random.nextInt(2);
        
        errorLabel.setText("Votre meilleur score pour ce niveau est de : " + Save.getBestScore(saveSelectionString, levelSelection.replace(".csv", "")) + "\n\n");
        
        while(board.InitialPosition() || !board.solveWithinTime(50,solution)) { // Vérifier si une ou plusieurs tuiles sont à leur position initiale. Si c'est le cas, mélanger à nouveau. {
        	board = new Board("levels/" + levelSelection);
        	if (board.isEZ()) {
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
            errorLabel.setText(errorLabel.getText() + "Tentative mélange n°" + count + "\n"); // Afficher le compteur de tentatives
            if (count == maxCount) {
            	errorLabel.setText("Impossible de mélanger le plateau sans revenir à sa position initiale");
                errorLabel.setVisible(true);
                return;
            }
        } 
        displayBoard(board, primaryStage);
        // Créer une scène fixe
        Scene scene = new Scene(root, 968, 544);
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
                if(board.getGrid()[i] instanceof Empty) {
                	button.setText("");
                	button.setStyle("-fx-background-color : Lightblue;");
                }
                if(board.getGrid()[i] instanceof Block) {
                	button.setVisible(false);
                }
                GridPane.setRowIndex(button, i/board.getBoardSize());
                GridPane.setColumnIndex(button, i%board.getBoardSize());
              
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
		        if (adjacentEmptyBoxes.size() == 1) {
		            int rowEmpty = adjacentEmptyBoxes.get(0)[0];
		            int colEmpty = adjacentEmptyBoxes.get(0)[1];

		            // Effectuer l'échange entre la case non vide et la case vide sélectionnée aléatoirement
		            int rowNonEmpty = index1 / board.getBoardSize();
		            int colNonEmpty = index1 % board.getBoardSize();
		            if (board.swap(rowNonEmpty, colNonEmpty, rowEmpty, colEmpty)) {
		                // Actualiser l'affichage après l'échange
		                displayBoard(board, primaryStage);
		                errorLabel.setVisible(false); // Rendre errorLabel non visible
		            } else {
		            	errorLabel.setText("Mouvement invalide. Veuillez choisir une autre case vide adjacente.");
	                    errorLabel.setVisible(true);
		            }
		        } else if (adjacentEmptyBoxes.size() > 1) {
		            Alert alert = new Alert(AlertType.INFORMATION);
		            alert.setTitle("Choix de la case vide adjacente");
		            alert.setHeaderText("Il y a plusieurs cases vides adjacentes.");
		            alert.setContentText("Veuillez choisir une des cases vides suivantes :");
		        
		            alert.setX(primaryStage.getX());
		            alert.setY(primaryStage.getY());
		            String[] options = new String[adjacentEmptyBoxes.size()];
		            for (int i = 0; i < adjacentEmptyBoxes.size(); i++) {
		                int[] emptyBox = adjacentEmptyBoxes.get(i);
		                int rowEmpty = emptyBox[0];
		                int colEmpty = emptyBox[1];
		                
		                int index = rowEmpty * board.getBoardSize() + colEmpty;
		                options[i] = "Case "+ (char)(65+i);
		                Button button = (Button) boardPane.getChildren().get(index);
		                button.setText(""+(char)(i+65));
		            }

		            alert.getButtonTypes().clear();
		            for (String option : options) {
		                alert.getButtonTypes().add(new ButtonType(option));
		            }
		            alert.getButtonTypes().add(ButtonType.CANCEL);

		            Optional<ButtonType> result = alert.showAndWait();
		            if (result.isPresent() && result.get() != ButtonType.CANCEL) {
		                int choice = alert.getButtonTypes().indexOf(result.get());
		                int[] emptyBox = emptyBoxes.get(choice);
		                int rowEmpty1 = emptyBox[0];
		                int colEmpty1 = emptyBox[1];

		                int rowNonEmpty = index1 / board.getBoardSize();
		                int colNonEmpty = index1 % board.getBoardSize();

		                if (board.swap(rowNonEmpty, colNonEmpty, rowEmpty1, colEmpty1)) { // on fait l'échange
		                    displayBoard(board, primaryStage); // Actualiser l'affichage après l'échange
		                } else {
		                	errorLabel.setText("Mouvement invalide. Veuillez choisir une case vide adjacente.");
	                        errorLabel.setVisible(true);
		                }
		            } else {
		            	errorLabel.setText("Aucun choix effectué. Veuillez réessayer.");
		            	errorLabel.setVisible(true);
		            }
		        } else {
		        	errorLabel.setText("Aucune case vide adjacente. Veuillez choisir une autre case non vide.");
	                errorLabel.setVisible(true);
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
		  errorLabel.setText("Temps d'exécution du solveur : " + executionTime + " millisecondes");
		  errorLabel.setVisible(true);
	}
	 private void updateTimerLabel() {
	        int hours = elapsedTimeInSeconds / 3600;
	        int minutes = (elapsedTimeInSeconds % 3600) / 60;
	        int seconds = elapsedTimeInSeconds % 60;
	        String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
	        timerLabel.setText(time);
	 }
	 
	 private void initTimer() {
	        timerLabel = new Label("00:00:00");
	        timeline = new Timeline();
	        timeline.setCycleCount(Animation.INDEFINITE);
	        KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
	            elapsedTimeInSeconds++;
	            updateTimerLabel();
	        });
	        timeline.getKeyFrames().add(keyFrame);
	 }

	 public static void main(String[] args) {
	        launch(args);
	    }
}