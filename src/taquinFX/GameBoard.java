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
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.util.Duration;
import javafx.scene.media.MediaPlayer;

public class GameBoard extends Application { // Définit la classe GameBoard qui hérite de la classe Application de
												// JavaFX

	private String levelSelection; // Variable pour stocker la sélection du niveau par l'utilisateur
	private String savesString; // Variable pour stocker le chemin des sauvegardes
	private GridPane boardPane; // Définit un panneau qui contiendra le plateau de jeu
	private static final String IMAGE_PATH = "file:../images/noel1.jpg";
	private Board board; // Définit le plateau de jeu lui-même
	private static final int TILE_SIZE = 55; // Définit la taille de chaque tuile sur le plateau
	private Label errorLabel; // Variable pour stocker un label qui affichera les messages d'erreur
	int score = 0; // Variable pour stocker le score actuel du joueur
	private Label timerLabel; // Variable pour stocker un label qui affichera le timer
	private Timeline timeline; // Variable pour stocker la ligne du temps pour le timer
	private Label movesLabel; // Variable pour stocker un label qui affichera les mouvements effectués
	private int elapsedTimeInSeconds = 0; // Variable pour stocker le temps écoulé en secondes
	private VBox game; // Définit une boîte verticale qui contiendra le jeu
	private VBox buttons; // Définit une boîte verticale qui contiendra les boutons
	private VBox buttonsRight; // Définit une boîte verticale qui contiendra les boutons à droite
	private Button previousButton; // Définit le bouton 'précédent'
	private Button nextButton; // Définit le bouton 'suivant'
	private int currentStep; // Variable pour stocker l'étape actuelle
	private MediaPlayer mediaPlayer; // Variable pour stocker le lecteur multimédia

	public GameBoard(String levelSelection, String savesString, MediaPlayer mediaPlayer) { // Constructeur de la classe
		super(); // Appelle le constructeur de la classe parente
		this.levelSelection = levelSelection; // Initialise la sélection de niveau
		this.savesString = "../Saves/" + savesString; // Initialise le chemin de sauvegarde
		this.mediaPlayer = mediaPlayer; // Initialise le lecteur multimédia
	}

	public void start(Stage primaryStage) { // Méthode pour démarrer l'affichage
		showGame(primaryStage); // Appelle la méthode showGame pour afficher le jeu
	}

