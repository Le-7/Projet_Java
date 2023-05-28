package taquinFX;

import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.scene.media.MediaPlayer;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

// Classe représentant la carte du jeu
public class Map {
	private String saveString;
	// Chemin vers l'image utilisée pour la carte
	private static final String IMAGE_PATH = "file:../images/map_java.jpg";

	// Nombre de lignes et de colonnes pour le GridPane
	private static final int NUM_ROWS = 20;
	private static final int NUM_COLS = 20;

	// Dimensions de la fenêtre principale
	private static final double WINDOW_WIDTH = 1700;
	private static final double WINDOW_HEIGHT = 600;

	private MediaPlayer mediaPlayer;

	// Méthode principale pour démarrer l'affichage
	public void start(Stage primaryStage) {
		showMap(primaryStage);
	}

	public Map(String saveString, MediaPlayer mediaPlayer) {
		this.saveString = saveString + ".csv";
		this.mediaPlayer = mediaPlayer;
	}

	// Méthode pour afficher la carte
	public Scene showMap(Stage primaryStage) {
		// Chargement de l'image de la carte
		Image image = new Image(IMAGE_PATH);

		// Création d'une ImageView pour afficher l'image
		ImageView imageView = new ImageView(image);
		imageView.setPreserveRatio(true);

		// Création d'un GridPane pour organiser les boutons de la carte
		GridPane gridPane = createGridPane();

		// Création d'un StackPane pour superposer l'image de la carte et les boutons
		StackPane stackPane = new StackPane();
		stackPane.getChildren().addAll(imageView, gridPane);

		// Création d'une scène pour afficher le StackPane
		Scene scene = new Scene(stackPane);
		scene.getStylesheets().add("file:../css/TaquinStyle.css");

		// Configuration de la scène principale
		primaryStage.setScene(scene);
		primaryStage.setWidth(WINDOW_WIDTH);
		primaryStage.setHeight(WINDOW_HEIGHT);
		primaryStage.setResizable(false);
		primaryStage.setX(0);
		primaryStage.setY(0);

		// Affichage de la fenêtre principale
		primaryStage.show();

		// Positions des boutons sur la carte et des niveaux
		int[] cols = { 0, 2, 3, 4, 7, 8, 9, 11, 14, 16, 18 };
		int[] rows = { 8, 9, 11, 13, 13, 11, 9, 8, 8, 7, 8 };
		int[] levels = { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9, 10 };

		// Boucle pour créer les boutons
		for (int i = 0; i <= 10; i++) {
			// Création d'un bouton avec le texte et le niveau correspondant
			Button btn = createButton(String.valueOf(levels[i]), levels[i] + 1);
			placeBtn(btn, gridPane, cols[i], rows[i], primaryStage, String.valueOf(levels[i]));
		}
// Création du bouton "Quitter le jeu"
		Button quitBtn = createQuitButton(primaryStage);

		// Configuration de VBox pour aligner le bouton en bas
		VBox vbox = new VBox();
		vbox.getChildren().add(quitBtn);
		vbox.setAlignment(Pos.BOTTOM_CENTER);

		// Ajout de VBox à GridPane
		GridPane.setConstraints(vbox, 0, 5);
		gridPane.getChildren().add(vbox);
		return scene;
	}

	// Méthode pour créer le bouton "Quitter le jeu"
	private Button createQuitButton(Stage primaryStage) {
		Button quitBtn = new Button("←");
		// Configuration du style du bouton
		quitBtn.setMinWidth(60);
		quitBtn.setMaxWidth(50);
		// Configuration du style du bouton
		quitBtn.setStyle(" -fx-background-radius: 15; -fx-font-size: 16px;");
		// Configuration de l'action du bouton
		quitBtn.setOnAction(e -> {
			// Stop le MediaPlayer
			if (mediaPlayer != null) {
				mediaPlayer.stop();
			}
			Menu menu = new Menu();
			primaryStage.close();
			Stage newStage = new Stage();
			menu.showMenu(newStage);
		});

		return quitBtn;
	}

