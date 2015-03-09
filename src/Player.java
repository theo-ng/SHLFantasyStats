
public abstract class Player {
	private String name;
	private char position;
	private double fantasyValue;

	public String getName() {
		return name;
	}
	
	public char getPosition() {
		return position;
	}
	
	public double getFantasyValue() {
		return fantasyValue;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPosition(char pos) {
		position = pos;
	}
}