	public Scene showGame(Stage primaryStage) { // Méthode pour afficher le jeu

		board = new Board("../levels/" + levelSelection + ".csv"); // Initialise le plateau de jeu avec le niveau
																	// sélectionné
		int maxCount = 1000000; // Nombre maximum d'essais pour mélanger le plateau
		AtomicInteger count = new AtomicInteger(0); // Initialise un compteur pour les essais de mélange
		errorLabel = new Label(); // Initialise le label d'erreur
		errorLabel.setVisible(false); // Rend le label d'erreur invisible

		Label level = new Label("Niveau " + Integer.parseInt(levelSelection.substring(3)));
		level.setId("#level");

		// Bouton pour lancer le solveur automatique
		Button solverManuButton = new Button("Solveur manuel");
		solverManuButton.setId("solverManu");
		solverManuButton.setVisible(false);

		// Bouton pour lancer le solveur automatique
		Button solverAutoButton = new Button("Solveur automatique");
		solverAutoButton.setId("solverAuto");
		solverAutoButton.setVisible(false);

		solverAutoButton.setOnAction(event -> {
			solverAuto(primaryStage);
			solverAutoButton.setVisible(false);
			solverManuButton.setVisible(false);
		});

		solverManuButton.setOnAction(event -> {
			solverManu(primaryStage);
			solverAutoButton.setVisible(false);
			solverManuButton.setVisible(false);
		});

		// Bouton pour lancer le solveur
		Button solverButton = new Button("Solveur du taquin");
		solverButton.setId("solverButton");
		solverButton.setOnAction(event -> {
			timeline.stop();
			desactivateTaquin();
			solverManuButton.setVisible(true);
			solverAutoButton.setVisible(true);
			buttons.getChildren().remove(solverButton);
		});

		Button quitBtn = new Button("Quitter le jeu");
		quitBtn.setMinWidth(100);
		quitBtn.setMaxWidth(200);
		quitBtn.setId("quitBtn");
		quitBtn.setOnAction(e -> {
			primaryStage.close();
		});

		primaryStage.setTitle("Taquin");// Titre de la fenêtre
		// Création de la grille pour afficher le plateau
		boardPane = new GridPane();
		boardPane.setAlignment(Pos.CENTER);

		// boardPane.setHgap(10); //espace cases
		// boardPane.setVgap(10);

		Button returnToMapButton = new Button("Retour à la carte");
		returnToMapButton.setMinWidth(200);
		returnToMapButton.setMaxWidth(300);
		returnToMapButton.setId("returnToMap");
		returnToMapButton.setOnAction(e -> {
			primaryStage.close();
			// Extraire le nom du fichier
			String fileName = savesString.substring(savesString.lastIndexOf("/") + 1);
			Map map = new Map(fileName.replace(".csv", ""), mediaPlayer);
			map.showMap(primaryStage);
		});

		buttons = new VBox();
		buttons.setAlignment(Pos.CENTER);
		buttons.getChildren().addAll(solverButton, returnToMapButton, quitBtn);
		buttons.setSpacing(10);
		buttons.setMinHeight(230);
		buttons.setMaxHeight(230);

		buttonsRight = new VBox();
		buttonsRight.setAlignment(Pos.CENTER);
		buttonsRight.getChildren().addAll(solverAutoButton, solverManuButton);
		buttonsRight.setSpacing(10);

		// Conteneur pour le jeu et le label d'erreur
		game = new VBox();
		game.setId("#game");
		game.setAlignment(Pos.CENTER);
		game.setSpacing(10);
		game.getChildren().add(errorLabel);
		game.getChildren().add(boardPane);

		BorderPane root = new BorderPane(); // Conteneur principal
		// Création des labels pour les mouvements et le timer
		movesLabel = new Label("Moves : " + score); // Afficher le meilleur score pour ce niveau
		movesLabel.setId("movesLabel");
		Label infoLabel = new Label("Meilleur score : " + Menu.getBestScore(savesString, levelSelection)
				+ "\n\nMeilleur temps : " + Menu.getBestTime(savesString, levelSelection) + " secondes");
		infoLabel.setId("infoLabel");

		VBox textPane = new VBox(10);
		textPane.setAlignment(Pos.CENTER_LEFT);
		textPane.setPrefWidth(300);
		textPane.setPrefHeight(700);
		textPane.setPadding(new Insets(15));
		initTimer();

		textPane.getChildren().addAll(timerLabel, movesLabel, infoLabel);

		root.setTop(level);
		root.setLeft(textPane);
		root.setCenter(game);
		root.setBottom(buttons);
		root.setRight(buttonsRight);

		// Configuration de l'arrière-plan
		Image image = new Image(IMAGE_PATH);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
		game.setBackground(new Background(backgroundImage));
		root.setStyle("-fx-background-color: #FFFDFA;");
		BorderPane.setAlignment(level, Pos.CENTER); // Centrage vertical et horizontal du label level
		BorderPane.setMargin(level, new Insets(25)); // Modifier les valeurs de marge selon vos besoins
		BorderPane.setMargin(buttonsRight, new Insets(25));

		List<String> solution = new ArrayList<>(); // Liste pour stocker la solution

		// Générateur de nombres aléatoires pour le mélange du plateau
		Random random = new Random();
		int randomNumber = random.nextInt(2);

		displayBoard(board, primaryStage, true);// Afficher le plateau
		errorLabel.setText("En cours de mélange...");
		errorLabel.setVisible(true);
		// Pause pendant 5 secondes avant de mélanger
		PauseTransition pause = new PauseTransition(Duration.seconds(5));
		pause.setOnFinished(event -> {
			// Mélange le plateau
			while (board.InitialPosition() || !board.solveWithinTime(500, solution)) {
				board = new Board("../levels/" + levelSelection + ".csv");
				if (board.isEZ()) {
					if (randomNumber == 0) {
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
		Scene scene = new Scene(root, 500, 500); // Créer une scène fixe
		primaryStage.setWidth(1200);
		primaryStage.setHeight(700);
		scene.getStylesheets().add("file:../css/Board.css");
		// Appliquer la scène à la fenêtre principale
		primaryStage.setScene(scene);
		primaryStage.show();

		return scene;
	}

//verifie si le jeu est résolu 
	public static boolean gameSolved(short[] grid, String Csv_path) {
		try {
			BufferedReader reader = new BufferedReader(new FileReader(Csv_path));// crée un lecteur de fichier
			String line;
			int i = 0;
			int boardSize = (int) Math.sqrt(grid.length);// calcule la taill de la grille de jeu

			while ((line = reader.readLine()) != null) { // parcourir le fichier par ligne
				String[] values = line.split(",");// separer chaque ligne par ligne

				for (int j = 0; j < values.length; j++) { // parcout chaque valeur de la ligne
					if (grid[i * boardSize + j] != Integer.parseInt(values[j])) { // verifie si la valeur actuelle
																					// corresponds a la valeur de la
																					// grille de jeu
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

	// cette fonction affiche le plateau de jeu
	private void displayBoard(Board board, Stage primaryStage, boolean initial) {
		boardPane.getChildren().clear(); // Nettoie le contenu actuel du plateau de jeu
		for (javafx.scene.Node node : game.getChildren()) { // Parcourt tous les éléments de la scène de jeu
			// Active tous les boutons
			if (node instanceof Button) {
				Button button = (Button) node;
				button.setDisable(false);
			}
		}
		// Active le bouton de résolution, s'il existe
		if (buttons.lookup("#solverButton") != null) {
			Button solverButton = (Button) buttons.lookup("#solverButton");
			solverButton.setDisable(false);
		}

		// Parcourt toutes les cases de la grille
		for (int i = 0; i < board.getBoardSize() * board.getBoardSize(); i++) {
			final int currentIndex = i;

			// Crée un nouveau bouton pour chaque case
			Button button = new Button(board.getGrid()[i].getDisplay());
			button.setPrefSize(TILE_SIZE, TILE_SIZE);

			// Associe un gestionnaire d'événement pour le clic sur le bouton
			button.setOnAction(event -> handleButtonClick(currentIndex, primaryStage, board));

			// Si la case est vide, met le texte du bouton à vide et change la couleur de
			// fond
			if (board.getGrid()[i] instanceof Empty) {
				button.setText("");
				button.setStyle("-fx-background-color : Lightblue;");
			}

			// Si la case est un bloc, rend le bouton invisible
			if (board.getGrid()[i] instanceof Block) {
				button.setVisible(false);
			}
			// Place le bouton dans la grille
			GridPane.setRowIndex(button, i / board.getBoardSize());
			GridPane.setColumnIndex(button, i % board.getBoardSize());

			// Ajoute le bouton à la scène
			boardPane.getChildren().add(button);
		}

		// Vérifie si le jeu est résolu
		if (gameSolved(IDASolver.convertBoxArrayToShortArray(board.getGrid()),
				"../levels/" + levelSelection + ".csv")) {
			if (initial == false) {
				timeline.stop();
				Menu.updateScoreAndAccessibility(savesString, levelSelection, score, elapsedTimeInSeconds);

				// Affiche une alerte pour annoncer la victoire
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Félicitations !");
				alert.setHeaderText(null);
				alert.setContentText(
						"Vous avez gagné en " + elapsedTimeInSeconds + " secondes et en " + score + " coups !");
				alert.getDialogPane().getButtonTypes().clear();
				alert.getDialogPane().getButtonTypes().add(ButtonType.OK);
				alert.setOnCloseRequest(e -> {
					primaryStage.close();
					String fileName = savesString.substring(savesString.lastIndexOf("/") + 1);
					Map map = new Map(fileName.replace(".csv", ""), mediaPlayer);
					map.showMap(primaryStage);
					;
				});

				alert.showAndWait();
			} else if (buttons.lookup("#solverButton") != null) {
				// Désactive le bouton de résolution
				Button button = (Button) buttons.lookup("#solverButton");
				button.setDisable(true);
			}
			// Désactive tous les boutons du plateau de jeu
			for (javafx.scene.Node node : boardPane.getChildren()) {
				if (node instanceof Button) {
					Button button = (Button) node;
					button.setDisable(true);
				}
			}
		}
	}

	// Cette fonction gère les clics sur les boutons
	private void handleButtonClick(int index1, Stage primaryStage, Board board) {
		// Vérifie si la case sur laquelle on a cliqué est vide
		if (!(board.getGrid()[index1] instanceof Empty)) {
			// Si la case n'est pas vide :
			List<int[]> emptyBoxes = board.findEmptyBoxes(); // On récupère les cases vides
			List<int[]> adjacentEmptyBoxes = new ArrayList<>();

			for (int[] emptyBox : emptyBoxes) {
				int rowEmpty = emptyBox[0];
				int colEmpty = emptyBox[1];
				int index2 = rowEmpty * board.getBoardSize() + colEmpty;

				// Si la case rentrée est adjacente à une case vide, on l'ajoute à la liste
				if (board.isAdjacent(index1, index2)) {
					adjacentEmptyBoxes.add(emptyBox);
				}
			}
			// Si la case non vide est adjacente à une seule case vide
			if (adjacentEmptyBoxes.size() == 1) {
				int rowEmpty = adjacentEmptyBoxes.get(0)[0];
				int colEmpty = adjacentEmptyBoxes.get(0)[1];

				// Effectuer l'échange entre la case non vide et la case vide sélectionnée
				int rowNonEmpty = index1 / board.getBoardSize();
				int colNonEmpty = index1 % board.getBoardSize();
				if (board.swap(rowNonEmpty, colNonEmpty, rowEmpty, colEmpty)) {
					// Actualiser l'affichage après l'échange
					displayBoard(board, primaryStage, false);
					score++;
					movesLabel.setText("Moves: " + score);
					errorLabel.setVisible(false); // Rendre errorLabel non visible
				}
			}
			// Si la case non vide est adjacente à plus d'une case vide
			else if (adjacentEmptyBoxes.size() > 1) {
				// Créer une fenêtre d'alerte pour permettre à l'utilisateur de choisir la case
				// vide à échanger
				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Choix de la case vide adjacente");
				alert.setHeaderText("Il y a plusieurs cases vides adjacentes.");
				alert.setContentText("Veuillez choisir une des cases vides suivantes :");
				alert.setX(primaryStage.getX());
				alert.setY(primaryStage.getY());

				// Créer des options pour l'alerte en fonction des cases vides adjacentes
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
				// Ajouter les options à l'alerte et afficher l'alerte
				alert.getButtonTypes().clear();
				for (String option : options) {
					alert.getButtonTypes().add(new ButtonType(option));
				}
				alert.getButtonTypes().add(ButtonType.CANCEL);
				Optional<ButtonType> result = alert.showAndWait();

				// Effectuer l'échange selon le choix de l'utilisateur et mettre à jour
				// l'affichage
				if (result.isPresent() && result.get() != ButtonType.CANCEL) {
					int choice = alert.getButtonTypes().indexOf(result.get());
					int[] emptyBox = adjacentEmptyBoxes.get(choice);
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
			} else {
				errorLabel.setText("Mouvement invalide. Veuillez choisir une case adjacente.");
				errorLabel.setVisible(true);
			}
		}
	}

	private void solverAuto(Stage primaryStage) {
		// Affiche un message pour indiquer que le solveur est en train de travailler
		errorLabel.setText("Veuillez patienter, le solveur est en marche...\n");
		errorLabel.setVisible(true);

		long startTime = System.currentTimeMillis();// Enregistre le temps de début
		List<String> solution = board.solve(); // Résout le plateau de jeu et obtient la solution

		// Enregistre le temps de fin et calcule le temps d'exécution
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		// Crée une nouvelle Timeline pour animer la solution
		Timeline timeline = new Timeline();

		// Parcourt tous les mouvements de la solution
		for (int i = 0; i < solution.size(); i++) {
			String move = solution.get(i);
			String[] parts = move.split(" ");
			int index1 = Integer.parseInt(parts[1]);
			int index2 = Integer.parseInt(parts[2]);

			// Crée un nouveau KeyFrame pour chaque mouvement
			KeyFrame keyFrame = new KeyFrame(Duration.seconds(i * 3), event -> {
				// Echange les deux cases
				if (board.swap2(index1, index2)) {
					// Affiche le nouveau plateau de jeu
					displayBoard(board, primaryStage, true);
					// Désactive le taquin pendant la résolution
					desactivateTaquin();
				}
			});
			timeline.getKeyFrames().add(keyFrame); // Ajoute le KeyFrame à la Timeline
		}

		// Affiche le temps d'exécution une fois la Timeline terminée
		timeline.setOnFinished(event -> {
			errorLabel.setText("Temps d'exécution du solveur : " + executionTime + " millisecondes");
			errorLabel.setVisible(true);
		});
		timeline.play();// Lance l'animation
	}

	private void solverManu(Stage primaryStage) {
		currentStep = 0; // Réinitialise l'étape actuelle à 0
		// Affiche un message pour indiquer que le solveur est en train de travailler
		errorLabel.setText("Le solveur est en marche...\n");
		errorLabel.setVisible(true);
		List<String> solution1 = board.solve();// Résout le plateau de jeu et obtient la solution
		createNavigationButtons(primaryStage, solution1); // Crée les boutons de navigation
		updateButtons(solution1); // Met à jour les boutons
	}

	private void createNavigationButtons(Stage primaryStage, List<String> solution) {
		previousButton = new Button("Étape précédente"); // Crée le bouton pour revenir à l'étape précédente
		// Ajoute une action au bouton pour effectuer l'étape précédente
		previousButton.setOnAction(event -> {
			if (currentStep >= 0) {
				currentStep--;
				performStep(solution);

				// Affiche le nouveau plateau de jeu et désactive le taquin
				displayBoard(board, primaryStage, true);
				desactivateTaquin();

				// Met à jour les boutons
				updateButtons(solution);
			}
		});
		nextButton = new Button("Étape suivante");// Crée le bouton pour aller à l'étape suivante

		// Ajoute une action au bouton pour effectuer l'étape suivante
		nextButton.setOnAction(event -> {
			if (currentStep < solution.size()) {
				performStep(solution);
				currentStep++;

				// Affiche le nouveau plateau de jeu et désactive le taquin
				displayBoard(board, primaryStage, true);
				desactivateTaquin();
				updateButtons(solution); // Met à jour les boutons
			}
		});
		game.getChildren().addAll(previousButton, nextButton); // Ajoute les boutons au jeu
	}

	private void updateButtons(List<String> solution) {
		// Désactive le bouton précédent si l'on est à la première étape
		// Et le bouton suivant si l'on est à la dernière étape
		previousButton.setDisable(currentStep <= 0);
		nextButton.setDisable(currentStep >= solution.size());
	}

	private void performStep(List<String> solution) {
		System.out.println(currentStep); // Affiche l'étape actuelle dans la console

		// Récupère le mouvement correspondant à l'étape actuelle
		String move = solution.get(currentStep);
		String[] parts = move.split(" ");
		int index1 = Integer.parseInt(parts[1]);
		int index2 = Integer.parseInt(parts[2]);
		board.swap2(index1, index2); // Effectue le mouvement
	}

	private void desactivateTaquin() {
		// Désactive tous les boutons du taquin
		for (javafx.scene.Node node : boardPane.getChildren()) {
			if (node instanceof Button) {
				Button button = (Button) node;
				button.setDisable(true);
			}
		}
	}

	private void updateTimerLabel() {
		// Convertit le temps écoulé en heures, minutes et secondes
		int hours = elapsedTimeInSeconds / 3600;
		int minutes = (elapsedTimeInSeconds % 3600) / 60;
		int seconds = elapsedTimeInSeconds % 60;

		// Met à jour le label du timer avec le nouveau temps
		String time = String.format("%02d:%02d:%02d", hours, minutes, seconds);
		timerLabel.setText(time);
		timerLabel.setId("timerLabel");
	}

	private void initTimer() {
		timerLabel = new Label("00:00:00"); // Initialise le label du timer avec un temps de 00:00:00

		// Crée une nouvelle Timeline pour le timer
		timeline = new Timeline();
		timeline.setCycleCount(Animation.INDEFINITE);

		// Ajoute un KeyFrame à la Timeline pour incrémenter le temps toutes les
		// secondes
		KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), event -> {
			elapsedTimeInSeconds++;
			updateTimerLabel();
		});
		timeline.getKeyFrames().add(keyFrame);
	}
}
