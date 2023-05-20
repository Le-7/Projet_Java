package taquinFX;

import java.util.Objects;

class Node implements Comparable<Node> {
    private Board board; // Le plateau du jeu associé à ce nœud
    private Node parent; // Le nœud parent dans l'arbre de recherche
    private String move; // Le mouvement effectué pour atteindre ce nœud à partir du nœud parent
    private int cost; // Le coût du chemin depuis le nœud initial jusqu'à ce nœud
    private int heuristic; // L'estimation heuristique du coût restant jusqu'à la solution

    public Node(Board board, Node parent, String move) {
        this.board = board;
        this.parent = parent;
        this.move = move;
        this.cost = parent != null ? parent.getCost() + 1 : 0; // Le coût est le coût du parent + 1
        this.heuristic = calculateHeuristic(); // Calculer l'estimation heuristique
    }

    public Board getBoard() {
        return board;
    }

    public Node getParent() {
        return parent;
    }

    public String getMove() {
        return move;
    }

    public int getCost() {
        return cost;
    }

    public int getHeuristic() {
        return heuristic;
    }

    // Calcule l'estimation heuristique du coût restant jusqu'à la solution
    private int calculateHeuristic() {
        int heuristic = 0;
        
        for (int i = 0; i < board.getGrid().length; i++) {
            int value = board.getGrid()[i].getValue();
            if (value != 0) {
                int targetRow = (value - 1) / board.getBoardSize(); // La ligne cible pour cette valeur
                int targetCol = (value - 1) % board.getBoardSize(); // La colonne cible pour cette valeur
                int currentRow = i / board.getBoardSize(); // La ligne actuelle pour cette valeur
                int currentCol = i % board.getBoardSize(); // La colonne actuelle pour cette valeur
                heuristic += Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol); // Calcul de la distance de Manhattan
            }
        }

        return heuristic;
    }


    // Comparaison des nœuds en fonction de leur priorité (coût + estimation heuristique)
    @Override
    public int compareTo(Node other) {
        int priority1 = cost + heuristic;
        int priority2 = other.getCost() + other.getHeuristic();
        return Integer.compare(priority1, priority2);
    }

    // Vérifie si deux nœuds sont égaux en comparant les plateaux de jeu
    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Node other = (Node) obj;
        return board.equals(other.getBoard());
    }

    // Calcule le code de hachage en utilisant le plateau de jeu
    @Override
    public int hashCode() {
        return Objects.hash(board);
    }
}
