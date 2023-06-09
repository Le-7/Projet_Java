package taquinFX;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundImage;
import javafx.scene.layout.BackgroundPosition;
import javafx.scene.layout.BackgroundRepeat;
import javafx.scene.layout.BackgroundSize;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Stage;
//Musique de fond
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class Menu extends Application {
	private static final String IMAGE_PATH = "file:../images/menu.jpg";

	// Déclaration des variables d'interface utilisateur
	ComboBox<String> comboBox = new ComboBox<>();
	TextField textField = new TextField();
	Button button = new Button("Valider");
	Button mapButton = new Button("Accéder à la carte");
	Button quitBtn = new Button("Quitter le jeu");
	Label label1 = new Label("Nom de votre sauvegarde :");
	Label label2 = new Label("Reprendre la partie : ");

	// Musique
	String musicFile = "../song/NoelSong.mp3";
	Media sound = new Media(new File(musicFile).toURI().toString());
	MediaPlayer mediaPlayer = new MediaPlayer(sound);

	String clickFile = "../song/click2.mp3";
	Media clickSound = new Media(new File(clickFile).toURI().toString());
	MediaPlayer mediaPlayer2 = new MediaPlayer(clickSound);

	// Méthode principale pour démarrer l'affichage
	public void start(Stage primaryStage) {
		showMenu(primaryStage);
	}

	// Méthode pour afficher le menu
	public void showMenu(Stage primaryStage) {

		// Identifiants pour css
		button.setId("validerButton");
		mapButton.setId("mapButton");
		quitBtn.setId("quitButton");
		comboBox.setId("saveComboBox");
		textField.setId("textField");
		label1.setId("label1");

		// Configuration de l'arrière-plan
		Image image = new Image(IMAGE_PATH);
		BackgroundImage backgroundImage = new BackgroundImage(image, BackgroundRepeat.NO_REPEAT,
				BackgroundRepeat.NO_REPEAT, BackgroundPosition.CENTER,
				new BackgroundSize(BackgroundSize.AUTO, BackgroundSize.AUTO, false, false, true, false));
		primaryStage.setTitle("Jeu de Taquin");

		// Configuration du StackPane (une forme de layout)
		StackPane stackPane = new StackPane();
		stackPane.setBackground(new Background(backgroundImage));

		// Configuration du rectangle d'arrière-plan pour le menu
		Rectangle rectangle = new Rectangle();
		rectangle.setId("myRectangle");
		rectangle.setFill(Color.rgb(0, 0, 0, 0.3)); // Couleur de remplissage
		rectangle.setWidth(600); // Largeur
		rectangle.setHeight(400); // Hauteur
		rectangle.setStroke(Color.WHITE); // Couleur de l'outline
		rectangle.setStrokeWidth(2); // Épaisseur de l'outline
		stackPane.getChildren().add(rectangle); // Ajout du rectangle au StackPane

		// Configuration de la VBox (un autre type de layout)
		VBox vBox = new VBox();
		vBox.setAlignment(Pos.CENTER); // Alignement des éléments
		vBox.setSpacing(15); // Espacement entre les éléments
		vBox.setPadding(new Insets(250)); // Marge intérieure
		stackPane.getChildren().add(vBox); // Ajout de la VBox au StackPane

		// Configuration de la scène
		Scene scene = new Scene(stackPane, 968, 544);
		scene.getStylesheets().add("file:../css/TaquinStyle.css");

		// Configuration du label de bienvenue
		comboBox.getItems().addAll(getNames());
		Label welcomeLabel = new Label("Bienvenue sur notre jeu de Taquin");
		welcomeLabel.setFont(Font.font("Arial", FontWeight.BOLD, 24)); // Police et taille du texte
		welcomeLabel.setAlignment(Pos.CENTER); // Alignement du texte
		welcomeLabel.setTextFill(Color.WHITE); // Couleur du texte

		// Configuration du bouton "Valider"
		button.setOnAction(e -> {
			mediaPlayer2.stop();
			mediaPlayer2.play();
			String newName = textField.getText();

			if (newName.trim().isEmpty()) {
				// Afficher un message d'erreur dans une boîte de dialogue
				Alert errorAlert = new Alert(Alert.AlertType.ERROR);
				errorAlert.setTitle("Erreur");
				errorAlert.setHeaderText(null);
				errorAlert.setContentText("Veuillez entrer un nom pour la sauvegarde.");
				errorAlert.showAndWait();
			} else {
				saveName(newName);
				textField.clear();
				comboBox.getItems().addAll(getNames());
				// Ouvrir la fenêtre de la carte
				Map map = new Map(newName, mediaPlayer);
				primaryStage.close();
				Stage newStage = new Stage();
				map.showMap(newStage);
				mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				mediaPlayer.play();
			}
		});

		// Configuration du label "Nom de sauvegarde"
		label1.setFont(Font.font("Arial", FontWeight.BLACK, 18));
		label1.setTextFill(Color.AZURE); // Met le texte en bleu clair

		// Configuration du label "Reprendre la partie"
		label2.setFont(Font.font("Arial", FontWeight.BLACK, 18));
		label2.setTextFill(Color.AZURE); // Met le texte en bleu clair

		// Ajoute un espace de hauteur
		Label spacerLabel = new Label("");
		spacerLabel.setPrefHeight(40); // ajustez cette valeur pour modifier la taille de l'espace

		// Configuration du bouton "Quitter le jeu"
		quitBtn.setMinWidth(100);
		quitBtn.setMaxWidth(200);
		// quitBtn.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);
		// -fx-background-radius: 15; -fx-font-size: 16px;");
		quitBtn.setOnAction(e -> {
			mediaPlayer.stop();
			primaryStage.close(); // Fermer la fenêtre principale du jeu
		});

		// Configuration du bouton "Accéder à la carte"
		mapButton.setMinWidth(100); // Modifie la largeur minimum du bouton
		mapButton.setMaxWidth(200); // Modifie la largeur maximum du bouton
		// mapButton.setStyle("-fx-background-color: rgba(255, 255, 255, 0.5);
		// -fx-background-radius: 15; -fx-font-size: 16px;"); // Style CSS pour le
		// bouton
		mapButton.setOnAction(e -> {
			mediaPlayer2.stop(); // on stop d'abord si jamais on a cliqué sur un autre bouton avant et que le
									// player n'a pas eu le temps de s'arreter
			mediaPlayer2.play();

			String selectedSave = comboBox.getValue();
			if (selectedSave != null && !selectedSave.trim().isEmpty()) {
				Map map = new Map(selectedSave, mediaPlayer);
				primaryStage.close();
				Stage newStage = new Stage();
				map.showMap(newStage);

				// Musique répétée + lancée au moment de voir la map
				mediaPlayer.setCycleCount(MediaPlayer.INDEFINITE);
				mediaPlayer.play();
			} else {
				// Affichage d'une alerte en cas d'erreur
				Alert alert = new Alert(Alert.AlertType.ERROR);
				alert.setTitle("Erreur");
				alert.setHeaderText(null);
				alert.setContentText("Veuillez choisir une sauvegarde avant d'accéder à la carte.");
				alert.showAndWait();
			}
		});

		comboBox.setStyle("-fx-pref-width: 200px; -fx-background-radius: 10px; -fx-prompt-text-fill: gray;");

		// Ajout des éléments à la VBox
		vBox.getChildren().addAll(welcomeLabel, label1, textField, button, spacerLabel, label2, comboBox, mapButton,
				quitBtn);

		// Configuration finale de la scène et affichage
		primaryStage.setScene(scene);
		// primaryStage.setResizable(false); // Empêche le redimensionnement de la
		// fenêtre
		primaryStage.show();
	}

	// Obtient la liste des noms de sauvegarde à partir des fichiers existants
	private List<String> getNames() {
		String folder = "../Saves"; // Le dossier contenant les fichiers de sauvegarde
		List<String> savesNames = new ArrayList<String>();
		// Obtenir tous les fichiers CSV dans le dossier spécifié
		File folderFiles = new File(folder);
		File[] files = folderFiles.listFiles((dir, name) -> name.endsWith(".csv"));

		// Vérifier si le dossier contient des fichiers de sauvegarde
		if (files != null && files.length > 0) {
			// Affichez la liste des fichiers disponibles
			for (int i = 0; i < files.length; i++) {
				String fileName = files[i].getName().replace(".csv", ""); // Afficher chaque nom de fichier sans
																			// l'extension .csv
				savesNames.add(fileName);
			}
		}
		return savesNames;
	}

	// Sauvegarde le nom de la partie
	private void saveName(String prenom) {
		File file = new File("../Saves/" + prenom + ".csv");
		List<String[]> levelInfo = new ArrayList<>(); // Liste pour stocker les informations des niveaux
		if (!file.exists()) {
			// Ajoutez les niveaux et leurs données par défaut à la sauvegarde
			for (int i = 0; i <= 11; i++) {
				if (i == 0) {
					levelInfo.add(new String[] { "lvl" + i, "true", "0", "0" }); // Le premier niveau est accessible par
																					// défaut
				} else {
					levelInfo.add(new String[] { "lvl" + i, "false", "0", "0" }); // Les autres niveaux sont
																					// inaccessibles par défaut
				}
			}
			writeCSV(file, levelInfo);
		}
	}

	// Écrit les informations de niveau dans un fichier CSV
	private static void writeCSV(File file, List<String[]> levelInfo) {
		try (PrintWriter writer = new PrintWriter(file)) {
			for (String[] info : levelInfo) {
				writer.println(String.join(",", info));
			}
		} catch (FileNotFoundException e) {
			// Affiche un message d'erreur si une exception se produit lors de l'écriture du
			// fichier
			System.out.println("Erreur lors de l'écriture du fichier CSV : " + file.getName());
			e.printStackTrace();
		}
	}

	public static void updateScoreAndAccessibility(String SaveName, String Levelname, int newScore, int newTime) {
		File file = new File(SaveName);

		// Lire les informations actuelles du fichier CSV
		List<String[]> levelInfo = readCSV(file);
		if (levelInfo != null) {
			for (int i = 0; i < levelInfo.size(); i++) {
				String[] info = levelInfo.get(i);
				String bestScore = info[2]; // Meilleur score du niveau (en tant que chaîne de caractères)
				String bestTime = info[3];

				if (info[0].equals(Levelname) && Integer.parseInt(bestScore) == 0) { // Initialiser le meilleur score //
																						// que pour le niveau non joué
					info[2] = String.valueOf(newScore);
				}
				if (info[0].equals(Levelname) && Integer.parseInt(bestTime) == 0) { // Initialiser le meilleur temps que
																					// // pour le niveau non joué
					info[3] = String.valueOf(newTime);
				}
				// Mettre à jour le meilleur score si nécessaire
				if (info[0].equals(Levelname) && (Integer.parseInt(bestScore) > newScore)) {
					info[2] = String.valueOf(newScore); // Convertir le nouveau score en chaîne de caractères et le //
														// mettre à jour
				}

				// Mettre à jour le meilleur temps si nécessaire
				if (info[0].equals(Levelname) && (Integer.parseInt(bestTime) > newTime)) {
					info[3] = String.valueOf(newTime); // Convertir le nouveau temps en chaîne de caractères et le
														// mettre à jour
				}
				// Modifier l'accessibilité du niveau suivant
				if (info[0].equals(Levelname) && i < levelInfo.size() - 1) { // on verifie si on est pas a la derniere
																				// ligne
					String[] nextLevelInfo = levelInfo.get(i + 1); // Obtenir les informations du niveau suivant
					nextLevelInfo[1] = String.valueOf(true); // Mettre à jour l'accessibilité du niveau suivant
				}
			}
			// Écrire les nouvelles informations dans le fichier CSV
			writeCSV(file, levelInfo);
		}
	}

	// Cette méthode lit un fichier CSV et renvoie les données sous forme de liste
	// de tableaux de chaînes de caractères.
	private static List<String[]> readCSV(File file) {
		List<String[]> levelInfo = new ArrayList<>(); // Liste pour stocker les informations du fichier CSV.

		// Bloc try-with-resources pour assurer la fermeture du BufferedReader.
		try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
			String line;
			// Boucle pour lire chaque ligne du fichier CSV.
			while ((line = reader.readLine()) != null) {
				// Découpage de la ligne en utilisant la virgule comme séparateur.
				String[] data = line.split(",");
				// Vérification que la ligne contient bien 4 éléments avant de l'ajouter à la
				// liste.
				if (data.length == 4) {
					levelInfo.add(data);
				}
			}
		} catch (IOException e) {
			// Gestion des exceptions liées à la lecture du fichier.
			System.out.println("Erreur lors de la lecture du fichier CSV : " + file.getName());
			e.printStackTrace();
		}

		// Renvoi de la liste des données du fichier CSV.
		return levelInfo;
	}

	// Cette méthode renvoie le meilleur score pour un niveau spécifique en le
	// lisant depuis un fichier CSV.
	public static int getBestScore(String SaveName, String Levelname) {
		File file = new File(SaveName); // Création de l'objet File correspondant au fichier CSV.

		// Lecture des données du fichier CSV.
		List<String[]> levelInfo = readCSV(file);
		if (levelInfo != null) {
			// Boucle pour examiner chaque entrée dans les données du niveau.
			for (String[] info : levelInfo) {
				String level = info[0]; // Nom du niveau.
				String bestScore = info[2]; // Meilleur score pour le niveau.

				// Si le niveau correspond au niveau demandé, renvoi du meilleur score pour ce
				// niveau.
				if (level.equals(Levelname)) {
					return Integer.parseInt(bestScore);
				}
			}
		}
		return -1; // Renvoi de -1 si le niveau n'a pas été trouvé ou s'il n'y a pas de meilleur
					// score enregistré.
	}

	// Cette méthode renvoie le meilleur temps pour un niveau spécifique en le
	// lisant depuis un fichier CSV.
	public static int getBestTime(String SaveName, String Levelname) {
		File file = new File(SaveName); // Création de l'objet File correspondant au fichier CSV.

		// Lecture des données du fichier CSV.
		List<String[]> levelInfo = readCSV(file);
		if (levelInfo != null) {
			// Boucle pour examiner chaque entrée dans les données du niveau.
			for (String[] info : levelInfo) {
				String level = info[0]; // Nom du niveau.
				String bestTime = info[3]; // Meilleur temps pour le niveau.

				// Si le niveau correspond au niveau demandé, renvoi du meilleur temps pour ce
				// niveau.
				if (level.equals(Levelname)) {
					return Integer.parseInt(bestTime);
				}
			}
		}
		return -1;// Renvoi de -1 si le niveau n'a pas été trouvé ou s'il n'y a pas de meilleur
					// temps enregistré.
	}

	// Méthode principale pour lancer l'application.
	public static void main(String[] args) {
		launch(args);
	}
}
