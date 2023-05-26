package taquinFX;
//Importations nécessaires pour le fonctionnement du code
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
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

//Classe principale GameBoard qui hérite de l'Application JavaFX
public class GameBoard extends Application {
 // Variables de classe
 private String levelSelection; // Sélection du niveau
 private String savesString; // String pour les sauvegardes
 private GridPane boardPane; // Panneau pour le plateau de jeu
 private Board board; // Le plateau de jeu
 private static final int TILE_SIZE = 50; // Taille d'une tuile
 private Label errorLabel; // Label pour afficher les erreurs
 int score = 0; // Score actuel
 private Label timerLabel; // Label pour le timer
 private Timeline timeline; // Chronologie pour le timer
 private Label movesLabel;
 private int elapsedTimeInSeconds = 0; // Temps écoulé en secondes

 // Constructeur de GameBoard
 public GameBoard(String levelSelection, String savesString) {
     super(); // Appel du constructeur de la superclasse Application
     this.levelSelection = levelSelection; // Initialisation de la sélection du niveau
     this.savesString = "Saves/" + savesString; // Initialisation du string pour les sauvegardes
 }

 // Méthode principale pour démarrer l'affichage
 public void start(Stage primaryStage) {
     showGame(primaryStage); // Afficher le jeu
 }
 
 public Scene showGame(Stage primaryStage) {
	    board = new Board("levels/" + levelSelection); // Initialiser le plateau avec le niveau sélectionné
	    int maxCount = 1000000;// Maximum d'essais pour le mélange du plateau
	    int count = 0;// Initialiser un compteur pour les tentatives de mélange
	    // Label pour afficher les erreurs
	    errorLabel = new Label();
	    errorLabel.setVisible(false);

	    // Bouton pour lancer le solveur
	    Button solverButton = new Button("Solveur du taquin");
	    solverButton.setOnAction(event -> solver(primaryStage));
	    
	    primaryStage.setTitle("Taquin");// Titre de la fenêtre
	    // Création de la grille pour afficher le plateau
	    boardPane = new GridPane();
	    boardPane.setAlignment(Pos.CENTER);
	    boardPane.setHgap(10);
	    boardPane.setVgap(10);
	    
	    // Conteneur pour le jeu et le label d'erreur
	    VBox game = new VBox();
	    game.setAlignment(Pos.CENTER);
	    game.setSpacing(10);
	    game.getChildren().add(errorLabel);
	    game.getChildren().add(boardPane);
	    game.getChildren().add(solverButton);
	    
	    HBox root = new HBox(); // Conteneur principal
	    
	    // Création des labels pour les mouvements et le timer
	    movesLabel = new Label("Moves: "+score);
	    VBox textPane = new VBox(10);
	    textPane.setPadding(new Insets(10));
	    initTimer();
	    textPane.getChildren().addAll(timerLabel,movesLabel);
	    root.getChildren().addAll(textPane,game);
	    
	    List<String> solution = new ArrayList<>(); // Liste pour stocker la solution
	    
	    // Générateur de nombres aléatoires pour le mélange du plateau
	    Random random = new Random();
	    int randomNumber = random.nextInt(2);
	    
	    // Afficher le meilleur score pour ce niveau
	    errorLabel.setText("Votre meilleur score pour ce niveau est de : " + Menu.getBestScore(savesString, "levels/" + levelSelection)+ "\n\n");
	    errorLabel.setVisible(true);

	    // Tenter de mélanger le plateau jusqu'à ce qu'il ne soit plus à sa position initiale
	    while(board.InitialPosition() || !board.solveWithinTime(50,solution)) {
	        board = new Board("levels/" + levelSelection);
	        if (board.isEZ()) {
	            if(randomNumber == 0) {
	                System.out.println("Mélange automatique");
	                board.mixBoardAuto();
	                while (!board.isSolvable()) {
	                    board.mixBoardAuto();
	                }
	            } else {
	                System.out.println("Mélange manuel");
	                board.mixBoard(40);
	            }
	        } else {
	            board.mixBoard(40);
	        }
	        
	        // Augmenter le compteur d'essais
	        count++;
	        
	        // Afficher un message d'erreur si le maximum d'essais est atteint
	        if (count == maxCount) {
	            errorLabel.setText("Impossible de mélanger le plateau sans revenir à sa position initiale");
	            errorLabel.setVisible(true);
	        }
	    } 
	    displayBoard(board, primaryStage);// Afficher le plateau
	    timeline.play(); // Démarrer le timer
	    Scene scene = new Scene(root, 968, 544); // Créer une scène fixe
	    // Appliquer la scène à la fenêtre principale
	    primaryStage.setScene(scene);
	    primaryStage.show();
	    
	    return scene;
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
		  if (gameSolved(IDASolver.convertBoxArrayToShortArray(board.getGrid()), "levels/"+levelSelection)) {
        	 timeline.stop();
        	 for (javafx.scene.Node node : boardPane.getChildren()) {
                 if (node instanceof Button) {
                     Button button = (Button) node;
                     button.setDisable(true);
                 }
             }
        	 Menu.updateScoreAndAccessibility(savesString, levelSelection, shot);

        	 
             // Affichage de l'alerte
             Alert alert = new Alert(AlertType.INFORMATION);
             alert.setTitle("Félicitations !");
             alert.setHeaderText(null);
             alert.setContentText("Vous avez gagné en :"+elapsedTimeInSeconds+" secondes et en :"+shot+" coups !");
             alert.getDialogPane().getButtonTypes().clear();
             alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
             alert.setOnCloseRequest(e -> {
                 // Code pour retourner à la carte
                 System.out.println("Retour à la carte");
             });

             alert.showAndWait();
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
		                score++;
		                movesLabel.setText("Moves: "+score);
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
		                	score++;
			                movesLabel.setText("Moves: "+score);
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
	  
}
