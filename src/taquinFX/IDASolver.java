package taquinFX;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class IDASolver {
    private Board board;               // Plateau de jeu
    private short[] targetGrid;        // Configuration cible
    private List<String> solution;     // Solution trouvée

    public IDASolver(Board board, String csvPath) {
        this.board = board;
        this.targetGrid = readTargetGridFromCSV(csvPath);
        this.solution = null;
    }

    public List<String> solve() {
        // Conversion du tableau de cases en tableau de nombres courts (short)
        Node initialNode = new Node(TaquinSolver.convertBoxArrayToShortArray(board.getGrid()), null, "", targetGrid);
        int threshold = initialNode.getHeuristic();    // Seuil initial déterminé par l'heuristique du nœud initial

        while (true) {
            int nextThreshold = search(initialNode, 0, threshold);   // Recherche itérative avec augmentation du seuil
            if (nextThreshold == 0) {
                return solution;     // Solution trouvée
            }
            if (nextThreshold == Integer.MAX_VALUE) {
                return null;        // Aucune solution trouvée
            }
            threshold = nextThreshold;   // Mise à jour du seuil
        }
    }

    private int search(Node node, int cost, int threshold) {
        int f = cost + node.getHeuristic();     // Coût réel du nœud + valeur heuristique du nœud
        if (f > threshold) {
            return f;       // Le coût total dépasse le seuil, arrêt de la recherche pour cette branche
        }
        if (node.getHeuristic() == 0) {
            solution = getPath(node);   // Solution trouvée, enregistrement du chemin
            return 0;
        }

        int minThreshold = Integer.MAX_VALUE;
        List<Node> nextNodes = getNextNodes(node);     // Génération des nœuds enfants
        for (Node nextNode : nextNodes) {
            int nextThreshold = search(nextNode, cost + 1, threshold);    // Recherche récursive avec augmentation du coût
            if (nextThreshold == 0) {
                return 0;       // Solution trouvée, arrêt de la recherche
            }
            minThreshold = Math.min(minThreshold, nextThreshold);   // Mise à jour du seuil minimum
        }
        return minThreshold;
    }

    private List<Node> getNextNodes(Node currentNode) {
        List<Node> nextNodes = new ArrayList<>();
        List<short[]> emptyBoxes = TaquinSolver.findEmptyBoxes(currentNode.getGrid());

        if (emptyBoxes.size() > 1) {
            // Si plusieurs cases vides existent, sélectionner une seule case vide pour générer les mouvements
            short[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1), targetGrid);
                nextNodes.add(newNode);
            }
        } else {
            // Si une seule case vide existe, générer tous les mouvements possibles
            short[] emptyBox = emptyBoxes.get(0);
            int emptyIndex = emptyBox[0] * board.getBoardSize() + emptyBox[1];

            // Essaie de déplacer la case vide vers le haut
            if (emptyBox[0] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "UP " + emptyIndex + " " + (emptyIndex - board.getBoardSize()), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers le bas
            if (emptyBox[0] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + board.getBoardSize());
                Node newNode = new Node(newGrid, currentNode, "DOWN " + emptyIndex + " " + (emptyIndex + board.getBoardSize()), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la gauche
            if (emptyBox[1] > 0) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex - 1);
                Node newNode = new Node(newGrid, currentNode, "LEFT " + emptyIndex + " " + (emptyIndex - 1), targetGrid);
                nextNodes.add(newNode);
            }

            // Essaie de déplacer la case vide vers la droite
            if (emptyBox[1] < board.getBoardSize() - 1) {
                short[] newGrid = currentNode.getGrid().clone();
                swap(newGrid, emptyIndex, emptyIndex + 1);
                Node newNode = new Node(newGrid, currentNode, "RIGHT " + emptyIndex + " " + (emptyIndex + 1), targetGrid);
                nextNodes.add(newNode);
            }
        }

        return nextNodes;
    }

    private void swap(short[] grid, int index1, int index2) {
        // Échange de deux cases dans le tableau
        short temp = grid[index1];
        grid[index1] = grid[index2];
        grid[index2] = temp;
    }

    private short[] readTargetGridFromCSV(String csvPath) {
        // Lecture de la configuration cible à partir d'un fichier CSV
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