	// Méthode pour créer un bouton avec le texte et le niveau correspondant
	private Button createButton(String text, int level) {
		Button button = new Button(text);
		button.setId("pathButton");

		return button;
	}

	// Méthode pour créer un GridPane avec des contraintes de colonne et de ligne
	private GridPane createGridPane() {
		GridPane gridPane = new GridPane();

		// Configuration des contraintes de colonne
		for (int i = 0; i < NUM_COLS; i++) {
			ColumnConstraints colConstraints = new ColumnConstraints();
			colConstraints.setPercentWidth(100.0 / NUM_COLS);
			gridPane.getColumnConstraints().add(colConstraints);
		}

		// Configuration des contraintes de ligne
		for (int i = 0; i < NUM_ROWS; i++) {
			RowConstraints rowConstraints = new RowConstraints();
			rowConstraints.setPercentHeight(100.0 / NUM_ROWS);
			gridPane.getRowConstraints().add(rowConstraints);
		}

		// Afficher les lignes et les colonnes du quadrillage
		// gridPane.setGridLinesVisible(true);

		// Configuration de l'alignement du GridPane
		gridPane.setAlignment(Pos.CENTER);
		return gridPane;
	}

	// Méthode pour charger un niveau spécifique
	public void loadLevel(int level, Stage primaryStage) {
		GameBoard gameBoard = new GameBoard("lvl" + level, saveString, mediaPlayer);
		gameBoard.showGame(primaryStage);
	}

	// Méthode pour créer un bouton sur le GridPane à une position spécifique
    public void placeBtn(Button btn, GridPane gridPane, int columns, int rows, Stage primaryStage, String lvl) {
        AnchorPane anchorPane = new AnchorPane(btn);
        GridPane.setConstraints(anchorPane, columns, rows);
        gridPane.getChildren().add(anchorPane);

        int level = Integer.parseInt(lvl);
        String levelFile = "lvl" + lvl;
        boolean isAccessible = isLevelAccessible("Saves/"+saveString, levelFile);

     // Définition du style du bouton en fonction de l'accessibilité du niveau
        if (isAccessible) {
            btn.setStyle("-fx-background-color: #ADD8E6;"); // Bleu clair
        } else {
            btn.setStyle("-fx-background-color: #E74C3C; -fx-background-image: url('file:../images/cadenas2.png'); -fx-background-repeat: no-repeat; -fx-background-position: center; -fx-background-size: 20px 20px;"); // Rouge avec image de cadenas
        }


        // Configuration de l'action à effectuer lorsque le bouton est cliqué
        btn.setOnAction(event -> {
            if (isAccessible) {
                // Chargement du niveau si accessible
                loadLevel(level, primaryStage);
            } else {
                // Affichage d'une erreur si le niveau n'est pas accessible
                Alert alert = new Alert(AlertType.ERROR);
                alert.setTitle("Erreur d'accès au niveau");
                alert.setHeaderText(null);
                alert.setContentText("Vous n'avez pas encore accès à ce niveau.");
                alert.showAndWait();
            }
        });
    }

	private static boolean isLevelAccessible(String saveFile, String levelFile) {
		// Vérifier la sauvegarde pour voir si le niveau est accessible
		try {
			// Lire le contenu du fichier de sauvegarde
			List<String> saveLines = Files.readAllLines(Path.of(saveFile));

			// Rechercher le niveau correspondant dans le fichier de sauvegarde
			for (String saveLine : saveLines) {
				String[] saveData = saveLine.split(",");
				String saveLevel = saveData[0];
				String saveAccessible = saveData[1];

				// Vérifier si le niveau correspond au niveau recherché
				if (saveLevel.equals(levelFile)) {
					return Boolean.parseBoolean(saveAccessible);
				}
			}
		} catch (IOException e) {
			System.out.println("Erreur lors de la lecture du fichier de sauvegarde : " + saveFile);
			e.printStackTrace();
		}

		// Par défaut, considérer le niveau comme inaccessible
		return false;
	}
}
