package taquinFX;
public class Box{
	
	private int value;
	private boolean movable;
	private String display;
	
	public Box(int value, boolean movable, String display) {
		this.value = value;
		this.movable = movable;
		this.display=display;
	}
	
	public int getValue() {
		return value;
	}
	
	public boolean isMovable() {
		return movable;
	}
	
	public String getDisplay() {
		return display;
		
	}
}