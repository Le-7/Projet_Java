import javafx.application.Application;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class TaquinFX extends Application {

    private int[][] board;
    private int boardSize;
    private int emptyRow;
    private int emptyCol;
    private GridPane grid;

    @Override
    public void start(Stage primaryStage) {
        boardSize = 3;
        board = new int[boardSize][boardSize];

        shuffleBoard();

        grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setHgap(10);
        grid.setVgap(10);

        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
            	Button button = new Button(board[i][j] == 0 ? "" : Integer.toString(board[i][j]));
                button.setPrefSize(50, 50);
                button.setOnAction(event -> handleMove(button));
                grid.add(button, j, i);
            }
        }
        
        Button solveButton = new Button("Résoudre");
        solveButton.setOnAction(event -> solveWithRandomWalk());
        grid.add(solveButton, 0, boardSize);
        GridPane root = new GridPane();
        root.setAlignment(Pos.CENTER);
        root.setHgap(10);
        root.setVgap(10);
        root.add(grid, 0, 0);
        root.add(solveButton, 0, 1);
        updateButtonStates();
        Scene scene = new Scene(root, 300, 300);
        primaryStage.setTitle("Taquin");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public void shuffleBoard() {
        // Initialise le plateau avec les chiffres 1 à N^2-1, plus un espace vide
        List<Integer> numbers = new ArrayList<>();
        for (int i = 1; i < boardSize * boardSize; i++) {
            numbers.add(i);
        }
        numbers.add(0);
        Collections.shuffle(numbers);

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

    private void handleMove(Button button) {
        int row = GridPane.getRowIndex(button);
        int col = GridPane.getColumnIndex(button);

        if (isValidMove(row, col)) {
            swap(row, col);
            updateBoard();
            updateButtonStates();
            if (isSolved()) {
                showCompletionDialog();
            }
        }
    }

    private void updateButtonStates() {
        for (int i = 0; i < grid.getChildren().size(); i++) {
            Button button = (Button) grid.getChildren().get(i);
            int row = GridPane.getRowIndex(button);
            int col = GridPane.getColumnIndex(button);
            if (isValidMove(row, col)) {
                button.setDisable(false);
                button.setStyle("-fx-opacity: 1; -fx-color: lightgreen;");
                
            } else {
                button.setDisable(true);
                button.setStyle("-fx-opacity: 0.7;");

            }
        }
    }

    private boolean isValidMove(int row, int col) {
        return ((row == emptyRow && Math.abs(col - emptyCol) == 1) ||
                (col == emptyCol && Math.abs(row - emptyRow) == 1));
    }

    private void swap(int row, int col) {
        int temp = board[row][col];
        board[row][col] = board[emptyRow][emptyCol];
        board[emptyRow][emptyCol] = temp;
        emptyRow = row;
        emptyCol = col;
    }

    private void updateBoard() {
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                Button button = (Button) grid.getChildren().get(i* boardSize + j);
                if (board[i][j] != 0) {
                	button.setText("" + board[i][j]);
				}else {
					button.setText("");
				}
                button.setOnAction(event -> handleMove(button));
                grid.getChildren().set(i * boardSize + j, button);
            }
        }
    }

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

    private void showCompletionDialog() {
        Dialog<Void> dialog = new Dialog<>();
        dialog.setTitle("Taquin");
        dialog.setHeaderText("Félicitations ! Vous avez terminé le jeu !");
        Button okButton = new Button("OK");
        okButton.setOnAction(event -> dialog.close());
        dialog.getDialogPane().setContent(new StackPane(new Label("Vous êtes un génie !")));
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);
        dialog.showAndWait();
    }
    private void solveWithRandomWalk() {
        int maxIterations = 1000;
        int iteration = 0;
        while (iteration < maxIterations && !isSolved()) {
            List<Button> validMoves = new ArrayList<>();
            for (int i = 0; i < grid.getChildren().size(); i++) {
                Button button = (Button) grid.getChildren().get(i);
                int row = GridPane.getRowIndex(button);
                int col = GridPane.getColumnIndex(button);
                if (isValidMove(row, col)) {
                    validMoves.add(button);
                }
            }
            if (!validMoves.isEmpty()) {
                int randomIndex = (int) (Math.random() * validMoves.size());
                handleMove(validMoves.get(randomIndex));
            }
            iteration++;
        }
        if (isSolved()) {
            showCompletionDialog();
        }
    }


    public static void main(String[] args) {
        launch(args);
    }
}

