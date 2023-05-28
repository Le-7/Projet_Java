package taquinFX;

import java.util.Arrays;

class Node implements Comparable<Node> {
	private short[] grid; // Le plateau du jeu associé à ce nœud
	private Node parent; // Le nœud parent dans l'arbre de recherche
	private String move; // Le mouvement effectué pour atteindre ce nœud à partir du nœud parent
	private int cost; // Le coût du chemin depuis le nœud initial jusqu'à ce nœud
	private int heuristic; // L'estimation heuristique du coût restant jusqu'à la solution
	private short[] targetGrid;

	public Node(short[] grid, Node parent, String move, short[] targetGrid) {
		this.grid = grid;
		this.parent = parent;
		this.move = move;
		this.cost = parent != null ? parent.getCost() + 1 : 0; // Le coût est le coût du parent + 1
		this.targetGrid = targetGrid;
		this.heuristic = calculateHeuristic(); // Calculer l'estimation heuristique
	}

	public short[] getGrid() {
		return grid;
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
		int boardSize = (int) Math.sqrt(grid.length);
		for (int i = 0; i < grid.length; i++) {
			int value = grid[i];
			int targetRow = 0;
			int targetCol = 0;
			if (value > 0) {
				for (int j = 0; j < targetGrid.length; j++) {
					if (targetGrid[j] == value) {
						targetRow = j / boardSize;
						targetCol = j % boardSize;
					}
				}

				int currentRow = i / boardSize; // La ligne actuelle pour cette valeur
				int currentCol = i % boardSize; // La colonne actuelle pour cette valeur
				heuristic += Math.abs(currentRow - targetRow) + Math.abs(currentCol - targetCol); // Calcul de la
																									// distance de
																									// Manhattan
			}
		}

		return heuristic;
	}

	// Récupère le chemin de l'initial node à ce nœud
	public String getPath() {
		if (parent == null) {
			return "";
		}
		return parent.getPath() + " " + move;
	}

	// Comparaison des nœuds en fonction de leur priorité (coût + estimation
	// heuristique)
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
		return Arrays.equals(grid, other.getGrid());
	}

	@Override
	public int hashCode() {
		return Arrays.hashCode(grid);
	}
}
