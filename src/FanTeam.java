import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class FanTeam {

	private String owner;
	List<Player> fTeam = new ArrayList<Player>();
	private int changes;
	private double fScore;
	
	public FanTeam(String name, List<Player> team, Integer changes) {
		owner = name;
		fTeam = team;
		this.changes = changes;
		fScore = 0;
	}
	
	public String getOwner() {
		return owner;
	}
	
	public List<Player> getTeam() {
		return fTeam;
	}
	
	public int getChanges() {
		return changes;
	}
	
	public Double getScore() {
		return fScore;
	}
	
	public void setOwner(String name) {
		owner = name;
	}
	
	public void setTeam(List<Player> newteam) {
		fTeam = newteam;
	}
	
	public void setChanges(Integer value) {
		changes = value;
	}
	
	public void calculateScore(Map<String, Double> skaters, Map<String, Double> goalies) {
		fScore = changes;
		for(int i=0;i<fTeam.size()-1;i++) {
			fScore += skaters.get(fTeam.get(i));
		}
		fScore += goalies.get(fTeam.get(fTeam.size()));
	}
}
