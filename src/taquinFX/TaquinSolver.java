package taquinFX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class TaquinSolver {
    private Board board;
    private Queue<Node> openList;
    private Set<Node> closedSet;
    private Set<String> visitedSet;
    private short[] targetGrid;

    public TaquinSolver(Board board,String csvPath) {
        this.board = board;
        openList = new PriorityQueue<>();
        closedSet = new HashSet<>();
        visitedSet = new HashSet<>();
        targetGrid = readTargetGridFromCSV(csvPath);
    }

    public List<String> solve() {
        // Crée un nœud initial à partir du plateau donné et l'ajoute à la liste ouverte
    	 Node initialNode = new Node(convertBoxArrayToShortArray(board.getGrid()), null, "");
         openList.add(initialNode);

         while (!openList.isEmpty()) {
             // Récupère le nœud actuel à partir de la liste ouverte et l'ajoute à l'ensemble fermé
             Node currentNode = openList.poll();
             String gridString = convertGridToString(currentNode.getGrid());
             if (!visitedSet.contains(gridString)) {
                 visitedSet.add(gridString);
                 openList.add(currentNode);
             }

             // Vérifie si le plateau du nœud actuel correspond à un plateau résolu
             if (gameSolved(currentNode.getGrid())) {
                 return getPath(currentNode); // Retourne le chemin jusqu'au nœud actuel
             }

             // Obtient les prochains nœuds à partir du nœud actuel
             List<Node> nextNodes = getNextNodes(currentNode);
             for (Node nextNode : nextNodes) {
                 // Ajoute les prochains nœuds à la liste ouverte s'ils ne sont pas dans l'ensemble fermé
                 if (!closedSet.contains(nextNode)) {
                     openList.add(nextNode);
                 }
             }
         }

         return null; // Aucune solution trouvée
     }

    private List<Node> getNextNodes(Node currentNode) {
        List<Node> nextNodes = new ArrayList<>();
        List<short[]> emptyBoxes = findEmptyBoxes(currentNode.getGrid());
        if (emptyBoxes.size() > 1) {
            // Si plusieurs cases vides existent, sélectionner une seule case vide pour générer les mouvements
            short[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1));
                nextNodes.add(newNode);
            }
        } else {
            // Si une seule case vide existe, générer les mouvements pour cette case
            short[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1));
                nextNodes.add(newNode);
            }
        }

        return nextNodes;
    }

    private void swap(short[] newGrid, int index1, int index2) {
        short temp = newGrid[index1];
        newGrid[index1] = newGrid[index2];
        newGrid[index2] = temp;
    }

    private List<String> getPath(Node node) {
        // Retourne le chemin à partir du nœud final jusqu'au nœud initial
        List<String> path = new ArrayList<>();
        while (node.getParent() != null) {
            path.add(0, node.getMove());
            node = node.getParent();
        }
        return path;
    }

    public static List<short[]> findEmptyBoxes(short[] grid) {
        List<short[]> emptyBoxes = new ArrayList<>();
        int boardSize = (int) Math.sqrt(grid.length);
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0) {
                int row = i / boardSize;
                int col = i % boardSize;
                emptyBoxes.add(new short[]{(short) row, (short) col});
            }
        }
        return emptyBoxes;
    }

    public static short[] convertBoxArrayToShortArray(Box[] boxes) {
        short[] shortArray = new short[boxes.length];
        for (int i = 0; i < boxes.length; i++) {
            shortArray[i] = (short) boxes[i].getValue();
        }
        return shortArray;
    }

    private String convertGridToString(short[] grid) {
        StringBuilder sb = new StringBuilder();
        for (short value : grid) {
            sb.append(value);
            sb.append("-");
        }
        return sb.toString();
    }
    private short[] readTargetGridFromCSV(String csvPath) {
        try {
            List<Short> gridValues = new ArrayList<>();
            BufferedReader reader = new BufferedReader(new FileReader(csvPath));
            String line;

            while ((line = reader.readLine()) != null) {
                String[] values = line.split(",");
                for (String value : values) {
                    gridValues.add(Short.parseShort(value));
                }
            }

            reader.close();

            short[] grid = new short[gridValues.size()];
            for (int i = 0; i < gridValues.size(); i++) {
                grid[i] = gridValues.get(i);
            }

            return grid;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean gameSolved(short[] grid) {
        if (targetGrid.length != grid.length) {
            return false; // Les grilles n'ont pas la même taille
        }

        for (int i = 0; i < grid.length; i++) {
            if (targetGrid[i] != grid[i]) {
                return false; // Les valeurs des cases sont différentes
            }
        }

        return true; // Les grilles sont identiques
    }
}
