package taquinFX;
public class Box{
	
	private int value; //valeur de la case
	private boolean movable;// si la case peut etre deplacé ou non
	private String display; //facon dont la case sera affiché
	
	//constructeur pour créer une instance "BOX" avec une valeur, capacité de mouvement et un affichage
	public Box(int value, boolean movable, String display) {
		this.value = value; //initialisation de la valeur
		this.movable = movable; // initialisation de movable
		this.display=display;
	}
	
//methode pour obtenir la valeur de la case
	public int getValue() {
		return value;
	}
//methode pour verifier si la case est mobile
	public boolean isMovable() {
		return movable;
	}

	public String getDisplay() {
		return display;
		
	}
}