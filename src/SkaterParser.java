import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;


public class SkaterParser {

	private static String s22url = "https://dl.dropboxusercontent.com/u/34714712/SHL%20S22MAIN/SHL-ProTeamScoring.html"; 
	
	public static void main(String[] args) throws IOException {
		print("Fetching %s...", s22url);
		Document doc = Jsoup.connect(s22url).get();
		Elements tstats = doc.select("pre");

		//Skaters node
		for(Element e: tstats) {
			List<Skater> players = new ArrayList<Skater>();
			String name ;
			int g,a,p,h,b;
			Scanner scanner = new Scanner(e.childNode(1).toString());
//			print("%s", scanner.next());
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
					players.add(player);
				}
				else if(line.length == 51) {
					if(line[2].matches("\\(R\\)"))
							name = line[0] + " " + line[1] + " " + line[2];
					else {
						name = line[0] + " " + line[1];
						g = Integer.parseInt(line[5]);
						a = Integer.parseInt(line[6]);
						p = Integer.parseInt(line[7]);
						h = Integer.parseInt(line[11]);
						b = Integer.parseInt(line[17]);
						Skater player = new Skater(name, g, a, p, h, b);
						players.add(player);
					}
				}
				else if(line.length == 50) {
					name = line[0] + " " + line[1];
					g = Integer.parseInt(line[4]);
					a = Integer.parseInt(line[5]);
					p = Integer.parseInt(line[6]);
					h = Integer.parseInt(line[10]);
					b = Integer.parseInt(line[16]);
					Skater player = new Skater(name, g, a, p, h, b);
					players.add(player);
				}
			}
			for (Skater sk: players) {
				print("%s",sk.getName());
				print("%.00f",sk.getFantasyValue());
			}
		}
//		//Goalie headers
//		for(Element e: tstats) {
//			print("%s", e.childNode(2));
//		}
//		//Goalies node
//		for(Element e: tstats) {
//			print("%s", e.childNode(3));
//		}
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
}
