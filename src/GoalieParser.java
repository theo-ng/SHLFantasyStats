import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class GoalieParser {

	private static String s22url = "https://dl.dropboxusercontent.com/u/34714712/SHL%20S22MAIN/SHL-ProTeamScoring.html"; 

	public static void main(String[] args) throws IOException {
		print("Fetching %s...", s22url);
		Document doc = Jsoup.connect(s22url).get();
		Elements tstats = doc.select("pre");
		print("Parsing Goalie data");
		parseData(tstats);
		print("Exported to goalie.txt");
	}

	private static void print(String msg, Object... args) {
		System.out.println(String.format(msg, args));
	}

	private static String trim(String s, int width) {
		if (s.length() > width)
			return s.substring(0, width-1) + ".";
		else
			return s;
	}
	private static void writeFile(Map<String, Double> map) throws IOException {
		
		File file = new File("goalies.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (Map.Entry<String, Double> entry: map.entrySet()) {
			String key = entry.getKey();
			Double val = entry.getValue();
			DecimalFormat df = new DecimalFormat("#.##");
			String line = key + ": " + df.format(val) +"\n";
			writer.write(line);
		}
		writer.close();
	}
	
	private static LinkedHashMap sortMap(Map<String, Double> map) {
		List mapKeys = new ArrayList(map.keySet());
		List mapVals = new ArrayList(map.values());
		Collections.sort(mapVals, Collections.reverseOrder());
		Collections.sort(mapKeys,Collections.reverseOrder());
		
		LinkedHashMap sortedmap = new LinkedHashMap();
		
		Iterator valueIt = mapVals.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator keyIt = mapKeys.iterator();
			
			while(keyIt.hasNext()) {
				Object key = keyIt.next();
				String comp1 = map.get(key).toString();
				String comp2 = val.toString();
				
				if(comp1.equals(comp2)) {
					map.remove(key);
					mapKeys.remove(key);
					sortedmap.put((String)key, (Double)val);
					break;
				}
			}
		}
		return sortedmap;
		
	}
	private static void parseData(Elements stats) {
		Map<String,Double> goalies  = new HashMap<String, Double>();
		//Goalies node
		for(Element e: stats) {
			String name ;
			int w,so,sa,ga;
			Scanner scanner = new Scanner(e.childNode(3).toString());
			//    				print("%s", scanner.next());
			while(scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("\\s+");
//				print("%s",line.length);
				if(line.length == 24) {
					name = line[0] + " " + line[1];
					w = Integer.parseInt(line[4]);
					so = Integer.parseInt(line[11]);
					ga = Integer.parseInt(line[12]);
					sa = Integer.parseInt(line[13]);
					Goalie goalie = new Goalie(name, w, so, ga, sa);
					goalies.put(goalie.getName(), goalie.getFantasyValue());
				}
				else if(line.length == 25) {
					if(line[2].matches("\\(R\\)")) {
						name = line[0] + " " + line[1];
						line = Arrays.copyOfRange(line, 3, 25);
					}
					else 
						name = line[0] + " " + line[1];
					w = Integer.parseInt(line[2]);
					so = Integer.parseInt(line[9]);
					ga = Integer.parseInt(line[10]);
					sa = Integer.parseInt(line[11]);
					Goalie goalie = new Goalie(name, w, so, ga, sa);
					goalies.put(goalie.getName(), goalie.getFantasyValue());
				}
			}
		}
		try {
			writeFile(sortMap(goalies));
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
