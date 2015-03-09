import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class FanGroup {

	private List<Map<String, Double>> standings = new ArrayList<Map<String, Double>>();
	
	public FanGroup(List<FanTeam> list) {
		Map<String, Double> map = new HashMap<String, Double>();
		for(int i=0;i<list.size();i++) {
			map.put(list.get(i).getOwner(), list.get(i).getScore());
			standings.add(map);
		}
	}
}
