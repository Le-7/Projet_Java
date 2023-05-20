package taquinFX;

import java.util.*;

public class TaquinSolver {
    private Board board;
    private Queue<Node> openList;
    private Set<Node> closedSet;
    private Set<String> visitedSet;



    public TaquinSolver(Board board) {
        this.board = board;
        openList = new PriorityQueue<>();
        closedSet = new HashSet<>();
        visitedSet = new HashSet<>();
    }

    public List<String> solve() {
        // Crée un nœud initial à partir du plateau donné et l'ajoute à la liste ouverte
        Node initialNode = new Node(convertBoxArrayToIntArray(board.getGrid()), null, "");
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
            if (Main.gameSolved(currentNode.getGrid(),board.getCsvPath())) {
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
        List<int[]> emptyBoxes = findEmptyBoxes(currentNode.getGrid());
        if (emptyBoxes.size() > 1) {
            // Si plusieurs cases vides existent, sélectionner une seule case vide pour générer les mouvements
            int[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1));
                nextNodes.add(newNode);
            }
        } else {
            // Si une seule case vide existe, générer les mouvements pour cette case
            int[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                int[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1));
                nextNodes.add(newNode);
            }
        }

        return nextNodes;
    }

    private void swap(int[] newGrid, int index1, int index2) {
        int temp = newGrid[index1];
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

    public static List<int[]> findEmptyBoxes(int[] grid) {
        List<int[]> emptyBoxes = new ArrayList<>();
        int boardSize = (int) Math.sqrt(grid.length);
        for (int i = 0; i < grid.length; i++) {
            if (grid[i] == 0) {
                int row = i / boardSize;
                int col = i % boardSize;
                emptyBoxes.add(new int[]{row, col});
            }
        }
        return emptyBoxes;
    }
    public static int[] convertBoxArrayToIntArray(Box[] boxes) {
        int[] intArray = new int[boxes.length];
        for (int i = 0; i < boxes.length; i++) {
            intArray[i] = boxes[i].getValue();
        }
        return intArray;
    }
    private String convertGridToString(int[] grid) {
        StringBuilder sb = new StringBuilder();
        for (int value : grid) {
            sb.append(value);
            sb.append("-");
        }
        return sb.toString();
    }


}
