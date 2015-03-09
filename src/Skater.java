
public class Skater extends Player{

	private String name;
	private char position;
	private int goals, assists, points, hits, blocks;
	private double fantasyValue;
	
	public Skater(String pname, int g, int a, int p, int h, int b) {
		name = pname;
		position = 'F';
		goals = g;
		assists = a;
		points = p;
		hits = h;
		blocks = b;
		fantasyValue = 4*goals+2*assists+points+0.4*hits+1.5*blocks;
	}
	
	public String getName() {
		return name;
	}
	
	public char getPosition() {
		return position;
	}
	
	public int getGoals() {
		return goals;
	}
	
	public int getAssists() {
		return assists;
	}

	public int getPoints() {
		return points;
	}

	public int getHits() {
		return hits;
	}

	public int getBlocks() {
		return blocks;
	}

	public double getFantasyValue() {
		return fantasyValue;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setPosition(char c) {
		this.position = c;
	}
	
	public void setGoals(int goals) {
		this.goals = goals;
	}
	
	public void setAssists(int assists) {
		this.assists = assists;
	}

	public void setPoints(int points) {
		this.points = points;
	}

	public void setHits(int hits) {
		this.hits = hits;
	}

	public void setBlocks(int blocks) {
		this.blocks = blocks;
	}
}
