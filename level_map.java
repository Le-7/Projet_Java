// Importation des packages nécessaires
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.stage.Stage;


public class level_map extends Application {
    private static final String IMAGE_PATH = "file:images/map_java.jpg"; // Chemin de l'image
    private static final String LOCKED_IMAGE_PATH = "file:images/cadenas2.png"; // image de cadenas pour les niveaux bloqué
    private static final int NUM_ROWS = 22; // Nombre de lignes de la grille
    private static final int NUM_COLS = 21; // Nombre de colonnes de la grille

    private static int unlockedLevel = 1; //maintenir le niveau débloqué par l'utilisateur
    @Override
    public void start(Stage primaryStage) {
        Image image = new Image(IMAGE_PATH); // Chargement de l'image
        ImageView imageView = new ImageView(image);// Création de l'ImageView pour afficher l'image
        imageView.setPreserveRatio(true); // Maintien du ratio de l'image

        GridPane gridPane = createGridPane();// Création de la grille
        gridPane.setGridLinesVisible(false);// on enleve l'affichage de la grille

        // Bouton 1
        Button button1 = createButton("1",1);

     // Ajout d'un gestionnaire d'événements au bouton
     button1.setOnAction(new EventHandler<ActionEvent>() {
                  @Override
         public void handle(ActionEvent event) {
             Platform.runLater(new Runnable() {
                 @Override
                 public void run() {
                     new TaquinFX().start(new Stage());  // Lancer TaquinFX
                 }
             });
         }
     });

     AnchorPane anchorPane1 = new AnchorPane(button1);
     GridPane.setConstraints(anchorPane1, 0, 10); // 1ère colonnes, 9ème lignes
     gridPane.getChildren().add(anchorPane1);

        // Bouton 2
        Button button2 = createButton("2",2);
        AnchorPane anchorPane2 = new AnchorPane(button2);
        AnchorPane.setRightAnchor(button2, 0.0); // Positionné à droite
        GridPane.setConstraints(anchorPane2, 1, 9); // 2eme colonnes, 8 lignes
        gridPane.getChildren().add(anchorPane2);

        // Bouton 3
        Button button3 = createButton("3",3);
        AnchorPane anchorPane3 = new AnchorPane(button3);
        GridPane.setConstraints(anchorPane3, 3, 10); // 3ème colonnes, 9ème lignes
        gridPane.getChildren().add(anchorPane3);

        // Bouton 4
        Button button4 = createButton("4",4);
        AnchorPane anchorPane4 = new AnchorPane(button4);
        GridPane.setConstraints(anchorPane4, 4, 12); // 4ème colonnes, 11ème lignes
        gridPane.getChildren().add(anchorPane4);

        // Bouton 5
        Button button5 = createButton("5",5);
        AnchorPane anchorPane5 = new AnchorPane(button5);
        GridPane.setConstraints(anchorPane5, 5, 14); // 4ème colonnes, 13ème lignes
        gridPane.getChildren().add(anchorPane5);

     // Bouton 6
        Button button6 = createButton("6",6);
        AnchorPane anchorPane6 = new AnchorPane(button6);
        GridPane.setConstraints(anchorPane6, 7, 14); // 6ème colonnes, 13ème lignes
        gridPane.getChildren().add(anchorPane6);

     // Bouton 7
        Button button7 = createButton("7",7);
        AnchorPane anchorPane7 = new AnchorPane(button7);
        AnchorPane.setRightAnchor(button7, 0.0); // Positionné à droite
        GridPane.setConstraints(anchorPane7, 8,12); // 7ème colonne, 11ème lignes
        gridPane.getChildren().add(anchorPane7);

     // Bouton 8
        Button button8 = createButton("8",8);
        AnchorPane anchorPane8 = new AnchorPane(button8);
        AnchorPane.setRightAnchor(button8, 0.0); // Positionné à droite
        GridPane.setConstraints(anchorPane8, 9, 10); // 8ème colonne, 9ème lignes
        gridPane.getChildren().add(anchorPane8);

     // Bouton 9
        Button button9 = createButton("9",9);
        AnchorPane anchorPane9 = new AnchorPane(button9);
        AnchorPane.setRightAnchor(button9, 0.0); // Positionné à droite
        GridPane.setConstraints(anchorPane9, 11, 9); // 10ème colonne, 8ème lignes
        gridPane.getChildren().add(anchorPane9);

        // Bouton 10
        Button button10 = createButton("10",10);
        AnchorPane anchorPane10 = new AnchorPane(button10);
        GridPane.setConstraints(anchorPane10, 13, 10); // 12ème colonne, 9ème lignes
        gridPane.getChildren().add(anchorPane10);
        
     // Bouton 11
        Button button11 = createButton("11",11);
        AnchorPane anchorPane11 = new AnchorPane(button11);
        GridPane.setConstraints(anchorPane11, 15, 9); // 14eme colonne, 8ème ligne
        gridPane.getChildren().add(anchorPane11);

     // Bouton 12
        Button button12 = createButton("12",12);
        AnchorPane anchorPane12 = new AnchorPane(button12);
        GridPane.setConstraints(anchorPane12, 17, 8); // 16ème colonne, 7ème ligne
        gridPane.getChildren().add(anchorPane12);

     // Bouton 13
        Button button13 = createButton("13",13);
        AnchorPane anchorPane13 = new AnchorPane(button13);
        AnchorPane.setRightAnchor(button13, 0.0); // Positionné à droite
        GridPane.setConstraints(anchorPane13, 19, 10); // 18eme colonne, 9ème ligne
        gridPane.getChildren().add(anchorPane13);


        StackPane stackPane = new StackPane(); // Création d'un StackPane pour superposer la grille et l'image
        stackPane.getChildren().addAll(imageView, gridPane); // Ajout de l'image et de la grille au StackPane


        Scene scene = new Scene(stackPane); // Création de la scène avec le StackPane comme nœud racine
        primaryStage.setScene(scene); // Ajout de la scène au primaryStage
        primaryStage.setResizable(false);// Rend la fenêtre non redimensionnable
        primaryStage.show(); // Affichage de la fenêtre


        // Redimensionnement de la grille lorsque l'ImageView est
        imageView.fitWidthProperty().addListener((obs, oldVal, newVal) -> {
            gridPane.setPrefWidth(newVal.doubleValue());
            gridPane.setMaxWidth(newVal.doubleValue());
        });
        imageView.fitHeightProperty().addListener((obs, oldVal, newVal) -> {
            gridPane.setPrefHeight(newVal.doubleValue());
            gridPane.setMaxHeight(newVal.doubleValue());
        });
    }

