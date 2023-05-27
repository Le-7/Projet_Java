package taquinFX;
//Importations nécessaires pour le fonctionnement du code
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.atomic.AtomicInteger;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.PauseTransition;
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
 private VBox game;
 private Button previousButton;
 private Button nextButton;
 private int currentStep;
 
 
 // Constructeur de GameBoard
 public GameBoard(String levelSelection, String savesString) {
     super(); // Appel du constructeur de la superclasse Application
     this.levelSelection = levelSelection; // Initialisation de la sélection du niveau
     this.savesString = "../Saves/" + savesString; // Initialisation du string pour les sauvegardes
 }

 // Méthode principale pour démarrer l'affichage
 public void start(Stage primaryStage) {
     showGame(primaryStage); // Afficher le jeu
 }
 
 public Scene showGame(Stage primaryStage) {
	 	board = new Board("../levels/" + levelSelection+".csv"); // Initialiser le plateau avec le niveau sélectionné
	    int maxCount = 1000000;// Maximum d'essais pour le mélange du plateau
	    AtomicInteger count = new AtomicInteger(0);// Initialiser un compteur pour les tentatives de mélange
	    // Label pour afficher les erreurs
	    errorLabel = new Label();
	    errorLabel.setVisible(false);

	    // Bouton pour lancer le solveur
	    Button solverButton = new Button("Solveur du taquin");
	    solverButton.setId("solverButton");
	    solverButton.setOnAction(event -> solverHandle(primaryStage));
	    
        Button quitBtn = new Button("Quitter le jeu");
        quitBtn.setMinWidth(100);
        quitBtn.setMaxWidth(200);
        quitBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 15; -fx-font-size: 16px;");
        quitBtn.setOnAction(e -> {
            primaryStage.close();
        });
        
	    primaryStage.setTitle("Taquin");// Titre de la fenêtre
	    // Création de la grille pour afficher le plateau
	    boardPane = new GridPane();
	    boardPane.setAlignment(Pos.CENTER);
	    boardPane.setHgap(10);
	    boardPane.setVgap(10);
	    
	    Button returnToMapButton = new Button("Retour à la carte");
        returnToMapButton.setMinWidth(100);
        returnToMapButton.setMaxWidth(200);
        returnToMapButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5); -fx-background-radius: 15; -fx-font-size: 16px;");
        returnToMapButton.setOnAction(e -> {
        	// Extraire le nom du fichier
        	
        	String fileName = savesString.substring(savesString.lastIndexOf("/") + 1);
            Map map = new Map(fileName.replace(".csv", ""));
            map.showMap(primaryStage);
        });
        
	    // Conteneur pour le jeu et le label d'erreur
	    game = new VBox();
	    game.setAlignment(Pos.CENTER);
	    game.setSpacing(10);
	    game.getChildren().add(errorLabel);
	    game.getChildren().add(boardPane);
	    game.getChildren().add(solverButton);
	    game.getChildren().add(quitBtn);
        game.getChildren().add(returnToMapButton);
	    
	    HBox root = new HBox(); // Conteneur principal
	    
	    // Création des labels pour les mouvements et le timer
	    movesLabel = new Label("Moves: "+score); // Afficher le meilleur score pour ce niveau
	    Label infoLabel = new Label("Votre meilleur score pour ce niveau est de : " + Menu.getBestScore(savesString, levelSelection)+ "\n\nVotre meilleur temps pour ce niveau est de : "+Menu.getBestTime(savesString, levelSelection)+" secondes");
	    VBox textPane = new VBox(10);
	    textPane.setPadding(new Insets(10));
	    initTimer();
	    textPane.getChildren().addAll(timerLabel,movesLabel,infoLabel);
	    root.getChildren().addAll(textPane,game);
	    
	    List<String> solution = new ArrayList<>(); // Liste pour stocker la solution
	    
	    // Générateur de nombres aléatoires pour le mélange du plateau
	    Random random = new Random();
	    int randomNumber = random.nextInt(2);
	    
	    displayBoard(board,primaryStage,true);// Afficher le plateau
	    errorLabel.setText("En cours de mélange...");
	  	errorLabel.setVisible(true);
	    // Pause pendant 5 secondes avant de mélanger 
	    PauseTransition pause = new PauseTransition(Duration.seconds(5));
	    pause.setOnFinished(event -> {
	        // Mélange le plateau
	    	while(board.InitialPosition() || !board.solveWithinTime(500,solution)) {
	    		board = new Board("../levels/" + levelSelection+".csv");
		        if (board.isEZ()) {
		            if(randomNumber == 0) {
		               errorLabel.setText("Mélange automatique");
		               errorLabel.setVisible(true);
		                board.mixBoardAuto();
		                while (!board.isSolvable()) {
		                    board.mixBoardAuto();
		                }
		            } else {
		            	errorLabel.setText("Mélange manuel");
		            	errorLabel.setVisible(true);
		                board.mixBoard(100);
		            }
		        } else {
		        	errorLabel.setText("Mélange manuel");
	            	errorLabel.setVisible(true);
		            board.mixBoard(100);
		        }
		        count.incrementAndGet();

	            if (count.get() == maxCount) {
	                errorLabel.setText("Impossible de mélanger le plateau sans revenir à sa position initiale");
	                errorLabel.setVisible(true);
	                break;
	            }
	        }
	        displayBoard(board, primaryStage, false); 
	        timeline.play(); // Commence le minuteur 
	    });
	    pause.play();
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
	 

	 private void displayBoard(Board board, Stage primaryStage, boolean initial) {
		 boardPane.getChildren().clear();
		 for (javafx.scene.Node node : game.getChildren()) {
    		 if (node instanceof Button) {
                 Button button = (Button) node;
                 button.setDisable(false);
             }
    	 }
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
		  if (gameSolved(IDASolver.convertBoxArrayToShortArray(board.getGrid()), "../levels/"+levelSelection+".csv")) {
	        	 if(initial == false) {
	        		 timeline.stop();
	        		 Menu.updateScoreAndAccessibility(savesString, levelSelection, score, elapsedTimeInSeconds);
	        		// Affichage de l'alerte
		             Alert alert = new Alert(AlertType.INFORMATION);
		             alert.setTitle("Félicitations !");
		             alert.setHeaderText(null);
		             alert.setContentText("Vous avez gagné en "+elapsedTimeInSeconds+" secondes et en "+score+" coups !");
		             alert.getDialogPane().getButtonTypes().clear();
		             alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
		             alert.setOnCloseRequest(e -> {
		            	 String fileName = savesString.substring(savesString.lastIndexOf("/") + 1);
		                 Map map = new Map(fileName.replace(".csv", ""));
		                 map.showMap(primaryStage);;
		             });

		             alert.showAndWait();
	        	 }else if(game.lookup("#solverButton") != null){
	        		 Button button = (Button) game.lookup("#solverButton");
		             button.setDisable(true);
	        	 }else if(game.lookup("#solverManu") != null) {
	        		 Button buttonManu = (Button) game.lookup("#solverManu");
		             buttonManu.setDisable(true);
		             Button buttonAuto = (Button) game.lookup("#solverAuto");
		             buttonAuto.setDisable(true);
	        	 }
	        	 for (javafx.scene.Node node : boardPane.getChildren()) {
	                 if (node instanceof Button) {
	                     Button button = (Button) node;
	                     button.setDisable(true);
	                 }
	             }
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
		                displayBoard(board, primaryStage,false);
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
		                options[i] = "Case " + (char) (65 + i);
		                Button button = (Button) boardPane.getChildren().get(index);
		                button.setText("" + (char) (i + 65));
		            }

		            alert.getButtonTypes().clear();
		            for (String option : options) {
		                alert.getButtonTypes().add(new ButtonType(option));
		            }
		            alert.getButtonTypes().add(ButtonType.CANCEL);

		            Optional<ButtonType> result = alert.showAndWait();
		            if (result.isPresent() && result.get() != ButtonType.CANCEL) {
		                int choice = alert.getButtonTypes().indexOf(result.get());
		                int[] emptyBox = adjacentEmptyBoxes.get(choice); // Utiliser adjacentEmptyBoxes au lieu de emptyBoxes
		                int rowEmpty1 = emptyBox[0];
		                int colEmpty1 = emptyBox[1];

		                int rowNonEmpty = index1 / board.getBoardSize();
		                int colNonEmpty = index1 % board.getBoardSize();

		                if (board.swap(rowNonEmpty, colNonEmpty, rowEmpty1, colEmpty1)) {
		                    score++;
		                    movesLabel.setText("Moves: " + score);
		                    displayBoard(board, primaryStage, false);
		                } else {
		                    errorLabel.setText("Mouvement invalide. Veuillez choisir une case vide adjacente.");
		                    errorLabel.setVisible(true);
		                }
		            } else {
		            	errorLabel.setText("Aucun choix effectué. Veuillez réessayer.");
		            	errorLabel.setVisible(true);
		            }
		        }
		    }
	 }
	 private void solverHandle(Stage primaryStage) {
		 Button solverAuto = new Button("Résolution Automatique");
		 solverAuto.setId("#solverAuto");
		 Button solverManu = new Button("Résolution Manuelle");
		 solverAuto.setId("#solverManu");
		 Button buttonToRemove = (Button) game.lookup("#solverButton");
		 game.getChildren().add(0, solverAuto);
		 game.getChildren().add(1, solverManu);
		 game.getChildren().remove(buttonToRemove);
		 solverAuto.setOnAction(event -> {
			 solverAuto(primaryStage);
		 }
		 );
		 solverManu.setOnAction(event -> {
			 solverManu(primaryStage);
		 }
		 );
	 }

	 private void solverAuto(Stage primaryStage) {
		    errorLabel.setText("Veuillez patienter, le solveur est en marche...\n");
		    errorLabel.setVisible(true);
		    long startTime = System.currentTimeMillis();

		    List<String> solution = board.solve();
    		long endTime = System.currentTimeMillis();
	        long executionTime = endTime - startTime;
		    Timeline timeline = new Timeline();
		    for (int i = 0; i < solution.size(); i++) {
		        String move = solution.get(i);
		        String[] parts = move.split(" ");
		        int index1 = Integer.parseInt(parts[1]);
		        int index2 = Integer.parseInt(parts[2]);
		        System.out.println("point 1 : ("+ index1/board.getBoardSize() + ","+index1%board.getBoardSize()+ ") Point 2 : ("+ index2/board.getBoardSize() + ","+index2%board.getBoardSize());

		        
		        KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 7), event -> {
		            if (board.swap2(index1, index2)) {
		                displayBoard(board, primaryStage, true);
		            }
		        });
		        timeline.getKeyFrames().add(keyFrame);
		    }

		    timeline.setOnFinished(event -> {
		        
		        errorLabel.setText("Temps d'exécution du solveur : " + executionTime + " millisecondes");
		        errorLabel.setVisible(true);
		    });

		    timeline.play();
	 }
	 

	 private void solverManu(Stage primaryStage) {
		 	currentStep = -1;
		    errorLabel.setText("Veuillez patienter, le solveur est en marche...\n");
		    errorLabel.setVisible(true);

		    List<String> solution1 = board.solve();
		    for (int i = 0; i < solution1.size(); i++) {
		        String move = solution1.get(i);
		        String[] parts = move.split(" ");
		        int index1 = Integer.parseInt(parts[1]);
		        int index2 = Integer.parseInt(parts[2]);
		        System.out.println("point 1 : ("+ index1/board.getBoardSize() + ","+index1%board.getBoardSize()+ ") Point 2 : ("+ index2/board.getBoardSize() + ","+index2%board.getBoardSize());
		    }

		    createNavigationButtons(primaryStage, solution1);
		    updateButtons(solution1);
		}

		private void createNavigationButtons(Stage primaryStage, List<String> solution) {
		    previousButton = new Button("Étape précédente");
		    previousButton.setOnAction(event -> {
		        if (currentStep > 0) {
		            currentStep--;
		            performStep(solution);
		            displayBoard(board, primaryStage, true);
		            updateButtons(solution);
		        }
		    });

		    nextButton = new Button("Étape suivante");
		    nextButton.setOnAction(event -> {
		        if (currentStep < solution.size() - 1) {
		            currentStep++;
		            performStep(solution);
		            displayBoard(board, primaryStage, true);
		            updateButtons(solution);
		        }
		    });

		    // Placez les boutons de navigation dans votre conteneur approprié
		    // par exemple, si vous utilisez un VBox, vous pouvez les ajouter ainsi :
		    game.getChildren().addAll(previousButton, nextButton);
		}

		private void updateButtons(List<String> solution) {
		    previousButton.setDisable(currentStep <= 0);
		    nextButton.setDisable(currentStep >= solution.size() - 1);
		}

		private void performStep(List<String> solution) {
		    String move = solution.get(currentStep);
		    System.out.println(move);
		    String[] parts = move.split(" ");
		    int index1 = Integer.parseInt(parts[1]);
		    int index2 = Integer.parseInt(parts[2]);
		    System.out.println("Point 1: ("+index1/board.getBoardSize()+","+index1%board.getBoardSize()+") Point 2 :("+index2/board.getBoardSize()+","+index2%board.getBoardSize()+")");
		    System.out.println(board.swap2(index1, index2));
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