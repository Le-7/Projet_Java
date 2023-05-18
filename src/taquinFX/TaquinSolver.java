package taquinFX;

import java.util.*;

public class TaquinSolver {
    private Board board;
    private Queue<Node> openList;
    private Set<Node> closedSet;

    public TaquinSolver(Board board) {
        this.board = board;
        openList = new PriorityQueue<>();
        closedSet = new HashSet<>();
    }

    public List<String> solve() {
        // Crée un nœud initial à partir du plateau donné et l'ajoute à la liste ouverte
        Node initialNode = new Node(board, null, "");
        openList.add(initialNode);

        while (!openList.isEmpty()) {
            // Récupère le nœud actuel à partir de la liste ouverte et l'ajoute à l'ensemble fermé
            Node currentNode = openList.poll();
            closedSet.add(currentNode);

            // Vérifie si le plateau du nœud actuel correspond à un plateau résolu
            if (currentNode.getBoard().gameSolved()) {
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
        List<int[]> emptyBoxes = currentNode.getBoard().findEmptyBoxes();
        if (emptyBoxes.size() > 1) {
            // Si plusieurs cases vides existent, sélectionner une seule case vide pour générer les mouvements
            int[] emptyBox = emptyBoxes.get(0);
            int emptyRow = emptyBox[0];
            int emptyCol = emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyRow > 0) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow - 1, emptyCol);
                Node newNode = new Node(newBoard, currentNode, "UP " + emptyRow + "," + emptyCol + " " + (emptyRow - 1) + "," + emptyCol);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyRow < board.getBoardSize() - 1) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow + 1, emptyCol);
                Node newNode = new Node(newBoard, currentNode, "DOWN " + emptyRow + "," + emptyCol + " " + (emptyRow + 1) + "," + emptyCol);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyCol > 0) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow, emptyCol - 1);
                Node newNode = new Node(newBoard, currentNode, "LEFT " + emptyRow + "," + emptyCol + " " + emptyRow + "," + (emptyCol - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyCol < board.getBoardSize() - 1) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow, emptyCol + 1);
                Node newNode = new Node(newBoard, currentNode, "RIGHT " + emptyRow + "," + emptyCol + " " + emptyRow + "," + (emptyCol + 1));
                nextNodes.add(newNode);
            }
        } else {
            // Si une seule case vide existe, générer les mouvements pour cette case
            int emptyRow = emptyBoxes.get(0)[0];
            int emptyCol = emptyBoxes.get(0)[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyRow > 0) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow - 1, emptyCol);
                Node newNode = new Node(newBoard, currentNode, "UP " + emptyRow + "," + emptyCol + " " + (emptyRow - 1) + "," + emptyCol);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyRow < board.getBoardSize() - 1) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow + 1, emptyCol);
                Node newNode = new Node(newBoard, currentNode, "DOWN " + emptyRow + "," + emptyCol + " " + (emptyRow + 1) + "," + emptyCol);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyCol > 0) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow, emptyCol - 1);
                Node newNode = new Node(newBoard, currentNode, "LEFT " + emptyRow + "," + emptyCol + " " + emptyRow + "," + (emptyCol - 1));
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyCol < board.getBoardSize() - 1) {
                Board newBoard = new Board(currentNode.getBoard());
                newBoard.swap2(emptyRow, emptyCol, emptyRow, emptyCol + 1);
                Node newNode = new Node(newBoard, currentNode, "RIGHT " + emptyRow + "," + emptyCol + " " + emptyRow + "," + (emptyCol + 1));
                nextNodes.add(newNode);
            }
        }

        return nextNodes;
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
}