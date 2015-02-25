
public class Goalie {

	private String name;
	private int wins, shutouts, saves;
	private double fantasyValue;
	
	public Goalie(String gname, int w, int so, int ga, int sa) {
		name = gname;
		wins = w;
		shutouts = so;
		saves = sa - ga;
		fantasyValue = 2*wins+10*shutouts+0.15*saves;
	}
	
	public String getName() {
		return name;
	}
	
	public int getWins() {
		return wins;
	}
	
	public int getShutouts() {
		return shutouts;
	}
	
	public int getSaves() {
		return saves;
	}
	
	public double getFantasyValue() {
		return fantasyValue;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void setWins(int wins) {
		this.wins = wins;
	}
	
	public void setShutouts(int shutouts) {
		this.shutouts = shutouts;
	}
	
	public void setSaves(int saves) {
		this.saves = saves;
	}
}
