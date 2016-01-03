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

	private static String index_url = "https://dl.dropboxusercontent.com/u/34714712/S26%20-%20SHL%20SMJHL%20MAIN/SHL-ProTeamScoring.html"; 

	public static void main(String[] args) throws IOException {
		print("Fetching %s...", index_url);
		Document doc = Jsoup.connect(index_url).get();
		Elements tstats = doc.select("table[class=basictablesorter STHSScoring_GoaliesTable]");
		print("Parsing Goalie data");
		parseNewData(tstats);
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
		print("Exported to goalie.txt");
	}
	
	private static void exportStats(List<Goalie> log) throws IOException {
		File file = new File("GStats.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		Goalie g;
		String gname;
		Integer w,s,so;
		Double fp;
		for (int i=0;i<log.size();i++) {
			g = log.get(i);
			gname = g.getName();
			w = g.getWins();
			s = g.getSaves();
			so = g.getShutouts();
			fp = g.getFantasyValue();
			DecimalFormat df = new DecimalFormat("#.##");
			String line = gname+": "+df.format(w)+", "+df.format(so)+", "+df.format(s)+", "+df.format(fp)+"\n";
			writer.write(line);
		}
		writer.close();
		print("Exported to GStats.txt");
	}
	
	private static LinkedHashMap<String, Double> sortMap(Map<String, Double> map) {
		List<String> mapKeys = new ArrayList<String>(map.keySet());
		List<Double> mapVals = new ArrayList<Double>(map.values());
		Collections.sort(mapVals, Collections.reverseOrder());
		Collections.sort(mapKeys,Collections.reverseOrder());
		
		LinkedHashMap<String, Double> sortedmap = new LinkedHashMap<String, Double>();
		
		Iterator<Double> valueIt = mapVals.iterator();
		while (valueIt.hasNext()) {
			Object val = valueIt.next();
			Iterator<String> keyIt = mapKeys.iterator();
			
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
	
	private static void parseNewData(Elements stats) {
		Map<String,Double> gmap  = new HashMap<String, Double>();
		List<Goalie> goalies = new ArrayList<Goalie>();
		for(int i=0;i<stats.size();i++) {
//			if(i%3==2) { 
				Element table = stats.get(i);
				Elements rows = table.select("tr");
				String name ;
				int w,so,sa,ga;
				for(int j=0;j<rows.size();j++) {
					Element row = rows.get(j);
					Elements cols = row.select("td");

					if(!cols.isEmpty()) {
						name = cols.get(0).text();
						w = Integer.parseInt(cols.get(2).text());
						so = Integer.parseInt(cols.get(9).text());
						ga = Integer.parseInt(cols.get(10).text());
						sa = Integer.parseInt(cols.get(11).text());
						Goalie goalie = new Goalie(name, w, so, ga, sa);
						goalies.add(goalie);
						gmap.put(goalie.getName(), goalie.getFantasyValue());
					}

				}
//			}
		}
		try {
			writeFile(sortMap(gmap));
			exportStats(goalies);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}
	
	private static void parseData(Elements stats) {
		Map<String,Double> gmap  = new HashMap<String, Double>();
		List<Goalie> goalies = new ArrayList<Goalie>();
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
					goalies.add(goalie);
					gmap.put(goalie.getName(), goalie.getFantasyValue());
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
					goalies.add(goalie);
					gmap.put(goalie.getName(), goalie.getFantasyValue());
				}
			}
		}
		try {
			writeFile(sortMap(gmap));
			exportStats(goalies);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
