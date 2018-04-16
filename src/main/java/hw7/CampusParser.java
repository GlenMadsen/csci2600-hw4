package hw7;

import java.util.*;
import java.io.*;
import javafx.util.Pair;


public class CampusParser {
	/** @param: filename The path to the "CSV" file that contains the <hero, book> pairs                                                                                                
        @param: charsInBooks The Map that stores parsed <book, Set-of-heros-in-book> pairs;
        	    usually an empty Map
        @param: chars The Set that stores parsed characters; usually an empty Set.
        @effects: adds parsed <book, Set-of-heros-in-book> pairs to Map charsInBooks;
        		  adds parsed characters to Set chars
        @throws: IOException if file cannot be read of file not a CSV file                                                                                     
	 */
    public static void readData(String Nodes, String Edges, Set<Pair<String, String>> BuildingEdges, Map<String, ArrayList<String>> Buildings) 
    		throws IOException {
    	BufferedReader reader = new BufferedReader(new FileReader(Nodes));
        String line = null;
        String[] lines;
        while ((line = reader.readLine()) != null) 
        {
             int i = line.indexOf(",");
             if ((i == -1)) {
            	 throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }  
             int count = 0;
             for(int j = 0; j < line.length(); j++)
             {
            	 if(line.charAt(j)==',')
            	 {
            		 count++;
            	 }
             }
             if(count != 3)
             {
            	 throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }
             lines = line.split(",");
             String building;
             String id;
             String x;
             String y;
             if(line.startsWith(","))
             {
            	 building = "";
                 id = lines[1];
                 x = lines[2];
                 y = lines[3];
             }
             else
             {
            	 building = lines[0];
                 id = lines[1];
                 x = lines[2];
                 y = lines[3];
             }
     		//System.out.println("hllllo");
             ArrayList<String> t = new ArrayList<String>();
             t.add(building);
             t.add(x);
             t.add(y);
             Buildings.put(id, t);
        }
		//System.out.println("heoooolllllo");
        reader.close();
        reader = new BufferedReader(new FileReader(Edges));
        line = null;
        while ((line = reader.readLine()) != null) 
        {
             int i = line.indexOf(",");
             if ((i == -1)) {
            	 throw new IOException("File "+Nodes+" not a CSV (Building Name, id, x, y) file.");
             }
             lines = line.split(",");
             Pair<String, String> edge = new Pair<String, String>(lines[0], lines[1]);
             BuildingEdges.add(edge);
        }
        reader.close();
    }
}
