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


public class SkaterParser {

	private static String index_url = "https://dl.dropboxusercontent.com/u/34714712/S27%20-%20MAIN/SHL-ProTeamScoring.html"; 

	public static void main(String[] args) throws IOException {
		print("Fetching %s...", index_url);
		Document doc = Jsoup.connect(index_url).get();
		Elements table = doc.select("table[class=basictablesorter STHSScoring_PlayersTable1]");
		print("Parsing Skater data");
		parseNewData(table);
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

		File file = new File("skaters.txt");
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

	private static void exportStats(List<Skater> list) throws IOException {

		String name;
		Integer g,a,p,h,sb;
		Skater sk;
		File file = new File("SKstats.txt");
		BufferedWriter writer = new BufferedWriter(new FileWriter(file));
		for (int i=0;i<list.size();i++) {
			sk = list.get(i);
			name = sk.getName();
			g = sk.getGoals();
			a = sk.getAssists();
			p = sk.getPoints();
			h = sk.getHits();
			sb = sk.getBlocks();
			DecimalFormat df = new DecimalFormat("#.##");
			String line = name+": "+df.format(g)+", "+df.format(a)+", "+df.format(p)+", "+df.format(h)+", "+df.format(sb)+"\n";
			writer.write(line);
		}
		writer.close();
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
		Map<String,Double> players  = new HashMap<String, Double>();
		List<Skater> skaters = new ArrayList<Skater>();
		for(int i=0;i<stats.size();i++) {
//			if(i%3==0) { 
				Element table = stats.get(i);
				Elements rows = table.select("tr");
				String name ;
				int g,a,p,h,b;

				for(int j=0;j<rows.size();j++) {
					Element row = rows.get(j);
					Elements cols = row.select("td");

					if(!cols.isEmpty()) {
						name = cols.get(0).text();
						g = Integer.parseInt(cols.get(4).text());
						a = Integer.parseInt(cols.get(5).text());
						p = Integer.parseInt(cols.get(6).text());
						h = Integer.parseInt(cols.get(10).text());
						b = Integer.parseInt(cols.get(16).text());
						Skater player = new Skater(name, g, a, p, h, b);
						skaters.add(player);
						players.put(player.getName(), player.getFantasyValue());
					}

				}
//			}
		}
		try {
			writeFile(sortMap(players));
			print("Exported to skater.txt");
			exportStats(skaters);
			print("Exported to SKstats.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

	}

	private static void parseData(Elements stats) {
		Map<String,Double> players  = new HashMap<String, Double>();
		List<Skater> skaters = new ArrayList<Skater>();
		//Skaters node
		for(Element e: stats) {
			String name ;
			int g,a,p,h,b;
			Scanner scanner = new Scanner(e.childNode(1).toString());
			//    				print("%s", scanner.next());
			while(scanner.hasNextLine()) {
				String[] line = scanner.nextLine().split("\\s+");
				if(line.length == 49) {
					name = line[0];
					g = Integer.parseInt(line[3]);
					a = Integer.parseInt(line[4]);
					p = Integer.parseInt(line[5]);
					h = Integer.parseInt(line[9]);
					b = Integer.parseInt(line[15]);
					Skater player = new Skater(name, g, a, p, h, b);
					skaters.add(player);
					players.put(player.getName(), player.getFantasyValue());
				}
				else if(line.length == 51) {
					if(line[2].matches("\\(R\\)"))
						name = line[0] + " " + line[1];
					else 
						name = line[0] + " " + line[1] + " " + line[2];
					g = Integer.parseInt(line[5]);
					a = Integer.parseInt(line[6]);
					p = Integer.parseInt(line[7]);
					h = Integer.parseInt(line[11]);
					b = Integer.parseInt(line[17]);
					Skater player = new Skater(name, g, a, p, h, b);
					skaters.add(player);
					players.put(player.getName(), player.getFantasyValue());
				}
				else if(line.length == 50) {
					name = line[0] + " " + line[1];
					g = Integer.parseInt(line[4]);
					a = Integer.parseInt(line[5]);
					p = Integer.parseInt(line[6]);
					h = Integer.parseInt(line[10]);
					b = Integer.parseInt(line[16]);
					Skater player = new Skater(name, g, a, p, h, b);
					skaters.add(player);
					players.put(player.getName(), player.getFantasyValue());
				}
			}
		}
		try {
			writeFile(sortMap(players));
			print("Exported to skater.txt");
			exportStats(skaters);
			print("Exported to SKstats.txt");
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
}
