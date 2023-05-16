import javafx.application.Application;
import javafx.geometry.HPos;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;

import java.util.*;

public class TaquinFX extends Application {

    private int[][] board; // Plateau du jeu de Taquin
    private int boardSize; // Taille du tableau     
    private int emptyRow; // Indices de l'emplacement vide 
    private int emptyCol;
    private GridPane grid; // Grille d'affichage du jeu 
    private static final String IMAGE_PATH = "file:images/noel1.jpg"; // Chemin de l'image
    private int var_score = 0;
    private Label score;

    @Override
    public void start(Stage primaryStage) {
        boardSize = 3; // Initialisation de la taille du plateau à 3
        board = new int[boardSize][boardSize]; // Initialisation du tableau du plateau à une taille de 3x3
       
        shuffleBoard(); // Mélange du tableau 

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10); // Espacement horizontal entre les boutons
        grid.setVgap(10); // Espacement vertical entre les boutons

        // Création des boutons sur le plateau
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button button = new Button(board[i][j] == 0 ? "" : Integer.toString(board[i][j]));
                button.setPrefSize(100, 100);
                button.setStyle("-fx-background-color: rgba(0, 255, 250, 0.7);");
                button.setOnAction(event -> handleMove(button));
                grid.add(button, j, i);
            }
        }

        Button solveButton = new Button("Résoudre");
        solveButton.setOnAction(event -> solveWithRandomWalk());
        
        score = new Label("Votre score est de : " + Integer.toString(var_score));
        score.setStyle("-fx-font-size: 16px;");
        
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10); // Espacement horizontal entre les éléments 
        root.setVgap(10); // Espacement vertical entre les éléments
        root.setStyle("-fx-background-image: url('" + IMAGE_PATH + "'); -fx-background-size: cover;"); // Image de fond
        //root.setStyle("-fx-background-color: lightblue;"); // Couleur de fond du root
        root.add(grid, 0, 0); // Ajout de la grille à la première colonne et première ligne 
        root.add(score, 1, 0); // Ajout du label score à la deuxième colonne et première ligne 
        GridPane.setHalignment(score, HPos.LEFT); // Alignement à gauche du label score
        root.add(solveButton, 0, 1); // Ajout du bouton résoudre à la première colonne et deuxième ligne 

        updateButtonStates(); // Mise à jour des états des boutons

        Scene scene = new Scene(root, 600, 600); // Création de la scène avec une largeur et hauteur de 600
        primaryStage.setTitle("Taquin");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    //méthode pour melanger le plateau 
    public void shuffleBoard() {
        // Initialise le plateau avec les chiffres 1 à N^2-1, plus un espace vide
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < boardSize * boardSize; i++) {
            numbers.add(i);
        }
        numbers.add(0);
        Collections.shuffle(numbers);
// attribution des nombres mélangés au plateau 
        int index = 0;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                board[i][j] = numbers.get(index);
                if (board[i][j] == 0) {
                    emptyRow = i;
                    emptyCol = j;
                }
                index++;
            }
        }
    }  
//recuperation l'indice de la ligne et de la colonne du bouton cliqué 
    private void handleMove(Button button) {
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);
// si le mouvement est valide alors on effectue le mouvement et il met a jours le plateau 
        if (isValidMove(row, col)) {
            swap(row, col);
            updateBoard();
            updateButtonStates();
// si le plateau est resolu, on affiche le dialogue de fin 
            if (isSolved()) {
                showCompletionDialog();
            }
        }
    }
//parcourir toutes les cases et les deplacer en fonction de si le mouvement est valide 
    private void updateButtonStates() { 
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Button button = (Button) grid.getChildren().get(i);
            int row = GridPane.getRowIndex(button);
            int col = GridPane.getColumnIndex(button);
            if (isValidMove(row, col)) {
                button.setDisable(false);
                
            } else {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 0.7;");

            }
            
        }
    }
//le mouvement est valide si le bouton est adjacent à l'emplacement vide 
    private boolean isValidMove(int row, int col) {
        return ((row == emptyRow && Math.abs(col - emptyCol) == 1) ||
                (col == emptyCol && Math.abs(row - emptyRow) == 1));
    }
    
//echanger la position de la case selectionné avec l'emplacement vide
    private void swap(int row, int col) {
        int temp = board[row][col];
        board[row][col] = board[emptyRow][emptyCol];
        board[emptyRow][emptyCol] = temp;
        emptyRow = row;
        emptyCol = col;
        var_score ++;
        score.setText("Votre score est de : "+ Integer.toString(var_score));
    }
    
//mettre a jours le  de tous les boutons pour refleter l'etat actuel du plateau
    private void updateBoard() { 
        for (int i = 0; i < boardSize; i++) { //parcours chaque ligne du tableau 
            for (int j = 0; j < boardSize; j++) { //parcours chaque colonne du tableau -> accede a chaque case du tableau
                Button button = (Button) grid.getChildren().get(i* boardSize + j);//
                if (board[i][j] != 0) {
                	button.setText("" + board[i][j]);
				}else {
					button.setText("");
				}
                button.setOnAction(event -> handleMove(button));//gestionnaire d evenement qui est appelé lorsque l'utilisateur clique sur le bouton 
                grid.getChildren().set(i * boardSize + j, button);
            }
        }
    }
//verifie si le plateau est resolu en vérifiant si toutes les cases sont a leurs place 
    private boolean isSolved() {
        int index = 1;
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                if (board[i][j] != index % (boardSize * boardSize)) {
                    return false;
                }
                index++;
            }
        }
        return true;
    }
//afficher un message indiquant que le jeu est terminé 
    private void showCompletionDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Taquin");
        dialog.setHeaderText("Félicitations ! Vous avez terminé le jeu !");
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> dialog.close());
        dialog.getDialogPane().setContent(new StackPane(new Label("Vous êtes un génie !\n\nVous avez résussi la partie en " + Integer.toString(var_score) + " coups.")));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
    
    private void solveWithRandomWalk() {
        int maxIterations = 1000; // nombre maximal d'itération que l'algorithme va effectuer 
        int iteration = 0; //initialisation du compteur à 0
//une boucle qui permet de continuer les mouvements tant qu'il n'a pas atteint le nombre maximal d'itération et que le jeu n'est ps fini
        while (iteration < maxIterations && !isSolved()) {
        //creation d une liste pour stocker les cases qui peuvent etre déplacés et qui sont valide
            List<Button> validMoves = new ArrayList<>();
            //parcours de toutes les cases de la grille
            for (int i = 0; i < grid.getChildren().size(); i++) {
                Button button = (Button) grid.getChildren().get(i);
                //recuperation des coordonnées de la grille 
                int row = GridPane.getRowIndex(button);
                int col = GridPane.getColumnIndex(button);
                //si le mouvement de la case est valide, ajout de la case à la liste des mouvements valide
                if (isValidMove(row, col)) {
                    validMoves.add(button);
                }
            }
            //si la liste des mouvements valides n'est pas vide,on effectue un mouvement aleatoire parmi les mouvements valide
            if (!validMoves.isEmpty()) {
                int randomIndex = (int) (Math.random() * validMoves.size());
                handleMove(validMoves.get(randomIndex));
            }
            //incrementer le compteur
            iteration++;
        }
        //si le jeu est resolu, afficher le message de fin
        if (isSolved()) {
            showCompletionDialog();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}