    private Button createButton(String text, int level) {
        Button button = new Button(text); // Création d'un bouton avec le texte spécifié
        if (level <= unlockedLevel) {// si le niveau est debloqué 
            // Mise en forme du bouton pour le rendre rond
            button.setStyle("-fx-background-radius: 100; "
                    + "-fx-min-width: 62; "
                    + "-fx-min-height: 60; "
                    + "-fx-max-width: 70; "
                    + "-fx-max-height: 60;"
                    + "-fx-background-color: rgba(211,211, 255, 0.9)");
        }else {
            button = createLockedButton(); // sinon les boutons vont avoir des cadenas
        }
        return button;
    }

    private Button createLockedButton() {
        // Création d'un bouton avec une image de cadenas
        Image image = new Image(LOCKED_IMAGE_PATH);
        ImageView imageView = new ImageView(image);

        // Définir la taille de l'image
        imageView.setFitWidth(50);  // la largeur de l'image
        imageView.setFitHeight(60); // la hauteur de l'image
        imageView.setPreserveRatio(true);  // conserver le ratio de l'image

        Button button = new Button("", imageView);
        button.setStyle("-fx-background-radius: 100; "
                + "-fx-min-width: 22; "
                + "-fx-min-height: 50; "
                + "-fx-max-width: 60; "
                + "-fx-max-height: 60;"
                + "-fx-background-color: rgba(211,211, 255, 0.9)");
        return button;
    }

    private GridPane createGridPane() { 
        GridPane gridPane = new GridPane();// Création d'un nouveau GridPane
        for (int i = 0; i < NUM_COLS; i++) { // Définition des colonnes de la grille
            ColumnConstraints colConstraints = new ColumnConstraints();
            colConstraints.setPercentWidth(100.0 / NUM_COLS);
            gridPane.getColumnConstraints().add(colConstraints);
        }
        // Définition des lignes de la grille
        for (int i = 0; i < NUM_ROWS; i++) {
            RowConstraints rowConstraints = new RowConstraints();
            rowConstraints.setPercentHeight(100.0 / NUM_ROWS);
            gridPane.getRowConstraints().add(rowConstraints);
        }
        // Centrage du contenu de la grille
        gridPane.setAlignment(Pos.CENTER);
        // Affichage des lignes de la grille pour une meilleure visualisation
        gridPane.setGridLinesVisible(true);
        // Retour de la grille créée
        return gridPane;
    }

    // Méthode main pour lancer l'application
    public static void main(String[] args) {
        launch(args);
    }
}
